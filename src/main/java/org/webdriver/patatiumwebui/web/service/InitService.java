package org.webdriver.patatiumwebui.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webdriver.patatiumwebui.utils.TaxUtil;

@Service
@Transactional
public class InitService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 系统初始化,
     * 建表:
     * 0.纳税申报日期配置表
     * 1.企业设置表,corp_setting
     * 2.一键报税状态表,tax_declare_status
     * 3.报税进度状态表,tax_declare_progress
     */
    public void init() {
        initTaxExpireDateSetting();
        initCorpSetting();
        initTaxParameter();
        initTaxDeclareProgress();
    }

    /*
     * 0.纳税申报日期配置表,一般是每月15号,报税日期截止,
    drop table if exists tax_expire_date_setting;
    create table IF NOT EXISTS tax_expire_date_setting
    (
       id int AUTO_INCREMENT not null,
       year_string varchar(5) not null,
       month_string varchar(5) not null,
       date_string varchar(5) not null,
       create_time varchar(14) not null,
       update_time varchar(14) default null,
       primary key(id)
    );
    COMMENT ON TABLE tax_expire_date_setting IS '纳税申报日期配置表';
    COMMENT ON COLUMN tax_expire_date_setting.year_string IS '纳税申报日期配置表-年';
    COMMENT ON COLUMN tax_expire_date_setting.month_string IS '纳税申报日期配置表-月';
    COMMENT ON COLUMN tax_expire_date_setting.date_string IS '纳税申报日期配置表-日';
    COMMENT ON COLUMN tax_expire_date_setting.create_time IS '创建时间';
    COMMENT ON COLUMN tax_expire_date_setting.update_time IS '更新时间';
     */
    private void initTaxExpireDateSetting() {
        this.jdbcTemplate.execute("drop table if exists tax_expire_date_setting");

        StringBuilder sb = new StringBuilder();
        sb.append(" create table IF NOT EXISTS tax_expire_date_setting ");
        sb.append(" ( ");
        sb.append("    id int AUTO_INCREMENT not null, ");
        sb.append("    year_string varchar(5) not null, ");
        sb.append("    month_string varchar(5) not null, ");
        sb.append("    date_string varchar(5) not null, ");
        sb.append("    create_time varchar(14) not null, ");
        sb.append("    update_time varchar(14) default null, ");
        sb.append("    primary key(id) ");
        sb.append(" ) ");
        this.jdbcTemplate.execute(sb.toString());

        this.jdbcTemplate.execute("COMMENT ON TABLE tax_expire_date_setting IS '纳税申报日期配置表'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_expire_date_setting.year_string IS '纳税申报日期配置表-年'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_expire_date_setting.month_string IS '纳税申报日期配置表-月'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_expire_date_setting.date_string IS '纳税申报日期配置表-日'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_expire_date_setting.create_time IS '创建时间'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_expire_date_setting.update_time IS '更新时间'");
    }

    /**
     * 1.企业设置表,corp_setting
     -- 企业设置表,用于保存企业用户名/密码
     drop table if exists corp_setting;
     create table IF NOT EXISTS corp_setting
     (
     id int AUTO_INCREMENT not null,
     username varchar(255) not null,
     password varchar(255) not null,
     domain_name varchar(255) default null,
     create_time varchar(14) not null,
     update_time varchar(14) default null,
     primary key(id)
     );
     COMMENT ON TABLE corp_setting IS '企业设置表,用于保存企业用户名/密码';
     COMMENT ON COLUMN corp_setting.username IS '用户名';
     COMMENT ON COLUMN corp_setting.password IS '密码';
     COMMENT ON COLUMN corp_setting.domain_name IS '域名';
     COMMENT ON COLUMN corp_setting.create_time IS '创建时间';
     COMMENT ON COLUMN corp_setting.update_time IS '更新时间';
     */
    private void initCorpSetting() {
        // -- 企业设置表,用于保存企业用户名/密码
        this.jdbcTemplate.execute("drop table if exists corp_setting");

        StringBuilder sb = new StringBuilder();
        sb.append(" create table IF NOT EXISTS corp_setting ");
        sb.append(" ( ");
        sb.append("    id int AUTO_INCREMENT not null, ");
        sb.append("    username varchar(255) not null, ");
        sb.append("    password varchar(255) not null, ");
        sb.append("    domain_name varchar(255) default null, ");
        sb.append("    create_time varchar(14) not null, ");
        sb.append("    update_time varchar(14) default null, ");
        sb.append("    primary key(id) ");
        sb.append(" ) ");
        this.jdbcTemplate.execute(sb.toString());

        this.jdbcTemplate.execute("COMMENT ON TABLE corp_setting IS '企业设置表,用于保存企业用户名/密码'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN corp_setting.username IS '用户名'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN corp_setting.password IS '密码'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN corp_setting.domain_name IS '域名'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN corp_setting.create_time IS '创建时间'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN corp_setting.update_time IS '更新时间'");
    }

    /**
     * 2.一键报税参数表
     drop table if exists tax_parameter;
     create table IF NOT EXISTS tax_parameter
     (
     id int AUTO_INCREMENT not null,
     key varchar(40) not null,
     value varchar(200) not null,
     create_time varchar(14) not null,
     update_time varchar(14) default null,
     primary key(id)
     );
     COMMENT ON TABLE tax_parameter IS '一键报税参数表,用于保存key/value';
     COMMENT ON COLUMN tax_parameter.key IS '键名称';
     COMMENT ON COLUMN tax_parameter.value IS '键值';
     COMMENT ON COLUMN tax_parameter.create_time IS '创建时间';
     COMMENT ON COLUMN tax_parameter.update_time IS '更新时间';
     insert into tax_parameter(key, value, create_time) values ('taxDeclareStatus', '0', '20181119140401');
     insert into tax_parameter(key, value, create_time) values ('taxThreadCount', '1', '20181119140401');
     insert into tax_parameter(key, value, create_time) values ('checkcodeOffsetX', '1', '20181119140401');
     insert into tax_parameter(key, value, create_time) values ('browserPathType', '0', '20181119140401');
     insert into tax_parameter(key, value, create_time) values ('dragCheckCodeRepeatCount', '10', '20181119140401');// 拖动验证码的重试次数,
     */
    private void initTaxParameter() {
        this.jdbcTemplate.execute(" drop table if exists tax_parameter ");

        StringBuilder sb = new StringBuilder();
        sb.append(" create table IF NOT EXISTS tax_parameter ");
        sb.append(" ( ");
        sb.append(" id int AUTO_INCREMENT not null, ");
        sb.append(" key varchar(40) not null, ");
        sb.append(" value varchar(200) not null, ");
        sb.append(" create_time varchar(14) not null, ");
        sb.append(" update_time varchar(14) default null, ");
        sb.append(" primary key(id) ");
        sb.append(" ) ");
        this.jdbcTemplate.execute(sb.toString());

        this.jdbcTemplate.execute(" COMMENT ON TABLE tax_parameter IS '一键报税参数表,用于保存key/value' ");
        this.jdbcTemplate.execute(" COMMENT ON COLUMN tax_parameter.key IS '键名称' ");
        this.jdbcTemplate.execute(" COMMENT ON COLUMN tax_parameter.value IS '键值' ");
        this.jdbcTemplate.execute(" COMMENT ON COLUMN tax_parameter.create_time IS '创建时间' ");
        this.jdbcTemplate.execute(" COMMENT ON COLUMN tax_parameter.update_time IS '更新时间' ");
        this.jdbcTemplate.execute(" insert into tax_parameter(key, value, create_time) values ('taxDeclareStatus', '0', '20181119140401') ");
        this.jdbcTemplate.execute(" insert into tax_parameter(key, value, create_time) values ('taxThreadCount', '1', '20181119140401') ");
        String checkcodeOffsetX = "0";
        String checkcodeOffsetY = "0";
        if (TaxUtil.isWin7()) {
            checkcodeOffsetX = "1";
            checkcodeOffsetY = "1";
        }
        this.jdbcTemplate.execute(" insert into tax_parameter(key, value, create_time) values ('checkcodeOffsetX', '{checkcodeOffsetX}', '20181119140401') ".replace("{checkcodeOffsetX}", checkcodeOffsetX));
        this.jdbcTemplate.execute(" insert into tax_parameter(key, value, create_time) values ('checkcodeOffsetY', '{checkcodeOffsetY}', '20181119140401') ".replace("{checkcodeOffsetY}", checkcodeOffsetY));
        this.jdbcTemplate.execute(" insert into tax_parameter(key, value, create_time) values ('browserPathType', '0', '20181119140401') ");
        this.jdbcTemplate.execute(" insert into tax_parameter(key, value, create_time) values ('dragCheckCodeRepeatCount', '10', '20181119140401') ");
    }

    /**
     * 3.报税进度状态表,tax_declare_progress
     drop table if exists tax_declare_progress;
     create table IF NOT EXISTS tax_declare_progress
     (
     id int AUTO_INCREMENT not null,
     start_time varchar(14) not null,
     user_total_count int not null default 0,
     user_finish_count int not null default 0,
     user_finish_success_count int not null default 0,
     current_user varchar(50) default null,
     current_tax_time varchar(14) default null,
     create_time varchar(14) not null,
     update_time varchar(14) default null,
     primary key(id)
     );
     COMMENT ON TABLE tax_declare_progress IS '报税进度状态表,每次启动都会写入一条,最多保存1000条';
     COMMENT ON COLUMN tax_declare_progress.start_time IS '报税启动时间';
     COMMENT ON COLUMN tax_declare_progress.user_total_count IS '报税用户总数';
     COMMENT ON COLUMN tax_declare_progress.user_finish_count IS '已报税完成数';
     COMMENT ON COLUMN tax_declare_progress.user_finish_success_count IS '已报税完成成功数';
     COMMENT ON COLUMN tax_declare_progress.current_user IS '当前报税人';
     COMMENT ON COLUMN tax_declare_progress.current_tax_time IS '当前报税时间';
     COMMENT ON COLUMN tax_declare_progress.create_time IS '创建时间';
     COMMENT ON COLUMN tax_declare_progress.update_time IS '更新时间';
     */
    private void initTaxDeclareProgress() {
        this.jdbcTemplate.execute("drop table if exists tax_declare_progress");

        StringBuilder sb = new StringBuilder();
        sb.append(" create table IF NOT EXISTS tax_declare_progress ");
        sb.append(" ( ");
        sb.append("    id int AUTO_INCREMENT not null, ");
        sb.append("    start_time varchar(14) not null, ");
        sb.append("    user_total_count int not null default 0, ");
        sb.append("    user_finish_count int not null default 0, ");
        sb.append("    user_finish_success_count int not null default 0, ");
        sb.append("    current_user varchar(50) default null, ");
        sb.append("    current_tax_time varchar(14) default null, ");
        sb.append("    create_time varchar(14) not null, ");
        sb.append("    update_time varchar(14) default null, ");
        sb.append("    primary key(id) ");
        sb.append(" ) ");
        this.jdbcTemplate.execute(sb.toString());

        this.jdbcTemplate.execute("COMMENT ON TABLE tax_declare_progress IS '报税进度状态表,每次启动都会写入一条,最多保存1000条'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.start_time IS '报税启动时间'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.user_total_count IS '报税用户总数'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.user_finish_count IS '已报税完成数'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.user_finish_success_count IS '已报税完成成功数'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.current_user IS '当前报税人'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.current_tax_time IS '当前报税时间'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.create_time IS '创建时间'");
        this.jdbcTemplate.execute("COMMENT ON COLUMN tax_declare_progress.update_time IS '更新时间'");
    }
}
