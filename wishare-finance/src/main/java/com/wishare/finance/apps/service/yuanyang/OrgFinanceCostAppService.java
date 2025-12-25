package com.wishare.finance.apps.service.yuanyang;

import java.util.List;
import java.util.stream.Collectors;

import com.wishare.finance.apps.model.bill.vo.OrgFinanceCostInfoV;
import com.wishare.finance.apps.service.configure.organization.ShareChargeCostOrgAppService;
import com.wishare.finance.apps.service.configure.organization.StatutoryBodyAccountAppService;
import com.wishare.finance.domains.configure.organization.dto.BankAccountCostOrgDto;
import com.wishare.finance.domains.configure.organization.dto.ShareChargeCostOrgDto;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title OrgFinanceCostAppService
 * @date 2024.06.18  11:17
 * @description 核算成本中心
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrgFinanceCostAppService {

    private final OrgClient orgClient;

    private final StatutoryBodyAccountAppService statutoryBodyAccountAppService;

    private final ShareChargeCostOrgAppService shareChargeCostOrgAppService;

    /**
     * 分页查询成本中心信息
     * @param queryF 分页参数
     * @return {@link OrgFinanceCostInfoV}
     */
    public PageV<OrgFinanceCostInfoV> getPage(PageF<SearchF<?>> queryF) {
        PageV<OrgFinanceCostInfoV> page = orgClient.getPage(queryF);

        // 获取拓展信息
        if (!CollectionUtils.isEmpty(page.getRecords())){
            for (OrgFinanceCostInfoV record : page.getRecords()) {
                // 获取关联银行账户
                List<BankAccountCostOrgDto> centerRelation = statutoryBodyAccountAppService.queryCostCenterRelation(
                        record.getId());
                String unionBankName = centerRelation.stream().map(BankAccountCostOrgDto::getBankAccount).
                        collect(Collectors.joining(","));
                record.setBankAccountCode(unionBankName);
                // 获取分成费项
                List<ShareChargeCostOrgDto> shareChargeCostOrgDtos = shareChargeCostOrgAppService.queryShareChargeCostCenterRelation(
                        record.getId());
                String shareCostOrgName = shareChargeCostOrgDtos.stream().map(ShareChargeCostOrgDto::getShareChargeName).
                        collect(Collectors.joining(","));
                record.setShareProportion(!CollectionUtils.isEmpty(shareChargeCostOrgDtos)?
                        shareChargeCostOrgDtos.get(0).getShareProportion():null);
                record.setShareChargeName(shareCostOrgName);
            }
        }
        return page;
    }
}
