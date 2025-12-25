package com.wishare.contract.apps.service.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.entity.contractset.ContractInvoiceDetailE;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.interfaces.ApiBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractPaymentDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.service.contractset.ContractPaymentDetailService;
import com.wishare.contract.domains.vo.contractset.ContractPaymentDetailV;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同付款明细表
* </p>
*
* @author ljx
* @since 2022-09-29
*/
@Service
@Slf4j
public class ContractPaymentDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPaymentDetailService contractPaymentDetailService;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    /**
     * 新增付款明细
     */
    public ContractPaymentDetailE saveContractPaymentDetail(ContractPaymentDetailF contractPaymentDetailF) {
        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        String paymentApplyNumber = paymentApplyNumber(identityInfo.getTenantId(), contractPaymentDetailF.getCollectionPlanId());
        ContractPaymentDetailE map = Global.mapperFacade.map(contractPaymentDetailF, ContractPaymentDetailE.class);
        // 付款编号
        map.setPaymentApplyNumber(paymentApplyNumber);
        // 创建人操作人
        map.setTenantId(identityInfo.getTenantId());
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(now);
        map.setOperator(identityInfo.getUserId());
        map.setOperatorName(identityInfo.getUserName());
        map.setGmtModify(now);
        contractPaymentDetailService.save(map);
        return map;
    }

    public List<ContractPaymentDetailV> contractPaymentDetailList(Long contractId, Long collectionPlanId) {
        return contractPaymentDetailService.contractPaymentDetailList(contractId, collectionPlanId, curIdentityInfo().getTenantId());
    }

    /**
     * 根据收款计划id删除付款明细
     */
    public void deleteByCollectionPlanId(Long collectionPlanId) {
        contractPaymentDetailService.deleteByCollectionPlanId(collectionPlanId);
    }

    /**
     * 根据合同id删除付款明细
     */
    public void deleteByContractId(Long contractId) {
        contractPaymentDetailService.deleteByCollectionPlanId(contractId);
    }

    private String paymentApplyNumber(String tenantId, Long collectionPlanId) {
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Long count = contractPaymentDetailService.selectCountCurrentDate();
        //付款编号
        String code = String.format("%0" + 3 + "d", count + 1);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        if (null != tenantInfoRv && StringUtils.isNotBlank(tenantInfoRv.getEnglishName())) {
            return tenantInfoRv.getEnglishName() + "FK" + year + code;
        } else {
            return "FK" + year + code;
        }
    }

}
