package com.wishare.contract.apps.service.revision.pay;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wishare.contract.apps.fo.revision.income.ContractSrfxxF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.fo.org.CustomerAccountListF;
import com.wishare.contract.apps.remote.fo.org.CustomerListF;
import com.wishare.contract.apps.remote.vo.org.CustomerAccountListV;
import com.wishare.contract.apps.remote.vo.org.CustomerAccountV;
import com.wishare.contract.apps.remote.vo.org.CustomerListV;
import com.wishare.contract.apps.remote.vo.org.CustomerV;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeExpandE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeExpandService;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandListV;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 支出合同订立信息拓展表
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Service
@Slf4j
public class ContractPayConcludeExpandAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeExpandService contractPayConcludeExpandService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    public ContractPayConcludeExpandV get(ContractPayConcludeExpandF contractPayConcludeExpandF){
        return contractPayConcludeExpandService.get(contractPayConcludeExpandF).orElse(null);
    }

    public ContractPayConcludeExpandListV list(ContractPayConcludeExpandListF contractPayConcludeExpandListF){
        return contractPayConcludeExpandService.list(contractPayConcludeExpandListF);
    }

    public Long save(ContractPayConcludeExpandSaveF contractPayConcludeExpandF){
        return contractPayConcludeExpandService.save(contractPayConcludeExpandF);
    }

    public void update(ContractPayConcludeExpandUpdateF contractPayConcludeExpandF){
        contractPayConcludeExpandService.update(contractPayConcludeExpandF);
    }

    public void updateByContractId(ContractPayConcludeExpandUpdateF contractPayConcludeExpandF){
        contractPayConcludeExpandService.updateByContractId(contractPayConcludeExpandF);
    }

    public boolean removeById(Long id){
        return contractPayConcludeExpandService.removeById(id);
    }

    public PageV<ContractPayConcludeExpandV> page(PageF<ContractPayConcludeExpandPageF> request) {
        return contractPayConcludeExpandService.page(request);
    }

    public PageV<ContractPayConcludeExpandV> frontPage(PageF<SearchF<ContractPayConcludeExpandE>> request) {
        return contractPayConcludeExpandService.frontPage(request);
    }

    public ContractPayConcludeExpandV getPaymentInformationDetailsById(String contractId) {
        Optional<ContractPayConcludeExpandV> contractPayConcludeExpandV = contractPayConcludeExpandService.get(new ContractPayConcludeExpandF().setContractId(contractId));
        contractPayConcludeExpandV.ifPresent(e -> {
            String skdwxxJson = e.getSkdwxx();
            List<ContractSrfxxF> skdwxxList = StrUtil.isBlank(skdwxxJson)
                    ? Collections.emptyList()
                    : JSONObject.parseArray(skdwxxJson, ContractSrfxxF.class);

            if (CollUtil.isNotEmpty(skdwxxList) && Objects.nonNull(skdwxxList.get(0))) {
                ContractSrfxxF contractSrfxxF = skdwxxList.get(0);

                String payeeid = contractSrfxxF.getPayeeid();
                String payee = contractSrfxxF.getPayee();
                CustomerListV customerList = orgFeignClient.getCustomerList(
                        new CustomerListF().setMainDataCode(payeeid).setName(payee)
                );

                String payeeaccounnumber = contractSrfxxF.getPayeeaccounnumber();
                String payeeaccounname = contractSrfxxF.getPayeeaccounname();
                String payeeaccounbank = contractSrfxxF.getPayeeaccounbank();
                CustomerAccountListV customerAccountList = new CustomerAccountListV();
                if(StringUtils.isNotBlank(payeeaccounnumber) || StringUtils.isNotBlank(payeeaccounname)){
                    customerAccountList = orgFeignClient.getCustomerAccountList(
                            new CustomerAccountListF()
                                    .setAccountNum(payeeaccounnumber)
                                    .setName(payeeaccounname)
                    );
                }


                String recipient = Optional.ofNullable(customerList.getInfoList())
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(CustomerV::getId)
                        .orElse(null);

                String account = Optional.ofNullable(customerAccountList.getInfoList())
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(CustomerAccountV::getId)
                        .orElse(null);

                String mainDataCode = Optional.ofNullable(customerAccountList.getInfoList())
                        .filter(list -> !list.isEmpty())
                        .map(list -> list.get(0))
                        .map(CustomerAccountV::getMainDataCode)
                        .orElse(null);

                e.setRecipientCode(payeeid)
                        .setRecipient(recipient)
                        .setRecipientName(payee)

                        .setNameOfReceivingAccount(payeeaccounname)
                        .setAccount(account)
                        .setAccountCode(mainDataCode)
                        .setBankAccountNumber(payeeaccounnumber)
                        .setOpeningBank(payeeaccounbank);
            }

            ContractPayConcludeE payConcludeE = contractPayConcludeMapper.selectById(contractId);
            e.setDepartmentList(configFeignClient.getDeportList(Collections.singletonList(payConcludeE.getCommunityId())));
        });

        return contractPayConcludeExpandV.orElse(new ContractPayConcludeExpandV());
    }
}
