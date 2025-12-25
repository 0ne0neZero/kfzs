package com.wishare.finance.domains.configure.chargeitem.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxItemCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxItemCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemD;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxChargeItemRelationE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxChargeItemRelationRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxItemRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.easyexcel.BaseExcelDataListener;
import com.wishare.finance.infrastructure.easyexcel.TaxItemData;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 税目引用层
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaxItemDomainService {

    private final TaxItemRepository taxItemRepository;

    private final TaxChargeItemRelationRepository taxChargeItemRelationRepository;

    /**
     * 新增税种信息
     *
     * @param command 新增税目命令
     * @return TaxItemE
     */
    public TaxItemE addTaxItem(AddTaxItemCommand command) {
        List<Long> chargeItemIdList = command.getChargeItemIdList();
        TaxItemE taxItemE = Global.mapperFacade.map(command, TaxItemE.class);
        taxItemE.generateId();

        //校验费项是否关联其他税目
        /*if (CollectionUtils.isNotEmpty(chargeItemIdList)) {
            List<TaxChargeItemRelationE> otherTaxRelationCharge = taxChargeItemRelationRepository.queryByChargeItemIdList(chargeItemIdList, taxItemE.getId());
            ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(otherTaxRelationCharge), ErrorMessage.TAX_CHARGE_ITEM_RELATION_OTHER);
        }

        //保存关联关系
        List<TaxChargeItemRelationE> taxChargeItemRelationList = new ArrayList<>();

        for (Long chargeItemId : chargeItemIdList) {
            TaxChargeItemRelationE relationE = new TaxChargeItemRelationE();
            relationE.generateId();
            relationE.setChargeItemId(chargeItemId);
            relationE.setTaxItemId(taxItemE.getId());
            taxChargeItemRelationList.add(relationE);
        }
        taxChargeItemRelationRepository.saveBatch(taxChargeItemRelationList);
        */
        taxItemRepository.save(taxItemE);
        return taxItemE;
    }

    /**
     * 更新税目
     *
     * @param command 更新税目命令
     * @return Boolean
     */
    public Boolean update(UpdateTaxItemCommand command) {
        Long taxItemId = command.getId();
        List<Long> chargeItemIdList = command.getChargeItemIdList();
        String code = command.getCode();

        //校验费项是否存在
       /* TaxItemE taxItemE = taxItemRepository.getById(taxItemId);
        ErrorAssertUtil.notNullThrow300(taxItemE, ErrorMessage.TAX_ITEM_NOT_EXIST);*/

        //校验编码是否存在
        TaxItemE existCodeTaxItemE = taxItemRepository.getByCode(code, taxItemId);
        if (existCodeTaxItemE != null && !command.getCode().equals(existCodeTaxItemE.getCode())) {
            ErrorAssertUtil.isNullThrow300(existCodeTaxItemE, ErrorMessage.TAX_ITEM_CODE_EXIST);
        }
        TaxItemE taxItemE = Global.mapperFacade.map(command, TaxItemE.class);

        //校验费项是否关联其他税目
        /*if (CollectionUtils.isNotEmpty(chargeItemIdList)) {
            List<TaxChargeItemRelationE> otherTaxRelationCharge = taxChargeItemRelationRepository.queryByChargeItemIdList(chargeItemIdList, taxItemE.getId());
            ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(otherTaxRelationCharge), ErrorMessage.TAX_CHARGE_ITEM_RELATION_OTHER);
        }

        //获取税目费项关联关系
        List<TaxChargeItemRelationE> taxChargeItemRelationList = taxChargeItemRelationRepository.queryByTaxItemIdList(List.of(taxItemId));
        List<TaxChargeItemRelationE> deleteRelationList = new ArrayList<>();

        //找出要删除和新增的关联关系
        for (TaxChargeItemRelationE taxChargeItemRelationE : taxChargeItemRelationList) {
            if (!chargeItemIdList.contains(taxChargeItemRelationE.getChargeItemId())) {
                deleteRelationList.add(taxChargeItemRelationE);
            } else {
                chargeItemIdList.remove(taxChargeItemRelationE.getChargeItemId());
            }
        }
        List<TaxChargeItemRelationE> addRelationList = new ArrayList<>();
        for (Long chargeItemId : chargeItemIdList) {
            TaxChargeItemRelationE relationE = new TaxChargeItemRelationE();
            relationE.generateId();
            relationE.setTaxItemId(taxItemId);
            relationE.setChargeItemId(chargeItemId);
            addRelationList.add(relationE);
        }
        taxChargeItemRelationRepository.removeBatchByIds(deleteRelationList);
        taxChargeItemRelationRepository.saveBatch(addRelationList);
        */
        return taxItemRepository.updateById(taxItemE);
    }

    /**
     * 根据费项id查询税目
     *
     * @param chargeItemIdList 费项id集合
     * @return List
     */
    public List<TaxChargeItemD> queryByChargeIdList(List<Long> chargeItemIdList) {
        List<TaxChargeItemRelationE> taxChargeItemRelationList = taxChargeItemRelationRepository.queryByChargeItemIdList(chargeItemIdList, null);
        List<Long> taxItemIdList = taxChargeItemRelationList.stream().map(TaxChargeItemRelationE::getTaxItemId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(taxItemIdList)){
            return new ArrayList<>();
        }
        List<TaxItemE> taxItemList = taxItemRepository.listByIds(taxItemIdList);
        //根据税目id进行分组
        Map<Long, List<TaxItemE>> collect = taxItemList.stream().collect(Collectors.groupingBy(TaxItemE::getId));
        List<TaxChargeItemD> taxItemDtoList = new ArrayList<>();
        for (TaxChargeItemRelationE taxChargeItemRelationE : taxChargeItemRelationList) {
            TaxChargeItemD taxItemD = new TaxChargeItemD();
            taxItemD.setChargeItemId(taxChargeItemRelationE.getChargeItemId());
            List<TaxItemE> taxItemByCharge = collect.get(taxChargeItemRelationE.getTaxItemId());
            taxItemD.setTaxItem(CollectionUtils.isEmpty(taxItemByCharge) ? null : taxItemByCharge.get(0));
            taxItemDtoList.add(taxItemD);
        }
        return taxItemDtoList;
    }

    /**
     * 分页查询税目
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<TaxItemD> queryByPage(PageF<SearchF<TaxItemE>> queryF) {
        QueryWrapper<TaxItemE> queryWrapper = queryF.getConditions().getQueryModel();
        Page<TaxItemE> page = taxItemRepository.page(Page.of(queryF.getPageNum(), queryF.getPageSize()), queryWrapper);
        List<TaxItemE> records = page.getRecords();
        List<TaxItemD> pageResult = Global.mapperFacade.mapAsList(records,TaxItemD.class);
        /*List<Long> taxItemIdList = records.stream().map(TaxItemE::getId).collect(Collectors.toList());

        List<TaxChargeItemRelationD> taxChargeItemRelationList = taxChargeItemRelationRepository.queryChargeByTaxItemIdList(taxItemIdList);

        List<TaxItemD> pageResult = new ArrayList<>();
        Map<Long, List<TaxChargeItemRelationD>> collect = taxChargeItemRelationList.stream().collect(Collectors.groupingBy(TaxChargeItemRelationD::getTaxItemId));
        for (TaxItemE taxItemE : records) {
            TaxItemD taxItemD = Global.mapperFacade.map(taxItemE, TaxItemD.class);
            taxItemD.setChargeItemList(collect.get(taxItemE.getId()));
            pageResult.add(taxItemD);
        }*/
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), pageResult);
    }

    /**
     * 根据id获取税目
     *
     * @param id 税目id
     * @return TaxItemD
     */
    public TaxItemD queryById(Long id) {
        //校验费项是否存在
        TaxItemE taxItemE = taxItemRepository.getById(id);
        ErrorAssertUtil.notNullThrow300(taxItemE, ErrorMessage.TAX_ITEM_NOT_EXIST);

        TaxItemD taxItemD = Global.mapperFacade.map(taxItemE, TaxItemD.class);
        /*List<TaxChargeItemRelationD> taxChargeItemRelationList = taxChargeItemRelationRepository.queryChargeByTaxItemIdList(List.of(id));
        if (CollectionUtils.isNotEmpty(taxChargeItemRelationList)) {
            taxItemD.setChargeItemList(taxChargeItemRelationList);
        }*/
        return taxItemD;
    }

    /**
     * 根据id删除税目
     *
     * @param id 税目id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        LambdaUpdateWrapper<TaxChargeItemRelationE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TaxChargeItemRelationE::getTaxItemId,id);
        List<TaxChargeItemRelationE> list = taxChargeItemRelationRepository.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            throw BizException.throw400("该税目已关联税收商品，请解除关系");
        }
        return taxItemRepository.removeById(id);
//        return taxChargeItemRelationRepository.removeByTaxItemId(id);
    }

    /**
     * 导入税目
     *
     * @param file file
     * @return Boolean
     */
    public Boolean importTaxItem(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<TaxItemData> taxItemDataList = new ArrayList<>();
            //读取excel文件数据
            BaseExcelDataListener<TaxItemData> baseDataListener = new BaseExcelDataListener<>(taxItemDataList::addAll);
            EasyExcel.read(inputStream, TaxItemData.class, baseDataListener).sheet().doRead();
            //校验编码是否存在
            List<String> codeList = taxItemDataList.stream().map(TaxItemData::getCode).collect(Collectors.toList());
            List<TaxItemE> existTaxItemList = taxItemRepository.getByCodeList(codeList);
            if (CollectionUtils.isNotEmpty(existTaxItemList)) {
                String msg = existTaxItemList.stream().map(TaxItemE::getCode).collect(Collectors.joining(","));
                throw BizException.throw400(ErrorMessage.TAX_ITEM_CODE_EXIST.getErrMsg() + msg);
            }

            //获取关联的费项
            List<String> chargeItemCodeCollect = taxItemDataList.stream().map(TaxItemData::getChargeItemCode).collect(Collectors.toList());
            List<String> chargeItemCodeList = new ArrayList<>();
            for (String chargeItemCode : chargeItemCodeCollect) {
                chargeItemCodeList.addAll(Arrays.asList(chargeItemCode.split("\n")));
            }
            ChargeItemRepository chargeItemRepository = Global.ac.getBean(ChargeItemRepository.class);
            List<ChargeItemE> chargeItemList = chargeItemRepository.queryByCodeList(chargeItemCodeList);
            Map<String, List<ChargeItemE>> collect = chargeItemList.stream().collect(Collectors.groupingBy(ChargeItemE::getCode));

            List<TaxItemE> taxItemList = new ArrayList<>();
            List<TaxChargeItemRelationE> taxChargeItemRelationList = new ArrayList<>();
            for (TaxItemData object : taxItemDataList) {
                TaxItemE taxItemE = new TaxItemE();
                taxItemE.generateId();
                taxItemE.setName(object.getName());
                taxItemE.setCode(object.getCode());
                String[] split = object.getChargeItemCode().split("\n");
                for (String code : split) {
                    TaxChargeItemRelationE relationE = new TaxChargeItemRelationE();
                    relationE.generateId();
                    relationE.setChargeItemId(collect.get(code).get(0).getId());
                    relationE.setTaxItemId(taxItemE.getId());
                    taxChargeItemRelationList.add(relationE);
                }
                taxItemList.add(taxItemE);
            }
            taxItemRepository.saveBatch(taxItemList);
            taxChargeItemRelationRepository.saveBatch(taxChargeItemRelationList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 查询税目信息列表
     *
     * @return List<TaxItemD>
     */
    public List<TaxItemD> queryTaxItemList() {
        List<TaxItemE> taxItemES = taxItemRepository.list();
       return Global.mapperFacade.mapAsList(taxItemES,TaxItemD.class);
    }
}
