package org.webdriver.patatiumwebui.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webdriver.patatiumwebui.common.RPAQueue;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxQueueVO;

@Service
@Transactional
public class TaxDeclareStatusService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private TaxParameterService taxParameterService;

    /**
     * 保存一键报税状态
     */
    public void saveOrUpdateTaxDeclareStatus(CorpTaxQueueVO corpTaxQueueVO) {
        taxParameterService.updateTaxParameter(TaxConstant.TAX_DECLARE_STATUS, corpTaxQueueVO.getStatus());
        // 立即启动报税
        if ("1".equals(corpTaxQueueVO.getStatus())) {
            RPAQueue.put(corpTaxQueueVO);
        }
    }
}
