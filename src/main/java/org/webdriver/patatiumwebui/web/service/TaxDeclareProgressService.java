package org.webdriver.patatiumwebui.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webdriver.patatiumwebui.web.model.TaxDeclareProgress;
import org.webdriver.patatiumwebui.web.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaxDeclareProgressService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 需要保留多少报税进度状态表
     * @param keepLength
     */
    public void deleteTaxDeclareProgress(int keepLength) {
        StringBuilder sb = new StringBuilder();
        sb.append(" delete from tax_declare_progress where id < ( ");
        sb.append("     select id from tax_declare_progress ");
        sb.append("     order by id desc ");
        sb.append("     limit 1000,1 ");
        sb.append(" ) ");

        this.jdbcTemplate.execute(sb.toString());
    }

    /**
     * 新增报税进度
     */
    public int addTaxDeclareProgress(TaxDeclareProgress taxDeclareProgress) {
        StringBuilder sb = new StringBuilder();
        sb.append(" insert into TAX_DECLARE_PROGRESS  ");
        sb.append(" (START_TIME,USER_TOTAL_COUNT,USER_FINISH_COUNT,USER_FINISH_SUCCESS_COUNT,CURRENT_USER,CURRENT_TAX_TIME,CREATE_TIME,UPDATE_TIME) ");
        sb.append(" values ");
        sb.append(" (:START_TIME,:USER_TOTAL_COUNT,:USER_FINISH_COUNT,:USER_FINISH_SUCCESS_COUNT,:CURRENT_USER,:CURRENT_TAX_TIME,:CREATE_TIME,:UPDATE_TIME) ");

        Map<String, Object> params = new HashMap<>();
        params.put("START_TIME", taxDeclareProgress.getStartTime());
        params.put("USER_TOTAL_COUNT", taxDeclareProgress.getUserTotalCount());
        params.put("USER_FINISH_COUNT", taxDeclareProgress.getUserFinishCount());
        params.put("USER_FINISH_SUCCESS_COUNT", taxDeclareProgress.getUserFinishSuccessCount());
        params.put("CURRENT_USER", taxDeclareProgress.getCurrentUser());
        params.put("CURRENT_TAX_TIME", taxDeclareProgress.getCurrentTaxTime());
        params.put("CREATE_TIME", taxDeclareProgress.getCreateTime());
        params.put("UPDATE_TIME", taxDeclareProgress.getUpdateTime());

        this.namedParameterJdbcTemplate.update(sb.toString(), params);

        return this.jdbcTemplate.queryForObject("CALL SCOPE_IDENTITY()", Integer.class);
    }

    /**
     * 报税成功时,更新进度,
     * @param taxDeclareProgressId
     * @param currentUser
     */
    public void updateTaxDeclareProgress(int taxDeclareProgressId, String currentUser, boolean isSuccess) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update TAX_DECLARE_PROGRESS set ");
        sb.append(" USER_FINISH_COUNT=USER_FINISH_COUNT+1 ");
        if (isSuccess) {
            sb.append(" ,USER_FINISH_SUCCESS_COUNT=USER_FINISH_SUCCESS_COUNT+1 ");
        }
        sb.append(" ,CURRENT_USER=:CURRENT_USER ");
        sb.append(" ,CURRENT_TAX_TIME=:CURRENT_TAX_TIME ");
        sb.append(" ,UPDATE_TIME=:UPDATE_TIME ");
        sb.append(" where ID=:ID ");

        String currentDateTime = DateUtil.getDateShow(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("CURRENT_USER", currentUser);
        params.put("CURRENT_TAX_TIME", currentDateTime);
        params.put("UPDATE_TIME", currentDateTime);
        params.put("ID", taxDeclareProgressId);
        this.namedParameterJdbcTemplate.update(sb.toString(), params);
    }

    /**
     * 取得一键报税状态
     * @return
     */
    public List<TaxDeclareProgress> getTaxDeclareProgress(int limit) {
        List<TaxDeclareProgress> result = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" select * from TAX_DECLARE_PROGRESS ");
        sb.append(" order by create_time desc limit ? ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(), limit);
        if (list.size() > 0) {
            for (Map<String, Object> item: list) {
                TaxDeclareProgress taxDeclareProgress = new TaxDeclareProgress();

                taxDeclareProgress.setId((Integer)item.get("ID"));
                taxDeclareProgress.setStartTime((String)item.get("START_TIME"));
                taxDeclareProgress.setUserTotalCount((Integer)item.get("USER_TOTAL_COUNT"));
                taxDeclareProgress.setUserFinishCount((Integer)item.get("USER_FINISH_COUNT"));
                taxDeclareProgress.setUserFinishSuccessCount((Integer)item.get("USER_FINISH_SUCCESS_COUNT"));
                taxDeclareProgress.setUserFinishFailCount(taxDeclareProgress.getUserFinishCount() - taxDeclareProgress.getUserFinishSuccessCount());
                taxDeclareProgress.setCurrentUser((String)item.get("CURRENT_USER"));
                taxDeclareProgress.setCurrentTaxTime((String)item.get("CURRENT_TAX_TIME"));
                taxDeclareProgress.setCreateTime((String)item.get("CREATE_TIME"));
                taxDeclareProgress.setUpdateTime((String)item.get("UPDATE_TIME"));

                taxDeclareProgress.setStartTimeShow(DateUtil.getDateShow(taxDeclareProgress.getStartTime(), "yyyy-MM-dd HH:mm"));
                if (StringUtils.isNotEmpty(taxDeclareProgress.getCurrentTaxTime())) {
                    taxDeclareProgress.setCurrentTaxTimeShow(DateUtil.getDateShow(taxDeclareProgress.getCurrentTaxTime(), "yyyy-MM-dd HH:mm"));

                    Date currentTaxTime = DateUtil.getDateFromString(taxDeclareProgress.getCurrentTaxTime(), "yyyyMMddHHmmss");
                    Date startTime = DateUtil.getDateFromString(taxDeclareProgress.getStartTime(), "yyyyMMddHHmmss");
                    int minutes = (int)(currentTaxTime.getTime() - startTime.getTime()) / (60 * 1000);
                    taxDeclareProgress.setTimeSpendMinute(String.valueOf(minutes));
                }

                taxDeclareProgress.setCreateTimeShow(DateUtil.getDateShow(taxDeclareProgress.getCreateTime(), "yyyy-MM-dd HH:mm"));

                result.add(taxDeclareProgress);
            }
        }
        return result;
    }

}
