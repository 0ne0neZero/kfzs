package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.contractset.ContractMaterialListF;
import com.wishare.contract.apps.fo.contractset.ContractMaterialListSaveF;
import com.wishare.contract.apps.fo.contractset.ContractSpaceResourcesF;
import com.wishare.contract.domains.consts.contractset.ContractSpaceResourcesFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractMaterialListE;
import com.wishare.contract.domains.entity.contractset.ContractSpaceResourcesE;
import com.wishare.contract.domains.mapper.contractset.ContractMaterialListMapper;
import com.wishare.contract.domains.vo.contractset.ContractMaterialListV;
import com.wishare.contract.domains.vo.contractset.ContractSpaceResourcesV;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.UidHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 合同采购物资清单
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@Service
@Slf4j
public class ContractMaterialListService extends ServiceImpl<ContractMaterialListMapper, ContractMaterialListE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractMaterialListMapper contractMaterialListMapper;

    public Long save(ContractMaterialListSaveF f){
        ContractMaterialListE map = Global.mapperFacade.map(f, ContractMaterialListE.class);
        Long id = UidHelper.nextId("contract_material_list");
        map.setId(id);
        contractMaterialListMapper.insert(map);
        return map.getId();
    }

    /**
    * 根据Id更新
    */
    public void update(ContractMaterialListSaveF f){
        ContractMaterialListE map = Global.mapperFacade.map(f, ContractMaterialListE.class);
        contractMaterialListMapper.updateById(map);
    }


    public void deleteByContractId(Long contractId) {
        contractMaterialListMapper.deleteByContractId(contractId);
    }

    public List<ContractMaterialListV> list(ContractMaterialListF f){
        QueryWrapper<ContractMaterialListE> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(f.getContractId())) {
            queryWrapper.eq(ContractSpaceResourcesFieldConst.CONTRACT_ID, f.getContractId());
        }
        return Global.mapperFacade.mapAsList(contractMaterialListMapper.selectList(queryWrapper),ContractMaterialListV.class);
    }
}
