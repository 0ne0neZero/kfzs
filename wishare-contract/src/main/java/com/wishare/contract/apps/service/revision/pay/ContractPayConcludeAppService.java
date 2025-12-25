package com.wishare.contract.apps.service.revision.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.fo.revision.pay.report.ContractPayReportDetailListV;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.enums.revision.ContractTypeEnum;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.enums.revision.SigningMethodEnum;
import com.wishare.contract.domains.enums.revision.VirtuallyTypeEnum;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayPlanConcludeMapper;
import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.enums.ConcludeContractNatureEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayPlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayPlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPaySettlementConcludeMapper;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeTreeV;
import com.wishare.contract.domains.vo.revision.pay.ContractSignDateAttachV;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeListV;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 支出合同订立信息表
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Service
@Slf4j
public class ContractPayConcludeAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeExpandAppService contractPayConcludeExpandAppService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private AttachmentService attachmentService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;
    @Autowired
    private ContractPayPlanConcludeMapper contractPayPlanConcludeMapper;
    @Autowired
    private ContractPayConcludeMapper contractPayConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettlementConcludeMapper contractPaySettlementConcludeMapper;

    public ContractPayConcludeV get(ContractPayConcludeF contractPayConcludeF){
        return contractPayConcludeService.get(contractPayConcludeF).orElse(null);
    }

    public ContractPayConcludeListV list(ContractPayConcludeListF contractPayConcludeListF){
        return contractPayConcludeService.list(contractPayConcludeListF);
    }

    public ContractPayConcludeListV queryInfo(ContractPayConcludeListF contractPayConcludeListF){
        return contractPayConcludeService.queryInfo(contractPayConcludeListF);
    }

    public List<ContractPayConcludeV> queryContractInfo(String id){
        return contractPayConcludeService.queryContractInfo(id);
    }

    public String save(ContractPayConcludeSaveF contractPayConcludeF){
        return contractPayConcludeService.save(contractPayConcludeF);
    }

    public void update(ContractPayConcludeUpdateF contractPayConcludeF){
        contractPayConcludeService.update(contractPayConcludeF);
    }

    public boolean removeById(String id){
        return contractPayConcludeService.removeById(id);
    }

    public PageV<ContractPayConcludeV> page(PageF<ContractPayConcludePageF> request) {
        return contractPayConcludeService.page(request);
    }

    public PageV<ContractPayConcludeV> choose(PageF<ContractPayPageF> request) {
        return contractPayConcludeService.choose(request);
    }

    public PageV<ContractPayConcludeTreeV> frontPage(PageF<SearchF<ContractPayConcludeE>> request) {
        PageV<ContractPayConcludeTreeV> pageV = contractPayConcludeService.frontPage(request);
        for (ContractPayConcludeTreeV detailV : pageV.getRecords()) {
            if(detailV.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
                detailV.setChangContractAmountPj("");
            }else{
                detailV.setChangContractAmountPj(detailV.getChangContractAmount().toString());
            }
            ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
            concludeExpandF.setContractId(detailV.getId());
            ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
            if(ObjectUtils.isNotEmpty(concludeExpandV)){
                detailV.setConundertaker(concludeExpandV.getConundertaker());
            }

            //-- 字段赋值
            detailV.setContractNatureName(VirtuallyTypeEnum.parseName(detailV.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                    .setContractTypeName(ContractTypeEnum.parseName(detailV.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                    .setSigningMethodName(SigningMethodEnum.parseName(detailV.getSigningMethod()));//-- 签约方式 0 新签 1 补充协议 2 续签

            //-- 金额字段处理
            if (Objects.nonNull(detailV.getContractAmountOriginalRate())) {
                detailV.setUnPayAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getPayAmount()));
                detailV.setUnInvoiceAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getInvoiceAmount()));
            }

            //-- 推送信息处理
            detailV.setPartyAId(extractPartyAId(detailV.getPartyAId()));

            contractPayConcludeService.dealBtnShowForDetail(detailV);
        }
        for (ContractPayConcludeTreeV detailV : pageV.getRecords()) {
            if (CollectionUtils.isNotEmpty(detailV.getChildren())) {
                for (ContractPayConcludeTreeV child : detailV.getChildren()) {
                    if(child.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
                        child.setChangContractAmountPj("");
                    }else{
                        child.setChangContractAmountPj(child.getChangContractAmount().toString());
                    }

                    ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
                    concludeExpandF.setContractId(child.getId());
                    ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);

                    //承办人
                    if(ObjectUtils.isNotEmpty(concludeExpandV)){
                        child.setConundertaker(concludeExpandV.getConundertaker());
                    }

                    //-- 字段赋值
                    child.setContractNatureName(VirtuallyTypeEnum.parseName(child.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                            .setContractTypeName(ContractTypeEnum.parseName(child.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                            .setSigningMethodName(SigningMethodEnum.parseName(child.getSigningMethod()));//-- 签约方式 0 新签 1 补充协议 2 续签

                    //-- 金额字段处理
                    if (Objects.nonNull(child.getContractAmountOriginalRate())) {
                        child.setUnPayAmount(child.getContractAmountOriginalRate().subtract(child.getPayAmount()));
                        child.setUnInvoiceAmount(child.getContractAmountOriginalRate().subtract(child.getInvoiceAmount()));
                    }

                    //-- 推送信息处理
                    child.setPartyAId(extractPartyAId(child.getPartyAId()));

                    contractPayConcludeService.dealBtnShowForDetail(child);
                }
            }
        }
        return pageV;
    }

    public PageV<ContractPayConcludeTreeV> frontPageV2(PageF<SearchF<ContractPayConcludeQuery>> request) {

        PageF<SearchF<ContractPayConcludeE>> payRequest = new PageF<>();
        BeanUtils.copyProperties(request, payRequest);
        //合同业务线
        Field lineSelectField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLineSelect".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(lineSelectField)){
            if(lineSelectField.getValue().equals(ContractBusinessLineEnum.全部.getCode())){
                payRequest.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
                payRequest.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLineSelect"));
            }else{
                payRequest.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLineSelect"));
                payRequest.getConditions().getFields().stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLine".equals(field.getName())).forEach(field -> field.setValue(lineSelectField.getValue()));
            }
        }

        PageV<ContractPayConcludeTreeV> pageV = contractPayConcludeService.frontPageV2(payRequest);
        //获取合同管理类别
        List<DictionaryCode> conmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), null);
        List<DictionaryCode> agencyConmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.建管合同管理类别.getCode(), null);
        List<DictionaryCode> businessConmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.商管合同管理类别.getCode(), null);
        Map<String, List<ContractPayPlanConcludeE>> contractCountMap = new HashMap<>();
        Map<String, List<ContractPaySettlementConcludeE>> contractSettleMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(pageV.getRecords())){
            log.info("获取合同对应计划");
            LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ContractPayPlanConcludeE::getContractId, pageV.getRecords().stream().map(ContractPayConcludeTreeV::getId).collect(Collectors.toList()))
                    .eq(ContractPayPlanConcludeE::getDeleted,0);
            List<ContractPayPlanConcludeE> concludeEList = contractPayPlanConcludeMapper.selectList(queryWrapper);
            contractCountMap = concludeEList.stream().collect(Collectors.groupingBy(ContractPayPlanConcludeE::getContractId));
            //获取合同有无审批中结算单
            LambdaQueryWrapper<ContractPaySettlementConcludeE> querySettlrWrapper = new LambdaQueryWrapper<>();
            querySettlrWrapper.in(ContractPaySettlementConcludeE::getContractId, pageV.getRecords().stream().map(ContractPayConcludeTreeV::getId).collect(Collectors.toList()))
                    .notIn(ContractPaySettlementConcludeE::getPid,0)
                    .eq(ContractPaySettlementConcludeE::getReviewStatus,ReviewStatusEnum.审批中.getCode())
                    .eq(ContractPaySettlementConcludeE::getDeleted,0)
                    .orderByAsc(ContractPaySettlementConcludeE::getGmtCreate);  ;
            List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(querySettlrWrapper);
            contractSettleMap = settlementList.stream().collect(Collectors.groupingBy(ContractPaySettlementConcludeE::getContractId));
        }
        //下面的维持不变
        for (ContractPayConcludeTreeV detailV : pageV.getRecords()) {
            //是否开始NK（履约状态：正在履行，已推送合同库，已生成结算计划，无关联的NK合同或关联的NK合同已结束NK时）
            detailV.setIsStartNK(detailV.getStatus().equals(ContractRevStatusEnum.正在履行.getCode())
                && Objects.nonNull(detailV.getContractNature()) && detailV.getContractNature().equals(ConcludeContractNatureEnum.SUCCESS.getCode())
                && detailV.getContractType().equals(ContractTypeEnum.普通合同.getCode())
                && CollectionUtils.isNotEmpty(contractCountMap.get(detailV.getId()))
                && CollectionUtils.isEmpty(contractSettleMap.get(detailV.getId()))
                && NkStatusEnum.未开启.getCode().equals(detailV.getNkStatus())
            );
            //是否结束NK
            detailV.setIsEndNK(detailV.getNkStatus().equals(NkStatusEnum.已开启.getCode()));
            detailV.setShowNkBpm(detailV.getNkStatus().equals(NkStatusEnum.关闭中.getCode()) ? detailV.getBpmProcInstId() : null);
            if(detailV.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
                detailV.setChangContractAmountPj("");
            }else{
                detailV.setChangContractAmountPj(detailV.getChangContractAmount().toString());
            }
            ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
            concludeExpandF.setContractId(detailV.getId());
            ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
            if(ObjectUtils.isNotEmpty(concludeExpandV)){
                detailV.setConundertaker(concludeExpandV.getConundertaker());
            }

            //-- 字段赋值
            detailV.setContractNatureName(VirtuallyTypeEnum.parseName(detailV.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                    .setContractTypeName(ContractTypeEnum.parseName(detailV.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                    .setSigningMethodName(SigningMethodEnum.parseName(detailV.getSigningMethod()))//-- 签约方式 0 新签 1 补充协议 2 续签
                    .setContractBusinessLineName(ContractBusinessLineEnum.parseName(detailV.getContractBusinessLine()));
            //-- 金额字段处理
            if (Objects.nonNull(detailV.getContractAmountOriginalRate())) {
                detailV.setUnPayAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getPayAmount()));
                detailV.setUnInvoiceAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getInvoiceAmount()));
            }

            //-- 推送信息处理
            detailV.setPartyAId(extractPartyAId(detailV.getPartyAId()));
            //合同管理类别汉化
            if(StringUtils.isNotEmpty(detailV.getConmanagetype())) {
                DictionaryCode conmanagetypeDict = new DictionaryCode();
                if(ContractBusinessLineEnum.物管.getCode().equals(detailV.getContractBusinessLine())){
                    conmanagetypeDict = conmanageTypeDictList.stream().filter(d -> d.getCode().equals(detailV.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                }else if (ContractBusinessLineEnum.建管.getCode().equals(detailV.getContractBusinessLine())){
                    conmanagetypeDict = agencyConmanageTypeDictList.stream().filter(d -> d.getCode().equals(detailV.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                }else{
                    conmanagetypeDict = businessConmanageTypeDictList.stream().filter(d -> d.getCode().equals(detailV.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                }
                //合同管理类别描述
                detailV.setConmanagetypeName(conmanagetypeDict.getName());
            }

            //if(detailV.getReviewStatus() == 2 && CollectionUtils.isEmpty(contractCountMap.get(detailV.getId()))){
            if(detailV.getReviewStatus() == 2 ){
                detailV.setIsShowXz(Boolean.TRUE);
            }
            contractPayConcludeService.dealBtnShowForDetail(detailV);
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

    public ContractPayConcludeListV queryInfoNew(ContractPayConcludeListF contractPayConcludeListF) {
        return contractPayConcludeService.queryInfoNew(contractPayConcludeListF);
    }

    public void handleMergePayFund(String pid) {
        contractPayConcludeService.handleMergePayFund(pid);
    }

    public void handleMergePayFund(MergePayFundPidF mergePayFundPidF) {
        contractPayConcludeService.batchHandleMergePayFund(mergePayFundPidF);
    }

    /**
     * 查询合同签约日期和扫描件信息
     *
     * @param id
     * @return
     */
    public ContractSignDateAttachV contractSignDateAttach(String id) {
        ContractSignDateAttachV contractSignDateAttachV = new ContractSignDateAttachV();
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
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
