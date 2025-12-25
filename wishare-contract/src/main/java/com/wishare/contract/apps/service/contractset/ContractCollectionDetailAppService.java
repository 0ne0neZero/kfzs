package com.wishare.contract.apps.service.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.consts.contractset.ContractInvoiceDetailFieldConst;
import com.wishare.contract.domains.vo.contractset.CollectionDetailPlanV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.interfaces.ApiBase;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractCollectionDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.service.contractset.ContractCollectionDetailService;
import com.wishare.contract.domains.vo.contractset.ContractCollectionDetailV;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同收款明细表
* </p>
*
* @author ljx
* @since 2022-09-26
*/
@Service
@Slf4j
public class ContractCollectionDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionDetailService contractCollectionDetailService;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    /**
     * 新增收款明细
     */
    public ContractCollectionDetailE saveContractCollectionDetail(ContractCollectionDetailF contractCollectionDetailF) {
        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        String receiptNumber = receiptNumber(identityInfo.getTenantId(), contractCollectionDetailF.getCollectionPlanId());
        ContractCollectionDetailE map = Global.mapperFacade.map(contractCollectionDetailF, ContractCollectionDetailE.class);
        // 收款编号
        map.setReceiptNumber(receiptNumber);
        // 创建人操作人
        map.setTenantId(identityInfo.getTenantId());
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(now);
        map.setOperator(identityInfo.getUserId());
        map.setOperatorName(identityInfo.getUserName());
        map.setGmtModify(now);
        contractCollectionDetailService.save(map);
        return map;
    }

    /**
     * 指定合同收款明细列表
     */
    public List<CollectionDetailPlanV> contractCollectionDetailList(Long contractId, Long collectionPlanId) {
        return contractCollectionDetailService.contractCollectionDetailList(contractId, collectionPlanId, curIdentityInfo().getTenantId());
    }

    /**
     * 根据收款计划id删除收款明细
     */
    public void deleteByCollectionPlanId(Long collectionPlanId) {
        contractCollectionDetailService.deleteByCollectionPlanId(collectionPlanId);
    }

    /**
     * 根据合同id删除收款明细
     */
    public void deleteByContractId(Long contractId) {
        contractCollectionDetailService.deleteByCollectionPlanId(contractId);
    }

    private String receiptNumber(String tenantId, Long collectionPlanId) {
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Long count = contractCollectionDetailService.selectCountCurrentDate();
        //收款编号
        String code = String.format("%0" + 4 + "d", count + 1);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        if (null != tenantInfoRv && StringUtils.isNotBlank(tenantInfoRv.getEnglishName())) {
            return tenantInfoRv.getEnglishName() + "SK" + year + code;
        } else {
            return "SK" + year + code;
        }
    }
}
