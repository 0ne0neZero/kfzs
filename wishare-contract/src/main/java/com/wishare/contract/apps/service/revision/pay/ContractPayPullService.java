package com.wishare.contract.apps.service.revision.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.ContractPayAddF;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.clients.SpaceFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.fo.ContractBasePullF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalDataF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalF;
import com.wishare.contract.apps.remote.vo.*;
import com.wishare.contract.apps.remote.vo.revision.CustomerRv;
import com.wishare.contract.apps.remote.vo.revision.SupplierRv;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeExpandE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.template.ContractRecordInfoE;
import com.wishare.contract.domains.enums.revision.ActionTypeEnum;
import com.wishare.contract.domains.enums.revision.ContractTypeEnum;
import com.wishare.contract.domains.mapper.contractset.ContractProcessRecordMapper;
import com.wishare.contract.domains.mapper.revision.attachment.AttachmentMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeExpandMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.fund.ContractPayFundMapper;
import com.wishare.contract.domains.mapper.revision.template.ContractRecordInfoMapper;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.service.revision.pay.fund.ContractPayFundService;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalResultV;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeDetailV;
import com.wishare.contract.infrastructure.utils.query.LambdaQueryWrapperX;
import com.wishare.contract.infrastructure.utils.query.WrapperX;
import com.wishare.owl.exception.OwlBizException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @Description： 支出合同业务方法-合同改版
 * @Author： chenglong
 * @since： 2023/6/25  13:54
 */
@Service
@Slf4j
public class ContractPayPullService {
    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private SpaceFeignClient spaceFeignClient;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractProcessRecordMapper contractProcessRecordMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private AttachmentMapper attachmentMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeExpandMapper contractPayConcludeExpandMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayFundMapper contractPayFundMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRecordInfoMapper contractRecordInfoMapper;
    @Value("${contract.devFlag:0}")
    private Integer devFlag;

    @Value("${contract.checkApproval:true}")
    private Boolean checkApproval;
    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayFundService contractPayFundService;
    @Autowired
    @Lazy
    private ContractPayBusinessService contractPayBusinessService;

    /**
     * 合同信息推送中交
     *
     * @param form
     * @return
     */
    @Async
    public void dealContractPull(String id, ContractPayAddF form) {

        //附件fileid准备
        List<String> fjxxfileids = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(form.getFjxx())) {
            form.getFjxx().forEach(f -> {
                fjxxfileids.add(f.getFileId());
            });
        }
        ContractPayConcludeDetailV detailV = new ContractPayConcludeDetailV();
        detailV.setContractId(id);
        ContractIncomePullF pullF = convertToContractPullF(detailV,form);
        String requestBody = converToJSONStr(pullF);

        //国家处理
        if (StringUtils.isNotEmpty(requestBody) && requestBody.contains("CHI")) {
            requestBody = requestBody.replace("CHI", "chi");
        }

        //附件id重新转小写
        for (int i = 0; i < fjxxfileids.size(); i++) {
            requestBody = requestBody.replace(fjxxfileids.get(i).toUpperCase(), fjxxfileids.get(i));
        }

        System.out.println("推送中交合同請求報文打印========" + requestBody);
        if(StringUtils.isNotEmpty(requestBody)) {
            ContractBasePullF contractBasePullF = new ContractBasePullF();
            contractBasePullF.setRequestBody(requestBody);
            externalFeignClient.contractPull(contractBasePullF);
        }
    }


    public String dealContractPull(String requestBody, Integer type, String id) {
        //附件fileid准备
        List<String> fjxxfileids = new ArrayList<>();

        //国家处理
        if (StringUtils.isNotEmpty(requestBody) && requestBody.contains("CHI")) {
            requestBody = requestBody.replace("CHI", "chi");
        }
        //附件id重新转小写
        for (int i = 0; i < fjxxfileids.size(); i++) {
            requestBody = requestBody.replace(fjxxfileids.get(i).toUpperCase(), fjxxfileids.get(i));
        }
        log.info("推送中交合同請求報文==============" + requestBody);
        System.out.println("推送中交请求报文====================" + requestBody);
        if (StringUtils.isNotEmpty(requestBody)) {
            ContractBasePullF contractBasePullF = new ContractBasePullF();
            contractBasePullF.setRequestBody(requestBody);
            contractBasePullF.setType(type);


            ContractBasePullV contractBasePullV = externalFeignClient.contractPull(contractBasePullF);
//            ContractBasePullV contractBasePullV = new ContractBasePullV();
            if(ObjectUtils.isNotEmpty(contractBasePullV)) {
                ContractPayConcludeE map =  contractPayConcludeMapper.selectById(id);
                map.setFromid(contractBasePullV.getFromid());
                map.setConmaincode(contractBasePullV.getConmaincode());
                map.setContractNature(contractBasePullV.getStatus());
                map.setPartyAId(contractBasePullV.getMessage());
                contractPayConcludeMapper.updateById(map);
                if(map.getContractNature() == 1 && map.getContractType() == 4){
                    modifyContract(map);
                }
                /*if(map.getContractNature() == 1 && map.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                    contractPayConcludeService.handleConcludeSupple(map);
                }*/
            }
            log.info("推送返回来支出合同的报文: {}" , JSON.toJSONString(contractBasePullV));
            return contractBasePullV.getMessage();
        }
        return "报文拼装有误,请检查";
    }

    public void modifyContract(ContractPayConcludeE map){
        Boolean isSendCode = Boolean.FALSE;
        //查主合同
        ContractPayConcludeE mainOldContract = contractPayConcludeMapper.selectById(map.getPid());
        ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(map.getPid());
        List<ContractPayFundE> oldFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .eq(ContractPayFundE.CONTRACT_ID, map.getId())
                .eq(ContractPayFundE.IS_HY, 0)
                .eq(ContractPayFundE.DELETED, 0)
                .isNotNull("cbApportionId"));
        if(!map.getCommunityId().equals(mainContract.getCommunityId())){
            if(CollectionUtils.isNotEmpty(oldFunList)){
                isSendCode = Boolean.TRUE;
            }else{
                isSendCode = Boolean.FALSE;
            }
        }
        String mainId = mainContract.getId();
        String pid = mainContract.getPid();
        Integer contractType = mainContract.getContractType();
        //获取到主合同变更后金额
        BigDecimal changContractAmount = mainContract.getChangContractAmount();
        BeanUtils.copyProperties(map,mainContract);
        mainContract.setContractType(contractType);
        mainContract.setId(mainId);
        mainContract.setPid(pid);
        mainContract.setApprovalDate(null);
        //重新将合同变更后金额设置到主合同
        mainContract.setChangContractAmount(changContractAmount);
        contractPayConcludeMapper.updateById(mainContract);

        //更新扩展表
        LambdaQueryWrapper<ContractPayConcludeExpandE> payQueryWrapper = new LambdaQueryWrapper<>();
        payQueryWrapper.eq(ContractPayConcludeExpandE::getContractId, mainId)
                .eq(ContractPayConcludeExpandE::getDeleted,0);
        ContractPayConcludeExpandE mainExpand = contractPayConcludeExpandMapper.selectOne(payQueryWrapper);
        LambdaQueryWrapper<ContractPayConcludeExpandE> payQueryWrapper1 = new LambdaQueryWrapper<>();
        payQueryWrapper1.eq(ContractPayConcludeExpandE::getContractId, map.getId())
                .eq(ContractPayConcludeExpandE::getDeleted,0);
        ContractPayConcludeExpandE childExpand = contractPayConcludeExpandMapper.selectOne(payQueryWrapper1);
        Long mainExpandId = mainExpand.getId();
        BeanUtils.copyProperties(childExpand,mainExpand);
        mainExpand.setContractId(mainId);
        mainExpand.setId(mainExpandId);
        contractPayConcludeExpandMapper.updateById(mainExpand);

        //复制清单
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPayFundE.CONTRACT_ID, mainId)
                .eq(ContractPayFundE.DELETED,0);
        List<ContractPayFundE> list = contractPayFundMapper.selectList(queryWrapper);
        if(ObjectUtils.isEmpty(list)){
            // fix: 修复清单项删除问题
            // 修改合同推送成功时，如果主表清单不为空，不做处理
            // 主合同清单为空时，清空主表清单并新增子合同清单
            QueryWrapper<ContractPayFundE> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq(ContractPayFundE.CONTRACT_ID, map.getId())
                    .eq(ContractPayFundE.DELETED,0);
            List<ContractPayFundE> list1= contractPayFundMapper.selectList(queryWrapper1);
            if(ObjectUtils.isNotEmpty(list1)){
                for(ContractPayFundE s : list1){
                    ContractPayFundE contractIncomeFundE = new ContractPayFundE();
                    BeanUtils.copyProperties(s,contractIncomeFundE);
                    contractIncomeFundE.setId(null);
                    contractIncomeFundE.setContractId(mainId);
                    contractPayFundMapper.insert(contractIncomeFundE);
                }
            }
        }

        //更新合同扫描件
        QueryWrapper<AttachmentE> attachmentEQueryWrapper = new QueryWrapper<>();
        attachmentEQueryWrapper.eq(AttachmentE.BUSINESS_ID, mainId)
                .eq(AttachmentE.DELETED,0);
        List<AttachmentE> attachmentEList = attachmentMapper.selectList(attachmentEQueryWrapper);
        for(AttachmentE s : attachmentEList){
            attachmentMapper.deleteById(s);
        }

        QueryWrapper<AttachmentE> attachmentEQueryWrapper1 = new QueryWrapper<>();
        attachmentEQueryWrapper1.eq(AttachmentE.BUSINESS_ID, map.getId())
                .eq(AttachmentE.DELETED,0);
        List<AttachmentE> attachmentEList1 = attachmentMapper.selectList(attachmentEQueryWrapper1);
        for(AttachmentE s : attachmentEList1){
            AttachmentE attachmentE = new AttachmentE();
            BeanUtils.copyProperties(s,attachmentE);
            attachmentE.setId(null);
            attachmentE.setBusinessId(mainId);
            attachmentMapper.insert(attachmentE);
        }

        //更新合同正文
        QueryWrapper<ContractRecordInfoE> recordInfoEQueryWrapper = new QueryWrapper<>();
        recordInfoEQueryWrapper.eq(ContractRecordInfoE.CONTRACT_ID, mainId).eq(ContractRecordInfoE.DELETED, 0);
        List<ContractRecordInfoE> contractRecordInfoES = contractRecordInfoMapper.selectList(recordInfoEQueryWrapper);
        if(ObjectUtils.isNotEmpty(contractRecordInfoES)){
            for(ContractRecordInfoE s : contractRecordInfoES){
                contractRecordInfoMapper.deleteById(s);
            }
        }
        QueryWrapper<ContractRecordInfoE> recordInfoEQueryWrapper1 = new QueryWrapper<>();
        recordInfoEQueryWrapper1.eq(ContractRecordInfoE.CONTRACT_ID, map.getId()).eq(ContractRecordInfoE.DELETED, 0);
        List<ContractRecordInfoE> contractRecordInfoES1 = contractRecordInfoMapper.selectList(recordInfoEQueryWrapper1);
        if(ObjectUtils.isNotEmpty(contractRecordInfoES1)){
            for(ContractRecordInfoE s : contractRecordInfoES1){
                ContractRecordInfoE contractRecordInfoE = new ContractRecordInfoE();
                BeanUtils.copyProperties(s,contractRecordInfoE);
                contractRecordInfoE.setContractId(mainId);
                contractRecordInfoE.setId(null);
                contractRecordInfoMapper.insert(contractRecordInfoE);
            }
        }
        if(isSendCode){
            ContractPayConcludeE mainNewContract = contractPayConcludeMapper.selectById(map.getPid());
            contractPayBusinessService.extracted(mainOldContract, oldFunList, mainNewContract);
        }

    }


    public ContractBasePullV verifyContract(String requestBody, Integer type, String id) {
        log.info("推送中交合同請求報文==============" + requestBody);
        ContractBasePullV contractBasePullV = new ContractBasePullV();
        if (StringUtils.isNotEmpty(requestBody)) {
            ContractBasePullF contractBasePullF = new ContractBasePullF();
            contractBasePullF.setRequestBody(requestBody);
            contractBasePullF.setType(type);
            contractBasePullV = externalFeignClient.contractVerify(contractBasePullF);
            return contractBasePullV;
        }
        return contractBasePullV;
    }

    public ContractBasePullV noUsedContract(String requestBody) {
        log.info("推送中交合同停用報文==============" + requestBody);
        ContractBasePullV contractBasePullV = new ContractBasePullV();
        if (StringUtils.isNotEmpty(requestBody)) {
            ContractBasePullF contractBasePullF = new ContractBasePullF();
            contractBasePullF.setRequestBody(requestBody);
            contractBasePullV = externalFeignClient.noused(contractBasePullF);
            return contractBasePullV;
        }
        return contractBasePullV;
    }

    /**
     * 合同信息转换为中交推送接口文档参数格式
     */
    private ContractIncomePullF convertToContractPullF(ContractPayConcludeDetailV detailV, ContractPayAddF form) {
        String id = detailV.getContractId();
        ContractIncomePullF data = new ContractIncomePullF();
        //-- 新增字段格式相同直接赋值
        BeanUtils.copyProperties(form,data);

        //特征信息
        ContractTzxxF tzxxF = new ContractTzxxF();
        if(StringUtils.isNotEmpty(form.getContractpaymentterms())){
            form.setContractpaymentterms(form.getContractpaymentterms().replace("\n",""));
        }
        if(form.getNoPaySign() != null && form.getNoPaySign() == 1){
            form.setRatemethod(null);
            form.setPaymentmethod(null);
            form.setInvoicetype(null);
            form.setContractpaymentterms(null);
            form.setIssfyyfk(null);
            form.setIscoalition(null);
            form.setBdwcdhtehsamt(null);
            form.setBghbdwcdhtehsamt(null);
            form.setPaymentcurrency(null);
            form.setPaymethod(null);
        }else{
            form.setPaymentcurrency("156");
        }

        BeanUtils.copyProperties(form, tzxxF);

        if(CollectionUtils.isNotEmpty(form.getInvoicetype())){
            StringJoiner joiner = new StringJoiner(",");
            form.getInvoicetype().forEach(invoicetype -> {
                joiner.add(invoicetype);
            });
            tzxxF.setInvoicetype(joiner.toString());
        }
        if(CollectionUtils.isNotEmpty(form.getSafekind())){
            StringJoiner joiner = new StringJoiner(",");
            form.getSafekind().forEach(safekind -> {
                joiner.add(safekind);
            });
            tzxxF.setSafekind(joiner.toString());
        }
        String tzxxStr = JSON.toJSONString(tzxxF);
        if (!tzxxStr.equals("{}")) {
            data.setTzxx(tzxxStr);
        }


        //租赁信息
        ContractZlxxF zlxxF = new ContractZlxxF();
        BeanUtils.copyProperties(form, zlxxF);
        String zlxxStr = JSON.toJSONString(zlxxF);
        if (!zlxxStr.equals("{}")) {
            data.setZlxx(zlxxStr);
        }

        //支付方信息 //我方单位
        if (CollectionUtils.isNotEmpty(form.getFkdwxx())) {
            List<ContractZffxxF> contractSrfxxFList = new ArrayList<>();
            ContractZffxxF contractZffxxF = form.getFkdwxx().get(0);
            if(StringUtils.isNotEmpty(contractZffxxF.getDraweeid())) {
                SupplierRv supplierRv = orgFeignClient.getSupplierVById(contractZffxxF.getDraweeid());
                if (null != supplierRv && StringUtils.isNotEmpty(supplierRv.getMainDataCode())) {
                    contractZffxxF.setDraweeid(supplierRv.getMainDataCode());
                    contractZffxxF.setDrawee(supplierRv.getName());
                }
                if(StringUtils.isNotEmpty(contractZffxxF.getTruedraweeid())) {
                    SupplierRv supplierRv1 = orgFeignClient.getSupplierVById(contractZffxxF.getTruedraweeid());
                    if (null != supplierRv1 && StringUtils.isNotEmpty(supplierRv1.getMainDataCode())) {
                        contractZffxxF.setTruedraweeid(supplierRv1.getMainDataCode());
                    }
                }
                contractSrfxxFList.add(contractZffxxF);
                data.setFkdwxx(JSON.toJSONString(contractSrfxxFList));
            }else {
                data.setFkdwxx(JSON.toJSONString(form.getFkdwxx()));
            }
        }
        //收入方信息 //对方单位
        if(CollectionUtils.isNotEmpty(form.getSkdwxx())) {
            List<ContractSrfxxF> contractSrfxxFList = new ArrayList<>();
            ContractSrfxxF contractSrfxxF =form.getSkdwxx().get(0);
            if(StringUtils.isNotEmpty(contractSrfxxF.getPayeeid())) {
                CustomerRv customerRv = orgFeignClient.getCustomerVById(contractSrfxxF.getPayeeid());
                if (null != customerRv && StringUtils.isNotEmpty(customerRv.getMainDataCode())) {
                    contractSrfxxF.setPayeeid(customerRv.getMainDataCode());
                    contractSrfxxF.setPayee(customerRv.getName());
                }
                if(StringUtils.isNotEmpty(contractSrfxxF.getTruepayeeid())) {
                    CustomerRv customerRv1 = orgFeignClient.getCustomerVById(contractSrfxxF.getTruepayeeid());
                    if (null != customerRv1 && StringUtils.isNotEmpty(customerRv1.getMainDataCode())) {
                        contractSrfxxF.setTruepayeeid(customerRv1.getMainDataCode());
                    }
                }
                contractSrfxxFList.add(contractSrfxxF);
                data.setSkdwxx(JSON.toJSONString(contractSrfxxFList));
        }else {
                data.setSkdwxx(JSON.toJSONString(form.getSkdwxx()));
            }
        }

        //保证金信息
        if(CollectionUtils.isNotEmpty(form.getBzjxx())) {
            data.setBzjxx(JSON.toJSONString(form.getBzjxx()));
        }
        //审批信息//
        List<ContractSpxxF> spxxList = new ArrayList<>();
        OpinionApprovalV opinionApprovalV = new OpinionApprovalV();
        if (devFlag != 1) {
            opinionApprovalV = opinionApproval(id, detailV.getIsPush());
        }
        if(ObjectUtils.isNotEmpty(opinionApprovalV.getET_RESULT())){
            for(OpinionApprovalResultV sk : opinionApprovalV.getET_RESULT()){
                ContractSpxxF spxx = new ContractSpxxF();
                spxx.setExamdate(sk.getEXAMDATE());
                spxx.setOperatordeptname(sk.getOPERATORDEPTNAME());
                spxx.setOperatorunitname(sk.getOPERATORUNITNAME());
                spxx.setExamtype(sk.getLOGTYPE());
                spxx.setOperatorunitid(sk.getOPERATORUNITID());
                if(StringUtils.isNotEmpty(sk.getEXAMOPINION())){
                    spxx.setExamopinion(sk.getEXAMOPINION());
                }else{
                    spxx.setExamopinion("同意");
                }
                spxx.setOperator(sk.getOPERATOR());
                spxx.setOperatordeptid(sk.getOPERATORDEPTID());
                spxx.setExamresult(sk.getEXAMRESULT());
                spxx.setExamrole(sk.getEXAMROLE());
                spxxList.add(spxx);
            }
        }else{
            if (devFlag != 1) {
                if (checkApproval && ObjectUtils.isNotEmpty(detailV.getIsPush()) && detailV.getIsPush()) {
                    //走到这里说明：是调用场景，合同侧有流程id，但从OA处查询不到流程信息，也抛异常，中断后续推送流程
                    throw new OwlBizException("该合同查询不到审批流程信息");
                }
            }
            //非调用场景走原逻辑
            ContractSpxxF spxx = new ContractSpxxF();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            spxx.setExamdate(formatter.format(new Date()));
            spxx.setOperatordeptname("合同推送");
            spxx.setOperator("合同推送");
            spxx.setOperatorunitname("合同推送");
            spxx.setExamtype("合同推送");
            spxx.setOperatorunitid("合同推送");
            spxx.setExamopinion("合同推送");
            spxx.setOperator("合同推送");
            spxx.setOperatordeptid("合同推送");
            spxx.setExamresult("合同推送");
            spxx.setExamrole("合同推送");
            spxxList.add(spxx);
        }
        data.setSpxx(JSON.toJSONString(spxxList));


        //附件
        LambdaQueryWrapper<AttachmentE> queryWrapper4 = new LambdaQueryWrapper<>();
        queryWrapper4.eq(AttachmentE::getBusinessId, id).eq(AttachmentE::getBusinessType,1002)
                .eq(AttachmentE::getDeleted, 0);
        List<AttachmentE> attachmentEList = attachmentMapper.selectList(queryWrapper4);
        if(ObjectUtils.isNotEmpty(attachmentEList)){
            List<ContractFjxxPullF> list = new ArrayList<>();
            attachmentEList.forEach(fj -> {
                ContractFjxxPullF pullF = new ContractFjxxPullF();
                //默认类型为01 pdf
                pullF.setBusitype("01");
                pullF.setFileId(fj.getFileuuid());
                pullF.setFileName(fj.getName());
                list.add(pullF);
            });
            data.setFjxx(JSON.toJSONString(list));
            data.setAttachmentEList(attachmentEList);
        }else {
            List<ContractFjxxPullF> list = new ArrayList<>();
            ContractFjxxPullF pullF = new ContractFjxxPullF();
            //默认类型为01 pdf
            pullF.setBusitype("01");
            pullF.setFileId("110a315e3ce54527bbb3fad342f12e29");
            pullF.setFileName("中交认证合同.pdf");
            list.add(pullF);
            AttachmentE attachmentE = new AttachmentE();
            attachmentE.setFileuuid("110a315e3ce54527bbb3fad342f12e29");
            attachmentEList.add(attachmentE);
            data.setFjxx(JSON.toJSONString(list));
            data.setAttachmentEList(attachmentEList);
        }
        //合同对方信息
        if (StringUtils.isNotEmpty(form.getOppositeOne())) {
            List<ContractDfxxF> htdfxxList = new ArrayList<>();
            ContractDfxxF htdfxx = new ContractDfxxF();
            //是否我方其他单位 //todo
            htdfxx.setIsotherown("0");
            //通过customer需要将oppositeoneid转换为code

            SupplierRv supplierRv = orgFeignClient.getSupplierVById(form.getOppositeOneId());
            if (null != supplierRv && StringUtils.isNotEmpty(supplierRv.getMainDataCode())) {
                htdfxx.setUnitid(supplierRv.getMainDataCode());
                htdfxx.setUnitname(supplierRv.getName());
            }
//            htdfxx.setUnitid(form.getOppositeOneId());
            //htdfxx.setUnitname(form.getOppositeOne());

            if(StringUtils.isNotEmpty(form.getIssqr())){
                htdfxx.setIssqr(form.getIssqr());
            }
            if(StringUtils.isNotEmpty(form.getAuthorizedname())){
                htdfxx.setAuthorizedname(form.getAuthorizedname());
            }

            htdfxxList.add(htdfxx);
            data.setHtdfxx(JSON.toJSONString(htdfxxList));
        }
        //保险信息
        if(CollectionUtils.isNotEmpty(form.getBxxx())) {
            data.setBxxx(JSON.toJSONString(form.getBxxx()));
        }
        //担保信息
        if(CollectionUtils.isNotEmpty(form.getDbxx())) {
            data.setDbxx(JSON.toJSONString(form.getDbxx()));
        }

        //--老字段对照处理
        //合同id
        if(StringUtils.isNotBlank(form.getConmaincode())){
            data.setFromid(form.getFromid());
            data.setConmaincode(form.getConmaincode());
        }else{
            data.setFromid(id);
        }

        //合同承办人转4A编码
        if(StringUtils.isNotBlank(form.getConundertakerid())){
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(form.getConundertakerid());
            if(ObjectUtils.isNotEmpty(s)){
                data.setConundertakerid(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
            }
        }

        //合同中文名称
        data.setConnamecn(form.getName());
        //合同编码
        data.setConcode(form.getContractNo());
        //补充合同所属原合同主数据编码
//        data.setSupconmaincode(form.getPid());
        //项目编码
        if(StringUtils.isNotEmpty(form.getCommunityId())){
            SpaceCommunityRv spaceCommunityRv = spaceFeignClient.getById(form.getCommunityId());
            //项目编码
            data.setProjnumber(spaceCommunityRv.getSerialNumber());
            //项目名称
            data.setProjname(form.getCommunityName());
        }
        //合同业务类型
        data.setBusinesstype(form.getBizCode());
        //签约日期
        if(ObjectUtils.isNotEmpty(form.getSignDate())){
            data.setSigndate(form.getSignDate() + "");
        }else {
            data.setSigndate("2024-07-01");
        }
        //合同概述
        if(StringUtils.isNotEmpty(form.getRemark())){
            data.setConsummary(form.getRemark().replace("\n",""));
        }
        //管理部门ID
        OrgInfoRv orgInfoRv = orgFeignClient.getByOrgId(Long.parseLong(form.getDepartId()));
        if (null != orgInfoRv && StringUtils.isNotEmpty(orgInfoRv.getOid())) {
            data.setOid(orgInfoRv.getOid());
            data.setOname(orgInfoRv.getOidName());
        }
        //我方签约单位id
        //我方单位名称
        OrgFinanceRv orgFinanceRv = orgFeignClient.getByFinanceId(Long.valueOf(form.getOurPartyId()));
        if (null != orgFinanceRv && StringUtils.isNotEmpty(orgFinanceRv.getOid())) {
            data.setPartiesunitid(orgFinanceRv.getOid());
            data.setPartiesunitname(orgFinanceRv.getOidName());
        }
        //合同实际履约主体id
        data.setSjlydwid(form.getSjlydwid());
        //合同实际履约主体名称
        data.setSjlydwname(form.getSjlydwidname());
        //合同状态- //todo 默认01
        if(data.getStatus() != 6){
            data.setConstatus("01");
        }else{
            data.setConstatus("04");
        }
        //币种
        data.setCurrency("156");
        if(form.getSigningMethod() == 0){
            //合同总金额含税
            data.setHsamt(form.getContractAmountOriginalRate());
        }
        if(form.getSigningMethod() == 1){
            //ContractPayConcludeE map =  contractPayConcludeMapper.selectById(form.getPid());
            ContractPayConcludeE map = queryRootContract(form.getPid());
            //原合同金额
            data.setHsamt(map.getContractAmountOriginalRate());
            //合同变更后总金额
            data.setHsbgamt(map.getChangContractAmount());
            data.setSupconmaincode(map.getConmaincode());
        }
        if (ContractTypeEnum.修改合同.getCode().equals(form.getContractType())) {
            data.setHsamt(form.getContractAmountOriginalRate());
            if (form.getChangContractAmount().compareTo(BigDecimal.ZERO) > 0) {
                data.setHsbgamt(form.getChangContractAmount());
            }
        }
        //如果是无支付写死给03
        if(form.getNoPaySign() != null && form.getNoPaySign() == 1){
            //收支类型
            data.setIncomeexpendtype("03");
            //支付方信息
            data.setFkdwxx(null);
            //收入方信息
            data.setSkdwxx(null);
            //币种
            data.setCurrency(null);
            //合同总金额(含税)
            data.setHsamt(null);
            //合同变更后总金额（含税）
            data.setHsbgamt(null);
            //税额
            data.setTaxamt(null);
            //税率
            data.setTaxrate(null);
        }

        return data;
    }

    /**
     * 查询合同的最终父合同
     *
     * @param pid
     * @return
     */
    private ContractPayConcludeE queryRootContract(String pid){
        //查询父合同信息
        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.selectById(pid);
        //若父合同不是根合同 且 父合同的父合同不是自己，再继续上探，否则返回
        while (!"0".equals(payConcludeE.getPid()) && !payConcludeE.getId().equals(payConcludeE.getPid())) {
            payConcludeE = contractPayConcludeMapper.selectById(payConcludeE.getPid());
        }
        return payConcludeE;
    }

    public OpinionApprovalV opinionApproval(String id, Boolean isPush) {
        OpinionApprovalF opinionApprovalF = new OpinionApprovalF();
        OpinionApprovalDataF opinionApprovalDataF = new OpinionApprovalDataF();
        ContractProcessRecordE recordE = new ContractProcessRecordE();
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eq(ContractProcessRecordE::getContractId, id).eq(ContractProcessRecordE::getSubType,2).eq(ContractProcessRecordE::getDeleted, 0);
        recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(recordE)){
            LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper1 = WrapperX.lambdaQueryX();
            queryWrapper1.eq(ContractProcessRecordE::getContractId, id).eq(ContractProcessRecordE::getSubType,1).eq(ContractProcessRecordE::getDeleted, 0);
            recordE = contractProcessRecordMapper.selectOne(queryWrapper1);
            if(ObjectUtils.isEmpty(recordE)){
                if (ObjectUtils.isNotEmpty(isPush) && isPush) {
                    //走到这里说明：是调用场景且合同没有流程数据，是有问题的合同，修改为直接抛异常，中断后续推送
                    throw new OwlBizException("该合同审批流程数据缺失");
                }
                //非调用场景走原逻辑
                return new OpinionApprovalV();
            }
        }
        opinionApprovalDataF.setFormdataid(id);
        opinionApprovalDataF.setRequestId(recordE.getProcessId());
        opinionApprovalF.setIT_DATA(opinionApprovalDataF);
        return externalFeignClient.opinionApproval(opinionApprovalF);
    }

    /**
     * 合同信息javabean转换为jsonstr
     */
    private String converToJSONStr(ContractIncomePullF pullF) {
        try {
            ContractIncomePullBaseF basePull = new ContractIncomePullBaseF();

            BeanUtils.copyProperties(pullF, basePull);

            String pullFStr = JSON.toJSONString(basePull);
            JSONObject pullFObj = JSONObject.parseObject(pullFStr);
            //租赁信息
            if(StringUtils.isNotEmpty(pullF.getZlxx())) {
                pullFObj.put("ZLXX",JSONObject.parseObject(pullF.getZlxx()));
            }
            //特征信息
            if(StringUtils.isNotEmpty(pullF.getTzxx())) {
                pullFObj.put("TZXX",JSONObject.parseObject(pullF.getTzxx()));
            }

            //合同对方信息
            if(StringUtils.isNotEmpty(pullF.getHtdfxx())) {
                pullFObj.put("HTDFXX",JSONObject.parseArray(pullF.getHtdfxx()));
            }

            //支付方信息
            if(StringUtils.isNotEmpty(pullF.getFkdwxx())) {
                pullFObj.put("FKDWXX",JSONObject.parseArray(pullF.getFkdwxx()));
            }

            //收入方信息
            if(StringUtils.isNotEmpty(pullF.getSkdwxx())) {
                pullFObj.put("SKDWXX",JSONObject.parseArray(pullF.getSkdwxx()));
            }
            //保证金信息
            if(StringUtils.isNotEmpty(pullF.getBzjxx())) {
                pullFObj.put("BZJXX",JSONObject.parseArray(pullF.getBzjxx()));
            }
            //审批信息
            if(StringUtils.isNotEmpty(pullF.getSpxx())) {
                pullFObj.put("SPXX",JSONObject.parseArray(pullF.getSpxx()));
            }
            //附件信息
            if(StringUtils.isNotEmpty(pullF.getFjxx())) {
                pullFObj.put("FJXX",JSONObject.parseArray(pullF.getFjxx()));
            }
            //保险信息
            if(StringUtils.isNotEmpty(pullF.getBxxx())) {
                pullFObj.put("BXXX",JSONObject.parseArray(pullF.getBxxx()));
            }
            //担保信息
            if(StringUtils.isNotEmpty(pullF.getDbxx())) {
                pullFObj.put("DBXX",JSONObject.parseArray(pullF.getDbxx()));
            }


            //报文参数转大写处理
            return pullFObj.toJSONString().toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    public String getJsonStr(ContractPayConcludeDetailV detailV) {
        ContractPayAddF form = new ContractPayAddF();
        BeanUtils.copyProperties(detailV, form);
        ContractIncomePullF pullF = convertToContractPullF(detailV, form);

        List<String> fjxxfileids = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pullF.getAttachmentEList())) {
            pullF.getAttachmentEList().forEach(f -> {
                fjxxfileids.add(f.getFileuuid());
            });
        }

        String requestBody = converToJSONStr(pullF);

        //国家处理
        if (StringUtils.isNotEmpty(requestBody) && requestBody.contains("CHI")) {
            requestBody = requestBody.replace("CHI", "chi");
        }

        //附件id重新转小写
        for (int i = 0; i < fjxxfileids.size(); i++) {
            requestBody = requestBody.replace(fjxxfileids.get(i).toUpperCase(), fjxxfileids.get(i));
        }

        //fromId有字符串的情况
        if(StringUtils.isNotEmpty(detailV.getFromid())){
            requestBody = requestBody.replace(detailV.getFromid().toUpperCase(), detailV.getFromid());
        }

        log.info("推送中交请求报文==============" + requestBody);
        return requestBody;

    }

    public String getJsonStrNoUsed(ContractPayConcludeDetailV detailV) {
        ContractIncomePullF data = new ContractIncomePullF();
        data.setFromid(detailV.getFromid());
        data.setFromsyscode("GREG-MDM");
        data.setConmaincode(detailV.getConmaincode());
        if(StringUtils.isNotEmpty(detailV.getIssupplycontract()) && detailV.getIssupplycontract().equals("1")){
            ContractPayConcludeE concludeE = contractPayConcludeMapper.selectById(detailV.getPid());
            data.setSupconmaincode(concludeE.getConmaincode());
        }
        //我方签约单位id
        OrgFinanceRv orgFinanceRv = orgFeignClient.getByFinanceId(Long.valueOf(detailV.getOurPartyId()));
        if (null != orgFinanceRv && StringUtils.isNotEmpty(orgFinanceRv.getOid())) {
            data.setPartiesunitid(orgFinanceRv.getOid());
            data.setPartiesunitname(orgFinanceRv.getOidName());
        }
        data.setBusinesstype(detailV.getBizCode());
        //合同实际履约主体id
        data.setSjlydwid(detailV.getSjlydwid());
        //合同实际履约主体名称
        data.setSjlydwname(detailV.getSjlydwidname());
        data.setEnablestatus("0");
        data.setDeactivatedesc("1");
        data.setIssupplycontract(detailV.getIssupplycontract());
        String pullFStr = JSON.toJSONString(data);
        JSONObject pullFObj = JSONObject.parseObject(pullFStr);
        return pullFObj.toJSONString().toUpperCase();
    }
}
