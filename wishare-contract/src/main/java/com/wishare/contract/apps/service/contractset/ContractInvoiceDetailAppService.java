package com.wishare.contract.apps.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.consts.contractset.ContractInvoiceDetailFieldConst;
import com.wishare.contract.domains.vo.contractset.ContractInvoiceDetailV;
import com.wishare.contract.domains.vo.contractset.InvoiceDetailPlanV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.interfaces.ApiBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractInvoiceDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.contract.domains.service.contractset.ContractInvoiceDetailService;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同开票明细表
* </p>
*
* @author ljx
* @since 2022-09-26
*/
@Service
@Slf4j
public class ContractInvoiceDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractInvoiceDetailService contractInvoiceDetailService;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    /**
     * 新增开票明细
     */
    public ContractInvoiceDetailE saveContractInvoiceDetail(ContractInvoiceDetailF contractInvoiceDetailF) {
        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        String invoiceApplyNumber = invoiceApplyNumber(identityInfo.getTenantId(), contractInvoiceDetailF.getCollectionPlanId());
        ContractInvoiceDetailE map = Global.mapperFacade.map(contractInvoiceDetailF, ContractInvoiceDetailE.class);
        map.setId(UidHelper.nextId(ContractInvoiceDetailFieldConst.CONTRACT_INVOICE_DETAIL));
        // 开票编号
        map.setInvoiceApplyNumber(invoiceApplyNumber);
        // 创建人操作人
        map.setTenantId(identityInfo.getTenantId());
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(now);
        map.setOperator(identityInfo.getUserId());
        map.setOperatorName(identityInfo.getUserName());
        map.setGmtModify(now);
        contractInvoiceDetailService.save(map);
        return map;
    }

    /**
     * 指定合同开票明细列表
     */
    public List<InvoiceDetailPlanV> contractInvoiceDetailList(Long contractId, Long collectionPlanId) {
        return contractInvoiceDetailService.contractInvoiceDetailList(contractId, collectionPlanId, curIdentityInfo().getTenantId());
    }

    /**
     * 根据收款计划id删除开票明细
     */
    public void deleteByCollectionPlanId(Long collectionPlanId) {
        contractInvoiceDetailService.deleteByCollectionPlanId(collectionPlanId);
    }

    /**
     * 根据合同id删除开票明细
     */
    public void deleteByContractId(Long contractId) {
        contractInvoiceDetailService.deleteByCollectionPlanId(contractId);
    }

    /**
     * 根据中台发票id查开票明细
     */
    public List<ContractInvoiceDetailV> invoiceDetailListByInvoiceId(Long invoiceId) {
        return contractInvoiceDetailService.invoiceDetailListByInvoiceId(invoiceId);
    }


    private String invoiceApplyNumber(String tenantId, Long collectionPlanId) {
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Long count = contractInvoiceDetailService.selectCountCurrentDate();
        //开票编号
        String code = String.format("%0" + 3 + "d", count + 1);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        if (null != tenantInfoRv && StringUtils.isNotBlank(tenantInfoRv.getEnglishName())) {
            return tenantInfoRv.getEnglishName() + "KP" + year + code;
        } else {
            return "KP" + year + code;
        }
    }
}
