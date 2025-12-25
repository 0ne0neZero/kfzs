package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.domains.consts.contractset.ContractConcludeFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractSpaceResourcesE;
import com.wishare.contract.domains.mapper.contractset.ContractSpaceResourcesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.contractset.ContractSpaceResourcesV;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wishare.contract.domains.consts.contractset.ContractSpaceResourcesFieldConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@Service
@Slf4j
public class ContractSpaceResourcesService extends ServiceImpl<ContractSpaceResourcesMapper, ContractSpaceResourcesE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractSpaceResourcesMapper contractSpaceResourcesMapper;


    /**
    * 根据请求参数获取指定对象
    *
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractSpaceResourcesV> get(Long id){
        ContractSpaceResourcesE contractSpaceResourcesE = contractSpaceResourcesMapper.selectById(id);
        if (contractSpaceResourcesE != null) {
            return Optional.of(Global.mapperFacade.map(contractSpaceResourcesE, ContractSpaceResourcesV.class));
        }else {
            return Optional.empty();
        }
    }

    /**
    * 列表接口，一般用于下拉列表
    *
    * @param contractSpaceResourcesF 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public List<ContractSpaceResourcesV> list(ContractSpaceResourcesF contractSpaceResourcesF){
        QueryWrapper<ContractSpaceResourcesE> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(contractSpaceResourcesF.getContractId())) {
            queryWrapper.eq(ContractSpaceResourcesFieldConst.CONTRACT_ID, contractSpaceResourcesF.getContractId());
        }
        if (Objects.nonNull(contractSpaceResourcesF.getTenantId())) {
            queryWrapper.eq(ContractSpaceResourcesFieldConst.TENANT_ID, contractSpaceResourcesF.getTenantId());
        }
        if (Objects.nonNull(contractSpaceResourcesF.getCode())) {
            queryWrapper.eq(ContractSpaceResourcesFieldConst.CODE, contractSpaceResourcesF.getCode());
        }
        if (Objects.nonNull(contractSpaceResourcesF.getName())) {
            queryWrapper.eq(ContractSpaceResourcesFieldConst.NAME, contractSpaceResourcesF.getName());
        }
        if (Objects.nonNull(contractSpaceResourcesF.getDeleted())) {
            queryWrapper.eq(ContractSpaceResourcesFieldConst.DELETED, 0);
        }
        return Global.mapperFacade.mapAsList(contractSpaceResourcesMapper.selectList(queryWrapper),ContractSpaceResourcesV.class);
    }

    public Long save(ContractSpaceResourcesSaveF contractSpaceResourcesF){
        ContractSpaceResourcesE map = Global.mapperFacade.map(contractSpaceResourcesF, ContractSpaceResourcesE.class);
        Long id = UidHelper.nextId("contract_space_resources");
        map.setId(id);
        contractSpaceResourcesMapper.insert(map);
        return map.getId();
    }

    /**
    * 根据Id更新
    *
    * @param contractSpaceResourcesF 根据Id更新
    */
    public void update(ContractSpaceResourcesUpdateF contractSpaceResourcesF){
        ContractSpaceResourcesE map = Global.mapperFacade.map(contractSpaceResourcesF, ContractSpaceResourcesE.class);
        contractSpaceResourcesMapper.updateById(map);
    }

    /**
    *
    * @param {table.convertTableName?uncap_first}F 根据Id删除
    * @return 删除结果
    */
    public boolean remove(Long id){
        ContractSpaceResourcesE map = new ContractSpaceResourcesE();
        map.setId(id);
        contractSpaceResourcesMapper.deleteById(map);
        return true;
    }

    public void deleteByContractId(Long contractId) {
        contractSpaceResourcesMapper.deleteByContractId(contractId);
    }

    public PageV<ContractSpaceResourcesV> pageList(PageF<SearchF<ContractSpaceResourcesE>> request, String tenantId) {
        PageV<ContractSpaceResourcesV> dictPageV = new PageV<>();
        //分页
        IPage<ContractSpaceResourcesE> page = Page.of(request.getPageNum(), request.getPageSize());
        //获取查询信息
        QueryWrapper<ContractSpaceResourcesE> queryModel = request.getConditions().getQueryModel();
        queryModel.eq("deleted", 0)
                .eq("tenantId",tenantId)
                .orderByDesc("gmtCreate");
        //查询列表
        IPage<ContractSpaceResourcesE> iPage = contractSpaceResourcesMapper.selectPage(page, queryModel);
        List<ContractSpaceResourcesV> pageList = Global.mapperFacade.mapAsList(iPage.getRecords(), ContractSpaceResourcesV.class);
        dictPageV.setPageNum(iPage.getCurrent());
        dictPageV.setTotal(iPage.getTotal());
        dictPageV.setPageSize(iPage.getSize());
        dictPageV.setRecords(pageList);
        return dictPageV;
    }
}
