package com.wishare.contract.apps.service.revision.income;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.contractset.blacklist.BlackListInfoF;
import com.wishare.contract.apps.fo.revision.ContractInfoToSpaceResourceF;
import com.wishare.contract.apps.fo.revision.ContractRevF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundSaveF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundUpdateF;
import com.wishare.contract.apps.fo.revision.pay.ContractQydwsExtendF;
import com.wishare.contract.apps.fo.revision.pay.ContractQydwsF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundUpdateF;
import com.wishare.contract.apps.fo.revision.remote.BondRelationF;
import com.wishare.contract.apps.remote.clients.*;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.fo.ContractBasePullF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalDataF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalF;
import com.wishare.contract.apps.remote.fo.proquery.ProcessQueryF;
import com.wishare.contract.apps.remote.vo.ContractBasePullV;
import com.wishare.contract.apps.remote.vo.OrgInfoRv;
import com.wishare.contract.apps.remote.vo.OrgRevFinanceCostRV;
import com.wishare.contract.apps.remote.vo.SpaceCommunityRv;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.UserStateRV;
import com.wishare.contract.apps.remote.vo.blacklist.BlackUserV;
import com.wishare.contract.apps.remote.vo.bpm.ProcessStartF;
import com.wishare.contract.apps.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.contract.apps.remote.vo.charge.ApproveFilter;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.remote.vo.revision.CustomerRv;
import com.wishare.contract.apps.service.contractset.ContractCategoryAppService;
import com.wishare.contract.apps.service.contractset.ContractUniqueCodeRuleAppService;
import com.wishare.contract.apps.service.revision.base.ContractBaseService;
import com.wishare.contract.apps.service.revision.common.ContractInfoToFxmCommonService;
import com.wishare.contract.apps.service.revision.org.ContractOrgRelationService;
import com.wishare.contract.apps.service.revision.relation.ContractRelationBusinessService;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeCorrectionE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeSettlementConcludeE;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import com.wishare.contract.domains.entity.revision.template.ContractRecordInfoE;
import com.wishare.contract.domains.enums.ConcludeContractNatureEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.enums.revision.log.LogActionTypeEnum;
import com.wishare.contract.domains.enums.settlement.SettlementTypeEnum;
import com.wishare.contract.domains.mapper.contractset.ContractProcessRecordMapper;
import com.wishare.contract.domains.mapper.revision.attachment.AttachmentMapper;
import com.wishare.contract.domains.mapper.revision.income.*;
import com.wishare.contract.domains.mapper.revision.income.fund.ContractIncomeFundMapper;
import com.wishare.contract.domains.mapper.revision.template.ContractRecordInfoMapper;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.bond.pay.RevisionBondPayService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.income.ContractIncomePlanConcludeService;
import com.wishare.contract.domains.service.revision.income.fund.ContractIncomeFundService;
import com.wishare.contract.domains.service.revision.log.RevisionLogService;
import com.wishare.contract.domains.service.revision.relation.ContractRelationRecordService;
import com.wishare.contract.domains.service.revision.template.ContractRecordInfoService;
import com.wishare.contract.domains.vo.contractset.ContractDetailV;
import com.wishare.contract.domains.vo.revision.ContractInfoV;
import com.wishare.contract.domains.vo.revision.ContractNoInfoV;
import com.wishare.contract.domains.vo.revision.ContractNumShow;
import com.wishare.contract.domains.vo.revision.fwsso.FwSSoBaseInfoF;
import com.wishare.contract.domains.vo.revision.income.*;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundV;
import com.wishare.contract.domains.vo.revision.income.settlement.IncomePlanPeriodV;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.procreate.BusinessInfoF;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateResultV;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateReturnV;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateV;
import com.wishare.contract.domains.vo.revision.proquery.ProcessQueryV;
import com.wishare.contract.domains.vo.revision.relation.RelationRecordDetailV;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoV;
import com.wishare.contract.infrastructure.utils.BeanChangeUtil;
import com.wishare.contract.infrastructure.utils.TemplateUtils;
import com.wishare.contract.infrastructure.utils.build.Builder;
import com.wishare.contract.infrastructure.utils.query.LambdaQueryWrapperX;
import com.wishare.contract.infrastructure.utils.query.WrapperX;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @Description 收入合同业务方法-合同改版
 * @Author chenglong
 * @since 2023/6/25  13:54
 */
@Service
@Slf4j
public class ContractIncomeBusinessService implements IOwlApiBase {
    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeService contractIncomeConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private AttachmentService attachmentService;

    @Setter(onMethod_ = {@Autowired})
    private ContractOrgRelationService orgRelationService;

    @Setter(onMethod_ = {@Autowired})
    private ContractCategoryAppService contractCategoryAppService;

    @Setter(onMethod_ = {@Autowired})
    private OrgEnhanceComponent orgEnhanceComponent;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ContractRelationRecordService contractRelationRecordService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractUniqueCodeRuleAppService contractUniqueCodeRuleAppService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractProcessRecordMapper contractProcessRecordMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRelationBusinessService contractRelationBusinessService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeFundService contractIncomeFundService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionBondPayService bondPayService;

    @Autowired
    private ContractIncomePlanConcludeMapper contractIncomePlanConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionLogService logService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private SpaceFeignClient spaceFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRecordInfoService contractRecordInfoService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private TemplateUtils templateUtils;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeExpandAppService contractIncomeConcludeExpandAppService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomePullService contractIncomePullService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ExternalFeignClient externalFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRecordInfoMapper contractRecordInfoMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractInfoToFxmCommonService contractInfoToFxmCommonService;

    @Setter(onMethod_ = {@Autowired})
    private ContractBaseService contractBaseService;

    @Value("${process.create.bizCode:}")
    private String bizCode;

    @Value("${contract.devFlag:0}")
    private Integer devFlag;

    @Value("${contract.post.check:true}")
    private Boolean contractPostCheck;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private ContractIncomeConcludeCorrectionMapper contractIncomeConcludeCorrectionMapper;
    @Autowired
    private ContractIncomeFundMapper contractIncomeFundMapper;
    @Autowired
    private ChargeClient chargeClient;
    @Autowired
    private BpmClient bpmClient;
    @Autowired
    private ContractIncomeSettlementConcludeMapper contractIncomeSettlementConcludeMapper;
    @Autowired
    private ContractIncomeConcludeSettlementPeriodMapper contractIncomeConcludeSettlementPeriodMapper;
    @Autowired
    private ContractIncomePlanConcludeService contractIncomePlanConcludeService;

    //-- 8848收入合同调整
    /**
     * 合同类型补充结算合同
     *
     * 收入合同主表数据库添加字段&代码同步添加补充新增字段
     *
     * 新增方法补充签约类型赋值逻辑&编辑方法去除签约类型&新增补充签约类型
     *
     * 合同编号-兼容手动输入&提交时校验唯一性
     *
     * 去除甲乙方&补充对方单位信息处理&所属项目处理
     *
     * 关联联动org客商数据同步更新后端方法修改 - 关联合同数量 & 关联合同
     *
     * 我方单位-法定单位信息处理
     *
     * 款项子表新增字段 - 税额 不含税金额 数量 费项
     *
     * 款项子表批处理接口&后端方法
     *
     * 款项子表-后端关联合同业务逻辑&金额字段计算处理
     *
     * 变更记录接口补充&按钮控制
     *
     * 终止接口修改 & 新增终止日期，反审字段 & 新增反审接口 & 反审按钮控制
     *
     * 已拒绝状态按钮控制修改 && 详情补充 费项&税率 数据列
     */



    /**
     * 新增支出合同数据
     * @param form
     * @return
     */
    @Transactional
    public ContractInfoV addIncomeContract(ContractIncomeAddF form) {
        ContractInfoV contractInfoV = new ContractInfoV();
        String relationId = form.getId();
        form.setId(null);
        form.setSignDate(null);
        //将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(form.getRemark())) {
            form.setRemark(form.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        //是否归档设置为0，mapperFacade设置到实体类的archived字段中
        form.setArchived(0);
        //-- 转换实体类
        ContractIncomeConcludeE concludeE = Global.mapperFacade.map(form, ContractIncomeConcludeE.class);
        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }

        //-- 租户ID
        concludeE.setTenantId(tenantId());
        //-- 初始化相关字段 收款金额
        concludeE.setCollectAmount(BigDecimal.ZERO)
                .setInvoiceAmount(BigDecimal.ZERO)
                .setChangContractAmount(BigDecimal.ZERO);

        //-- 为实体类补充处理相关字段的值
        fillNameForAdd(concludeE);

        /*if (StringUtils.isBlank(form.getContractNo())) {
            //-- 客户未输入合同编号后，自动生成合同编号
//            String code = contractConcludeAppService.revContractCode(tenantId(), form.getPid(), RevTypeEnum.支出合同.getCode());
            String code = contractUniqueCodeRuleAppService.genUniqueCode(form.getIsBackDate(),concludeE.getDepartName(),ContractAreaEnum.parseName(concludeE.getIsBackDate()),form.getConmanagetype(),concludeE.getCommunityName(),1,form.getConperformprovincesname());
            concludeE.setContractNo(code);
        }*/

        if(StringUtils.isNotEmpty(form.getConincrementype()) &&  (form.getConincrementype().equals("0") ||  form.getConincrementype().equals("3") || form.getConincrementype().equals("5"))){
            concludeE.setSealType(1);
        }

        //-- 审批状态 - 待提交
        concludeE.setReviewStatus(ReviewStatusEnum.待提交.getCode());
//        if(LocalDate.now().isBefore(form.getGmtExpireStart())){
//            concludeE.setStatus( ContractRevStatusEnum.尚未履行.getCode());
//        }else{
//            concludeE.setStatus( ContractRevStatusEnum.正在履行.getCode());
//        }
        //-- 新增合同，合同状态是"未生效"
        concludeE.setStatus(ContractRevStatusEnum.未生效.getCode());
        //-- 合同业务分类
        concludeE.setCategoryPath(CollectionUtils.isEmpty(form.getCategoryPathList()) ? "[]" : JSON.toJSONString(form.getCategoryPathList()));
        //-- 签约类型 默认新签，再根据传参判断是变更还是续签
        concludeE.setSigningMethod(SigningMethodEnum.新签.getCode());
        if (StringUtils.isNotBlank(relationId) && Objects.nonNull(form.getActionType())) {
            concludeE.setSigningMethod(ActionTypeEnum.变更.getCode().equals(form.getActionType()) ? SigningMethodEnum.变更.getCode() : SigningMethodEnum.续签.getCode());
        } else if (StringUtils.isNotBlank(concludeE.getPid()) && concludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())) {
            concludeE.setSigningMethod(SigningMethodEnum.补充.getCode());
            checkStartTimeOnSupplement(concludeE);
        }

        //校验信息
        if(form.getNoPaySign() == 0) {
            checkContractData(concludeE, form.getFkdwxx(), form.getSkdwxx(), true);
        }

        if(ObjectUtils.isNotEmpty(form.getQydws()) && !JSON.toJSONString(form.getQydws()).equals("[{}]")){
            for(int i = 0; i < form.getQydws().size(); i ++){
                if(i == 0){
                    concludeE.setOppositeOneId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i == 1){
                    concludeE.setOppositeTwoId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i ==2){
                    break;
                }
            }
        }

        //引用范本为否，删除范本ID
        if(Objects.nonNull(concludeE.getIsTemp()) && concludeE.getIsTemp() == 0){
            concludeE.setTempId(null);
        }
        if(Objects.nonNull(concludeE.getIsTemp()) && concludeE.getIsTemp() == 1 && CollectionUtils.isNotEmpty(form.getPdffjxx())){
            concludeE.setTempId(null);
        }
        //推送状态
        concludeE.setContractNature(ConcludeContractNatureEnum.TO_PUSH.getCode());
        contractIncomeConcludeMapper.insert(concludeE);
        contractInfoV.setId(concludeE.getId());

        boolean isEdit = false;
        if(form.getIsEditWps() != null && form.getIsEditWps()){
            isEdit = true;
        }

        //范本操作
        if(StringUtils.isNotBlank(concludeE.getTempId())){
            FileVo fileVo = templateUtils.genTemplateInfo(concludeE.getTempId(), concludeE, concludeE.getName());
            concludeE.setTempFilekey(fileVo.getFileKey());
            //-- 把修改记录更新导表里
            ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),concludeE.getTempId(),concludeE.getTempFilekey(),null,fileVo.getName(),Integer.parseInt(fileVo.getSize().toString()));
            ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
            if(isEdit){
                String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                contractInfoV.setLinkAdress(linkAdress);
            }
        }else{
            if(isEdit){
                FileVo fileVo = templateUtils.genTemplateInfo("15497043922911", concludeE, concludeE.getName());
                concludeE.setTempFilekey(fileVo.getFileKey());
                //-- 把修改记录更新导表里
                ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),concludeE.getTempId(),concludeE.getTempFilekey(),null,fileVo.getName(),Integer.parseInt(fileVo.getSize().toString()));
                ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                contractInfoV.setLinkAdress(linkAdress);
            }
        }

        //-- 处理范文文件
        dealContractTemp(concludeE, form.getPdffjxx());

        logService.insertOneLog(concludeE.getId(), form.getName(), LogActionTypeEnum.新增.getCode());

        if (!Boolean.TRUE.equals(form.getIsStash()) && !isEdit) {
            contractInfoV.setSsoLinkAdress(postContract(concludeE.getId()));
        }

        //-- 入库拓展表
        dealContractExpand(concludeE.getId(),form);
        form.getContractIncomeFundVList().stream().forEach( s-> {
            s.setContractId(concludeE.getId());
        });
        contractIncomeFundService.dealBatch(form.getContractIncomeFundVList(), true);

        //-- 推数据到市拓
        if(form.getIsBusInfo() != null && form.getIsBusInfo()){
            ParamCallBackInfoF paramCallBackInfoF = new ParamCallBackInfoF();
            paramCallBackInfoF.setId(form.getCttmsj().get(0).getId());
            paramCallBackInfoF.setContractId(concludeE.getId());
            boolean isSuccess = externalFeignClient.ceCallBack(paramCallBackInfoF);
            log.info("市拓回调接口是否成功" + isSuccess);
        }

        // 枫行梦新建收入合同同步到枫行梦
        if(concludeE.getSealType() != null && concludeE.getSealType() == 1){
            log.info("已经走到枫行梦...");
            ContractInfoToSpaceResourceF contractInfoToSpaceResourceF = new ContractInfoToSpaceResourceF();
            contractInfoToSpaceResourceF.setFormId(form.getBindpointresources()); // 临时表单id
            contractInfoToSpaceResourceF.setAgreementId(concludeE.getId()); // 合同id
            String requestBody = JSON.toJSONString(contractInfoToSpaceResourceF);
            if (StringUtils.isNotEmpty(requestBody)) {
                ContractBasePullF contractBasePullF = new ContractBasePullF();
                contractBasePullF.setRequestBody(requestBody);
                contractBasePullF.setType(0);
                Boolean isSuccess = externalFeignClient.contractInfoPull(contractBasePullF);
                log.info("新建收入合同同步到枫行梦是否成功" + isSuccess);
            }
        }

        return contractInfoV;
    }

    private void checkStartTimeOnSupplement(ContractIncomeConcludeE concludeE) {
        // 补充协议判定开始时间落在 主合同的开始时间到结束时间+1天内
        ContractIncomeConcludeE mainContract = contractIncomeConcludeMapper.selectById(concludeE.getPid());
        if (Objects.isNull(mainContract)){
            throw new OwlBizException("主合同不存在");
        }
        LocalDate childStart = concludeE.getGmtExpireStart();
        if (childStart.isBefore(mainContract.getGmtExpireStart()) ||
            childStart.isAfter(mainContract.getGmtExpireEnd().plusDays(1))){
            throw new OwlBizException("补充协议开始时间落在主合同开始时间到结束时间+1天内");
        }
    }

    /**
     * 编辑支出合同数据
     * @param form
     */
    @Transactional
    public Boolean modifySignDate(ContractIncomeEditF form) {
        //-- 校验合同ID是否正确
        ContractIncomeConcludeE e = contractIncomeConcludeService.getById(form.getId());
        if (Objects.isNull(e)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        e.setSignDate(form.getSignDate());
        e.setGmtModify(LocalDateTime.now());
        //-- 更新
        contractIncomeConcludeService.updateById(e);
        return Boolean.TRUE;
    }

    /**
     * 编辑支出合同数据
     * @param form
     */
    @Transactional
    public ContractInfoV editIncomeContract(ContractIncomeEditF form) {
        ContractInfoV contractInfoV = new ContractInfoV();
        //-- 校验合同ID是否正确
        ContractIncomeConcludeE e = contractIncomeConcludeService.getById(form.getId());
        if (Objects.isNull(e)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        //引用范本为否，删除范本ID
        if(Objects.nonNull(form.getIsTemp()) && form.getIsTemp() == 0){
            form.setTempId(null);
        }
        if(Objects.nonNull(form.getIsTemp()) && form.getIsTemp() == 1 && CollectionUtils.isNotEmpty(form.getPdffjxx())){
            form.setTempId(null);
        }
        //将form中的contractType重新设置为数据库中该合同原本的contractType，供后续使用
        form.setContractType(e.getContractType());
        //将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(form.getRemark())) {
            form.setRemark(form.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        //-- 转换实体类
        ContractIncomeConcludeE concludeE = Global.mapperFacade.map(form, ContractIncomeConcludeE.class);
        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }
        //-- 根据ID检索并赋值对应的名称字段
        fillNameForEdit(concludeE, e);

        //-- 校验合同编码唯一性
        // judgeContractNoOnly(concludeE);

        //-- 合同业务分类
        concludeE.setCategoryPath(CollectionUtils.isEmpty(form.getCategoryPathList()) ? "[]" : JSON.toJSONString(form.getCategoryPathList()));

        //-- 变更 & 续签类型不可编辑签约类型， 补充 or 新签 根据编辑后的属性再做判断
        if (SigningMethodEnum.补充.getCode().equals(concludeE.getSigningMethod()) || SigningMethodEnum.新签.getCode().equals(concludeE.getSigningMethod())) {
            concludeE.setSigningMethod(StringUtils.isNotBlank(concludeE.getPid()) && concludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())
                    ? SigningMethodEnum.补充.getCode() : SigningMethodEnum.新签.getCode());
        }
        if (ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType())){
            checkStartTimeOnSupplement(concludeE);
        }

        //校验信息
        if(form.getNoPaySign() == 0) {
            checkContractData(concludeE, form.getFkdwxx(), form.getSkdwxx(), false);
        }

        if(StringUtils.isNotEmpty(form.getConincrementype()) &&  (form.getConincrementype().equals("0") ||  form.getConincrementype().equals("3") || form.getConincrementype().equals("5"))){
            concludeE.setSealType(1);
        }else{
            concludeE.setSealType(null);
        }

        BeanChangeUtil<ContractIncomeConcludeE> t = new BeanChangeUtil<>();
        ContractIncomeRecordF e1 = Global.mapperFacade.map(e, ContractIncomeRecordF.class);
        ContractIncomeRecordF e2 = Global.mapperFacade.map(concludeE, ContractIncomeRecordF.class);
        String str = t.contrastObj(e1, e2);

        boolean isEdit = false;
        if(form.getIsEditWps() != null && form.getIsEditWps()){
            isEdit = true;
        }

        if(StringUtils.isNotEmpty(e.getTempId()) && StringUtils.isNotEmpty(form.getTempId()) && e.getTempId().equals(form.getTempId())){
            LambdaQueryWrapper<ContractRecordInfoE> payQueryWrapper = new LambdaQueryWrapper<>();
            payQueryWrapper.eq(ContractRecordInfoE::getContractId, concludeE.getId()).eq(ContractRecordInfoE::getDeleted,0);
            ContractRecordInfoE contractRecordInfoE = contractRecordInfoMapper.selectOne(payQueryWrapper);
            if(Objects.nonNull(contractRecordInfoE)){
                ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                contractInfoV.setLinkAdress(linkAdress);
            }
        }else{
            LambdaQueryWrapper<ContractRecordInfoE> payQueryWrapper = new LambdaQueryWrapper<>();
            payQueryWrapper.eq(ContractRecordInfoE::getContractId, concludeE.getId()).eq(ContractRecordInfoE::getDeleted,0);
            ContractRecordInfoE contractRecordInfoE = contractRecordInfoMapper.selectOne(payQueryWrapper);
            if(Objects.nonNull(contractRecordInfoE)) {
                contractRecordInfoMapper.deleteById(contractRecordInfoE);
            }
        }

        if(StringUtils.isEmpty(contractInfoV.getLinkAdress())) {
            if (StringUtils.isNotBlank(concludeE.getTempId())) {
                FileVo fileVo = templateUtils.genTemplateInfo(concludeE.getTempId(), concludeE, concludeE.getName());
                concludeE.setTempFilekey(fileVo.getFileKey());
                //-- 把修改记录更新导表里
                ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(), concludeE.getName(), concludeE.getTempId(), concludeE.getTempFilekey(), str, fileVo.getName(), Integer.parseInt(fileVo.getSize().toString()));
                ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                if (isEdit) {
                    String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                    contractInfoV.setLinkAdress(linkAdress);
                }
            } else {
                if (isEdit) {
                    FileVo fileVo = templateUtils.genTemplateInfo("15497043922911", concludeE, concludeE.getName());
                    concludeE.setTempFilekey(fileVo.getFileKey());
                    //-- 把修改记录更新导表里
                    ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(), concludeE.getName(), concludeE.getTempId(), concludeE.getTempFilekey(), null, fileVo.getName(), Integer.parseInt(fileVo.getSize().toString()));
                    ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                    String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                    contractInfoV.setLinkAdress(linkAdress);
                }
            }
        }

        if(ObjectUtils.isNotEmpty(form.getQydws()) && !JSON.toJSONString(form.getQydws()).equals("[{}]")){
            for(int i = 0; i < form.getQydws().size(); i ++){
                if(i == 0){
                    concludeE.setOppositeOneId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i == 1){
                    concludeE.setOppositeTwoId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i ==2){
                    break;
                }
            }
        }
//        if(LocalDate.now().isBefore(form.getGmtExpireStart())){
//            concludeE.setStatus( ContractRevStatusEnum.尚未履行.getCode());
//        }else{
//            concludeE.setStatus( ContractRevStatusEnum.正在履行.getCode());
//        }
        //同样的，编辑合同，合同状态也是"未生效"
        concludeE.setStatus(ContractRevStatusEnum.未生效.getCode());

        //-- 更新
        contractIncomeConcludeService.updateById(concludeE);
        contractInfoV.setId(concludeE.getId());
        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.编辑.getCode());

        if (!Boolean.TRUE.equals(form.getIsStash()) && !isEdit) {
            contractInfoV.setSsoLinkAdress(postContract(concludeE.getId()));
        }

        //-- 处理范文文件
        dealContractTemp(concludeE, form.getPdffjxx());

        //-- 编辑拓展表
        dealEditContractExpand(concludeE.getId(),form);
        Boolean isCheckFundAmount = true;
        if (StringUtils.isNotBlank(e.getConmaincode())) {
            isCheckFundAmount = false;
        }
        contractIncomeFundService.dealBatch(form.getContractIncomeFundVList(), isCheckFundAmount);
        contractInfoV.setIsSucess(true);

        if (StringUtils.isNotBlank(e.getContractNo())) {
            //-- 处理合同编号
            ContractNoInfoV contractNoInfoV = contractBaseService.generateIncomeContractNo(concludeE, e);
            log.info("ContractIncomeBusinessService.editIncomeContract:contractNoInfoV:{}", JSON.toJSONString(contractNoInfoV));
            if (Objects.isNull(contractNoInfoV)){
                throw new OwlBizException("合同编号生成失败-未知异常");
            }
            if (contractNoInfoV.getCode() == 0){
                throw new OwlBizException(contractNoInfoV.getFailReason());
            }
            concludeE.setContractNo(contractNoInfoV.getContractNo());
            //-- 更新
            contractIncomeConcludeService.updateById(concludeE);
        }

        return contractInfoV;
    }
    @Transactional
    public ContractInfoV correctionIncome(ContractIncomeEditF form) {
        ContractInfoV contractInfoV = new ContractInfoV();
        //-- 校验合同ID是否正确
        ContractIncomeConcludeE e = contractIncomeConcludeService.getById(form.getId());
        if (Objects.isNull(e)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        if(CollectionUtils.isNotEmpty(form.getContractIncomeFundVList()) && e.getContractType() == 0){
            List<ContractIncomeFundUpdateF> contractPayFundVList = form.getContractIncomeFundVList().stream().filter(record -> !"delete".equals(record.getActionCode())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(contractPayFundVList)){
                BigDecimal totalAmount = contractPayFundVList.stream()
                        .map(ContractIncomeFundUpdateF::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if(e.getChangContractAmount().compareTo(BigDecimal.ZERO) != 0){
                    if(totalAmount.compareTo(e.getChangContractAmount()) != 0){
                        throw new OwlBizException("当前清单金额["+totalAmount+"]与合同变更金额["+e.getChangContractAmount()+"]不一致，请重新填写");
                    }
                }else{
                    if(totalAmount.compareTo(e.getContractAmountOriginalRate()) != 0){
                        throw new OwlBizException("当前清单金额["+totalAmount+"]与合同金额["+e.getContractAmountOriginalRate()+"]不一致，请重新填写");
                    }
                }
            }
        }
        //引用范本为否，删除范本ID
        if(Objects.nonNull(form.getIsTemp()) && form.getIsTemp() == 0){
            form.setTempId(null);
        }
        if(Objects.nonNull(form.getIsTemp()) && form.getIsTemp() == 1 && CollectionUtils.isNotEmpty(form.getPdffjxx())){
            form.setTempId(null);
        }
        //将form中的contractType重新设置为数据库中该合同原本的contractType，供后续使用
        form.setContractType(e.getContractType());
        //将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(form.getRemark())) {
            form.setRemark(form.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        //-- 转换实体类
        ContractIncomeConcludeE concludeE = Global.mapperFacade.map(form, ContractIncomeConcludeE.class);
        BeanUtils.copyProperties(e, concludeE);
        BeanUtils.copyProperties(form, concludeE, getNullPropertyNames(form));
        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }
        //-- 根据ID检索并赋值对应的名称字段
        fillNameForEdit(concludeE, e);

        //-- 校验合同编码唯一性
        // judgeContractNoOnly(concludeE);

        //-- 合同业务分类
        concludeE.setCategoryPath(CollectionUtils.isEmpty(form.getCategoryPathList()) ? "[]" : JSON.toJSONString(form.getCategoryPathList()));

        //-- 变更 & 续签类型不可编辑签约类型， 补充 or 新签 根据编辑后的属性再做判断
        if (SigningMethodEnum.补充.getCode().equals(concludeE.getSigningMethod()) || SigningMethodEnum.新签.getCode().equals(concludeE.getSigningMethod())) {
            concludeE.setSigningMethod(StringUtils.isNotBlank(concludeE.getPid()) && concludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())
                    ? SigningMethodEnum.补充.getCode() : SigningMethodEnum.新签.getCode());
        }
        if (ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType())){
            checkStartTimeOnSupplement(concludeE);
        }

        //校验信息
        /*if(form.getNoPaySign() == 0 && concludeE.getContractType() == 0) {
            checkContractData(concludeE, form.getFkdwxx(), form.getSkdwxx(), false);
        }*/

        if(StringUtils.isNotEmpty(form.getConincrementype()) &&  (form.getConincrementype().equals("0") ||  form.getConincrementype().equals("3") || form.getConincrementype().equals("5"))){
            concludeE.setSealType(1);
        }else{
            concludeE.setSealType(null);
        }

        BeanChangeUtil<ContractIncomeConcludeE> t = new BeanChangeUtil<>();
        ContractIncomeRecordF e1 = Global.mapperFacade.map(e, ContractIncomeRecordF.class);
        ContractIncomeRecordF e2 = Global.mapperFacade.map(concludeE, ContractIncomeRecordF.class);
        String str = t.contrastObj(e1, e2);

        boolean isEdit = false;
        if(form.getIsEditWps() != null && form.getIsEditWps()){
            isEdit = true;
        }

        /*if(StringUtils.isNotEmpty(e.getTempId()) && StringUtils.isNotEmpty(form.getTempId()) && e.getTempId().equals(form.getTempId())){
            LambdaQueryWrapper<ContractRecordInfoE> payQueryWrapper = new LambdaQueryWrapper<>();
            payQueryWrapper.eq(ContractRecordInfoE::getContractId, concludeE.getId()).eq(ContractRecordInfoE::getDeleted,0);
            ContractRecordInfoE contractRecordInfoE = contractRecordInfoMapper.selectOne(payQueryWrapper);
            if(Objects.nonNull(contractRecordInfoE)){
                ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                contractInfoV.setLinkAdress(linkAdress);
            }
        }else{
            LambdaQueryWrapper<ContractRecordInfoE> payQueryWrapper = new LambdaQueryWrapper<>();
            payQueryWrapper.eq(ContractRecordInfoE::getContractId, concludeE.getId()).eq(ContractRecordInfoE::getDeleted,0);
            ContractRecordInfoE contractRecordInfoE = contractRecordInfoMapper.selectOne(payQueryWrapper);
            if(Objects.nonNull(contractRecordInfoE)) {
                contractRecordInfoMapper.deleteById(contractRecordInfoE);
            }
        }

        if(StringUtils.isEmpty(contractInfoV.getLinkAdress())) {
            if (StringUtils.isNotBlank(concludeE.getTempId())) {
                FileVo fileVo = templateUtils.genTemplateInfo(concludeE.getTempId(), concludeE, concludeE.getName());
                concludeE.setTempFilekey(fileVo.getFileKey());
                //-- 把修改记录更新导表里
                ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(), concludeE.getName(), concludeE.getTempId(), concludeE.getTempFilekey(), str, fileVo.getName(), Integer.parseInt(fileVo.getSize().toString()));
                ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                if (isEdit) {
                    String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                    contractInfoV.setLinkAdress(linkAdress);
                }
            } else {
                if (isEdit) {
                    FileVo fileVo = templateUtils.genTemplateInfo("15497043922911", concludeE, concludeE.getName());
                    concludeE.setTempFilekey(fileVo.getFileKey());
                    //-- 把修改记录更新导表里
                    ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(), concludeE.getName(), concludeE.getTempId(), concludeE.getTempFilekey(), null, fileVo.getName(), Integer.parseInt(fileVo.getSize().toString()));
                    ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                    String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                    contractInfoV.setLinkAdress(linkAdress);
                }
            }
        }*/

        if(ObjectUtils.isNotEmpty(form.getQydws()) && !JSON.toJSONString(form.getQydws()).equals("[{}]")){
            for(int i = 0; i < form.getQydws().size(); i ++){
                if(i == 0){
                    concludeE.setOppositeOneId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i == 1){
                    concludeE.setOppositeTwoId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i ==2){
                    break;
                }
            }
        }
//        if(LocalDate.now().isBefore(form.getGmtExpireStart())){
//            concludeE.setStatus( ContractRevStatusEnum.尚未履行.getCode());
//        }else{
//            concludeE.setStatus( ContractRevStatusEnum.正在履行.getCode());
//        }
        //同样的，编辑合同，合同状态也是"未生效"
       // concludeE.setStatus(ContractRevStatusEnum.未生效.getCode());

        //-- 更新
        contractIncomeConcludeService.updateById(concludeE);
        contractInfoV.setId(concludeE.getId());
        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.编辑.getCode());

        if (!Boolean.TRUE.equals(form.getIsStash()) && !isEdit) {
            contractInfoV.setSsoLinkAdress(postContract(concludeE.getId()));
        }

        //-- 处理范文文件
        dealContractTemp(concludeE, form.getPdffjxx());

        //-- 编辑拓展表
        dealEditContractExpand(concludeE.getId(),form);
        Boolean isCheckFundAmount = true;
        if (StringUtils.isNotBlank(e.getConmaincode())) {
            isCheckFundAmount = false;
        }
        contractIncomeFundService.dealBatch(form.getContractIncomeFundVList(), isCheckFundAmount);
        contractInfoV.setIsSucess(true);

 /*       if (StringUtils.isNotBlank(e.getContractNo())) {
            //-- 处理合同编号
            ContractNoInfoV contractNoInfoV = contractBaseService.generateIncomeContractNo(concludeE, e);
            log.info("ContractIncomeBusinessService.editIncomeContract:contractNoInfoV:{}", JSON.toJSONString(contractNoInfoV));
            if (Objects.isNull(contractNoInfoV)){
                throw new OwlBizException("合同编号生成失败-未知异常");
            }
            if (contractNoInfoV.getCode() == 0){
                throw new OwlBizException(contractNoInfoV.getFailReason());
            }
            concludeE.setContractNo(contractNoInfoV.getContractNo());
            //-- 更新
            contractIncomeConcludeService.updateById(concludeE);
        }*/

        return contractInfoV;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 编辑支出合同数据
     * @param form
     */
    @Transactional
    public ContractInfoV modifyIncomeContract(ContractIncomeAddF form) {
        //-- α.校验ID正确性
        ContractIncomeConcludeE oldConcludeE = contractIncomeConcludeService.getById(form.getId());
        if (Objects.isNull(oldConcludeE)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        ContractInfoV contractInfoV = new ContractInfoV();
        form.setId(null);
        form.setSignDate(null);
        //将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(form.getRemark())) {
            form.setRemark(form.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        //-- 转换实体类
        ContractIncomeConcludeE concludeE = Global.mapperFacade.map(form, ContractIncomeConcludeE.class);
        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }

        //bug修复，使用表单中的pid查询父合同CT码和fromid信息
        ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeMapper.selectById(concludeE.getPid());

        if(ObjectUtils.isNotEmpty(contractIncomeConcludeE)){
            concludeE.setConmaincode(contractIncomeConcludeE.getConmaincode());
            concludeE.setFromid(contractIncomeConcludeE.getFromid());
            concludeE.setSignDate(contractIncomeConcludeE.getSignDate());
        }

        //-- 租户ID
        concludeE.setTenantId(tenantId());
        //-- 初始化相关字段 收款金额
        concludeE.setCollectAmount(BigDecimal.ZERO)
                .setInvoiceAmount(BigDecimal.ZERO)
                .setChangContractAmount(oldConcludeE.getChangContractAmount());

        //-- 为实体类补充处理相关字段的值
        fillNameForAdd(concludeE);

        /*if (StringUtils.isBlank(form.getContractNo())) {
            //-- 客户未输入合同编号后，自动生成合同编号
//            String code = contractConcludeAppService.revContractCode(tenantId(), form.getPid(), RevTypeEnum.支出合同.getCode());
            String code = contractUniqueCodeRuleAppService.genUniqueCode(form.getIsBackDate(),concludeE.getDepartName(),ContractAreaEnum.parseName(concludeE.getIsBackDate()),form.getConmanagetype(),concludeE.getCommunityName(),1,form.getConperformprovincesname());
            concludeE.setContractNo(code);
        }*/
        concludeE.setIsBackReview(1);

        //-- 审批状态 - 待提交
        concludeE.setReviewStatus(ReviewStatusEnum.待提交.getCode());
//        if(LocalDate.now().isBefore(form.getGmtExpireStart())){
//            concludeE.setStatus( ContractRevStatusEnum.尚未履行.getCode());
//        }else{
//            concludeE.setStatus( ContractRevStatusEnum.正在履行.getCode());
//        }
        //修改接口生成新合同，合同状态也是"未生效"
        concludeE.setStatus(ContractRevStatusEnum.未生效.getCode());
        //-- 合同业务分类
        concludeE.setCategoryPath(CollectionUtils.isEmpty(form.getCategoryPathList()) ? "[]" : JSON.toJSONString(form.getCategoryPathList()));
        //-- 签约类型 默认新签，再根据传参判断是变更还是续签
        concludeE.setContractType(ContractTypeEnum.修改合同.getCode());

        //校验信息
        if(form.getNoPaySign() == 0) {
            checkContractData(concludeE, form.getFkdwxx(), form.getSkdwxx(), true);
        }

        if(ObjectUtils.isNotEmpty(form.getQydws()) && !JSON.toJSONString(form.getQydws()).equals("[{}]")){
            for(int i = 0; i < form.getQydws().size(); i ++){
                if(i == 0){
                    concludeE.setOppositeOneId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i == 1){
                    concludeE.setOppositeTwoId(form.getQydws().get(i).getOppositeOneId());
                    concludeE.setOppositeOne(form.getQydws().get(i).getOppositeOne());
                }
                if(i ==2){
                    break;
                }
            }
        }

        //引用范本为否，删除范本ID
        if(Objects.nonNull(concludeE.getIsTemp()) && concludeE.getIsTemp() == 0){
            concludeE.setTempId(null);
        }
        if(Objects.nonNull(concludeE.getIsTemp()) && concludeE.getIsTemp() == 1 && CollectionUtils.isNotEmpty(form.getPdffjxx())){
            concludeE.setTempId(null);
        }
        contractIncomeConcludeMapper.insert(concludeE);
        contractInfoV.setId(concludeE.getId());

        boolean isEdit = false;
        if(form.getIsEditWps() != null && form.getIsEditWps()){
            isEdit = true;
        }

        //范本操作
        if(StringUtils.isNotBlank(concludeE.getTempId())){
            FileVo fileVo = templateUtils.genTemplateInfo(concludeE.getTempId(), concludeE, concludeE.getName());
            concludeE.setTempFilekey(fileVo.getFileKey());
            //-- 把修改记录更新导表里
            ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),concludeE.getTempId(),concludeE.getTempFilekey(),null,fileVo.getName(),Integer.parseInt(fileVo.getSize().toString()));
            ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
            if(isEdit){
                String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                contractInfoV.setLinkAdress(linkAdress);
            }
        }else{
            if(isEdit){
                FileVo fileVo = templateUtils.genTemplateInfo("15497043922911", concludeE, concludeE.getName());
                concludeE.setTempFilekey(fileVo.getFileKey());
                //-- 把修改记录更新导表里
                ContractRecordInfoE contractRecordInfoE = contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),concludeE.getTempId(),concludeE.getTempFilekey(),null,fileVo.getName(),Integer.parseInt(fileVo.getSize().toString()));
                ContractRecordInfoV contractRecordInfoV = Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class);
                String linkAdress = externalFeignClient.tempUpload(contractRecordInfoV);
                contractInfoV.setLinkAdress(linkAdress);
            }
        }

        //-- 处理范文文件
        dealContractTemp(concludeE, form.getPdffjxx());

        logService.insertOneLog(concludeE.getId(), form.getName(), LogActionTypeEnum.新增.getCode());

        if (!Boolean.TRUE.equals(form.getIsStash()) && !isEdit) {
            contractInfoV.setSsoLinkAdress(postContract(concludeE.getId()));
        }

        //-- 入库拓展表
        dealContractExpand(concludeE.getId(),form);
        form.getContractIncomeFundVList().stream().forEach( s-> {
            s.setContractId(concludeE.getId());
        });
        Boolean isCheckFundAmount = false;
        List<ContractIncomeFundUpdateF> notAddList = form.getContractIncomeFundVList().stream()
                .filter(item -> !StringUtils.equals("add", item.getActionCode()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(notAddList)) {
            isCheckFundAmount = true;
        }
        contractIncomeFundService.dealBatchModify(form.getContractIncomeFundVList(), isCheckFundAmount);
        List<AttachmentE> attachmentEList = attachmentService.listById(form.getPid());
        if(ObjectUtils.isNotEmpty(attachmentEList)){
            AttachmentE attachmentF = new AttachmentE();
            BeanUtils.copyProperties(attachmentEList.get(0),attachmentF);
            attachmentF.setId(null);
            attachmentF.setBusinessId(concludeE.getId());
            attachmentService.save(attachmentF);
        }

        //-- 处理合同编号
        ContractIncomeConcludeE parentConcludeE =  contractIncomeConcludeService.getById(form.getPid());
        if (Objects.isNull(parentConcludeE)){
            throw new OwlBizException("父合同不存在");
        }
        ContractIncomeConcludeE contractIncomeConcludeE1 = new ContractIncomeConcludeE();
        contractIncomeConcludeE1.setId(concludeE.getId());
        contractIncomeConcludeE1.setContractNo(parentConcludeE.getContractNo());
        contractIncomeConcludeMapper.updateById(contractIncomeConcludeE1);
        return contractInfoV;
    }

    /**
     * 提交合同
     * @param id 合同ID
     * 修改表单
     */
    @Transactional(rollbackFor = Exception.class)
    public String postContract(String id) {
        //-- α.校验ID正确性
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        LambdaQueryWrapper<ContractProcessRecordE> queryWrappers = new LambdaQueryWrapper<>();
        queryWrappers.eq(ContractProcessRecordE::getContractId, concludeE.getId())
                .eq(ContractProcessRecordE::getDeleted, 0);
        List<ContractProcessRecordE> processList = contractProcessRecordMapper.selectList(queryWrappers);
        if(CollectionUtils.isNotEmpty(processList) && !userId().equals(processList.get(0).getOperator())){
            throw new OwlBizException("流程发起人为“"+processList.get(0).getOperatorName()+"”，非流程发起人不允许发起流程！");
        }
        //-- 校验合同参数
        if (contractPostCheck){
            checkBeforePost(concludeE);
        }
        //-- 处理合同编号
        ContractNoInfoV contractNoInfoV = contractBaseService.generateIncomeContractNo(concludeE, null);
        if (Objects.isNull(contractNoInfoV)){
            throw new OwlBizException("合同编号生成失败-未知异常");
        }
        if (contractNoInfoV.getCode() == 0){
            throw new OwlBizException(contractNoInfoV.getFailReason());
        }
        concludeE.setContractNo(contractNoInfoV.getContractNo());
        //-- β.记录日志
        logService.insertOneLog(id, concludeE.getName(), LogActionTypeEnum.提交.getCode());
        concludeE.setReviewStatus(ReviewStatusEnum.审批中.getCode());
        contractIncomeConcludeService.updateById(concludeE);
        try {
            return createProcess(concludeE);
        } catch (Exception e){
            // 回退合同编号的序号
            contractBaseService.callBackRedisCounter(contractNoInfoV);
            log.info("收入合同流程发起异常：{}",e.getMessage());
            throw new OwlBizException("OA流程发起超时，请稍后重试！");
        }
    }

    /**
     * 收入合同创建流程
     * 目前缺少的表单字段：合同发起审批层级、合同范本使用情况、我方签约单位、项目业态、是否补充协议
     * @param concludeE
     * @return
     */
    public String createProcess(ContractIncomeConcludeE concludeE){
        LambdaQueryWrapper<ContractProcessRecordE> queryWrappers = new LambdaQueryWrapper<>();
        queryWrappers.eq(ContractProcessRecordE::getContractId, concludeE.getId())
        .eq(ContractProcessRecordE::getType, ContractConcludeEnum.INCOME.getCode())
        .eq(ContractProcessRecordE::getDeleted, 0);
        if(concludeE.getIsBackReview() != null  && concludeE.getIsBackReview() == 1){
            queryWrappers.eq(ContractProcessRecordE::getSubType, 2);
        }else{
            queryWrappers.eq(ContractProcessRecordE::getSubType, 1);
        }
        ContractProcessRecordE sk = contractProcessRecordMapper.selectOne(queryWrappers);
        if (ObjectUtils.isNotEmpty(sk) && StringUtils.isNotBlank(sk.getProcessId())) {
            //说明该收入合同之前创建过流程，调更新接口更新流程名
            BusinessInfoF businessInfoF = buildBusinessInfoF(concludeE);
            businessInfoF.setProcessId(sk.getProcessId());
            log.info("收入合同更新审批表单数据:{}", JSON.toJSONString(businessInfoF));
            //响应结构保持不变
            ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
            if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                log.info("收入合同流程更新失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
                sk.setReviewStatus(ReviewStatusEnum.已拒绝.getCode());
                contractProcessRecordMapper.updateById(sk);
                throw new OwlBizException("流程更新失败");
            }
            //流程正常发起了，将流程记录表的审批状态修改为"审批中"
            sk.setReviewStatus(ReviewStatusEnum.审批中.getCode());
            contractProcessRecordMapper.updateById(sk);
            //流程名更新成功后再继续原逻辑
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if(ObjectUtils.isNotEmpty(s)){
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(sk.getProcessId());
            return externalFeignClient.validateFw(f);
        }
        BusinessInfoF businessInfoF = buildBusinessInfoF(concludeE);
        log.info("收入合同新增审批表单数据:{}", JSON.toJSONString(businessInfoF));
        ProcessCreateV processCreateV;
        if (devFlag == 1){
            ProcessCreateReturnV ES_RETURN = new ProcessCreateReturnV();
            ES_RETURN.setZZSTAT("S");
            ProcessCreateResultV ET_RESULT = new ProcessCreateResultV();
            ET_RESULT.setRequestid("mock requestId");
            processCreateV = new ProcessCreateV();
            processCreateV.setES_RETURN(ES_RETURN);
            processCreateV.setET_RESULT(ET_RESULT);
        } else {
            processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
        }
        Integer reviewStatusCode = ReviewStatusEnum.审批中.getCode();
        // γ.若合同审核流程创建失败，则数据不入库;
        if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
            log.info("收入合同流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
            reviewStatusCode = ReviewStatusEnum.已拒绝.getCode();
//            throw new OwlBizException("中建返回数据时,根据合同ID检索数据失败,流程创建失败");
        }
        // δ.构造入库数据,能存的都存下来
        String requestid = processCreateV.getET_RESULT().getRequestid();
        ContractProcessRecordE contractProcessRecordE = Builder.of(ContractProcessRecordE::new)
                .with(ContractProcessRecordE::setProcessId, requestid) // 流程请求id
                .with(ContractProcessRecordE::setContractId, concludeE.getId()) // 合同ID
                .with(ContractProcessRecordE::setReviewStatus, reviewStatusCode) // 审核状态
                .with(ContractProcessRecordE::setTenantId, concludeE.getTenantId()) // 租户ID
                .with(ContractProcessRecordE::setCreator, concludeE.getCreator()) // 创建人
                .with(ContractProcessRecordE::setCreatorName, concludeE.getCreatorName()) // 创建人名称
                .with(ContractProcessRecordE::setType, ContractConcludeEnum.INCOME.getCode()) // 类型（1合同订立支出2合同订立收入）
                .build();
        // η.非并发接口,为保证幂等性,无对应记录再插入数据 CHECK 代码逻辑不适用并发环境;暂不做redis缓存
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(ContractProcessRecordE::getProcessId, requestid).eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(recordE)) {
            contractProcessRecordMapper.updateById(contractProcessRecordE);
            log.info("返回的收入流程已存在,已更新数据库记录");
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if(ObjectUtils.isNotEmpty(s)){
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(requestid);
            return externalFeignClient.validateFw(f);
        }
        if(concludeE.getIsBackReview() != null && concludeE.getIsBackReview() == 1){
            contractProcessRecordE.setSubType(2);
        }else{
            contractProcessRecordE.setSubType(1);
        }
        // θ.数据入 [contract_process_record] 库
        contractProcessRecordMapper.insert(contractProcessRecordE);
        log.info("返回的收入流程不存在,已插入数据库记录");

        if(StringUtils.isNotBlank(requestid)){
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if(ObjectUtils.isNotEmpty(s)){
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(requestid);
            return externalFeignClient.validateFw(f);
        }

        return "";
    }

    private BusinessInfoF buildBusinessInfoF(ContractIncomeConcludeE concludeE){
        BusinessInfoF businessInfoF = new BusinessInfoF();
        //所属区域
        businessInfoF.setSsqy(convertRegion(concludeE));
        businessInfoF.setFormDataId(concludeE.getId());
        businessInfoF.setEditFlag(0);
        businessInfoF.setFormType(ContractConcludeEnum.INCOME.getCode());
        if(concludeE.getIsBackReview() != null && concludeE.getIsBackReview() == 1){
            businessInfoF.setFlowType(bizCode);
        }else{
            businessInfoF.setFlowType(concludeE.getBizCode());
        }
        //收支类型
        if(concludeE.getNoPaySign() != null && concludeE.getNoPaySign() == 1){
            businessInfoF.setSzlx(2);
        }else{
            businessInfoF.setSzlx(1);
        }
        //流程名
        businessInfoF.setContractName(concludeE.getName());
        //合同类型
        if(StringUtils.isNotEmpty(concludeE.getBizCode())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同类型审批.getCode(), concludeE.getBizCode());
            if (CollectionUtils.isNotEmpty(value)) {
                businessInfoF.setHtywlx(Integer.parseInt(value.get(0).getName()));
            }
        }
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(concludeE.getId());
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        //收入合同类别,1230修正：适配OA测的key
        if(ObjectUtils.isNotEmpty(concludeExpandV) && StringUtils.isNotEmpty(concludeExpandV.getConmanagetype())){
            if ("3".equals(concludeExpandV.getConmanagetype())) {
                businessInfoF.setSrhtlb(0);
            } else if ("4".equals(concludeExpandV.getConmanagetype())) {
                businessInfoF.setSrhtlb(1);
            }
            //businessInfoF.setSrhtlb(Integer.parseInt(concludeExpandV.getConmanagetype()));
        }
        //收入合同的增值合同类型
        if(ObjectUtils.isNotEmpty(concludeExpandV) && StringUtils.isNotEmpty(concludeExpandV.getConincrementype())){
            businessInfoF.setZzhtlx(Integer.parseInt(concludeExpandV.getConincrementype()));
        }
        //合同范本使用情况
        if (StringUtils.isNotBlank(concludeExpandV.getConmodelusecondition())) {
            if ("01".equals(concludeExpandV.getConmodelusecondition())) {
                businessInfoF.setHtfbsyqk(0);
            } else if ("02".equals(concludeExpandV.getConmodelusecondition())) {
                businessInfoF.setHtfbsyqk(1);
            } else if ("03".equals(concludeExpandV.getConmodelusecondition())) {
                businessInfoF.setHtfbsyqk(2);
            } else if ("99".equals(concludeExpandV.getConmodelusecondition())) {
                businessInfoF.setHtfbsyqk(3);
            }
        }
        //我方签约单位
        businessInfoF.setWfqydw("13797192283311".equals(concludeE.getOurPartyId()) ? 0 : 1);
        //是否满足集团招商刻度值
        if(ObjectUtils.isNotEmpty(concludeExpandV) && concludeExpandV.getFmzjtzskd() != null){
            businessInfoF.setSfmzjtzskdz(concludeExpandV.getFmzjtzskd());
        }
        //合同总金额（含税）
        businessInfoF.setHtzjehs(concludeE.getContractAmountOriginalRate());

        //是否补充协议
        businessInfoF.setSfbcxy(ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType()) ? 0 : 1);
        if(ContractBusinessLineEnum.物管.getCode().equals(concludeE.getContractBusinessLine())){
            businessInfoF.setGlzz(1);
            if(StringUtils.equals("总部", concludeE.getRegion())){
                businessInfoF.setGlzz(0);
            }
        }else{
            businessInfoF.setGlzz(3);
        }
        return businessInfoF;
    }

    /**
     * 修改合同区域：总部0 华北区域1 华东区域2 西部区域3 华南区域4
     * 普通合同区域：总部0 华北区域2 华东区域4 西部区域5 华南区域3
     *
     * @param concludeE
     * @return
     */
    private Integer convertRegion(ContractIncomeConcludeE concludeE) {
        if (StringUtils.isBlank(concludeE.getRegion())) {
            return null;
        }
        if (StringUtils.equals("总部", concludeE.getRegion())) {
            return 0;
        }
        if (StringUtils.equals("华北区域", concludeE.getRegion())) {
            return ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType()) ? 1 : 2;
        }
        if (StringUtils.equals("华东区域", concludeE.getRegion())) {
            return ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType()) ? 2 : 4;
        }
        if (StringUtils.equals("西部区域", concludeE.getRegion())) {
            return ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType()) ? 3 : 5;
        }
        if (StringUtils.equals("华南区域", concludeE.getRegion())) {
            return ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType()) ? 4 : 3;
        }
        return null;
    }

    /**
     * 校验合同编号唯一性
     * @param concludeE 合同数据
     */
    public void judgeContractNoOnly(ContractIncomeConcludeE concludeE) {

    }

    /**
     * 提交前对合同数据进行校验
     * @param concludeE 收入合同数据
     * @return 校验是否成功
     */
    public Boolean checkBeforePost(ContractIncomeConcludeE concludeE) {
        if (StringUtils.isBlank(concludeE.getOppositeOneId())) {
            throw new OwlBizException("对方单位1-为空，不可提交");
        }

        //-- 对方单位1 & 2
        CustomerRv one = orgFeignClient.getCustomerVById(concludeE.getOppositeOneId());

        if (!one.getDisabled().equals(0)) {
            throw new OwlBizException("对方单位1 数据已被禁用，不可提交");
        }

        if (StringUtils.isNotBlank(concludeE.getOppositeTwoId())) {
            CustomerRv two = orgFeignClient.getCustomerVById(concludeE.getOppositeTwoId());

            if (Objects.nonNull(two) && !two.getDisabled().equals(0)) {
                throw new OwlBizException("对方单位2 数据已被禁用，不可提交");
            }
        }

        //合同校验数据接口
        ContractBasePullV contractBasePullV = verifyContract(concludeE.getId());
        if(contractBasePullV.getIsright().equals("false")){
            throw new OwlBizException(contractBasePullV.getCheckinfo());
        }

        checkValueBeforePost(concludeE);

        return true;
    }

    /**
     * 提交合同前校验各参数是否正确
     * @param concludeE 合同参数
     */
    public void checkValueBeforePost(ContractIncomeConcludeE concludeE) {
        //-- 通用参数
        if (StringUtils.isBlank(concludeE.getName())) {
            throw new OwlBizException("合同名称 不可为空，请重新编辑");
        }

        if (Objects.isNull(concludeE.getContractType())) {
            throw new OwlBizException("合同属性 不可为空，请重新编辑");
        }
        if (ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType())) {
            if (StringUtils.isBlank(concludeE.getPid())) {
                throw new OwlBizException("关联主合同 不可为空，请重新编辑");
            }
        }


        if (StringUtils.isBlank(concludeE.getCostCenterId())) {
            throw new OwlBizException("成本中心 不可为空，请重新编辑");
        }
        if (Objects.isNull(concludeE.getGmtExpireStart())) {
            throw new OwlBizException("合同开始日期 不可为空，请重新编辑");
        }

//        List<ContractIncomeFundE> list = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
//                .orderByDesc(ContractIncomeFundE.GMT_CREATE)
//                .eq(ContractIncomeFundE.CONTRACT_ID, concludeE.getId()));
//        if(ObjectUtils.isEmpty(list) && concludeE.getNoPaySign() == 0){
//            throw new OwlBizException("合同清单不可为空，请填写后再提交");
//        }

        List<ContractRecordInfoE> lists = contractRecordInfoService.list(new QueryWrapper<ContractRecordInfoE>()
                .eq(ContractRecordInfoE.TENANT_ID, tenantId())
                .eq(ContractRecordInfoE.CONTRACT_ID, concludeE.getId()));
        if(ObjectUtils.isEmpty(lists) ||  lists.size() != 1){
            throw new OwlBizException("合同范本文件只能一个，请检查后再提交");
        }

    }

    public ContractIncomeConcludeDetailV detail(ContractDetailV vo) {
        String id = vo.getId();
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("该合同数据不存在");
        }
        ContractIncomeConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractIncomeConcludeDetailV.class);
        detailV.setServeType(ServeTypeEnum.convert(concludeE.getContractServeType()));

        //-- 拓展字段处理
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV,concludeExpandV);


        //-- 字段赋值
        detailV.setContractNatureName(VirtuallyTypeEnum.parseName(detailV.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                .setContractTypeName(ContractTypeEnum.parseName(detailV.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                .setSigningMethodName(SigningMethodEnum.parseName(detailV.getSigningMethod()));//-- 签约方式 0 新签 1 补充协议 2 续签
        detailV.setDetailTableId(detailV.getId());//-- 合同详情的TABLE页面需要用到这个字段，与合同ID同值，用于分页查询关联数据

        //-- 金额字段计算
        if (null != detailV.getContractAmountOriginalRate() && null != detailV.getCollectAmount()) {
            detailV.setUnCollectAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getCollectAmount()));
            detailV.setUnInvoiceAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getInvoiceAmount()));
        }

        //主合同金额处理
        if(StringUtils.isNotBlank(detailV.getPid()) && !detailV.getPid().equals("0")){
            ContractIncomeConcludeE mainConcludeE = contractIncomeConcludeService.getById(detailV.getPid());
            detailV.setMainContractAmount(mainConcludeE.getContractAmountOriginalRate());
            detailV.setPidName(mainConcludeE.getName());
            if(mainConcludeE.getChangContractAmount().compareTo(BigDecimal.ZERO) != 0){
                detailV.setMainContractBjAmount(mainConcludeE.getChangContractAmount());
            }else{
                detailV.setMainContractBjAmount(mainConcludeE.getContractAmountOriginalRate());
            }
        }
        if(vo.getIsBc() && detailV.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
            detailV.setChangContractAmount(detailV.getContractAmountOriginalRate());
        }
        dealBtnShowForDetail(detailV, concludeE);

        detailV.setCategoryPathList(JSON.parseArray(concludeE.getCategoryPath(), Long.class));

        detailV.setBindpointresources("点击查看点位资源");
        detailV.setGmtCreate(concludeE.getGmtCreate());

        //-- 费项&税率信息数据列填充
        fillChargeAndTaxRateList(detailV, vo.getIsBc());
        if(vo.getIsBc()){
            detailV.setPdffjxx(null);
            detailV.setFjxx(null);
            LambdaQueryWrapper<AttachmentE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AttachmentE::getBusinessId, vo.getId())
                    .eq(AttachmentE::getDeleted, 0)
                    .eq(AttachmentE::getBusinessType, "1002");
            List<AttachmentE> attachmentEList = attachmentMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(attachmentEList)){
                return detailV;
            }
            List<ContractFjxxF> fjxxList = new ArrayList<>();
            for(AttachmentE a : attachmentEList){
                ContractFjxxF fjxx = new ContractFjxxF();
                fjxx.setUid(a.getFileuuid())
                        .setName(a.getName())
                        .setSuffix(a.getSuffix())
                        .setSize(Objects.nonNull(a.getSize()) ? a.getSize().toString() : "0")
                        .setFileKey(a.getFileKey())
                        .setFileId(a.getFileuuid())
                        .setFilename(a.getName());
                fjxxList.add(fjxx);
            }
            detailV.setFjxx(fjxxList);
        }
        return detailV;
    }

    public void fillChargeAndTaxRateList(ContractIncomeConcludeDetailV detailV,Boolean isBc) {
        List<ContractIncomeFundE> list = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.CONTRACT_ID, detailV.getId()));
        List<ContractIncomeFundV> sb = Global.mapperFacade.mapAsList(list, ContractIncomeFundV.class);
        List<String> charge = new ArrayList<>();
        List<String> taxRate = new ArrayList<>();
        BigDecimal fundAmount = BigDecimal.ZERO;
        for (ContractIncomeFundE fundE : list) {
            fundAmount = fundAmount.add(fundE.getAmount());
            charge.add(fundE.getChargeItem());
            taxRate.add(fundE.getTaxRate());
        }
        //合同详情-清单累计金额：保留2位小数
        detailV.setFundAmount(fundAmount.setScale(2, RoundingMode.HALF_UP));
        detailV.setChargeItemShowList(charge.stream().collect(Collectors.joining(",")));
        detailV.setTaxRateShowList(taxRate.stream().collect(Collectors.joining(",")));
        if(detailV.getFundAmount().compareTo(BigDecimal.ZERO) != 0){
            detailV.setFundAmountPj(detailV.getFundAmount().toString() + "元");
        }
        if(detailV.getChangContractAmount().compareTo(BigDecimal.ZERO) != 0 ){
            detailV.setChangContractAmountPj(detailV.getChangContractAmount().toString() + "元");
        }
        detailV.setSupplyContractAmountPj(detailV.getContractAmountOriginalRate().toString() + "元");
        if(detailV.getSigningMethod() == 1 && (Objects.nonNull(detailV.getContractNature()) && detailV.getContractNature() == 2) || Objects.isNull(detailV.getContractNature())){
            detailV.setSupplyContractChangeAmountPj(Objects.nonNull(detailV.getMainContractBjAmount()) ? String.valueOf(detailV.getMainContractBjAmount().add(detailV.getContractAmountOriginalRate())) : detailV.getContractAmountOriginalRate().toString()
                    + "元");
        }
        if(detailV.getSigningMethod() == 1 && Objects.nonNull(detailV.getContractNature()) && detailV.getContractNature() != 2){
            detailV.setSupplyContractChangeAmountPj(detailV.getMainContractBjAmount().toString() + "元");
        }
        if(isBc){
            sb.forEach(x->x.setIsMain(1));
        }

        if(CollectionUtils.isNotEmpty(sb)){
            Map<String, List<ContractIncomePlanConcludeE>> funCountMap = new HashMap<>();
            Map<String, BigDecimal> funAmountMap = new HashMap<>();
            LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ContractIncomePlanConcludeE::getContractId, detailV.getId())
                    .ne(ContractIncomePlanConcludeE::getPid,"0")
                    .eq(ContractIncomePlanConcludeE::getDeleted,0);
            List<ContractIncomePlanConcludeE> concludeEList = contractIncomePlanConcludeMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(concludeEList)){
                funCountMap = concludeEList.stream().collect(Collectors.groupingBy(ContractIncomePlanConcludeE::getContractPayFundId));
                List<IncomeConcludePlanV2> planV2List = Global.mapperFacade.mapAsList(concludeEList, IncomeConcludePlanV2.class);
                contractIncomePlanConcludeService.markUsed(planV2List);

                funAmountMap = planV2List.stream()
                        .filter(x->Boolean.TRUE.equals(x.getUsed())) // 过滤 used = true 的数据
                        .collect(Collectors.groupingBy(
                                IncomeConcludePlanV2::getContractPayFundId,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        IncomeConcludePlanV2::getPlannedCollectionAmount,
                                        BigDecimal::add
                                )
                        ));
            }
            Map<String, List<ContractIncomePlanConcludeE>> finalFunCountMap = funCountMap;
            Map<String, BigDecimal> finalFunAmountMap = funAmountMap;
            sb.forEach(x->{
                x.setIsLock(CollectionUtils.isEmpty(finalFunCountMap.get(x.getId())) ? Boolean.FALSE : Boolean.TRUE);
                x.setPlanUseAmountNum(Objects.nonNull(finalFunAmountMap.get(x.getId())) ? finalFunAmountMap.get(x.getId()) : BigDecimal.ZERO);
            });
        }
        detailV.setContractIncomeFundVList(sb);
    }

    /**
     * 查询变更记录
     * @param id 合同ID
     * @return 变更记录
     */
    public RelationRecordDetailV changeRecord(String id) {

        List<ContractRelationRecordE> list = contractRelationRecordService.list(new QueryWrapper<ContractRelationRecordE>()
                .eq(ContractRelationRecordE.CONTRACT_TYPE, RevTypeEnum.收入合同.getCode())
                .eq(ContractRelationRecordE.ADD_ID, id)
                .eq(ContractRelationRecordE.TYPE, ActionTypeEnum.变更.getCode())
                .orderByDesc(ContractRelationRecordE.GMT_CREATE));

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        RelationRecordDetailV map = Global.mapperFacade.map(list.get(0), RelationRecordDetailV.class);

        Optional.ofNullable(contractIncomeConcludeService.getById(map.getAddId())).ifPresent(v -> {
            map.setAddName(v.getName())
                    .setAddContractNo(v.getContractNo());
        });

        Optional.ofNullable(contractIncomeConcludeService.getById(map.getOldId())).ifPresent(v -> {
            map.setOldName(v.getName())
                    .setOldContractNo(v.getContractNo());
        });

        return map;
    }

    //-- 新增支出合同订立信息表 根据ID检索并赋值对应的名称字段
    public void fillNameForEdit(ContractIncomeConcludeE concludeE, ContractIncomeConcludeE old) {

        //-- 合同分类
        if (Objects.nonNull(concludeE.getCategoryId()) && !concludeE.getCategoryId().equals(old.getCategoryId())) {
            Optional.ofNullable(contractCategoryAppService.queryById(concludeE.getCategoryId())).ifPresentOrElse(v -> {
                concludeE.setCategoryName(v.getName());
            }, () -> {throw new OwlBizException("根据合同分类ID检索分类数据失败");});
        }
        //-- 对方单位1
        if (StringUtils.isNotBlank(concludeE.getOppositeOneId())) {
            Optional.ofNullable(orgFeignClient.getCustomerVById(concludeE.getOppositeOneId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位1-客户数据已被禁用，请重新选择");
                }
                concludeE.setOppositeOne(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位1 ID检索客户数据失败");});
        }
        //-- 对方单位2
        if (StringUtils.isNotBlank(concludeE.getOppositeTwoId())) {
            Optional.ofNullable(orgFeignClient.getCustomerVById(concludeE.getOppositeTwoId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位2-客户数据已被禁用，请重新选择");
                }
                concludeE.setOppositeTwo(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位2 ID检索客户数据失败");});
        }
        //-- 我方单位
        if (StringUtils.isNotBlank(concludeE.getOurPartyId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceId(Long.parseLong(concludeE.getOurPartyId()))).ifPresentOrElse(v -> {
                concludeE.setOurParty(v.getNameCn());
            }, () -> {throw new OwlBizException("根据我方单位 ID检索法定单位数据失败");});
        }
        //-- 成本中心
        if (StringUtils.isNotBlank(concludeE.getCostCenterId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceCostId(Long.parseLong(concludeE.getCostCenterId()))).ifPresentOrElse(v -> {
                concludeE.setCostCenterName(v.getNameCn());
            }, () -> {throw new OwlBizException("根据成本中心ID检索成本中心数据失败");});
        }
        //-- 项目
        if (StringUtils.isNotBlank(concludeE.getCommunityId())) {
            Optional.ofNullable(spaceFeignClient.getById(concludeE.getCommunityId())).ifPresentOrElse(c -> {
                concludeE.setCommunityName(c.getName());
            }, () -> {throw new OwlBizException("根据项目ID检索项目数据失败");});
        }
        //-- 签约人
        if (StringUtils.isNotBlank(concludeE.getSignPersonId()) && !concludeE.getSignPersonId().equals(old.getSignPersonId())) {
            Optional.ofNullable(userFeignClient.getUsreInfoByUserId(concludeE.getSignPersonId())).ifPresentOrElse(v -> {
                concludeE.setSignPerson(v.getUserName());
            }, () -> {throw new OwlBizException("根据签约人ID检索签约人数据失败");});
        }
        //-- 所属公司
        if (StringUtils.isNotBlank(concludeE.getOrgId()) && !concludeE.getOrgId().equals(old.getOrgId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(concludeE.getOrgId()))).ifPresentOrElse(v -> {
                concludeE.setOrgName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属公司ID检索组织名称失败");});
        }
        concludeE.setRegion(ContractAreaEnum.parseName(concludeE.getIsBackDate()));
        //-- 所属部门
        if (StringUtils.isNotBlank(concludeE.getDepartId()) && !concludeE.getDepartId().equals(old.getDepartId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(concludeE.getDepartId()))).ifPresentOrElse(v -> {
                concludeE.setDepartName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属部门ID检索组织名称失败");});
        }
        //-- 负责人
        if (StringUtils.isNotBlank(concludeE.getPrincipalId()) && !concludeE.getPrincipalId().equals(old.getPrincipalId())) {
            Optional.ofNullable(userFeignClient.getUsreInfoByUserId(concludeE.getPrincipalId())).ifPresentOrElse(v -> {
                concludeE.setPrincipalName(v.getUserName());
            }, () -> {throw new OwlBizException("根据负责人ID检索名称失败");});
        }
        //-- 交易类型
        if (StringUtils.isNotBlank(concludeE.getDealTypeId()) && !concludeE.getDealTypeId().equals(old.getDealTypeId())) {
            concludeE.setDealType(IncomeDealTypeEnum.parseName(concludeE.getDealTypeId()));
        }
        //-- 币种
        if (StringUtils.isNotBlank(concludeE.getCurrencyCode())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.币种.getCode(), concludeE.getCurrencyCode());
            if (CollectionUtils.isNotEmpty(value)) {
                concludeE.setCurrency(value.get(0).getName());
            }
        }

    }

    //-- 新增支出合同订立信息表 根据ID检索并赋值对应的名称字段
    public void fillNameForAdd(ContractIncomeConcludeE concludeE) {

        //-- 合同分类
        if (Objects.nonNull(concludeE.getCategoryId())) {
            Optional.ofNullable(contractCategoryAppService.queryById(concludeE.getCategoryId())).ifPresentOrElse(v -> {
                concludeE.setCategoryName(v.getName());
            }, () -> {throw new OwlBizException("根据合同分类ID检索分类数据失败");});
        }
        //-- 对方单位1
        if (StringUtils.isNotBlank(concludeE.getOppositeOneId())) {
            Optional.ofNullable(orgFeignClient.getCustomerVById(concludeE.getOppositeOneId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位1-客户数据已被禁用，请重新选择");
                }
                concludeE.setOppositeOne(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位1 ID检索客户数据失败");});
        }
        //-- 对方单位2
        if (StringUtils.isNotBlank(concludeE.getOppositeTwoId())) {
            Optional.ofNullable(orgFeignClient.getCustomerVById(concludeE.getOppositeTwoId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位2-客户数据已被禁用，请重新选择");
                }
                concludeE.setOppositeTwo(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位2 ID检索客户数据失败");});
        }
        //-- 我方单位
        if (StringUtils.isNotBlank(concludeE.getOurPartyId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceId(Long.parseLong(concludeE.getOurPartyId()))).ifPresentOrElse(v -> {
                concludeE.setOurParty(v.getNameCn());
            }, () -> {throw new OwlBizException("根据我方单位 ID检索法定单位数据失败");});
        }
        //-- 成本中心
        if (StringUtils.isNotBlank(concludeE.getCostCenterId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceCostId(Long.parseLong(concludeE.getCostCenterId()))).ifPresentOrElse(v -> {
                concludeE.setCostCenterName(v.getNameCn());
            }, () -> {throw new OwlBizException("根据成本中心ID检索成本中心数据失败");});
        }
        //-- 项目
        if (StringUtils.isNotBlank(concludeE.getCommunityId())) {
            Optional.ofNullable(spaceFeignClient.getById(concludeE.getCommunityId())).ifPresentOrElse(c -> {
                concludeE.setCommunityName(c.getName());
            }, () -> {throw new OwlBizException("根据项目ID检索项目数据失败");});
        }
        //-- 签约人
        if (StringUtils.isNotBlank(concludeE.getSignPersonId())) {
            Optional.ofNullable(userFeignClient.getUsreInfoByUserId(concludeE.getSignPersonId())).ifPresentOrElse(v -> {
                concludeE.setSignPerson(v.getUserName());
            }, () -> {throw new OwlBizException("根据签约人ID检索签约人数据失败");});
        }
        //-- 所属公司
        if (StringUtils.isNotBlank(concludeE.getOrgId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(concludeE.getOrgId()))).ifPresentOrElse(v -> {
                concludeE.setOrgName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属公司ID检索组织名称失败");});
        }
        concludeE.setRegion(ContractAreaEnum.parseName(concludeE.getIsBackDate()));
        //-- 所属部门
        if (StringUtils.isNotBlank(concludeE.getDepartId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(concludeE.getDepartId()))).ifPresentOrElse(v -> {
                concludeE.setDepartName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属部门ID检索组织名称失败");});
        }
        //-- 负责人
        if (StringUtils.isNotBlank(concludeE.getPrincipalId())) {
            Optional.ofNullable(userFeignClient.getUsreInfoByUserId(concludeE.getPrincipalId())).ifPresentOrElse(v -> {
                concludeE.setPrincipalName(v.getUserName());
            }, () -> {throw new OwlBizException("根据负责人ID检索名称失败");});
        }
        //-- 币种
        if (StringUtils.isNotBlank(concludeE.getCurrencyCode())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.币种.getCode(), concludeE.getCurrencyCode());
            if (CollectionUtils.isNotEmpty(value)) {
                concludeE.setCurrency(value.get(0).getName());
            }
        }
        concludeE.setPrincipalId(userId())
                .setPrincipalName(userName());

    }

    //校验补充合同金额和应付应收金额
    public void checkContractData(ContractIncomeConcludeE concludeE, List<ContractZffxxF> fkdwxx, List<ContractSrfxxF> skdwxx, boolean isFlag){
        if(concludeE.getContractType() == 0){
            if(ObjectUtils.isNotEmpty(fkdwxx)){
                List<BigDecimal> sk = fkdwxx.stream().map(sb -> new BigDecimal(sb.getDraweepayamt())).collect(Collectors.toList());
                BigDecimal total = sk.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                if(total.compareTo(concludeE.getContractAmountOriginalRate()) != 0){
                    throw new OwlBizException("应付合同金额要等于主合同金额加上补充合同金额之和！");
                }
            }
            if(ObjectUtils.isNotEmpty(skdwxx)){
                List<BigDecimal> sk = skdwxx.stream().map(sb -> new BigDecimal(sb.getPayeeamt())).collect(Collectors.toList());
                BigDecimal total = sk.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                if(total.compareTo(concludeE.getContractAmountOriginalRate()) != 0){
                    throw new OwlBizException("应收合同金额要等于主合同金额加上补充合同金额之和！");
                }
            }
        }
        if(concludeE.getContractType() == 2){
            ContractIncomeConcludeE mainContract = contractIncomeConcludeMapper.selectById(concludeE.getPid());
            BigDecimal sumAmount = BigDecimal.ZERO;
            if(mainContract.getChangContractAmount().compareTo(BigDecimal.ZERO) == 0){
                sumAmount = sumAmount.add(concludeE.getContractAmountOriginalRate()).add(mainContract.getContractAmountOriginalRate());
            }else{
                sumAmount = sumAmount.add(concludeE.getContractAmountOriginalRate()).add(mainContract.getChangContractAmount());
            }
            BigDecimal ceAmount = sumAmount;
            if(ObjectUtils.isNotEmpty(fkdwxx)){
                List<BigDecimal> sk = fkdwxx.stream().map(sb -> new BigDecimal(sb.getDraweepayamt())).collect(Collectors.toList());
                BigDecimal total = sk.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                if(total.compareTo(ceAmount) != 0){
                    throw new OwlBizException("应付合同金额要等于主合同金额加上补充合同金额之和！");
                }
            }
            if(ObjectUtils.isNotEmpty(skdwxx)){
                List<BigDecimal> sk = skdwxx.stream().map(sb -> new BigDecimal(sb.getPayeeamt())).collect(Collectors.toList());
                BigDecimal total = sk.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                if(total.compareTo(ceAmount) != 0){
                    throw new OwlBizException("应收合同金额要等于主合同金额加上补充合同金额之和！");
                }
            }
            if(isFlag){
                QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(ContractIncomeConcludeE.PID, concludeE.getPid())
                        .eq(ContractIncomeConcludeE.DELETED,0);
                List<ContractIncomeConcludeE> contractPayConcludeEList = contractIncomeConcludeMapper.selectList(queryWrapper);
                if(ObjectUtils.isNotEmpty(contractPayConcludeEList)){
                    contractPayConcludeEList.stream().forEach( s -> {
                        if(s.getContractNature() == null || s.getContractNature() == 0 || ConcludeContractNatureEnum.TO_PUSH.getCode().equals(s.getContractNature())){
                            throw new OwlBizException("主合同有未推送的补充合同，不允许再次新增补充合同！");
                        }
                    });
                }
            }
        }
    }


    /**
     * 下拉选择主合同
     * @param request
     * @return
     */
    public PageV<ContractIncomeConcludeV> getMainContractIncome(PageF<SearchF<ContractIncomeConcludeE>> request) {


        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();

        SearchF<ContractIncomeConcludeE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.eq(ContractIncomeConcludeE.TENANT_ID, tenantId())
                .orderByDesc(ContractIncomeConcludeE.GMT_CREATE);


        queryWrapper.in(ContractIncomeConcludeE.REVIEW_STATUS, 2);

        queryWrapper.eq(ContractIncomeConcludeE.SIGNING_METHOD,0);
        //这里已经限制了推送成功，等价于查询有CT码的合同
        queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_NATURE,1);
        //增加条件：只查询普通合同
        queryWrapper.eq(ContractPayConcludeE.CONTRACT_TYPE,ContractTypeEnum.普通合同.getCode());
        //过滤终止合同
        queryWrapper.ne(ContractIncomeConcludeE.STATUS, ContractRevStatusEnum.合同终止.getCode());
        Page<ContractIncomeConcludeE> page = contractIncomeConcludeMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            LambdaQueryWrapper<ContractIncomeSettlementConcludeE> querySettWrapper = new LambdaQueryWrapper<>();
            querySettWrapper.in(ContractIncomeSettlementConcludeE::getContractId, page.getRecords().stream().map(ContractIncomeConcludeE::getId).collect(Collectors.toList()))
                    .ne(ContractIncomeSettlementConcludeE::getPid,"0")
                    .eq(ContractIncomeSettlementConcludeE::getSettlementType, SettlementTypeEnum.FINAL.getCode())
                    .eq(ContractIncomeSettlementConcludeE::getDeleted,0);
            List<ContractIncomeSettlementConcludeE> concludeSettList = contractIncomeSettlementConcludeMapper.selectList(querySettWrapper);
            List<String> finalSettleContractList = new ArrayList<>();
            finalSettleContractList.add("最终结算");
            concludeSettList.stream().forEach( s -> {
                finalSettleContractList.add(s.getContractId());
            });
            List<ContractIncomeConcludeV> resultList = Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeConcludeV.class);
            resultList.stream().forEach( s -> {
                s.setIsHaveFinalSettlement(finalSettleContractList.contains(s.getId()));
            });
            return PageV.of(request, page.getTotal(), resultList);
        }
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeConcludeV.class));
    }


    @Transactional
    public void endContract(ContractIncomeConcludeF form) {
        if (StringUtils.isBlank(form.getId())) {
            throw new OwlBizException("合同ID不可为空");
        }
        if (Objects.isNull(form.getEndDate())) {
            throw new OwlBizException("终止日期不可为空");
        }
        //-- 校验ID正确性
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(form.getId());
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }

        //-- TODO 终止前数据校验


        //-- 终止合同
        concludeE.setStatus(ContractRevStatusEnum.合同停用.getCode())
                .setEndDate(form.getEndDate());

        contractIncomeConcludeService.updateById(concludeE);

        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.终止.getCode());

        orgRelationService.mutualForIncome(concludeE);

        ContractBasePullV s = noUsedContract(form.getId());
        if(s.getStatus() == 0){
            throw new OwlBizException(s.getMessage());
        }
    }

    /**
     * 处理合同正文数据
     * @param concludeE 合同
     * @param contractFjxxFList 合同正文文件
     */
    public Boolean dealContractTemp(ContractIncomeConcludeE concludeE, List<ContractFjxxF> contractFjxxFList) {


        if (ObjectUtils.isEmpty(contractFjxxFList)) {
            if(!StringUtils.isNotBlank(concludeE.getTempId())){
                concludeE.setTempId("15497043922911");
                contractIncomeConcludeMapper.updateById(concludeE);
            }
            return false;
        }

        LambdaQueryWrapper<ContractRecordInfoE> payQueryWrapper = new LambdaQueryWrapper<>();
        payQueryWrapper.eq(ContractRecordInfoE::getContractId, concludeE.getId()).eq(ContractRecordInfoE::getDeleted,0);
        ContractRecordInfoE contractRecordInfoE = contractRecordInfoMapper.selectOne(payQueryWrapper);
        //contractRecordInfoE不为空且模板id相同，说明合同正文没有更新，直接返回
        if (ObjectUtils.isNotEmpty(contractRecordInfoE)
                && contractRecordInfoE.getTemplateId().equals(contractFjxxFList.get(0).getFileId())) {
            return true;
        }
        //否则，说明更新了合同正文，走下面的逻辑
        if (ObjectUtils.isNotEmpty(contractRecordInfoE)) {
            contractRecordInfoMapper.deleteById(contractRecordInfoE);
        }
        contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),contractFjxxFList.get(0).getFileId(),contractFjxxFList.get(0).getFileKey(),null,contractFjxxFList.get(0).getName(),Integer.parseInt(contractFjxxFList.get(0).getSize()));
        concludeE.setTempId(contractFjxxFList.get(0).getFileId());
        contractIncomeConcludeMapper.updateById(concludeE);
        //下面是原来的方式，若不存在，则新增，若存在则更新
//        if(ObjectUtils.isEmpty(contractRecordInfoE)){
//            contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),contractFjxxFList.get(0).getFileId(),contractFjxxFList.get(0).getFileKey(),null,contractFjxxFList.get(0).getName(),Integer.parseInt(contractFjxxFList.get(0).getSize()));
//            concludeE.setTempId(contractFjxxFList.get(0).getFileId());
//            contractIncomeConcludeMapper.updateById(concludeE);
//        }else{
//            if (ObjectUtils.isNotEmpty(contractFjxxFList)) {
//                contractRecordInfoE.setTemplateId(contractFjxxFList.get(0).getFileId());
//                contractRecordInfoE.setTemplateName(contractFjxxFList.get(0).getFileKey());
//                contractRecordInfoE.setFileName(contractFjxxFList.get(0).getName());
//                contractRecordInfoE.setFileSize(Integer.parseInt(contractFjxxFList.get(0).getSize()));
//                contractRecordInfoService.updateById(contractRecordInfoE);
//                concludeE.setTempId(contractFjxxFList.get(0).getFileId());
//                contractIncomeConcludeMapper.updateById(concludeE);
//            }
//        }

        return true;
    }

    /**
     * 处理按钮展示
     * @param map 入参
     */
    public Boolean dealBtnShowForDetail(ContractIncomeConcludeDetailV map, ContractIncomeConcludeE concludeE) {

        return true;
    }

    /**
     * 反审
     * @param id 合同ID
     */
    public Boolean backReview(String id) {
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("检索合同数据失败");
        }
        return true;
    }

    private void checkBlackList(ContractIncomeConcludeE concludeE) {
        CustomerRv customerRv = orgFeignClient.getCustomerVById(concludeE.getPartyAId());
        String mainDataCode = customerRv.getMainDataCode();
        if (Objects.isNull(mainDataCode)) {
            throw new OwlBizException("该合同对应的主数据编码不能为空");
        }
        BlackListInfoF blackListInfoF = Builder.of(BlackListInfoF::new)
                .with(BlackListInfoF::setComCode, "101476175") // *申请查询的单位4A编码 固定值 单位编码
                .with(BlackListInfoF::setSubjectInfoCode, customerRv.getMainDataCode()) // *往来单位主数据编码(BP码)
                .with(BlackListInfoF::setSocietyCreditCode, "") // 唯一识别码
                .with(BlackListInfoF::setLegalIdNumber, "") // 法人代表人身份证
                .with(BlackListInfoF::setSourceSystem, "GREG-MDM") // *来源系统 固定值
                .with(BlackListInfoF::setStepCode, "2") // *环节编码 目前对接合同只有新签才会用到
                .build();
        BlackUserV blackUserV = externalFeignClient.get(blackListInfoF);
        if (Objects.isNull(blackUserV)) {
            throw new OwlBizException("无法获取黑名单信息,请联系相关人员确认服务是否正常运行");
        }
        // 是黑名单用户抛异常提醒
        if (blackUserV.isBlack()) {
            throw new OwlBizException(blackUserV.getMessage());
        }
    }
    /**
     * 新增保证金时根据客户ID获取合同数据
     * @param form
     * @return
     */
    public List<ContractIncomeConcludeV> getContractForBond(BondRelationF form) {
        return null;
    }

    public SpaceCommunityRv getCommunityByCostCenterId(String id) {
        OrgRevFinanceCostRV costCenter = orgFeignClient.getByFinanceCostId(Long.parseLong(id));

        SpaceCommunityRv result = new SpaceCommunityRv();

        if (Objects.isNull(costCenter)) {
           return result;
        }
        if (StringUtils.isBlank(costCenter.getCommunityId())) {
            return result;
        }
        result = spaceFeignClient.getById(costCenter.getCommunityId());
        if (Objects.isNull(result)) {
            return result;
        }
        return result;
    }

    /**
     * 获取合同页面金额数量展示数据
     * @param request
     * @return
     */
    public ContractNumShow getPageShowNum(PageF<SearchF<ContractIncomeConcludeQuery>> request) {
        QueryWrapper<ContractIncomeConcludeQuery> queryWrapper = new QueryWrapper<>();
        //合同业务线
        Field lineSelectField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLineSelect".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(lineSelectField)){
            if(lineSelectField.getValue().equals(ContractBusinessLineEnum.全部.getCode())){
                request.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
                request.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLineSelect"));
            }else{
                request.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLineSelect"));
                request.getConditions().getFields().stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLine".equals(field.getName())).forEach(field -> field.setValue(lineSelectField.getValue()));
            }
        }
        SearchF<ContractIncomeConcludeQuery> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId())
                .eq("cc." + ContractPayConcludeE.DELETED,0);

        //-- 不是超级管理员时，根据所属部门进行权限隔离
        UserStateRV userStateRV = userFeignClient.getStateByUserId(userId());
        boolean superAccount = Objects.nonNull(userStateRV) && userStateRV.isSuperAccount();//-- 为 TRUE 时当前账号为超级管理员
        UserInfoRv userInfoRv = userFeignClient.getUsreInfoByUserId(userId());
        if(userInfoRv.getOrgIds().contains(13554968509111l)){
            superAccount = true;
        }
        if(!superAccount) {
            Set<String> orgListByOrgId = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            queryWrapper.and(i -> i.in(ContractPayConcludeE.DEPART_ID, orgListByOrgId)
                    .or(m -> m.isNull(ContractPayConcludeE.DEPART_ID)));
        }

        // List<ContractIncomeConcludeE> list = contractIncomeConcludeService.list(queryWrapper);
        List<ContractIncomeConcludeE> list = contractIncomeConcludeMapper.getPageShowNumV2(queryWrapper);

        ContractNumShow result = new ContractNumShow()
                .setContractAmount(BigDecimal.ZERO)
                .setCollectAmount(BigDecimal.ZERO)
                .setUnCollectAmount(BigDecimal.ZERO);

        for (ContractIncomeConcludeE concludeE : list) {
            if (Objects.nonNull(concludeE.getContractAmountOriginalRate()) && Objects.nonNull(concludeE.getCollectAmount())) {
                result.setContractAmount(result.getContractAmount().add(concludeE.getContractAmountOriginalRate()))
                        .setCollectAmount(concludeE.getCollectAmount().add(result.getCollectAmount()))
                        .setUnCollectAmount(result.getUnCollectAmount().add(concludeE.getContractAmountOriginalRate().subtract(concludeE.getCollectAmount())));
            }
        }

        return result;
    }

    public PageV<ContractIncomeConcludeV> pageForSelect(PageF<ContractRevF> request) {
        ContractRevF conditions = request.getConditions();
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();

        List<Integer> status = new ArrayList<>();
        status.add(ContractRevStatusEnum.尚未履行.getCode());
        status.add(ContractRevStatusEnum.正在履行.getCode());
        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeConcludeE.NAME, conditions.getName());
        }

        // 默认排序
        queryWrapper.orderByDesc(ContractIncomeConcludeE.GMT_CREATE)
                .eq(ContractIncomeConcludeE.TENANT_ID, tenantId())
                .in(ContractIncomeConcludeE.STATUS, status);
        Page<ContractIncomeConcludeE> page = contractIncomeConcludeMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeConcludeV.class));
    }


    /**
     * 拓展信息入库
     */
    private void dealContractExpand(String id, ContractIncomeAddF form) {
        ContractIncomeConcludeExpandSaveF data = new ContractIncomeConcludeExpandSaveF();
        BeanUtils.copyProperties(form,data);
        //合同id处理、数组字段jsonstr处理
        data.setContractId(id);

        //履约主题
        if(ObjectUtils.isNotEmpty(form.getSjlydwFs())){
            data.setSjlydw(JSON.toJSONString(form.getSjlydwFs()));
            data.setSjlydwid(form.getSjlydwFs().stream().map( s -> s.getId().toString()).collect(Collectors.joining(",")));
            data.setSjlydwidname(form.getSjlydwFs().stream().map( s -> s.getOrgName()).collect(Collectors.joining(",")));
        }

        //签约单位
        if(ObjectUtils.isNotEmpty(form.getQydws()) && !JSON.toJSONString(form.getQydws()).equals("[{}]")){
            data.setQydws(JSON.toJSONString(form.getQydws()));
        }

        //特征信息
        ContractTzxxF tzxxF = new ContractTzxxF();
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

        //支付方信息
        if(CollectionUtils.isNotEmpty(form.getFkdwxx()) && !JSON.toJSONString(form.getFkdwxx()).equals("[{}]")) {
            data.setFkdwxx(JSON.toJSONString(form.getFkdwxx()));
        }
        //收入方信息
        if(CollectionUtils.isNotEmpty(form.getSkdwxx()) && !JSON.toJSONString(form.getSkdwxx()).equals("[{}]")) {
            data.setSkdwxx(JSON.toJSONString(form.getSkdwxx()));
        }
        //保证金信息
        if(CollectionUtils.isNotEmpty(form.getBzjxx()) && !JSON.toJSONString(form.getBzjxx()).equals("[{}]")) {
            data.setBzjxx(JSON.toJSONString(form.getBzjxx()));
        }
        //审批信息
        if(CollectionUtils.isNotEmpty(form.getSpxx()) && !JSON.toJSONString(form.getSpxx()).equals("[{}]")) {
            data.setSpxx(JSON.toJSONString(form.getSpxx()));
        }
        //附件信息
        if(CollectionUtils.isNotEmpty(form.getFjxx()) && !JSON.toJSONString(form.getFjxx()).equals("[{}]")) {
            data.setFjxx(JSON.toJSONString(form.getFjxx()));
        }
        //PDF附件信息
        if(CollectionUtils.isNotEmpty(form.getPdffjxx()) && !JSON.toJSONString(form.getPdffjxx()).equals("[{}]")) {
            data.setPdffjxx(JSON.toJSONString(form.getPdffjxx()));
        }
        //保险信息
        if(CollectionUtils.isNotEmpty(form.getBxxx()) && !JSON.toJSONString(form.getBxxx()).equals("[{}]")) {
            data.setBxxx(JSON.toJSONString(form.getBxxx()));
        }
        //担保信息
        if(CollectionUtils.isNotEmpty(form.getDbxx()) && !JSON.toJSONString(form.getDbxx()).equals("[{}]")) {
            data.setDbxx(JSON.toJSONString(form.getDbxx()));
        }
        //合同对方信息，全用新字段
        if(CollectionUtils.isNotEmpty(form.getHtdfxx()) && !JSON.toJSONString(form.getHtdfxx()).equals("[{}]")) {
            data.setHtdfxx(JSON.toJSONString(form.getHtdfxx()));
        }
        //市拓投模数据信息
        if(CollectionUtils.isNotEmpty(form.getCttmsj())) {
            data.setCttmsj(JSON.toJSONString(form.getCttmsj()));
        }
        contractIncomeConcludeExpandAppService.save(data);
    }

    /**
     * 拓展信息编辑
     */
    private void dealEditContractExpand(String id, ContractIncomeEditF form) {
        ContractIncomeConcludeExpandUpdateF data = new ContractIncomeConcludeExpandUpdateF();
        BeanUtils.copyProperties(form,data);
        //合同id处理、数组字段jsonstr处理
        data.setContractId(id);

        //履约主题
        if(ObjectUtils.isNotEmpty(form.getSjlydwFs())){
            data.setSjlydw(JSON.toJSONString(form.getSjlydwFs()));
            data.setSjlydwid(form.getSjlydwFs().stream().map( s -> s.getId().toString()).collect(Collectors.joining(",")));
            data.setSjlydwidname(form.getSjlydwFs().stream().map( s -> s.getOrgName()).collect(Collectors.joining(",")));
        }

        //签约单位
        if(ObjectUtils.isNotEmpty(form.getQydws()) && !JSON.toJSONString(form.getQydws()).equals("[{}]")){
            data.setQydws(JSON.toJSONString(form.getQydws()));
        }

        //特征信息
        ContractTzxxF tzxxF = new ContractTzxxF();
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

        //支付方信息
        if(CollectionUtils.isNotEmpty(form.getFkdwxx())) {
            data.setFkdwxx(JSON.toJSONString(form.getFkdwxx()));
        }else {
            data.setFkdwxx("");
        }
        //收入方信息
        if(CollectionUtils.isNotEmpty(form.getSkdwxx())) {
            data.setSkdwxx(JSON.toJSONString(form.getSkdwxx()));
        }else {
            data.setSkdwxx("");
        }
        //保证金信息
        if(CollectionUtils.isNotEmpty(form.getBzjxx())) {
            data.setBzjxx(JSON.toJSONString(form.getBzjxx()));
        }else {
            data.setBzjxx("");
        }
        //审批信息
        if(CollectionUtils.isNotEmpty(form.getSpxx())) {
            data.setSpxx(JSON.toJSONString(form.getSpxx()));
        }else {
            data.setSpxx("");
        }
        //附件信息
        if(CollectionUtils.isNotEmpty(form.getFjxx())) {
            data.setFjxx(JSON.toJSONString(form.getFjxx()));
        }else {
            data.setFjxx("");
        }
        if(CollectionUtils.isNotEmpty(form.getPdffjxx())) {
            data.setPdffjxx(JSON.toJSONString(form.getPdffjxx()));
        }else {
            data.setPdffjxx("");
        }
        //保险信息
        if(CollectionUtils.isNotEmpty(form.getBxxx())) {
            data.setBxxx(JSON.toJSONString(form.getBxxx()));
        }else {
            data.setBxxx("");
        }
        //担保信息
        if(CollectionUtils.isNotEmpty(form.getDbxx())) {
            data.setDbxx(JSON.toJSONString(form.getDbxx()));
        }else {
            data.setDbxx("");
        }
        //合同对方信息，全用新字段
        if(CollectionUtils.isNotEmpty(form.getHtdfxx())) {
            data.setHtdfxx(JSON.toJSONString(form.getHtdfxx()));
        }else {
            data.setHtdfxx("");
        }
        contractIncomeConcludeExpandAppService.updateByContractId(data);
    }

    /**
     * 合同拓展字段回显详情
     */
    private void dealContractExpandToDetailV(ContractIncomeConcludeDetailV detailV, ContractIncomeConcludeExpandV concludeExpandV) {
        if(null != detailV && null != concludeExpandV) {
            //基础字段处理
            BeanUtils.copyProperties(concludeExpandV,detailV);

            //字段翻译处理
            //省市区
            detailV.setConperformcountryname("中国");


            detailV.setConlanguagename("中文");

            //合同管理类别conmanagetype
            if(StringUtils.isNotEmpty(detailV.getConmanagetype())) {
                List<DictionaryCode> value = new ArrayList<>();
                if(ContractBusinessLineEnum.物管.getCode().equals(detailV.getContractBusinessLine())){
                    value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同管理类别.getCode(), detailV.getConmanagetype());
                }else{
                    value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.建管合同管理类别.getCode(), detailV.getConmanagetype());
                }
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setConmanagetypename(value.get(0).getName());
                }
            }

            //增值业务类型conincrementype
            if(StringUtils.isNotEmpty(detailV.getConincrementype())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同增值业务类型.getCode(), detailV.getConincrementype());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setConincrementypename(value.get(0).getName());
                }
            }

            //收支类型incomeexpendtype
            if(StringUtils.isNotEmpty(detailV.getIncomeexpendtype())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收支类型.getCode(), detailV.getIncomeexpendtype());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setIncomeexpendtypename(value.get(0).getName());
                }
            }

            //争议解决方式disputesolutionwayname
            if(StringUtils.isNotEmpty(detailV.getDisputesolutionway())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.争议解决方式.getCode(), detailV.getDisputesolutionway());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setDisputesolutionwayname(value.get(0).getName());
                }
            }


            //付款方式
            if(StringUtils.isNotEmpty(detailV.getPaymentmethod())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同付款方式.getCode(), detailV.getPaymentmethod());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setPaymentmethodname(value.get(0).getName());
                }
            }

            //履约进度
            if(StringUtils.isNotEmpty(detailV.getPerformschedule())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.履约进度.getCode(), detailV.getPerformschedule());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setPerformschedulename(value.get(0).getName());
                }
            }

            //合同状态
            if(null != (detailV.getStatus())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同状态.getCode(), detailV.getStatus().toString());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setStatusname(value.get(0).getName());
                }
            }

            //合同范本
            if(StringUtils.isNotEmpty(detailV.getConmodelusecondition())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同范本使用情况.getCode(), detailV.getConmodelusecondition());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setConmodeluseconditionname(value.get(0).getName());
                }
            }



            //jsonstr 转obj 类型数据处理
            //租赁信息
            if(StringUtils.isNotEmpty(concludeExpandV.getZlxx())){
                try {
                    ContractZlxxF zlxxF = JSONObject.parseObject(concludeExpandV.getZlxx(),ContractZlxxF.class);
                    if(null != zlxxF) {
                        BeanUtils.copyProperties(zlxxF,detailV);
                        if(StringUtils.isNotEmpty(zlxxF.getRentperiod())){
                            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.租赁期限.getCode(), zlxxF.getRentperiod());
                            if (CollectionUtils.isNotEmpty(value)) {
                                detailV.setRentperiodname(value.get(0).getName());
                            }
                        }
                    }
                } catch (BeansException e) {
                }
            }

            //特征信息
            if(StringUtils.isNotEmpty(concludeExpandV.getTzxx())){
                try {
                    ContractTzxxF tzxxF = JSONObject.parseObject(concludeExpandV.getTzxx(),ContractTzxxF.class);
                    if(null != tzxxF) {
                        BeanUtils.copyProperties(tzxxF,detailV);
                    }
                    if(StringUtils.isNotEmpty(tzxxF.getInvoicetype())){
                        List<String> invoicetypelist = Arrays.asList(tzxxF.getInvoicetype().split(","));
                        detailV.setInvoicetype(invoicetypelist);
                        StringJoiner str = new StringJoiner(",");
                        if(CollectionUtils.isNotEmpty(invoicetypelist)) {
                            invoicetypelist.forEach(invoice ->{
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.发票类型.getCode(), invoice);
                                if (CollectionUtils.isNotEmpty(value)) {
                                    str.add(value.get(0).getName());
                                }

                            });
                        }
                        detailV.setInvoicetypename(str.toString());
                    }

                    if(StringUtils.isNotEmpty(tzxxF.getSafekind())){
                        List<String> safekindList = Arrays.asList(tzxxF.getSafekind().split(","));
                        detailV.setSafekind(safekindList);
                        StringJoiner str = new StringJoiner(",");
                        if(CollectionUtils.isNotEmpty(safekindList)) {
                            safekindList.forEach(safe ->{
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.险种.getCode(), safe);
                                if (CollectionUtils.isNotEmpty(value)) {
                                    str.add(value.get(0).getName());
                                }

                            });
                        }
                        detailV.setSafekindname(str.toString());
                    }

                    if(StringUtils.isNotEmpty(tzxxF.getPaymethod())){
                        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.价款方式.getCode(), tzxxF.getPaymethod());
                        if (CollectionUtils.isNotEmpty(value)) {
                            detailV.setPaymethodname(value.get(0).getName());
                        }
                    }

                    if(StringUtils.isNotEmpty(tzxxF.getPurchasemethod())){
                        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.采购方式.getCode(), tzxxF.getPurchasemethod());
                        if (CollectionUtils.isNotEmpty(value)) {
                            detailV.setPurchasemethodname(value.get(0).getName());
                        }
                    }

                    if(StringUtils.isNotEmpty(tzxxF.getFinishverifymethod())){
                        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.最终结算审核方式.getCode(), tzxxF.getFinishverifymethod());
                        if (CollectionUtils.isNotEmpty(value)) {
                            detailV.setFinishverifymethodname(value.get(0).getName());
                        }
                    }

                    if(StringUtils.isNotEmpty(tzxxF.getPaymentmethod())){
                        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同付款方式.getCode(), tzxxF.getPaymentmethod());
                        if (CollectionUtils.isNotEmpty(value)) {
                            detailV.setPaymentmethodname(value.get(0).getName());
                        }
                    }

                    if(StringUtils.isNotEmpty(tzxxF.getRatemethod())){
                        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.汇率确定方式.getCode(), tzxxF.getRatemethod());
                        if (CollectionUtils.isNotEmpty(value)) {
                            detailV.setRatemethodname(value.get(0).getName());
                        }
                    }

                    if(StringUtils.isNotEmpty(tzxxF.getPaymentcurrency())){
                        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支付币种.getCode(), tzxxF.getPaymentcurrency());
                        if (CollectionUtils.isNotEmpty(value)) {
                            detailV.setPaymentcurrencyname(value.get(0).getName());
                        }
                    }
                } catch (BeansException e) {
                }
            }

            //jsonarraystr转 list类型数据处理
            //附件信息
            if(StringUtils.isNotEmpty(concludeExpandV.getFjxx())){
                try {
                    List<ContractFjxxF> fjxx = JSONObject.parseArray(concludeExpandV.getFjxx(),ContractFjxxF.class);
                    if(CollectionUtils.isNotEmpty(fjxx)) {
                        fjxx.forEach(fj ->{
                            if(StringUtils.isNotEmpty(fj.getSuffix())){
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同附件类型.getCode(), fj.getSuffix());
                                if (CollectionUtils.isNotEmpty(value)) {
                                    fj.setSuffixname(value.get(0).getName());
                                }
                            }
                        });
                        detailV.setFjxx(fjxx);
                    }
                } catch (BeansException e) {
                }
            }

            //PDF附件信息
            if(StringUtils.isNotEmpty(concludeExpandV.getPdffjxx())){
                try {
                    List<ContractFjxxF> fjxx = JSONObject.parseArray(concludeExpandV.getPdffjxx(),ContractFjxxF.class);
                    if(CollectionUtils.isNotEmpty(fjxx)) {
                        fjxx.forEach(fj ->{
                            if(StringUtils.isNotEmpty(fj.getSuffix())){
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同附件类型.getCode(), fj.getSuffix());
                                if (CollectionUtils.isNotEmpty(value)) {
                                    fj.setSuffixname(value.get(0).getName());
                                }
                            }
                        });
                        detailV.setPdffjxx(fjxx);
                    }
                } catch (BeansException e) {
                }
            }

            //履约主体
            if(StringUtils.isNotEmpty(concludeExpandV.getSjlydw())){
                detailV.setSjlydwFs(JSONObject.parseArray(concludeExpandV.getSjlydw(),OrgInfoRv.class));
            }

            // 签约单位信息
            if(StringUtils.isNotEmpty(concludeExpandV.getQydws())){
                List<ContractQydwsF> qydwsFS = JSONObject.parseArray(concludeExpandV.getQydws(),ContractQydwsF.class);
                if(CollectionUtils.isNotEmpty(qydwsFS)) {
                    List<ContractQydwsExtendF> oppositeTwoIdName = new ArrayList<>();
                    qydwsFS.forEach(srf ->{
                        ContractQydwsExtendF contractQydwsExtendF = new ContractQydwsExtendF();
                        contractQydwsExtendF.setId(srf.getOppositeOneId());
                        contractQydwsExtendF.setName(srf.getOppositeOne());
                        contractQydwsExtendF.setIsInner(1);
                        oppositeTwoIdName.add(contractQydwsExtendF);
                        srf.setOppositeTwoIdName(oppositeTwoIdName);
                    });
                }
                detailV.setQydws(qydwsFS);
            }

            //担保信息
            if(StringUtils.isNotEmpty(concludeExpandV.getDbxx())){

                StringJoiner dblbstr = new StringJoiner(",");
                StringJoiner dbxsstr = new StringJoiner(",");
                StringJoiner dbblstr = new StringJoiner(",");
                StringJoiner dbjestr = new StringJoiner(",");
                StringJoiner thtjstr = new StringJoiner(",");
                try {
                    List<ContractDbxxF> dbxx = JSONObject.parseArray(concludeExpandV.getDbxx(),ContractDbxxF.class);
                    if(CollectionUtils.isNotEmpty(dbxx)) {
                        dbxx.forEach(db ->{
                            if(StringUtils.isNotEmpty(db.getGuarantyclassify())){
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.担保类别.getCode(), db.getGuarantyclassify());
                                if (CollectionUtils.isNotEmpty(value)) {
                                    db.setGuarantyclassifyname(value.get(0).getName());
                                    dblbstr.add(value.get(0).getName());
                                }
                            }

                            if(StringUtils.isNotEmpty(db.getGuarantyform())){
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.履约担保形式质保金.getCode(), db.getGuarantyform());
                                if (CollectionUtils.isNotEmpty(value)) {
                                    db.setGuarantyformname(value.get(0).getName());
                                    dbxsstr.add(value.get(0).getName());
                                }
                            }

                            if (StringUtils.isNotEmpty(db.getGuarantyproport())) {
                                dbblstr.add(db.getGuarantyproport());
                            }

                            if (StringUtils.isNotEmpty(db.getGuarantyamt())) {
                                dbjestr.add(db.getGuarantyamt());
                            }

                            if (StringUtils.isNotEmpty(db.getBackrule())) {
                                thtjstr.add(db.getBackrule());
                            }

                        });
                        detailV.setDbxx(dbxx);
                        detailV.setDblbstr(dblbstr.toString());
                        detailV.setDbxsstr(dbxsstr.toString());
                        detailV.setDbblstr(dbblstr.toString());
                        detailV.setDbjestr(dbjestr.toString());
                        detailV.setThtjstr(thtjstr.toString());
                    }
                } catch (BeansException e) {
                }
            }

            //保险信息
            if(StringUtils.isNotEmpty(concludeExpandV.getBxxx())){

                StringJoiner xzmcstr = new StringJoiner(",");
                StringJoiner bxjestr = new StringJoiner(",");
                StringJoiner mpestr = new StringJoiner(",");

                try {
                    List<ContractBxxxF> bxxx = JSONObject.parseArray(concludeExpandV.getBxxx(),ContractBxxxF.class);
                    if(CollectionUtils.isNotEmpty(bxxx)) {
                        bxxx.forEach(bx ->{
                            if (StringUtils.isNotEmpty(bx.getInsurancename())) {
                                xzmcstr.add(bx.getInsurancename());
                            }

                            if (StringUtils.isNotEmpty(bx.getInsuranceamt())) {
                                bxjestr.add(bx.getInsuranceamt());
                            }

                            if (StringUtils.isNotEmpty(bx.getDeductible())) {
                                mpestr.add(bx.getDeductible());
                            }
                        });
                        detailV.setBxxx(bxxx);

                        detailV.setXzmcstr(xzmcstr.toString());
                        detailV.setBxjestr(bxjestr.toString());
                        detailV.setMpestr(mpestr.toString());
                    }
                } catch (BeansException e) {
                }
            }
            //审批信息
            if(StringUtils.isNotEmpty(concludeExpandV.getSpxx())){
                try {
                    List<ContractSpxxF> spxx = JSONObject.parseArray(concludeExpandV.getSpxx(),ContractSpxxF.class);
                    if(CollectionUtils.isNotEmpty(spxx)) {
                        detailV.setSpxx(spxx);
                    }
                } catch (BeansException e) {
                }
            }
            //保证金信息

            if(StringUtils.isNotEmpty(concludeExpandV.getBzjxx())){
                try {
                    List<ContractBzjxxF> bzjxx = JSONObject.parseArray(concludeExpandV.getBzjxx(),ContractBzjxxF.class);
                    if(CollectionUtils.isNotEmpty(bzjxx)) {

                        StringJoiner bzjlxstr = new StringJoiner(",");
                        StringJoiner bzjblstr = new StringJoiner(",");
                        StringJoiner bzjjestr = new StringJoiner(",");
                        StringJoiner bzjthtjstr = new StringJoiner(",");

                        bzjxx.forEach(bzj ->{
                            if(StringUtils.isNotEmpty(bzj.getGuarantyamt())){
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同保证金类型.getCode(), bzj.getGuarantytype());
                                if (CollectionUtils.isNotEmpty(value)) {
                                    bzj.setGuarantytypename(value.get(0).getName());
                                    bzjlxstr.add(value.get(0).getName());

                                }
                            }

                            if (StringUtils.isNotEmpty(bzj.getGuarantyproport())) {
                                bzjblstr.add(bzj.getGuarantyproport());
                            }

                            if (StringUtils.isNotEmpty(bzj.getGuarantyamt())) {
                                bzjjestr.add(bzj.getGuarantyamt());
                            }

                            if (StringUtils.isNotEmpty(bzj.getGuarantyxjzfbhthtj())) {
                                bzjthtjstr.add(bzj.getGuarantyxjzfbhthtj());
                            }

                        });
                        detailV.setBzjxx(bzjxx);
                        detailV.setBzjlxstr(bzjlxstr.toString());
                        detailV.setBzjblstr(bzjblstr.toString());
                        detailV.setBzjjestr(bzjjestr.toString());
                        detailV.setBzjthtjstr(bzjthtjstr.toString());

                    }
                } catch (BeansException e) {
                }
            }
            //合同支付信息:收入方信息
            if(StringUtils.isNotEmpty(concludeExpandV.getSkdwxx())){
                StringJoiner srfstr = new StringJoiner(",");
                StringJoiner zhlxstr = new StringJoiner(",");
                StringJoiner zhmcstr = new StringJoiner(",");
                StringJoiner zhstr = new StringJoiner(",");
                StringJoiner khhstr = new StringJoiner(",");
                StringJoiner zhszgj = new StringJoiner(",");
                StringJoiner swiftcodestr = new StringJoiner(",");
                StringJoiner yshtjestr = new StringJoiner(",");
                StringJoiner sjskrstr = new StringJoiner(",");
                try {
                    List<ContractSrfxxF> srfxx = JSONObject.parseArray(concludeExpandV.getSkdwxx(),ContractSrfxxF.class);
                    List<ContractSrfxxPayeeExtendF> payeeName = new ArrayList<>();
                    List<ContractSrfxxTruePayeeExtendF> truepayeeName = new ArrayList<>();
                    List<ContractSrfxxPayAccountExtendF> payeeaccounnameInfo = new ArrayList<>();
                    List<ContractSrfxxTruePayAccountExtendF> truepayeeaccounnameInfo = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(srfxx)) {
                        srfxx.forEach(srf ->{

                            ContractSrfxxPayeeExtendF contractSrfxxPayeeExtendF = new ContractSrfxxPayeeExtendF();
                            ContractSrfxxTruePayeeExtendF contractSrfxxTruePayeeExtendF = new ContractSrfxxTruePayeeExtendF();
                            ContractSrfxxPayAccountExtendF contractSrfxxPayAccountExtendF = new ContractSrfxxPayAccountExtendF();
                            ContractSrfxxTruePayAccountExtendF contractSrfxxTruePayAccountExtendF = new ContractSrfxxTruePayAccountExtendF();

                            if(StringUtils.isNotEmpty(srf.getPayeeaccountype())){
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收款人账户类型.getCode(), srf.getPayeeaccountype());
                                if (CollectionUtils.isNotEmpty(value)) {
                                    srf.setPayeeaccountypename(value.get(0).getName());
                                    zhlxstr.add(value.get(0).getName());
                                }
                            }

                            // 收款人信息回显
                            if (StringUtils.isNotEmpty(srf.getPayee())) {
                                srfstr.add(srf.getPayee());
                                contractSrfxxPayeeExtendF.setName(srf.getPayee());
                                contractSrfxxPayeeExtendF.setId(srf.getPayeeid());
                                payeeName.add(contractSrfxxPayeeExtendF);
                                srf.setPayeeName(payeeName);
                            }
                            if (StringUtils.isNotEmpty(srf.getPayeeaccounname())) {
                                zhmcstr.add(srf.getPayeeaccounname());
                                contractSrfxxPayAccountExtendF.setName(srf.getPayeeaccounname());
                            }
                            if(StringUtils.isNotEmpty(srf.getPayeeaccountid())){
                                contractSrfxxPayAccountExtendF.setId(srf.getPayeeaccountid());
                                payeeaccounnameInfo.add(contractSrfxxPayAccountExtendF);
                                srf.setPayeeaccounnameInfo(payeeaccounnameInfo);
                            }
                            if (StringUtils.isNotEmpty(srf.getPayeeaccounnumber())) {
                                zhstr.add(srf.getPayeeaccounnumber());
                            }
                            if (StringUtils.isNotEmpty(srf.getPayeeaccounbank())) {
                                khhstr.add(srf.getPayeeaccounbank());
                            }
                            if (StringUtils.isNotEmpty(srf.getSwiftcode())) {
                                swiftcodestr.add(srf.getSwiftcode());
                            }
                            if (StringUtils.isNotEmpty(srf.getPayeeamt())) {
                                yshtjestr.add(srf.getPayeeamt());
                            }

                            zhszgj.add("中国");

                            // 实际收款人信息回显
                            if (StringUtils.isNotEmpty(srf.getTruepayee())) {
                                sjskrstr.add(srf.getTruepayee());
                                contractSrfxxTruePayeeExtendF.setId(srf.getTruepayeeid());
                                contractSrfxxTruePayeeExtendF.setName(srf.getTruepayee());
                                truepayeeName.add(contractSrfxxTruePayeeExtendF);
                                srf.setTruepayeeName(truepayeeName);
                            }


                            // 实际收款人账号信息回显
                            if (StringUtils.isNotEmpty(srf.getTruepayeeaccountid())) {
                                contractSrfxxTruePayAccountExtendF.setId(srf.getTruepayeeaccountid());
                                contractSrfxxTruePayAccountExtendF.setName(srf.getTruepayeeaccounname());
                                truepayeeaccounnameInfo.add(contractSrfxxTruePayAccountExtendF);
                                srf.setTruepayeeaccounnameInfo(truepayeeaccounnameInfo);
                            }

                        });
                        detailV.setSkdwxx(srfxx);
                        detailV.setSrfstr(srfstr.toString());
                        detailV.setZhlxstr(zhlxstr.toString());
                        detailV.setZhmcstr(zhmcstr.toString());
                        detailV.setZhstr(zhstr.toString());
                        detailV.setKhhstr(khhstr.toString());
                        detailV.setZhszgj(zhszgj.toString());
                        detailV.setSwiftcodestr(swiftcodestr.toString());
                        detailV.setYshtjestr(yshtjestr.toString());
                        detailV.setSjskrstr(sjskrstr.toString());
                    }
                } catch (BeansException e) {
                }
            }

            //合同支付信息:支付方信息
            if(StringUtils.isNotEmpty(concludeExpandV.getFkdwxx())){
                try {
                    List<ContractZffxxF> zffxx = JSONObject.parseArray(concludeExpandV.getFkdwxx(),ContractZffxxF.class);
                    List<ContractZffxxDraweeExtendF> draweeName = new ArrayList<>();
                    List<ContractZffxxTrueDraweeExtendF> truedraweeName = new ArrayList<>();


                    if(CollectionUtils.isNotEmpty(zffxx)) {
                        StringJoiner zcfstr = new StringJoiner(",");
                        StringJoiner yfhtjestr = new StringJoiner(",");
                        StringJoiner sjfkrstr = new StringJoiner(",");
                        zffxx.forEach(zff ->{

                            //支付方信息
                            ContractZffxxDraweeExtendF contractZffxxDraweeExtendF = new ContractZffxxDraweeExtendF();
                            ContractZffxxTrueDraweeExtendF contractZffxxTrueDraweeExtendF = new ContractZffxxTrueDraweeExtendF();
                            if (StringUtils.isNotEmpty(zff.getDrawee())) {
                                zcfstr.add(zff.getDrawee());
                                contractZffxxDraweeExtendF.setId(zff.getDraweeid());
                                contractZffxxDraweeExtendF.setName(zff.getDrawee());
                                draweeName.add(contractZffxxDraweeExtendF);
                                zff.setDraweeName(draweeName);
                            }
                            if (StringUtils.isNotEmpty(zff.getDraweepayamt())) {
                                yfhtjestr.add(zff.getDraweepayamt());
                            }

                            if (StringUtils.isNotEmpty(zff.getTruedrawee())) {
                                sjfkrstr.add(zff.getTruedrawee());
                                contractZffxxTrueDraweeExtendF.setName(zff.getTruedrawee());
                                contractZffxxTrueDraweeExtendF.setId(zff.getTruedraweeid());
                                truedraweeName.add(contractZffxxTrueDraweeExtendF);
                                zff.setTruedraweeName(truedraweeName);
                            }
                        });
                        detailV.setFkdwxx(zffxx);
                        detailV.setZcfstr(zcfstr.toString());
                        detailV.setYfhtjestr(yfhtjestr.toString());
                        detailV.setSjfkrstr(sjfkrstr.toString());
                    }
                } catch (BeansException e) {
                }
            }

            //合同市拓信息
            if(StringUtils.isNotEmpty(concludeExpandV.getCttmsj())){
                try {
                    List<ContractCttmsjF> cttmsj = JSONObject.parseArray(concludeExpandV.getCttmsj(),ContractCttmsjF.class);
                    if(CollectionUtils.isNotEmpty(cttmsj)) {
                        StringJoiner businesstype = new StringJoiner(",");
                        StringJoiner mearsurewaye = new StringJoiner(",");
                        StringJoiner properyfee = new StringJoiner(",");
                        StringJoiner chargearea = new StringJoiner(",");
                        StringJoiner caramount = new StringJoiner(",");
                        StringJoiner carprice = new StringJoiner(",");
                        StringJoiner yearlimit = new StringJoiner(",");
                        StringJoiner baseincome = new StringJoiner(",");
                        StringJoiner baseincomerate = new StringJoiner(",");
                        cttmsj.forEach(zff ->{
                            if (StringUtils.isNotEmpty(zff.getBusinessType())) {
                                businesstype.add(zff.getBusinessType());
                            }
                            if (StringUtils.isNotEmpty(zff.getMearsureWaye())) {
                                mearsurewaye.add(zff.getMearsureWaye());
                            }
                            if (StringUtils.isNotEmpty(zff.getProperyFee())) {
                                properyfee.add(zff.getProperyFee());
                            }
                            if (StringUtils.isNotEmpty(zff.getChargeArea())) {
                                chargearea.add(zff.getChargeArea());
                            }
                            if (StringUtils.isNotEmpty(zff.getCarAmount())) {
                                caramount.add(zff.getCarAmount());
                            }

                            if (StringUtils.isNotEmpty(zff.getCarPrice())) {
                                carprice.add(zff.getCarPrice());
                            }
                            if (StringUtils.isNotEmpty(zff.getYearLimit())) {
                                yearlimit.add(zff.getYearLimit());
                            }
                            if (StringUtils.isNotEmpty(zff.getBaseIncome())) {
                                baseincome.add(zff.getBaseIncome());
                            }
                            if (StringUtils.isNotEmpty(zff.getBaseIncomeRate())) {
                                baseincomerate.add(zff.getBaseIncomeRate());
                            }
                        });
                        detailV.setCttmsj(cttmsj);
                        detailV.setIsBusInfo(true);
                        String tmUrl = externalFeignClient.ceLogin();
                        ContractCttmsjF contractCttmsjF = cttmsj.get(0);
                        detailV.setTmCalculationUrl(
                                StrUtil.isBlank(tmUrl) ? "" :
                                        (tmUrl + "&projectCode=" + contractCttmsjF.getProjectCode() +
                                                "&version=" + contractCttmsjF.getVersionNo())
                        );
                    }
                } catch (BeansException e) {
                }
            }

            //合同对方信息
            if(StringUtils.isNotEmpty(concludeExpandV.getHtdfxx())){
                try {
                    List<ContractDfxxF> dfxx = JSONObject.parseArray(concludeExpandV.getHtdfxx(),ContractDfxxF.class);
                    if(CollectionUtils.isNotEmpty(dfxx)) {
                        detailV.setHtdfxx(dfxx);
                    }
                } catch (BeansException e) {
                }
            }

        }
    }

    public ContractIncomeConcludeDetailFjxxV getFjxxDetailV(PageF<SearchF<ContractIncomeFjxxF>> contractIncomeFjxxF) {
        ContractIncomeConcludeDetailFjxxV result = new ContractIncomeConcludeDetailFjxxV();
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(contractIncomeFjxxF.getConditions().getFields().get(0).getValue()+"");
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        if (null != concludeExpandV && StringUtils.isNotEmpty(concludeExpandV.getFjxx())) {
            try {
                List<ContractFjxxF> fjxx = JSONObject.parseArray(concludeExpandV.getFjxx(), ContractFjxxF.class);
                if (CollectionUtils.isNotEmpty(fjxx)) {
                    fjxx.forEach(fj -> {
                        if (StringUtils.isNotEmpty(fj.getSuffix())) {
                            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同附件类型.getCode(), fj.getSuffix());
                            if (CollectionUtils.isNotEmpty(value)) {
                                fj.setSuffixname(value.get(0).getName());
                            }
                        }
                    });
                    result.setRecords(fjxx);
                    result.setPageNum(1);
                    result.setPageSize(20);
                    result.setTotal(fjxx.size());
                }
            } catch (BeansException e) {
            }
        }
        return result;
    }

    public String getZjContractPullBody(String id) {
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        ContractIncomeConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractIncomeConcludeDetailV.class);
        //直接从源头将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(detailV.getRemark())) {
            detailV.setRemark(detailV.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV, concludeExpandV);
        return contractIncomePullService.getJsonStr(detailV);

    }

    public void autoPullContract(){
        List<String> toPullContractIdList = contractIncomeConcludeMapper.queryToPullZjContractIds();
        if (CollectionUtils.isEmpty(toPullContractIdList)){
            return;
        }
        for (String id : toPullContractIdList) {
            try {
                pullContract(id);
            } catch (Exception e){
                log.error("contract:{} pullContract error:{}",id,e.getMessage());
                contractIncomeConcludeMapper.incrementPullZjFailCountById(id);
            }
        }
    }

    @Transactional
    public String pullContract(String id){
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        if (Objects.nonNull(concludeE) && Objects.nonNull(concludeE.getContractNature()) && concludeE.getContractNature() == 1) {
            throw new OwlBizException("已推送成功，请点击查询按钮查看合同CT码");
        }

        //修改合同归档状态，只有非修改合同才处理归档状态
       /* if (!ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType())) {
            ContractIncomeConcludeE archivedConcludeE = new ContractIncomeConcludeE();
            archivedConcludeE.setId(id);
            archivedConcludeE.setArchived(1);
            contractIncomeConcludeService.updateById(archivedConcludeE);
        }*/
        LambdaQueryWrapper<AttachmentE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttachmentE::getBusinessId, id).eq(AttachmentE::getBusinessType,1002)
                .eq(AttachmentE::getDeleted, 0);
        List<AttachmentE> attachmentEList = attachmentMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(attachmentEList)){
            throw new OwlBizException("请上传合同扫描件");
        }

        //补充协议金额，同步至主合同逻辑上线时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedTime = LocalDateTime.parse("2025-09-04 21:00:00", formatter);

        if(Objects.nonNull(concludeE.getApprovalDate()) && concludeE.getApprovalDate().isAfter(parsedTime)
                && concludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())
                && (  (Objects.nonNull(concludeE.getContractNature())
                && concludeE.getContractNature() == 2)
                || Objects.isNull(concludeE.getContractNature()))
        ){
            contractIncomeConcludeService.handleConcludeSupple(concludeE.getContractAmountOriginalRate(),concludeE.getPid(),concludeE.getId());
            concludeE = contractIncomeConcludeService.getById(id);
        }
        //保存合同推送时间
        ContractIncomeConcludeE pushDateE = new ContractIncomeConcludeE();
        pushDateE.setId(id);
        //归档状态，推送直接更改为已归档
        pushDateE.setArchived(1);
        pushDateE.setContractPushDate(LocalDateTime.now());
        contractIncomeConcludeService.updateById(pushDateE);

        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.推送.getCode());
        ContractIncomeConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractIncomeConcludeDetailV.class);
        //直接从源头将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(detailV.getRemark())) {
            detailV.setRemark(detailV.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV, concludeExpandV);
        Integer type = 0;
        if(StringUtils.isNotBlank(detailV.getConmaincode())){
            type = 1;
        }
        //测试环境推送接口直接返回"成功"
        if (devFlag == 1) {
            try {
                Thread.sleep(2500);
            } catch (Exception e) {
            }
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            concludeE.setFromid("test#MDM#"+LocalDateTime.now().format(formatter1));
            concludeE.setConmaincode("CT"+LocalDateTime.now().format(formatter1));
            concludeE.setContractNature(1);
            concludeE.setPartyAId("成功");
            contractIncomeConcludeMapper.updateById(concludeE);
            if(concludeE.getContractNature() == 1 && concludeE.getContractType() == 4){
                contractIncomePullService.modifyContract(concludeE);
            }
            return "成功";
        }
        //推送调用，标志字段设置为ture
        detailV.setIsPush(true);
        String requestBody = contractIncomePullService.getJsonStr(detailV);
        return contractIncomePullService.dealContractPull(requestBody, type, id);

    }

    public ContractBasePullV verifyContract(String id){
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        ContractIncomeConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractIncomeConcludeDetailV.class);
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV, concludeExpandV);
        Integer type = 0;
        if(StringUtils.isNotBlank(detailV.getConmaincode())){
            type = 1;
        }
        String requestBody = contractIncomePullService.getJsonStr(detailV);
        return contractIncomePullService.verifyContract(requestBody, type, id);

    }

    public ContractBasePullV noUsedContract(String id){
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        ContractIncomeConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractIncomeConcludeDetailV.class);
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV, concludeExpandV);
        String requestBody = contractIncomePullService.getJsonStrNoUsed(detailV);
        return contractIncomePullService.noUsedContract(requestBody);
    }

    public List<ContractIncomeConcludeDetailV> getInfoListByIds(List<String> ids) {
        return contractIncomeConcludeService.getInfoListByIds(ids);
    }

    public ProcessQueryV queryStatus(String id) {
        ProcessQueryF processQueryF = new ProcessQueryF();
        return externalFeignClient.queryStatus(processQueryF);
    }

    public OpinionApprovalV opinionApproval(String id) {
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
                return new OpinionApprovalV();
            }
        }
        opinionApprovalDataF.setFormdataid(id);
        opinionApprovalDataF.setRequestId(recordE.getProcessId());
        opinionApprovalF.setIT_DATA(opinionApprovalDataF);
        return externalFeignClient.opinionApproval(opinionApprovalF);
    }

    public void updateContractInUse(LocalDate targetDate) {
        contractIncomeConcludeMapper.updateContractInUse(targetDate);
    }

    //修正收入合同
    @Transactional
    public String incomeCorrection(ContractIncomeEditF form) {
        //-- 校验合同ID是否正确
        ContractIncomeConcludeE income = contractIncomeConcludeService.getById(form.getId());
        if (Objects.isNull(income)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        if(CollectionUtils.isNotEmpty(form.getContractIncomeFundVList()) && income.getContractType() == 0){
            List<ContractIncomeFundUpdateF> contractPayFundVList = form.getContractIncomeFundVList().stream().filter(record -> !"delete".equals(record.getActionCode())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(contractPayFundVList)){
                BigDecimal totalAmount = contractPayFundVList.stream()
                        .map(ContractIncomeFundUpdateF::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if(income.getChangContractAmount().compareTo(BigDecimal.ZERO) != 0){
                    if(totalAmount.compareTo(income.getChangContractAmount()) != 0){
                        throw new OwlBizException("当前清单金额["+totalAmount+"]与合同变更金额["+income.getChangContractAmount()+"]不一致，请重新填写");
                    }
                }else{
                    if(totalAmount.compareTo(income.getContractAmountOriginalRate()) != 0){
                        throw new OwlBizException("当前清单金额["+totalAmount+"]与合同金额["+income.getContractAmountOriginalRate()+"]不一致，请重新填写");
                    }
                }
            }
        }
        //该合同现有计划
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
        queryPlanWrapper.eq(ContractIncomePlanConcludeE::getContractId, income.getId())
                .ne(ContractIncomePlanConcludeE::getPid,"0")
                .eq(ContractIncomePlanConcludeE::getDeleted,0);
        List<ContractIncomePlanConcludeE> concludePlanList = contractIncomePlanConcludeMapper.selectList(queryPlanWrapper);

        if(!income.getGmtExpireStart().equals(form.getGmtExpireStart())){
            if(CollectionUtils.isNotEmpty(concludePlanList)){
                throw new OwlBizException("修改失败，合同已拆分收款计划不支持修改合同开始日期");
            }
        }

        if(CollectionUtils.isNotEmpty(concludePlanList) && CollectionUtils.isNotEmpty(form.getContractIncomeFundVList())){
            //检验清单调减时，金额是否大于未结算金额
            /*Map<String, BigDecimal> resultMap = concludePlanList.stream()
                    .map(e -> {
                        BigDecimal planned = e.getPlannedCollectionAmount() != null ? e.getPlannedCollectionAmount() : BigDecimal.ZERO;
                        BigDecimal settlement = e.getSettlementAmount() != null ? e.getSettlementAmount() : BigDecimal.ZERO;
                        BigDecimal difference = planned.subtract(settlement);
                        return new AbstractMap.SimpleEntry<>(String.valueOf(e.getContractPayFundId()), difference);
                    })
                    .collect(Collectors.groupingBy(
                            Map.Entry::getKey,
                            Collectors.mapping(
                                    Map.Entry::getValue,
                                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                            )
                    ));

            form.getContractIncomeFundVList().forEach(x-> {
                if(resultMap.containsKey(x.getId())){
                    if(x.getAmount().compareTo(resultMap.get(x.getId())) < 0){

                    }
                }
            });*/

            if(CollectionUtils.isNotEmpty(form.getContractIncomeFundVList().stream().filter(x->"add".equals(x.getActionCode())).collect(Collectors.toList()))){
                //该合同现有计划
                LambdaQueryWrapper<ContractIncomeSettlementConcludeE> querySettWrapper = new LambdaQueryWrapper<>();
                querySettWrapper.eq(ContractIncomeSettlementConcludeE::getContractId, income.getId())
                        .ne(ContractIncomeSettlementConcludeE::getPid,"0")
                        .notIn(ContractIncomeSettlementConcludeE::getReviewStatus, ReviewStatusEnum.已通过.getCode(),ReviewStatusEnum.审批中.getCode())
                        .eq(ContractIncomeSettlementConcludeE::getDeleted,0);
                List<ContractIncomeSettlementConcludeE> concludeSettList = contractIncomeSettlementConcludeMapper.selectList(querySettWrapper);
                if(CollectionUtils.isNotEmpty(concludeSettList)){
                    List<IncomePlanPeriodV> periodList = new ArrayList<>();
                    concludeSettList.forEach(x-> periodList.addAll(contractIncomeConcludeSettlementPeriodMapper.getPeriodList(x.getId())));
                    Optional<Date> maxEndDate = periodList.stream()
                            .map(IncomePlanPeriodV::getEndDate)
                            .filter(Objects::nonNull)
                            .max(Date::compareTo);
                    // 格式化 Date 为 yyyy-MM-dd 字符串
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String endDateStr = sdf.format(maxEndDate.get());

                    // 格式化 LocalDate 为 yyyy-MM-dd 字符串
                    String expireEndStr = form.getGmtExpireEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if(endDateStr.equals(expireEndStr)){
                        throw new OwlBizException("当前确收计量周期已用尽且合同周期未增长则不可修改，请重新填写");
                    }
                }
            }

        }

        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        String correctionDesc = "";
        List<String> updateDescList = new ArrayList<>();
        compareField("合同开始日期", income.getGmtExpireStart(), form.getGmtExpireStart(), updateDescList);
        compareField("合同到期日期", income.getGmtExpireEnd(), form.getGmtExpireEnd(), updateDescList);
        if(CollectionUtils.isNotEmpty(updateDescList)){
            correctionDesc = "合同信息修改：" + updateDescList.stream().collect(Collectors.joining(","));
        }
        QueryWrapper<ContractIncomeConcludeCorrectionE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractIncomeConcludeCorrectionE.CONTRACTID, form.getId())
                .in(ContractIncomeConcludeCorrectionE.CORRECTION_STATUS,Arrays.asList(CorrectionStatusEnum.草稿.getCode(), CorrectionStatusEnum.已驳回.getCode()))
                .eq(ContractIncomeConcludeCorrectionE.DELETED,0);
        ContractIncomeConcludeCorrectionE contractIncomeConclude = contractIncomeConcludeCorrectionMapper.selectOne(queryWrapper);
        if(Objects.nonNull(contractIncomeConclude)){
            contractIncomeConclude.setOldGmtExpireStart(income.getGmtExpireStart());
            contractIncomeConclude.setOldGmtExpireEnd(income.getGmtExpireEnd());
            contractIncomeConclude.setNewGmtExpireStart(form.getGmtExpireStart());
            contractIncomeConclude.setNewGmtExpireEnd(form.getGmtExpireEnd());
            contractIncomeConclude.setCorrectionReason(form.getCorrectionReason());
            contractIncomeConclude.setCorrectionFile(JSON.toJSONString(form.getCorrectionFile()));
            contractIncomeConcludeCorrectionMapper.updateById(contractIncomeConclude);
            //删除原修正清单数据
            contractIncomeFundMapper.deleteByContractId(contractIncomeConclude.getId().toString());
        }else{
            contractIncomeConclude = new ContractIncomeConcludeCorrectionE();
            contractIncomeConclude.setContractId(form.getId());
            contractIncomeConclude.setCorrectionStatus(CorrectionStatusEnum.草稿.getCode());
            contractIncomeConclude.setOldGmtExpireStart(income.getGmtExpireStart());
            contractIncomeConclude.setOldGmtExpireEnd(income.getGmtExpireEnd());
            contractIncomeConclude.setNewGmtExpireStart(form.getGmtExpireStart());
            contractIncomeConclude.setNewGmtExpireEnd(form.getGmtExpireEnd());
            contractIncomeConclude.setCorrectionReason(form.getCorrectionReason());
            contractIncomeConclude.setCorrectionFile(JSON.toJSONString(form.getCorrectionFile()));
            contractIncomeConclude.setCreator(identityInfo.getUserId());
            contractIncomeConclude.setCreatorName(identityInfo.getUserName());
            contractIncomeConclude.setGmtCreate(now);
            contractIncomeConclude.setOperator(identityInfo.getUserId());
            contractIncomeConclude.setOperatorName(identityInfo.getUserName());
            contractIncomeConclude.setGmtModify(now);
            contractIncomeConclude.setDeleted(0);
            contractIncomeConcludeCorrectionMapper.insert(contractIncomeConclude);
        }

        List<ContractIncomeFundE> oldFunList = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.CONTRACT_ID, income.getId())
                .eq(ContractIncomeFundE.DELETED,0)
        );
        List<ContractIncomeFundUpdateF> newFunList = Optional.ofNullable(form.getContractIncomeFundVList())
                .orElse(Collections.emptyList());
        List<String> oldFunIdList = oldFunList.stream().map(ContractIncomeFundE::getId).collect(Collectors.toList());



        List<ContractIncomeFundUpdateF> newAddFunList = newFunList.stream().filter(record -> "add".equals(record.getActionCode())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(newAddFunList)){
            newAddFunList = new ArrayList<>();
            newAddFunList.addAll(newFunList.stream().filter(x->"edit".equals(x.getActionCode()) && !oldFunIdList.contains(x.getId())).collect(Collectors.toList()));
        }
        List<String> updateFunIdList  = newFunList.stream().filter(record -> "edit".equals(record.getActionCode()) && oldFunIdList.contains(record.getId())).map(ContractIncomeFundUpdateF::getId).collect(Collectors.toList());

        List<String> deleteFunIdList  = oldFunIdList.stream().filter(x->!updateFunIdList.contains(x)).collect(Collectors.toList());
        int iNum = 1;
        List<String> funUpdateDescList = new ArrayList<>();
        for(ContractIncomeFundE fun : oldFunList){
            ContractIncomeFundE oldFun = new ContractIncomeFundE();
            BeanUtils.copyProperties(fun, oldFun);
            oldFun.setCorrectionTag(0);
            oldFun.setId(null);
            oldFun.setContractId(contractIncomeConclude.getId().toString());
            oldFun.setMainId(fun.getId());
            contractIncomeFundService.save(oldFun);
            //修改数据
            if(CollectionUtils.isNotEmpty(updateFunIdList) && updateFunIdList.contains(fun.getId())){
                ContractIncomeFundUpdateF updateFun =  getUpdateFunSafely(newFunList, fun);;
                updateFun.setMainId(fun.getId());
                updateFun.setContractId(contractIncomeConclude.getId().toString());
                updateFun.setCorrectionTag(1);
                ContractIncomeFundE newUpdateFun = contractIncomeFundService.save(Global.mapperFacade.map(updateFun, ContractIncomeFundSaveF.class));
                List<String> desc = this.getCompareFileList(oldFun, newUpdateFun);
                if(CollectionUtils.isNotEmpty(desc)){
                    funUpdateDescList.add("清单修改第"+iNum+"行："+String.join("、", desc));
                }
            }
            //删除数据
            if(CollectionUtils.isNotEmpty(deleteFunIdList) && deleteFunIdList.contains(fun.getId())){
                funUpdateDescList.add(String.format("清单项删除：合同清单(%s)、费项(%s)、含税单价(%s)、数量(%s)、单位(%s)、含税金额(%s)、收费周期(%s)、收费方式(%s)、税率(%s)、税额(%s)、不含税金额(%s)、备注(%s)", oldFun.getType(),
                        oldFun.getChargeItem(),
                        oldFun.getStandAmount(),
                        oldFun.getAmountNum(),
                        oldFun.getStandard(),
                        oldFun.getAmount(),
                        oldFun.getChargeMethodName(),
                        oldFun.getPayWay(),
                        oldFun.getTaxRate(),
                        oldFun.getTaxRateAmount(),
                        oldFun.getAmountWithOutRate(),
                        oldFun.getRemark()));
            }
            iNum++;
        }
        if(CollectionUtils.isNotEmpty(newAddFunList)){
            for(ContractIncomeFundUpdateF fun : newAddFunList){
                fun.setContractId(contractIncomeConclude.getId().toString());
                fun.setCorrectionTag(1);
                contractIncomeFundService.save(Global.mapperFacade.map(fun, ContractIncomeFundSaveF.class));
                funUpdateDescList.add("清单新增第"+iNum+"行：");
                iNum++;
            }
        }
        if(CollectionUtils.isNotEmpty(funUpdateDescList)){
            if(StringUtils.isNotBlank(correctionDesc)){
                correctionDesc = correctionDesc+"；";
            }
            correctionDesc = correctionDesc + "清单修改：" + String.join("、", funUpdateDescList);
        }
        contractIncomeConclude.setCorrectionDesc(correctionDesc);
        contractIncomeConcludeCorrectionMapper.updateById(contractIncomeConclude);
        return "合同关键信息修改审批通过后，请及时更新收款计划";
    }

    private ContractIncomeFundUpdateF getUpdateFunSafely(List<ContractIncomeFundUpdateF> newFunList, ContractIncomeFundE fun) {
        // 前置条件检查
        if (fun == null || StringUtils.isEmpty(fun.getId()) || CollectionUtils.isEmpty(newFunList)) {
            return new ContractIncomeFundUpdateF();
        }

        String targetId = fun.getId();

        return newFunList.stream()
                .filter(Objects::nonNull)
                .filter(x -> StringUtils.isNotEmpty(x.getId()))
                .filter(x -> targetId.equals(x.getId()))
                .findFirst()
                .orElseGet(ContractIncomeFundUpdateF::new); // 使用 orElseGet 延迟创建
    }

    private List<String> getCompareFileList(ContractIncomeFundE oldFun,ContractIncomeFundE newFun ){
        List<String> resultList = new ArrayList<>();
        compareField("合同清单", oldFun.getType(), newFun.getType(), resultList);
        compareField("费项", oldFun.getChargeItem(), newFun.getChargeItem(), resultList);
        compareField("含税单价", oldFun.getStandAmount(), newFun.getStandAmount(), resultList);
        compareField("数量", oldFun.getAmountNum(), newFun.getAmountNum(), resultList);
        compareField("单位", oldFun.getStandard(), newFun.getStandard(), resultList);
        compareField("含税金额", oldFun.getAmount(), newFun.getAmount(), resultList);
        compareField("收费周期", oldFun.getChargeMethodName(), newFun.getChargeMethodName(), resultList);
        compareField("收费方式", oldFun.getPayWay(), newFun.getPayWay(), resultList);
        compareField("税率", oldFun.getTaxRate(), newFun.getTaxRate(), resultList);
        compareField("税额", oldFun.getTaxRateAmount(), newFun.getTaxRateAmount(), resultList);
        compareField("不含税金额", oldFun.getAmountWithOutRate(), newFun.getAmountWithOutRate(), resultList);
        compareField("备注", oldFun.getRemark(), newFun.getRemark(), resultList);
        return resultList;
    }
    private static void compareField(String fieldName, Object oldVal, Object newVal, List<String> diffs) {
        /*if (!Objects.equals(oldVal, newVal)) {
            String oldStr = oldVal != null ? oldVal.toString() : "空";
            String newStr = newVal != null ? newVal.toString() : "空";
            diffs.add(String.format("%s（%s改为%s）", fieldName, oldStr, newStr));
        }*/

        if (oldVal == null && newVal == null) {
            return;
        }
        if (oldVal == null || newVal == null) {
            String oldStr = oldVal != null ? oldVal.toString() : "空";
            String newStr = newVal != null ? newVal.toString() : "空";
            diffs.add(String.format("%s（%s改为%s）", fieldName, oldStr, newStr));
            return;
        }
            // 检查是否都是数字类型
        if (oldVal instanceof Number && newVal instanceof Number) {
            BigDecimal oldBig = new BigDecimal(oldVal.toString());
            BigDecimal newBig = new BigDecimal(newVal.toString());
            if (oldBig.compareTo(newBig) == 0) {
                return;
            }
        }
        // 如果不是数字类型，或者数字类型但比较不相等，则使用原来的比较
        if (!Objects.equals(oldVal, newVal)) {
            String oldStr = oldVal.toString();
            String newStr = newVal.toString();
            diffs.add(String.format("%s（%s改为%s）", fieldName, oldStr, newStr));
        }
    }

    //根据合同ID/修正记录ID获取合同详情
    public ContractIncomeConcludeDetailV getIncomeCorrectionDetail(String id) {
        ContractDetailV param = new ContractDetailV();
        ContractIncomeConcludeE mainContract = contractIncomeConcludeMapper.selectById(id);
        ContractIncomeConcludeCorrectionE contractIncomeConclude = new ContractIncomeConcludeCorrectionE();
        if(Objects.nonNull(mainContract)){
            param.setId(id);
        }else{
            contractIncomeConclude = contractIncomeConcludeCorrectionMapper.selectById(Long.parseLong(id));
            param.setId(contractIncomeConclude.getContractId());
        }
        ContractIncomeConcludeDetailV result = this.detail(param);

        if(Objects.nonNull(mainContract)){
            QueryWrapper<ContractIncomeConcludeCorrectionE> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(ContractIncomeConcludeCorrectionE.CONTRACTID, id)
                    .in(ContractIncomeConcludeCorrectionE.CORRECTION_STATUS,Arrays.asList(CorrectionStatusEnum.草稿.getCode(), CorrectionStatusEnum.已驳回.getCode()))
                    .eq(ContractIncomeConcludeCorrectionE.DELETED,0);
            contractIncomeConclude = contractIncomeConcludeCorrectionMapper.selectOne(queryWrapper);
        }
        if(Objects.nonNull(contractIncomeConclude)){
            //修正原因
            result.setCorrectionReason(contractIncomeConclude.getCorrectionReason());
            //修正附件
            if(StringUtils.isNotEmpty(contractIncomeConclude.getCorrectionFile())){
                try {
                    List<ContractFjxxF> correctionFileList = JSONObject.parseArray(contractIncomeConclude.getCorrectionFile(),ContractFjxxF.class);
                    if(CollectionUtils.isNotEmpty(correctionFileList)) {
                        correctionFileList.forEach(fj ->{
                            if(StringUtils.isNotEmpty(fj.getSuffix())){
                                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同附件类型.getCode(), fj.getSuffix());
                                if (CollectionUtils.isNotEmpty(value)) {
                                    fj.setSuffixname(value.get(0).getName());
                                }
                            }
                        });
                        result.setCorrectionFile(correctionFileList);
                    }
                } catch (BeansException e) {
                }
            }
            result.setGmtExpireStart(contractIncomeConclude.getNewGmtExpireStart());
            result.setGmtExpireEnd(contractIncomeConclude.getNewGmtExpireEnd());
            List<ContractIncomeFundE> funList = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                    .eq(ContractIncomeFundE.CONTRACT_ID, contractIncomeConclude.getId())
                    .eq(ContractIncomeFundE.CORRECTION_TAG,1)
                    .eq(ContractIncomeFundE.DELETED,0)
            );
            if(CollectionUtils.isNotEmpty(funList)){
                Map<String, BigDecimal> funAmountMap = new HashMap<>();
                List<ContractIncomeFundV> sb = Global.mapperFacade.mapAsList(funList, ContractIncomeFundV.class);
                Map<String, List<ContractIncomePlanConcludeE>> funCountMap = new HashMap<>();
                LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ContractIncomePlanConcludeE::getContractId, contractIncomeConclude.getContractId())
                        .ne(ContractIncomePlanConcludeE::getPid,"0")
                        .eq(ContractIncomePlanConcludeE::getDeleted,0);
                List<ContractIncomePlanConcludeE> concludePlanList = contractIncomePlanConcludeMapper.selectList(queryWrapper);
                if(CollectionUtils.isNotEmpty(concludePlanList)){
                    funCountMap = concludePlanList.stream().collect(Collectors.groupingBy(ContractIncomePlanConcludeE::getContractPayFundId));
                    List<IncomeConcludePlanV2> planV2List = Global.mapperFacade.mapAsList(concludePlanList, IncomeConcludePlanV2.class);
                    contractIncomePlanConcludeService.markUsed(planV2List);

                    funAmountMap = planV2List.stream()
                            .filter(x->Boolean.TRUE.equals(x.getUsed())) // 过滤 used = true 的数据
                            .collect(Collectors.groupingBy(
                                    IncomeConcludePlanV2::getContractPayFundId,
                                    Collectors.reducing(
                                            BigDecimal.ZERO,
                                            IncomeConcludePlanV2::getPlannedCollectionAmount,
                                            BigDecimal::add
                                    )
                            ));

                }
                Map<String, List<ContractIncomePlanConcludeE>> finalFunCountMap = funCountMap;
                Map<String, BigDecimal> finalFunAmountMap = funAmountMap;
                sb.forEach(x->{
                    x.setId(StringUtils.isNotEmpty(x.getMainId()) ? x.getMainId() : x.getId());
                    x.setIsLock(CollectionUtils.isEmpty(finalFunCountMap.get(x.getId())) ? Boolean.FALSE : Boolean.TRUE);
                    x.setPlanUseAmountNum(Objects.nonNull(finalFunAmountMap.get(x.getId())) ? finalFunAmountMap.get(x.getId()) : BigDecimal.ZERO);
                });
                result.setContractIncomeFundVList(sb);
            }
        }
        return result;
    }

    //根据合同ID查询修正记录列表
    public List<IncomeCorrectionHistoryV> getIncomeCorrectionList(String id){
        List<IncomeCorrectionHistoryV> resultList = new ArrayList<>();
        QueryWrapper<ContractIncomeConcludeCorrectionE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractIncomeConcludeCorrectionE.CONTRACTID, id)
                .eq(ContractIncomeConcludeCorrectionE.DELETED,0)
                .orderByDesc(ContractIncomeConcludeCorrectionE.GMTCREATE);
        List<ContractIncomeConcludeCorrectionE> correctionList = contractIncomeConcludeCorrectionMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(correctionList)){
            Integer num = correctionList.size();
            for(ContractIncomeConcludeCorrectionE correction: correctionList){
                IncomeCorrectionHistoryV result = new IncomeCorrectionHistoryV();
                BeanUtils.copyProperties(correction, result);
                result.setId(correction.getId().toString());
                result.setNo(num);
                result.setCorrectionStatusDesc(CorrectionStatusEnum.parseName(correction.getCorrectionStatus()));
                //是否可编辑
                result.setIsShowEdit(CorrectionStatusEnum.草稿.getCode().equals(correction.getCorrectionStatus()) || CorrectionStatusEnum.已驳回.getCode().equals(correction.getCorrectionStatus()));
                //是否可删除
                result.setIsShowDeleted(CorrectionStatusEnum.草稿.getCode().equals(correction.getCorrectionStatus()) || CorrectionStatusEnum.已驳回.getCode().equals(correction.getCorrectionStatus()));
                //是否展示发起审批
                result.setIsShowApprovalBpm(CorrectionStatusEnum.草稿.getCode().equals(correction.getCorrectionStatus()) || CorrectionStatusEnum.已驳回.getCode().equals(correction.getCorrectionStatus()));
                //是否展示查看审批记录
                result.setIsShowBpmDetail(!CorrectionStatusEnum.草稿.getCode().equals(correction.getCorrectionStatus()));
                resultList.add(result);
                num--;
            }
        }
        return resultList;
    }

    //根据修正记录ID发起BPM审批
    public Boolean incomeCorrectionApproval(String id){
        ContractIncomeConcludeCorrectionE contractIncomeConclude = contractIncomeConcludeCorrectionMapper.selectById(Long.parseLong(id));
        if(Objects.isNull(contractIncomeConclude) ){
            throw new OwlBizException("该记录不存在，请输入正确修正记录ID");
        }
        ContractIncomeConcludeE mainContract = contractIncomeConcludeMapper.selectById(contractIncomeConclude.getContractId());
        if (Objects.isNull(mainContract)){
            throw new OwlBizException("主合同不存在");
        }
        //收入合同修正BPM审批
        ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(mainContract.getCommunityId(),25);
        log.info("收入合同修正：获取审批规则,结果:{}", JSON.toJSONString(approveFilter));
        if (approveFilter.getApproveWay() == 1) {
            WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
            log.info("收入合同修正：获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));
            if (ObjectUtil.isNull(wflowModelHistorysV)) {
                throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
            }
            ProcessStartF processStartF = new ProcessStartF();
            Map<String, Object> formData = new HashMap<>();
            formData.put("flowType", "收入合同修正");
            formData.put("flowId", id);
            processStartF.setFormData(formData);
            processStartF.setBusinessKey(id);
            processStartF.setBusinessType("收入合同修正");
            processStartF.setSuitableTargetType("PROJECT");
            processStartF.setSuitableTargetId(mainContract.getCommunityId());
            processStartF.setSuitableTargetName(mainContract.getCommunityName());
            log.info("收入合同修正：发起审批流程入参,processStartF:{}", JSON.toJSONString(processStartF));
            String s = null;
            try {
                s = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
            } catch (Exception e) {
                log.info("流程发起异常：{}",e);
                log.error("流程发起异常：{}",e);
                throw new OwlBizException("流程发起超时，请稍后重试！");
            }
            contractIncomeConclude.setCorrectionStatus(CorrectionStatusEnum.审批中.getCode());
            contractIncomeConclude.setBpmProcInstId(s);
            contractIncomeConcludeCorrectionMapper.updateById(contractIncomeConclude);
            contractIncomeConcludeMapper.updateIsCorrectionAndPlan(mainContract.getId(),CorrectionStatusEnum.审批中.getCode());
        }else {
            //无需审批，直接关闭
            contractIncomeConclude.setCorrectionStatus(CorrectionStatusEnum.已通过.getCode());
            contractIncomeConclude.setBpmApprovalDate(LocalDateTime.now());
            contractIncomeConcludeCorrectionMapper.updateById(contractIncomeConclude);
            contractIncomeConcludeMapper.updateIsCorrectionAndPlan(mainContract.getId(),CorrectionStatusEnum.已通过.getCode());
            this.correctionIncomeFun(mainContract.getId(), id);
        }
        return Boolean.TRUE;
    }

    //根据修正记录ID删除数据
    public Boolean deleteIcomeCorrectionById(String id){
        ContractIncomeConcludeCorrectionE contractIncomeConclude = contractIncomeConcludeCorrectionMapper.selectById(Long.parseLong(id));
        if(Objects.isNull(contractIncomeConclude) ){
            throw new OwlBizException("该记录不存在，请输入正确修正记录ID");
        }
        contractIncomeConcludeCorrectionMapper.deletedCorrectionById(contractIncomeConclude.getId());
        contractIncomeConcludeMapper.updateIsCorrectionAndPlan(contractIncomeConclude.getContractId(),null);
        return Boolean.TRUE;
    }

    //修正审批通过之后。替换原清单数据
    public Boolean correctionIncomeFun(String contractId, String correctionId){

        ContractIncomeConcludeE income = contractIncomeConcludeMapper.selectById(contractId);

        ContractIncomeConcludeCorrectionE correction = contractIncomeConcludeCorrectionMapper.selectById(correctionId);

        //更改合同起止时间
        income.setGmtExpireStart(correction.getNewGmtExpireStart());
        income.setGmtExpireEnd(correction.getNewGmtExpireEnd());
        income.setGmtModify(LocalDateTime.now());
        contractIncomeConcludeMapper.updateById( income);

        //原合同清单数据
        List<ContractIncomeFundE> oldFunList = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .eq(ContractIncomeFundE.CONTRACT_ID, contractId)
                .eq(ContractIncomeFundE.DELETED,0)
        );

        //最新修正清单数据
        List<ContractIncomeFundE> newFunList = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .eq(ContractIncomeFundE.CONTRACT_ID, correctionId)
                .eq(ContractIncomeFundE.CORRECTION_TAG,1)
                .eq(ContractIncomeFundE.DELETED,0)
        );
        List<String> newIdList = new ArrayList<>();
        newIdList.add("新版清单ID");
        if(CollectionUtils.isNotEmpty(newFunList)){
            newFunList.forEach(x->x.setId(StringUtils.isNotEmpty(x.getMainId()) ? x.getMainId() : x.getId()));
            newIdList.addAll(newFunList.stream().map(ContractIncomeFundE::getId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isEmpty(oldFunList)){
            if(CollectionUtils.isNotEmpty(newFunList)){
                log.info("原合同无清单数据，本次新增清单数据");
                newFunList.forEach(x->{
                    x.setMainId(x.getId());
                    x.setId(null);
                    x.setContractId(contractId);
                    contractIncomeFundMapper.insert(x);
                });
                return Boolean.TRUE;
            }
            log.info("原合同无清单数据，不对清单处理");
            return Boolean.TRUE;
        }
        List<String> oldIdList = oldFunList.stream().map(ContractIncomeFundE::getId).collect(Collectors.toList());
        //修改清单数据
        List<ContractIncomeFundE> updateList = newFunList.stream().filter(x->oldIdList.contains(x.getId())).collect(Collectors.toList());
        //新增清单数据
        List<ContractIncomeFundE> addList = newFunList.stream().filter(x->!oldIdList.contains(x.getId())).collect(Collectors.toList());
        //删除清单数据
        List<ContractIncomeFundE> deleteList = oldFunList.stream().filter(x->!newIdList.contains(x.getId())).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(addList)){
            addList.forEach(x->{
                x.setMainId(x.getId());
                x.setId(null);
                x.setContractId(contractId);
                contractIncomeFundMapper.insert(x);
            });
        }
        if(CollectionUtils.isNotEmpty(updateList)){
            updateList.forEach(x->{
                x.setMainId(null);
                x.setContractId(contractId);
                contractIncomeFundMapper.updateById(x);
            });
        }
        if(CollectionUtils.isNotEmpty(deleteList)){
            deleteList.forEach(x->{
                contractIncomeFundMapper.deleteById(x.getId());
            });
        }
        return Boolean.TRUE;
    }
}
