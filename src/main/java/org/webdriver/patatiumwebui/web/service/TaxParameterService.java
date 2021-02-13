package org.webdriver.patatiumwebui.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.httprequest.vo.SettingVO;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.model.TaxParameter;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaxParameterService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 取得一键报税参数列表
     * @return
     */
    public List<TaxParameter> getTaxParameterLi() {
        List<TaxParameter> result = new ArrayList<>();
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from TAX_PARAMETER");
        if (list.size() > 0) {
            for (Map<String, Object> item: list) {
                TaxParameter taxParameter = new TaxParameter();
                taxParameter.setId((Integer)item.get("ID"));
                taxParameter.setKey((String)item.get("KEY"));
                taxParameter.setValue((String)item.get("VALUE"));
                taxParameter.setCreateTime((String)item.get("CREATE_TIME"));
                taxParameter.setUpdateTime((String)item.get("UPDATE_TIME"));

                if (StringUtils.isNotEmpty(taxParameter.getUpdateTime())) {
                    taxParameter.setModifyTimeShow(DateUtil.getDateShow(taxParameter.getUpdateTime(), "yyyy-MM-dd HH:mm"));
                } else if (StringUtils.isNotEmpty(taxParameter.getCreateTime())) {
                    taxParameter.setModifyTimeShow(DateUtil.getDateShow(taxParameter.getCreateTime(), "yyyy-MM-dd HH:mm"));
                }

                result.add(taxParameter);
            }

        }
        return result;
    }

    /**
     * 取得一键报税参数
     */
    public TaxParameter getTaxParameter(String key) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from TAX_PARAMETER where key=?", key);
        if (list.size() > 0) {
            Map<String, Object> item = list.get(0);
            TaxParameter taxParameter = new TaxParameter();
            taxParameter.setId((Integer)item.get("ID"));
            taxParameter.setKey((String)item.get("KEY"));
            taxParameter.setValue((String)item.get("VALUE"));
            taxParameter.setCreateTime((String)item.get("CREATE_TIME"));
            taxParameter.setUpdateTime((String)item.get("UPDATE_TIME"));

            if (StringUtils.isNotEmpty(taxParameter.getUpdateTime())) {
                taxParameter.setModifyTimeShow(DateUtil.getDateShow(taxParameter.getUpdateTime(), "yyyy-MM-dd HH:mm"));
            } else if (StringUtils.isNotEmpty(taxParameter.getCreateTime())) {
                taxParameter.setModifyTimeShow(DateUtil.getDateShow(taxParameter.getCreateTime(), "yyyy-MM-dd HH:mm"));
            }
            return taxParameter;
        }
        return null;
    }

    /**
     * 保存 一键报税参数表
     * @param taxParameter
     */
    public int addTaxParameter(TaxParameter taxParameter) {
        StringBuilder sb = new StringBuilder();
        sb.append(" insert into TAX_PARAMETER  ");
        sb.append(" (KEY,VALUE,CREATE_TIME,UPDATE_TIME) ");
        sb.append(" values ");
        sb.append(" (:KEY,:VALUE,:CREATE_TIME,:UPDATE_TIME) ");

        Map<String, Object> params = new HashMap<>();
        params.put("KEY", taxParameter.getKey());
        params.put("VALUE", taxParameter.getValue());
        params.put("CREATE_TIME", taxParameter.getCreateTime());
        params.put("UPDATE_TIME", taxParameter.getUpdateTime());

        this.namedParameterJdbcTemplate.update(sb.toString(), params);

        return this.jdbcTemplate.queryForObject("CALL SCOPE_IDENTITY()", Integer.class);
    }

    /**
     * 更新一键报税参数表
     */
    public void updateTaxParameter(String key, String value) {
        String sql = "update TAX_PARAMETER set VALUE=:VALUE, UPDATE_TIME=:UPDATE_TIME where KEY=:KEY";

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("VALUE", value);
        parameter.put("UPDATE_TIME", DateUtil.getDateShow(new Date(), "yyyyMMddHHmmss"));
        parameter.put("KEY", key);

        this.namedParameterJdbcTemplate.update(sql, parameter);
    }

    /**
     * 保存一键报税参数-线程数
     */
    public void saveOrUpdateTaxThreadCount(String taxThreadCount) {
        this.updateTaxParameter(TaxConstant.TAX_THREAD_COUNT, taxThreadCount);
    }

    /**
     * 保存一键报税参数-拖动验证码重试次数
     */
    public void saveOrUpdateDragCheckCodeRepeatCount(String dragCheckCodeRepeatCount) {
        this.updateTaxParameter(TaxConstant.DRAG_CHECKCODE_REPEAT_COUNT, dragCheckCodeRepeatCount);
    }

    /**
     * 保存一键报税参数-验证码X偏移
     */
    public void saveOrUpdateCheckcodeOffsetX(String checkcodeOffsetX) {
        this.updateTaxParameter(TaxConstant.CHECKCODE_OFFSET_X, checkcodeOffsetX);
    }

    /**
     * 保存一键报税参数-验证码Y偏移
     */
    public void saveOrUpdateCheckcodeOffsetY(String checkcodeOffsetY) {
        this.updateTaxParameter(TaxConstant.CHECKCODE_OFFSET_Y, checkcodeOffsetY);
    }

    /**
     * 保存一键报税参数-浏览器路径
     */
    public void saveOrUpdateBrowserPathAndType(String browserPath) {
        TaxParameter taxParameter = getTaxParameter(TaxConstant.BROWSER_PATH);
        if (!taxParameter.getValue().equals(browserPath)) {
            this.updateTaxParameter(TaxConstant.BROWSER_PATH, browserPath);
            this.updateTaxParameter(TaxConstant.BROWSER_PATH_TYPE, "1");// 标识浏览器类型为手工修改
        }
    }

    /**
     * 保存设置
     * @param settingVO
     */
    public void saveOrUpdateSetting(SettingVO settingVO) {
        if (StringUtils.isNotEmpty(settingVO.getTaxThreadCount())) {
            saveOrUpdateTaxThreadCount(settingVO.getTaxThreadCount());
        }
        if (StringUtils.isNotEmpty(settingVO.getDragCheckCodeRepeatCount())) {
            saveOrUpdateDragCheckCodeRepeatCount(settingVO.getDragCheckCodeRepeatCount());
        }
        if (StringUtils.isNotEmpty(settingVO.getCheckcodeOffsetX())) {
            saveOrUpdateCheckcodeOffsetX(settingVO.getCheckcodeOffsetX());
        }
        if (StringUtils.isNotEmpty(settingVO.getCheckcodeOffsetY())) {
            saveOrUpdateCheckcodeOffsetY(settingVO.getCheckcodeOffsetY());
        }
        if (StringUtils.isNotEmpty(settingVO.getBrowserPath())) {
            saveOrUpdateBrowserPathAndType(settingVO.getBrowserPath());
        }
    }

    /**
     * 保存或更新浏览器路径,给系统启动时调用
     */
    public void saveOrUpdateTaxBrowserPathWhenInit(String browserPath) {
        CorpSettingService corpSettingService = (CorpSettingService) SpringUtil.getBean("corpSettingService");
        if (!corpSettingService.isInitReady()) {
            InitService initService = (InitService) SpringUtil.getBean("initService");
            initService.init();
        }
        if (getTaxParameter(TaxConstant.BROWSER_PATH) != null) {
            if (getTaxParameter(TaxConstant.BROWSER_PATH_TYPE).getValue().equals("0")) {// 系统设置的 browserPath 可能修改,手工更新过的,不能修改,
                updateTaxParameter(TaxConstant.BROWSER_PATH, browserPath);
            }
        } else {
            TaxParameter taxParameter = new TaxParameter();
            taxParameter.setKey(TaxConstant.BROWSER_PATH);
            taxParameter.setValue(browserPath);
            String yyyyMMddHHmmss = DateUtil.getDateShow(new Date(), "yyyyMMddHHmmss");
            taxParameter.setCreateTime(yyyyMMddHHmmss);
            taxParameter.setUpdateTime(yyyyMMddHHmmss);
            addTaxParameter(taxParameter);
        }
    }
}
