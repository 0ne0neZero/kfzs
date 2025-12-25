package com.wishare.contract.domains.service.contractset;

import com.wishare.contract.domains.entity.contractset.ContractTemplateLogE;
import com.wishare.contract.domains.mapper.contractset.ContractTemplateLogRepository;
import com.wishare.starter.beans.IdentityInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractTemplateLogDomainsService {

    private final ContractTemplateLogRepository contractTemplateLogRepository;

}
