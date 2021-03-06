package org.webdriver.patatiumwebui.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.model.TaxExpireDateSetting;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CorpSettingService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 取得登录账号,密码
     * @return
     */
    public CorpSetting getCorpSetting() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from corp_setting");
        if (list.size() > 0) {
            Map<String, Object> one = list.get(0);
            CorpSetting corpSetting = new CorpSetting();
            corpSetting.setId((Integer)one.get("ID"));
            corpSetting.setUsername((String)one.get("USERNAME"));
            corpSetting.setPassword((String)one.get("PASSWORD"));
            corpSetting.setDomainName((String)one.get("DOMAIN_NAME"));
            corpSetting.setCreateTime((String)one.get("CREATE_TIME"));
            corpSetting.setUpdateTime((String)one.get("UPDATE_TIME"));

            if (StringUtils.isNotEmpty(corpSetting.getUpdateTime())) {
                corpSetting.setModifyTimeShow(DateUtil.getDateShow(corpSetting.getUpdateTime(), "yyyy-MM-dd HH:mm"));
            } else if (StringUtils.isNotEmpty(corpSetting.getCreateTime())) {
                corpSetting.setModifyTimeShow(DateUtil.getDateShow(corpSetting.getCreateTime(), "yyyy-MM-dd HH:mm"));
            }

            return corpSetting;
        }
        return null;
    }

    /**
     * 保存企业设置
     */
    public void saveOrUpdateCorpSetting(CorpSetting corpSetting) {
        CorpSetting corpSettingInDb = getCorpSetting();
        if (corpSettingInDb == null) {// 新增
            StringBuilder sb = new StringBuilder();
            sb.append(" insert into corp_setting(USERNAME, PASSWORD, DOMAIN_NAME, CREATE_TIME) ");
            sb.append(" values ");
            sb.append(" (:USERNAME, :PASSWORD, :DOMAIN_NAME, :CREATE_TIME) ");

            Map<String, Object> parameter = new HashMap<>();
            parameter.put("USERNAME", corpSetting.getUsername());
            parameter.put("PASSWORD", corpSetting.getPassword());
            parameter.put("DOMAIN_NAME", corpSetting.getDomainName());
            parameter.put("CREATE_TIME", DateUtil.getDateShow(new Date(), "yyyyMMddHHmmss"));

            this.namedParameterJdbcTemplate.update(sb.toString(), parameter);
        } else {
            // 只有一条数据,直接 update
            String sql = "update corp_setting set USERNAME=:USERNAME, PASSWORD=:PASSWORD, DOMAIN_NAME=:DOMAIN_NAME, UPDATE_TIME=:UPDATE_TIME";

            Map<String, Object> parameter = new HashMap<>();
            parameter.put("USERNAME", corpSetting.getUsername());
            parameter.put("PASSWORD", corpSetting.getPassword());
            parameter.put("DOMAIN_NAME", corpSetting.getDomainName());
            parameter.put("UPDATE_TIME", DateUtil.getDateShow(new Date(), "yyyyMMddHHmmss"));

            this.namedParameterJdbcTemplate.update(sql, parameter);
        }
    }

    /**
     * 是否初始化完成,通过查询 INFORMATION_SCHEMA.COLUMNS 中的字段 tax_declare_progress.update_time 是否有备注
     * 当存在这个字段并且这个字段有写入备注时,则表示初始化完成
     * @return true:已经初始化完成,false:没有初始化
     */
    public boolean isInitReady() {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(*) FROM INFORMATION_SCHEMA.COLUMNS ");
        sb.append(" where 1=1 ");
        sb.append(" and lower(table_name)='tax_declare_progress' ");
        sb.append(" and lower(column_name)='update_time' ");
        sb.append(" and remarks is not null ");
        sb.append(" and remarks != '' ");

        Integer count = this.jdbcTemplate.queryForObject(sb.toString(), Integer.class);
        return count > 0;
    }

    /**
     * 是否可以申请报税
     * 1.CORP_SETTING 有值
     * 2.TAX_PARAMETER.key = 'taxDeclareStatus', value:0,未启动,1:启动
     * 3.纳税申报日期配置表
     *  3.1. 没值,可以报税,
        3.2. 有值,当前日期 <= 报税日期,可以运行报税,
        3.3. 有值,当前日期 > 报税日期,不可以运行报税,
     * @return
     */
    public boolean canApplyTax() {
        Integer count = this.jdbcTemplate.queryForObject("SELECT count(*) FROM CORP_SETTING", Integer.class);
        if (count > 0) {
            Integer statusCount = this.jdbcTemplate.queryForObject("SELECT count(*) FROM TAX_PARAMETER where key='taxDeclareStatus' and value='1' ", Integer.class);
            if (statusCount > 0) {
                TaxExpireDateSettingService taxExpireDateSettingService = (TaxExpireDateSettingService)SpringUtil.getBean("taxExpireDateSettingService");
                return taxExpireDateSettingService.isCurrentDateLTETaxExpireDate();
            }
        }
        return false;
    }
}
