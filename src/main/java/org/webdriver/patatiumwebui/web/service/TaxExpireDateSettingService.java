package org.webdriver.patatiumwebui.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webdriver.patatiumwebui.web.model.TaxExpireDateSetting;
import org.webdriver.patatiumwebui.web.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaxExpireDateSettingService {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 取得当前年月的纳税申报日期配置表
     *
     * @return
     */
    public String getTaxEndDateString() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        TaxExpireDateSetting taxExpireDateSetting = this.getTaxExpireDateSetting(year, month);
        if (taxExpireDateSetting == null) {
            return "";
        }
        String yearString = taxExpireDateSetting.getYearString();
        String monthString = Integer.valueOf(taxExpireDateSetting.getMonthString()) < 10 ? "0" + taxExpireDateSetting.getMonthString() : taxExpireDateSetting.getMonthString();
        String dateString = Integer.valueOf(taxExpireDateSetting.getDateString()) < 10 ? "0" + taxExpireDateSetting.getDateString() : taxExpireDateSetting.getDateString();
        return yearString + "-" + monthString + "-" + dateString;
    }

    /**
     * 取得纳税申报日期配置表
     *
     * @return
     */
    public TaxExpireDateSetting getTaxExpireDateSetting(String year, String month) {
        Map<String, Object> params = new HashMap<>();
        params.put("YEAR_STRING", year);
        params.put("MONTH_STRING", month);
        List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList("select * from tax_expire_date_setting where YEAR_STRING=:YEAR_STRING and MONTH_STRING=:MONTH_STRING ", params);
        if (list.size() > 0) {
            Map<String, Object> one = list.get(0);
            TaxExpireDateSetting taxExpireDateSetting = new TaxExpireDateSetting();
            taxExpireDateSetting.setId((Integer) one.get("ID"));
            taxExpireDateSetting.setYearString((String) one.get("YEAR_STRING"));
            taxExpireDateSetting.setMonthString((String) one.get("MONTH_STRING"));
            taxExpireDateSetting.setDateString((String) one.get("DATE_STRING"));
            taxExpireDateSetting.setCreateTime((String) one.get("CREATE_TIME"));
            taxExpireDateSetting.setUpdateTime((String) one.get("UPDATE_TIME"));
            return taxExpireDateSetting;
        }
        return null;
    }

    /**
     * 保存纳税申报日期配置表
     */
    public void saveTaxExpireDateSetting(String year, String month, String date) {
        TaxExpireDateSetting taxExpireDateSettingInDb = getTaxExpireDateSetting(year, month);
        if (taxExpireDateSettingInDb == null) {// 新增
            StringBuilder sb = new StringBuilder();
            sb.append(" insert into tax_expire_date_setting(YEAR_STRING,MONTH_STRING,DATE_STRING,CREATE_TIME) ");
            sb.append(" values ");
            sb.append(" (:YEAR_STRING,:MONTH_STRING,:DATE_STRING,:CREATE_TIME) ");

            Map<String, Object> parameter = new HashMap<>();
            parameter.put("YEAR_STRING", year);
            parameter.put("MONTH_STRING", month);
            parameter.put("DATE_STRING", date);
            parameter.put("CREATE_TIME", DateUtil.getDateShow(new Date(), "yyyyMMddHHmmss"));

            this.namedParameterJdbcTemplate.update(sb.toString(), parameter);
        }
    }

    /**
     * 检查当前日期是否在报税日期内,
     *
     * @return
     */
    public boolean isCurrentDateLTETaxExpireDate() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        TaxExpireDateSetting taxExpireDateSetting = getTaxExpireDateSetting(year, month);
        if (taxExpireDateSetting == null) {
            return true;
        }

        String monthString = taxExpireDateSetting.getMonthString();
        if (Integer.valueOf(monthString) < 10) {
            monthString = "0" + monthString;
        }
        Date taxExpireDate = DateUtil.getDateFromString(taxExpireDateSetting.getYearString() + monthString + taxExpireDateSetting.getDateString(), "yyyyMMdd");
        Calendar taxExpireDateCalendar = Calendar.getInstance();
        taxExpireDateCalendar.setTime(taxExpireDate);
        taxExpireDateCalendar.add(Calendar.DATE, 1);

        if (calendar.compareTo(taxExpireDateCalendar) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 取得报税的过期日期
     * @return
     */
    public String getCurrentTaxExpireDateShow() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        TaxExpireDateSetting taxExpireDateSetting = getTaxExpireDateSetting(year, month);
        if (taxExpireDateSetting != null) {
            String monthString = taxExpireDateSetting.getMonthString();
            if (Integer.valueOf(monthString) < 10) {
                monthString = "0" + monthString;
            }
            return taxExpireDateSetting.getYearString() + "-" + monthString + "-" + taxExpireDateSetting.getDateString();
        }
        return "";
    }

}
