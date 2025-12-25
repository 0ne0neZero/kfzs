package com.wishare.contract.domains.service.revision.pay.settdetails;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsUpdateF;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPaySettDetailsMapper;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanDetailsV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/11:17
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractPaySettDetailsService extends ServiceImpl<ContractPaySettDetailsMapper, ContractPaySettDetailsE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettDetailsMapper contractPaySettDetailsMapper;

    @Nullable
    public ContractPaySettDetailsV getDetailsById(String id){
        ContractPaySettDetailsE map = contractPaySettDetailsMapper.selectById(id);
        ContractPaySettDetailsV map1 = Global.mapperFacade.map(map, ContractPaySettDetailsV.class);
        return map1;
    }

    public String save(ContractPaySettDetailsSaveF contractPayFundF) {
        ContractPaySettDetailsE map = Global.mapperFacade.map(contractPayFundF, ContractPaySettDetailsE.class);
        map.setTenantId(tenantId());
        contractPaySettDetailsMapper.insert(map);
        return map.getId();
    }


    /**
     * 根据Id更新
     *
     * @param contractPaySettDetailsUpdateF 根据Id更新
     */
    public void update(ContractPaySettDetailsUpdateF contractPaySettDetailsUpdateF) {
        if (contractPaySettDetailsUpdateF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPaySettDetailsE map = Global.mapperFacade.map(contractPaySettDetailsUpdateF, ContractPaySettDetailsE.class);
        contractPaySettDetailsMapper.updateById(map);
    }


    /**
     *
     * @param id 根据Id删除
     * @return 删除结果
     */
    public boolean removeById(String id){
        contractPaySettDetailsMapper.deleteById(id);
        return true;
    }


    @Nullable
    public List<ContractPaySettDetailsV> getDetailsBySettlementId(String id){
        LambdaQueryWrapper<ContractPaySettDetailsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPaySettDetailsE::getSettlementId, id);
        List<ContractPaySettDetailsE> contractPaySettlementConcludeEList = contractPaySettDetailsMapper.selectList(queryWrapper);
        List<ContractPaySettDetailsV> contractPaySettDetailsVList = Global.mapperFacade.mapAsList(contractPaySettlementConcludeEList, ContractPaySettDetailsV.class);
        return contractPaySettDetailsVList;
    }
}
