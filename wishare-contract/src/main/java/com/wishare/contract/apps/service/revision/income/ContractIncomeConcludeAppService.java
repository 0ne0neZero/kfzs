package com.wishare.contract.apps.service.revision.income;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.MergePayFundPidF;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeCorrectionE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeCorrectionMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomePlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayPlanConcludeMapper;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeListV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeTreeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeTreeV;
import com.wishare.contract.domains.vo.revision.pay.ContractSignDateAttachV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
* <p>
* 收入合同订立信息表
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Service
@Slf4j
public class ContractIncomeConcludeAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeService contractIncomeConcludeService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeExpandAppService contractIncomeConcludeExpandAppService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private AttachmentService attachmentService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;
    @Autowired
    private ContractIncomePlanConcludeMapper contractIncomePlanConcludeMapper;
    @Autowired
    private ContractIncomeConcludeCorrectionMapper contractIncomeConcludeCorrectionMapper;

    public ContractIncomeConcludeV get(ContractIncomeConcludeF contractIncomeConcludeF){
        return contractIncomeConcludeService.get(contractIncomeConcludeF).orElse(null);
    }

    public ContractIncomeConcludeListV list(ContractIncomeConcludeListF contractIncomeConcludeListF){
        return contractIncomeConcludeService.list(contractIncomeConcludeListF);
    }

    public ContractIncomeConcludeListV queryInfo(ContractIncomeConcludeListF contractIncomeConcludeListF){
        return contractIncomeConcludeService.queryInfo(contractIncomeConcludeListF);
    }

    public String save(ContractIncomeConcludeSaveF contractIncomeConcludeF){
        return contractIncomeConcludeService.save(contractIncomeConcludeF);
    }

    public void update(ContractIncomeConcludeUpdateF contractIncomeConcludeF){
        contractIncomeConcludeService.update(contractIncomeConcludeF);
    }

    public boolean removeById(String id){
        return contractIncomeConcludeService.removeById(id);
    }

    public PageV<ContractIncomeConcludeV> page(PageF<ContractIncomeConcludePageF> request) {
        return contractIncomeConcludeService.page(request);
    }

    public PageV<ContractIncomeConcludeV> choose(PageF<ContractIncomePageF> request) {
        return contractIncomeConcludeService.choose(request);
    }

    public PageV<ContractIncomeConcludeTreeV> frontPageV2(PageF<SearchF<ContractIncomeConcludeQuery>> request) {
        PageF<SearchF<ContractIncomeConcludeE>> incomeRequest = new PageF<>();
        BeanUtils.copyProperties(request, incomeRequest);
        //合同业务线
        Field lineSelectField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLineSelect".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(lineSelectField)){
            if(lineSelectField.getValue().equals(ContractBusinessLineEnum.全部.getCode())){
                incomeRequest.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
                incomeRequest.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLineSelect"));
            }else{
                //incomeRequest.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
                incomeRequest.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLineSelect"));
                incomeRequest.getConditions().getFields().stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLine".equals(field.getName())).forEach(field -> field.setValue(lineSelectField.getValue()));
                //incomeRequest.getConditions().getQueryModel().apply("contractBusinessLine = {0}",  lineSelectField.getValue());
            }
        }
        PageV<ContractIncomeConcludeTreeV> pageV = contractIncomeConcludeService.frontPageV2(incomeRequest);
        //获取合同管理类别
        List<DictionaryCode> conmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同管理类别.getCode(), null);
        List<DictionaryCode> agencyConmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.建管合同管理类别.getCode(), null);
/*        Map<String, List<ContractIncomePlanConcludeE>> contractCountMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(pageV.getRecords())){
            LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ContractIncomePlanConcludeE::getContractId, pageV.getRecords().stream().map(ContractIncomeConcludeTreeV::getId).collect(Collectors.toList()))
                    .eq(ContractIncomePlanConcludeE::getDeleted,0);
            List<ContractIncomePlanConcludeE> concludeEList = contractIncomePlanConcludeMapper.selectList(queryWrapper);
            contractCountMap = concludeEList.stream().collect(Collectors.groupingBy(ContractIncomePlanConcludeE::getContractId));
        }*/
        QueryWrapper<ContractIncomeConcludeCorrectionE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractIncomeConcludeCorrectionE.CORRECTION_STATUS, CorrectionStatusEnum.审批中.getCode())
                .eq(ContractIncomeConcludeCorrectionE.DELETED,0);
        List<ContractIncomeConcludeCorrectionE> correctionList = contractIncomeConcludeCorrectionMapper.selectList(queryWrapper);
        List<String> correctionIdList = new ArrayList<>();
        correctionIdList.add("修正合同ID");
        if(CollectionUtils.isNotEmpty(correctionList)){
            correctionIdList.addAll(correctionList.stream().map(ContractIncomeConcludeCorrectionE::getContractId).collect(Collectors.toList()));
        }
        for (ContractIncomeConcludeTreeV detailV : pageV.getRecords()) {
            if(Objects.isNull(detailV.getChangContractAmount()) || detailV.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
                detailV.setChangContractAmountPj("");
            }else{
                detailV.setChangContractAmountPj(detailV.getChangContractAmount().toString());
            }

            ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
            concludeExpandF.setContractId(detailV.getId());
            ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);

            //承办人
            if(ObjectUtils.isNotEmpty(concludeExpandV)){
                detailV.setConundertaker(concludeExpandV.getConundertaker());
            }

            //-- 字段赋值
            detailV.setContractNatureName(VirtuallyTypeEnum.parseName(detailV.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                    .setContractTypeName(ContractTypeEnum.parseName(detailV.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                    .setSigningMethodName(SigningMethodEnum.parseName(detailV.getSigningMethod()))//-- 签约方式 0 新签 1 补充协议 2 续签
                    .setContractBusinessLineName(ContractBusinessLineEnum.parseName(detailV.getContractBusinessLine()));

            if (Objects.nonNull(detailV.getContractAmountOriginalRate()) && Objects.nonNull(detailV.getCollectAmount()) && Objects.nonNull(detailV.getInvoiceAmount())) {
                //-- 金额字段计算
                detailV.setUnCollectAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getCollectAmount()));
                detailV.setUnInvoiceAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getInvoiceAmount()));
            }

            //-- 推送信息处理
            detailV.setPartyAId(extractPartyAId(detailV.getPartyAId()));

            //合同管理类别汉化
            if(StringUtils.isNotEmpty(detailV.getConmanagetype())) {
                DictionaryCode conmanagetypeDict = new DictionaryCode();
                if(ContractBusinessLineEnum.物管.getCode().equals(detailV.getContractBusinessLine())){
                    conmanagetypeDict = conmanageTypeDictList.stream().filter(d -> d.getCode().equals(detailV.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                }else{
                    conmanagetypeDict = agencyConmanageTypeDictList.stream().filter(d -> d.getCode().equals(detailV.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                }
                //合同管理类别描述
                detailV.setConmanagetypeName(conmanagetypeDict.getName());
            }
            //if(detailV.getReviewStatus() == 2 && CollectionUtils.isEmpty(contractCountMap.get(detailV.getId()))){
            if(detailV.getReviewStatus() == 2 ){
                detailV.setIsShowXz(Boolean.TRUE);
                //修正状态判断
                if((detailV.getContractNature() != null && detailV.getContractNature() == 1) && !correctionIdList.contains(detailV.getId()) && !ContractRevStatusEnum.合同停用.getCode().equals(detailV.getStatus()) && !ContractRevStatusEnum.合同终止.getCode().equals(detailV.getStatus()) && !ContractRevStatusEnum.未生效.getCode().equals(detailV.getStatus())){
                    detailV.setIsIncomeCorrection(Boolean.TRUE);
                }
            }
            contractIncomeConcludeService.dealBtnShowForDetail(detailV);
        }
        return pageV;
    }

    public PageV<ContractIncomeConcludeTreeV> frontPage(PageF<SearchF<ContractIncomeConcludeE>> request) {
        PageV<ContractIncomeConcludeTreeV> pageV = contractIncomeConcludeService.frontPage(request);
        for (ContractIncomeConcludeTreeV detailV : pageV.getRecords()) {
            if(detailV.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
                detailV.setChangContractAmountPj("");
            }else{
                detailV.setChangContractAmountPj(detailV.getChangContractAmount().toString());
            }

            ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
            concludeExpandF.setContractId(detailV.getId());
            ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);

            //承办人
            if(ObjectUtils.isNotEmpty(concludeExpandV)){
                detailV.setConundertaker(concludeExpandV.getConundertaker());
            }

            //-- 字段赋值
            detailV.setContractNatureName(VirtuallyTypeEnum.parseName(detailV.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                    .setContractTypeName(ContractTypeEnum.parseName(detailV.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                    .setSigningMethodName(SigningMethodEnum.parseName(detailV.getSigningMethod()));//-- 签约方式 0 新签 1 补充协议 2 续签

            if (Objects.nonNull(detailV.getContractAmountOriginalRate()) && Objects.nonNull(detailV.getCollectAmount()) && Objects.nonNull(detailV.getInvoiceAmount())) {
                //-- 金额字段计算
                detailV.setUnCollectAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getCollectAmount()));
                detailV.setUnInvoiceAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getInvoiceAmount()));
            }

            //-- 推送信息处理
            detailV.setPartyAId(extractPartyAId(detailV.getPartyAId()));

            contractIncomeConcludeService.dealBtnShowForDetail(detailV);
        }
        for (ContractIncomeConcludeTreeV detailV : pageV.getRecords()) {
            if (CollectionUtils.isNotEmpty(detailV.getChildren())) {
                for (ContractIncomeConcludeTreeV child : detailV.getChildren()) {
                    if(child.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
                        child.setChangContractAmountPj("");
                    }else{
                        child.setChangContractAmountPj(child.getChangContractAmount().toString());
                    }

                    ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
                    concludeExpandF.setContractId(child.getId());
                    ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);

                    //承办人
                    if(ObjectUtils.isNotEmpty(concludeExpandV)){
                        child.setConundertaker(concludeExpandV.getConundertaker());
                    }

                    //-- 字段赋值
                    child.setContractNatureName(VirtuallyTypeEnum.parseName(child.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                            .setContractTypeName(ContractTypeEnum.parseName(child.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                            .setSigningMethodName(SigningMethodEnum.parseName(child.getSigningMethod()));//-- 签约方式 0 新签 1 补充协议 2 续签

                    if (Objects.nonNull(child.getContractAmountOriginalRate()) && Objects.nonNull(child.getCollectAmount()) && Objects.nonNull(child.getInvoiceAmount())) {
                        //-- 金额字段计算
                        child.setUnCollectAmount(child.getContractAmountOriginalRate().subtract(child.getCollectAmount()));
                        child.setUnInvoiceAmount(child.getContractAmountOriginalRate().subtract(child.getInvoiceAmount()));
                    }

                    //-- 推送信息处理
                    child.setPartyAId(extractPartyAId(child.getPartyAId()));

                    contractIncomeConcludeService.dealBtnShowForDetail(child);
                }
            }
        }
        return pageV;
    }

    /**
     * 提取推送信息，如果推送信息为空或者是"成功"，直接返回
     * 否则尝试转为json，转成功，提取message信息，否则直接返回
     *
     * @param retMessage
     * @return
     */
    private String extractPartyAId(String retMessage) {
        if (StringUtils.isBlank(retMessage) || StringUtils.equals("成功", retMessage)) {
            return retMessage;
        }
        try {
            JSONObject msgJsonObject = JSON.parseObject(retMessage);
            if (msgJsonObject.containsKey("message")) {
                return msgJsonObject.getString("message");
            }
            return retMessage;
        } catch (Exception e) {
            return retMessage;
        }
    }

    public void contractInfoToFxm(List<String> contractIds) {
        contractIds.forEach(contractId -> contractIncomeConcludeService.contractInfoToFxmExcludeFormId(contractId));
    }

    public ContractIncomeConcludeListV queryInfoNew(ContractIncomeConcludeListF contractIncomeConcludeListF) {
        return contractIncomeConcludeService.queryInfoNew(contractIncomeConcludeListF);
    }


    public void handleMergePayFund(String pid) {
        contractIncomeConcludeService.handleMergeIncomeFund(pid);
    }

    public void handleMergePayFund(MergePayFundPidF mergePayFundPidF) {
        contractIncomeConcludeService.batchHandleMergePayFund(mergePayFundPidF);
    }

    /**
     * 查询合同签约日期和扫描件信息
     *
     * @param id
     * @return
     */
    public ContractSignDateAttachV contractSignDateAttach(String id) {
        ContractSignDateAttachV contractSignDateAttachV = new ContractSignDateAttachV();
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        if (ObjectUtils.isEmpty(concludeE)) {
            return contractSignDateAttachV;
        }
        contractSignDateAttachV.setSignDate(concludeE.getSignDate());
        List<AttachmentE> attachmentES = attachmentService.list(Wrappers.<AttachmentE>lambdaQuery()
                .eq(AttachmentE::getBusinessId, id)
                .eq(AttachmentE::getBusinessType, 1002)
                .eq(AttachmentE::getDeleted, 0)
                .eq(AttachmentE::getTenantId, ThreadLocalUtil.curIdentityInfo().getTenantId()));
        if (CollectionUtils.isNotEmpty(attachmentES)) {
            ZJFileVo zjFileVo = new ZJFileVo();
            BeanUtils.copyProperties(attachmentES.get(0), zjFileVo);
            zjFileVo.setFileId(attachmentES.get(0).getFileuuid());
            contractSignDateAttachV.setFileInfo(zjFileVo);
        }
        return contractSignDateAttachV;
    }
}
