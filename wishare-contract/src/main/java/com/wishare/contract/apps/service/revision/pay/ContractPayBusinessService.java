package com.wishare.contract.apps.service.revision.pay;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.ContractRevF;
import com.wishare.contract.apps.fo.contractset.blacklist.BlackListInfoF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundUpdateF;
import com.wishare.contract.apps.fo.revision.remote.BondRelationF;
import com.wishare.contract.apps.remote.clients.*;
import com.wishare.contract.apps.fo.revision.pay.ContractPayAddF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayEditF;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalDataF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalF;
import com.wishare.contract.apps.remote.fo.proquery.ProcessQueryF;
import com.wishare.contract.apps.remote.vo.*;
import com.wishare.contract.apps.remote.vo.bpm.ProcessStartF;
import com.wishare.contract.apps.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.contract.apps.remote.vo.charge.ApproveFilter;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.remote.vo.blacklist.BlackUserV;
import com.wishare.contract.apps.remote.vo.revision.CustomerRv;
import com.wishare.contract.apps.remote.vo.revision.SupplierRv;
import com.wishare.contract.apps.service.contractset.ContractCategoryAppService;
import com.wishare.contract.apps.service.contractset.ContractPayCostPlanService;
import com.wishare.contract.apps.service.contractset.ContractUniqueCodeRuleAppService;
import com.wishare.contract.apps.service.revision.base.ContractBaseService;
import com.wishare.contract.apps.service.revision.org.ContractOrgRelationService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectPlanMonthlyAllocationAppService;
import com.wishare.contract.apps.service.revision.relation.ContractRelationBusinessService;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeSettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.*;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPayConcludeSettdeductionE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import com.wishare.contract.domains.entity.revision.template.ContractRecordInfoE;
import com.wishare.contract.domains.enums.BillTypeEnum;
import com.wishare.contract.domains.enums.ConcludeContractNatureEnum;
import com.wishare.contract.domains.enums.CostControlTypeEnum;
import com.wishare.contract.domains.enums.OperationTypeEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.enums.revision.log.LogActionTypeEnum;
import com.wishare.contract.domains.enums.settlement.SettlementTypeEnum;
import com.wishare.contract.domains.mapper.contractset.ContractProcessRecordMapper;
import com.wishare.contract.domains.mapper.revision.attachment.AttachmentMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayPlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.*;
import com.wishare.contract.domains.mapper.revision.pay.fund.ContractPayFundMapper;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPayConcludeSettdeductionMapper;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPaySettDetailsMapper;
import com.wishare.contract.domains.mapper.revision.template.ContractRecordInfoMapper;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.bond.RevisionBondCollectService;
import com.wishare.contract.domains.service.revision.log.RevisionLogService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeExpandService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayPlanConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPaySettlementConcludeService;
import com.wishare.contract.domains.service.revision.pay.fund.ContractPayFundService;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPayConcludeSettdeductionService;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPaySettDetailsService;
import com.wishare.contract.domains.service.revision.relation.ContractRelationRecordService;
import com.wishare.contract.domains.service.revision.template.ContractRecordInfoService;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.contract.domains.vo.revision.ContractInfoV;
import com.wishare.contract.domains.vo.revision.ContractNoInfoV;
import com.wishare.contract.domains.vo.revision.ContractNumShow;
import com.wishare.contract.domains.vo.revision.MockJson;
import com.wishare.contract.domains.vo.revision.fwsso.FwSSoBaseInfoF;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeDetailFjxxV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeV;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeDetailV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeTreeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import com.wishare.contract.domains.vo.revision.pay.PayNkSettlementV;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.settlement.SettlementSimpleStr;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateResultV;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateReturnV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.*;
import com.wishare.contract.domains.vo.revision.proquery.ProcessQueryV;
import com.wishare.contract.domains.vo.revision.relation.RelationRecordDetailV;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoV;
import com.wishare.contract.infrastructure.utils.BeanChangeUtil;
import com.wishare.contract.infrastructure.utils.TemplateUtils;
import com.wishare.contract.domains.vo.revision.procreate.BusinessInfoF;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateV;
import com.wishare.contract.infrastructure.utils.build.Builder;
import com.wishare.contract.infrastructure.utils.query.LambdaQueryWrapperX;
import com.wishare.contract.infrastructure.utils.query.WrapperX;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.OrderBy;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @version 1.0.0
 * @Description 支出合同业务方法-合同改版
 * @Author chenglong
 * @since 2023/6/25  13:54
 */
@Service
@Slf4j
public class ContractPayBusinessService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private AttachmentService attachmentService;

    @Setter(onMethod_ = {@Autowired})
    private ContractUniqueCodeRuleAppService contractUniqueCodeRuleAppService;

    @Setter(onMethod_ = {@Autowired})
    private ContractCategoryAppService contractCategoryAppService;

    @Setter(onMethod_ = {@Autowired})
    private ContractOrgRelationService orgRelationService;

    @Setter(onMethod_ = {@Autowired})
    private ContractRelationRecordService contractRelationRecordService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionLogService logService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRelationBusinessService contractRelationBusinessService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractProcessRecordMapper contractProcessRecordMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private OrgEnhanceComponent orgEnhanceComponent;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private SpaceFeignClient spaceFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayFundService contractPayFundService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionBondCollectService bondCollectService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRecordInfoService contractRecordInfoService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private TemplateUtils templateUtils;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeExpandAppService contractPayConcludeExpandAppService;

    @Setter(onMethod_ = {@Autowired})
    @Lazy
    private ContractPayPullService contractPayPullService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRecordInfoMapper contractRecordInfoMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractBaseService contractBaseService;

    @Value("${process.create.flag:0}")
    private Integer createProcessFlag;

    @Value("${process.create.projectId:}")
    private String createProcessProjectId;

    @Value("${process.create.bizCode:}")
    private String bizCode;

    @Value("${contract.devFlag:0}")
    private Integer devFlag;

    @Value("${contract.post.check:true}")
    private Boolean contractPostCheck;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettlementConcludeMapper contractPaySettlementConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettDetailsMapper contractPaySettDetailsMapper;
    @Autowired
    private FinanceFeignClient financeFeignClient;
    @Autowired
    private ContractPayCostPlanService contractPayCostPlanService;
    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeSettlementPlanRelationMapper settlementPlanRelationMapper;
    @Autowired
    private ContractPayPlanConcludeMapper contractPayPlanConcludeMapper;
    @Autowired
    private ContractPayConcludeExpandService contractPayConcludeExpandService;
    @Autowired
    private ContractPayConcludeSettdeductionMapper contractPayConcludeSettdeductionMapper;
    @Autowired
    private ContractPayConcludeSettlementPeriodMapper contractPayConcludeSettlementPeriodMapper;
    @Autowired
    @Lazy
    private ContractPaySettlementConcludeService contractPaySettlementConcludeService;
    @Autowired
    private ContractPaySettDetailsService contractPaySettDetailsService;
    @Autowired
    private ContractPayConcludeSettdeductionService contractPayConcludeSettdeductionService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayFundMapper contractPayFundMapper;
    @Autowired
    private ChargeClient chargeClient;
    @Autowired
    private BpmClient bpmClient;
    @Autowired
    private ContractProjectPlanMonthlyAllocationAppService contractProjectPlanMonthlyAllocationAppService;
    @Autowired
    private ContractPayCostApportionMapper contractPayCostApportionMapper;

    @Autowired
    private ContractPayPlanConcludeService contractPayPlanConcludeService;
    @Autowired
    private ContractPayCostCommunityMapper contractPayCostCommunityMapper;

    //-- 8848支出合同调整
    /**
     * 支出合同数据库添加字段 & 代码同步添加字段
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
     * 款项子表新增字段 - 税额 不含税金额 数量
     *
     * 款项子表批处理接口&后端方法
     *
     * 款项子表-后端关联合同业务逻辑&金额字段计算处理
     *
     * 变更记录接口补充&按钮控制
     *
     * 终止接口修改 & 终止日期赋值
     *
     * 新增反审接口 & 反审按钮控制 & 已拒绝状态按钮控制修改 & 详情补充 费项&税率 数据列
     */


    /**
     * 新增支出合同数据
     *
     * @param form
     * @return id
     */
    @Transactional
    public ContractInfoV addPayContract(ContractPayAddF form) {
        if(CollectionUtils.isNotEmpty(form.getContractPayFundVList())){
            Map<String, List<ContractPayFundUpdateF>> fundMap = form.getContractPayFundVList().stream().filter(x->!"delete".equals(x.getActionCode()))
                    .collect(Collectors.groupingBy(
                            this::generateCompositeKey
                    ));
            boolean hasMultiValue = fundMap.values().stream()
                    .anyMatch(list -> list.size() > 1);
            if(hasMultiValue){
                throw new OwlBizException("该合同清单存在重复数据，请重新填写");
            }
        }
        //补充合同，前置校验清单金额
        if(form.getContractType().equals(ContractTypeEnum.补充协议.getCode()) && CollectionUtils.isNotEmpty(form.getContractPayFundVList())){
            checkFunList(form.getPid(),form.getContractPayFundVList());
        }
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
        ContractPayConcludeE concludeE = Global.mapperFacade.map(form, ContractPayConcludeE.class);
        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }
        //-- 租户ID
        concludeE.setTenantId(tenantId());
        //-- 初始化相关字段 收款金额
        concludeE.setPayAmount(BigDecimal.ZERO)
                .setInvoiceAmount(BigDecimal.ZERO)
                .setChangContractAmount(BigDecimal.ZERO);

        //-- 根据ID检索并赋值对应的名称字段
        fillNameForAdd(concludeE);

        /*if (StringUtils.isBlank(form.getContractNo())) {
            //-- 客户未输入合同编号后，自动生成合同编号
//            String code = contractConcludeAppService.revContractCode(tenantId(), form.getPid(), RevTypeEnum.支出合同.getCode());
            String code = contractUniqueCodeRuleAppService.genUniqueCode(form.getIsBackDate(),concludeE.getDepartName(),ContractAreaEnum.parseName(concludeE.getIsBackDate()),form.getConmanagetype(),concludeE.getCommunityName(),2,form.getConperformprovincesname());
            concludeE.setContractNo(code);
        }*/

        //-- 校验合同编码唯一性
        // judgeContractNoOnly(concludeE);

//        if(LocalDate.now().isBefore(form.getGmtExpireStart())){
//            concludeE.setStatus( ContractRevStatusEnum.尚未履行.getCode());
//        }else{
//            concludeE.setStatus( ContractRevStatusEnum.正在履行.getCode());
//        }
        //-- 合同状态，新增的都统一成"未生效"
        concludeE.setStatus(ContractRevStatusEnum.未生效.getCode());

        //-- 审批状态 - 待提交
        concludeE.setReviewStatus(ReviewStatusEnum.待提交.getCode());
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
        contractPayConcludeMapper.insert(concludeE);
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
            if(form.getIsEditWps() != null && form.getIsEditWps()){
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
        form.getContractPayFundVList().stream().forEach( s-> {
            s.setContractId(concludeE.getId());
        });

        List<ContractPayFundUpdateF> payFundList = form.getContractPayFundVList();
        if(form.getContractType().equals(ContractTypeEnum.补充协议.getCode()) && form.getIsHy().equals(1)){
            payFundList.forEach(x->{x.setIsHy(1);x.setBcContractId(x.getContractId());});
        }
        //新增支出合同，需要校验合同清单金额
        contractPayFundService.dealBatch(payFundList, true);

        return contractInfoV;
    }

    //对新增合同的清单项进行金额校验
    private void checkFunList(String contractId, List<ContractPayFundUpdateF> funList) {
        //查询主合同清单数据
        List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, contractId)
                .eq(ContractPayFundE.DELETED, 0));
        if (CollectionUtils.isEmpty(mainFunList)) {
            return;
        }
/*        Map<String, ContractPayFundE> mainFundYJMap = new HashMap<>();
        ContractPayConcludeE nkContract = contractPayConcludeMapper.queryNKContractById(contractId);
        if(Objects.nonNull(nkContract)){
            List<ContractPayFundE> mainYJFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .eq(ContractPayFundE.CONTRACT_ID, nkContract.getId())
                    .eq(ContractPayFundE.DELETED, 0));
            if(CollectionUtils.isNotEmpty(mainYJFunList)){
                mainFundYJMap = mainYJFunList.stream()
                        .collect(Collectors.toMap(
                                this::generateCompositeKey,
                                fund -> fund,
                                (fund1, fund2) -> fund1
                        ));
            }
        }*/

        //查询主合同有无审批中及审批通过的结算单
        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPaySettlementConcludeE::getContractId, contractId)
                .notIn(ContractPaySettlementConcludeE::getPid,0)
                .eq(ContractPaySettlementConcludeE::getDeleted,0)
                .in(ContractPaySettlementConcludeE :: getReviewStatus , ReviewStatusEnum.审批中.getCode(), ReviewStatusEnum.已通过.getCode());
        List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        //结算明细清单对应结算金额
        Map<String, BigDecimal> settDetMap = new HashMap<>();

        //结算单关联的结算计划
        List<String> planIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(settlementList)){
            //查询无审批中及审批通过的结算单明细
            LambdaQueryWrapper<ContractPaySettDetailsE> querySetdetWrapper = new LambdaQueryWrapper<>();
            querySetdetWrapper.in(ContractPaySettDetailsE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList()))
                    .eq(ContractPaySettDetailsE::getDeleted,0);
            List<ContractPaySettDetailsE> settDetList = contractPaySettDetailsMapper.selectList(querySetdetWrapper);
            settDetMap = settDetList.stream()
                    .filter(detail -> detail != null && detail.getPayFundId() != null && detail.getAmount() != null)
                    .collect(Collectors.groupingBy(
                            ContractPaySettDetailsE::getPayFundId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    ContractPaySettDetailsE::getAmount,
                                    BigDecimal::add
                            )
                    ));

            //查关联的结算计划id
            List<ContractPayConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                    .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                    .in(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList())));
            if (CollectionUtils.isNotEmpty(relations)) {
                planIdList = relations.stream()
                        .map(ContractPayConcludeSettlementPlanRelationE::getPlanId)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }

        Map<String, ContractPayFundE> mainFundMap = mainFunList.stream()
                .collect(Collectors.toMap(
                        this::generateCompositeKey,
                        fund -> fund,
                        (fund1, fund2) -> fund1
                ));
        Map<String, ContractPayFundUpdateF> bcFundMap = funList.stream()
                .collect(Collectors.toMap(
                        this::generateCompositeKey,
                        fund -> fund,
                        (fund1, fund2) -> fund1
                ));
        for(String key : bcFundMap.keySet()){
            ContractPayFundE mainFund = mainFundMap.get(key);
            ContractPayFundUpdateF bcFund = bcFundMap.get(key);
            if(Objects.nonNull(mainFund)){

                if(bcFund.getAmount().compareTo(BigDecimal.ZERO) >= 0){
                    continue;
                }

                //清单分组【费项ID，款项类型，付费方式ID，税率ID，付费类型ID，标准金额，收费标准ID】
                String title = mainFund.getChargeItem()+"_"
                        +mainFund.getType()+"_"
                        +"含税单价："+mainFund.getStandAmount();
                //主合同合同清单金额
                BigDecimal funMainAmount =  mainFund.getAmount();
                //补充合同清单金额
                BigDecimal funAmount =  bcFund.getAmount();
                //调减金额
                BigDecimal changeAmount = funAmount.add(funMainAmount);
                if(changeAmount.compareTo(BigDecimal.ZERO) < 0){
                    throw new OwlBizException("清单项：【" + title + "】调减金额不能大于未结算金额");
                }

                //主合同清单中结算单使用的清单金额
                BigDecimal settDetAmount = settDetMap.get(mainFund.getId());
                if(Objects.nonNull(settDetAmount)){
                    funMainAmount = funMainAmount.subtract(settDetAmount);
                }
                changeAmount = funAmount.add(funMainAmount);
                if(changeAmount.compareTo(BigDecimal.ZERO) < 0){
                    throw new OwlBizException("清单项：【" + title + "】调减金额不能大于未结算金额,结算单使用金额："+settDetAmount);
                }

                ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(contractId);
                //根据合同ID获取合同报账单中对下计提非进行中（1待推送/3推送失败/5已驳回/6单据驳回/8制单失败）临时账单ID
                List<String> billList = financeFeignClient.getReceivableBillIdList(contractId, mainContract.getCommunityId());
                if(CollectionUtils.isNotEmpty(billList)){
                    //根据临时账单ID查询对应成本计划
                    List<PayCostPlanE> payCostList = contractPayCostPlanService.list(Wrappers.<PayCostPlanE>lambdaQuery()
                            .eq(PayCostPlanE::getContractId, contractId)
                            .in(PayCostPlanE :: getBillId,  billList)
                            .eq(PayCostPlanE::getDeleted, 0));
                    if(CollectionUtils.isNotEmpty(payCostList)){
                        List<String> finalPlanIdList = planIdList;
                        //将结算单关联的计划过滤，防止金额重复
                        payCostList = payCostList.stream().filter(x->!finalPlanIdList.contains(x.getPlanId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(payCostList)){
                            List<String> planIds = payCostList.stream()
                                    .map(PayCostPlanE::getPlanId)
                                    .distinct()
                                    .collect(Collectors.toList());
                            //查询结算计划信息
                            LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
                            queryPlanWrapper.ne(ContractPayPlanConcludeE::getPid, 0)
                                    .in(ContractPayPlanConcludeE::getId, planIds)
                                    .eq(ContractPayPlanConcludeE::getContractPayFundId, mainFund.getId())
                                    .eq(ContractPayPlanConcludeE::getDeleted,0);
                            List<ContractPayPlanConcludeE> concludePlanList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);
                            if(CollectionUtils.isNotEmpty(concludePlanList)){
                                BigDecimal planAmount =  concludePlanList.stream()
                                        .filter(Objects::nonNull)
                                        .map(ContractPayPlanConcludeE::getPlannedCollectionAmount)
                                        .filter(Objects::nonNull)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                funMainAmount = funMainAmount.subtract(planAmount);
                                changeAmount = funAmount.add(funMainAmount);
                                if(changeAmount.compareTo(BigDecimal.ZERO) < 0){
                                    throw new OwlBizException("清单项：【" + title + "】YJ金额不能小于0,对下计提使用金额："+settDetAmount);
                                }
                            }
                        }
                    }
                }

                /*ContractPayFundE mainYJFund = mainFundYJMap.get(key);
                if(Objects.nonNull(mainYJFund)){
                    mainYJFund.setCheckAmount(mainYJFund.getAmount().add(bcFund.getAmount()));
                    contractPayFundService.updateById(mainYJFund);
                }*/
                mainFund.setCheckAmount(mainFund.getAmount().add(bcFund.getAmount()));
                contractPayFundService.updateById(mainFund);
                ContractPayFundE nkFun = contractPayFundMapper.selectOne(new QueryWrapper<ContractPayFundE>()
                        .orderByDesc(ContractPayFundE.GMT_CREATE)
                        .eq(ContractPayFundE.TENANT_ID, tenantId())
                        .eq(ContractPayFundE.MAIN_ID, mainFund.getId())
                        .eq(ContractPayFundE.DELETED, 0)
                );
                if(Objects.nonNull(nkFun)){
                    ContractPayFundE nkUpFun = new ContractPayFundE();
                    BeanUtils.copyProperties(mainFund, nkUpFun);
                    nkUpFun.setId(nkFun.getId());
                    nkUpFun.setContractId(nkFun.getContractId());
                    nkUpFun.setMainId(nkFun.getMainId());
                    contractPayFundMapper.updateById(nkUpFun);
                }
            }
        }
    }

    //清单分组【费项ID，款项类型，付费方式ID，税率ID，付费类型ID，标准金额，收费标准ID】
    private String generateCompositeKey(ContractPayFundE fund) {
        return fund.getChargeItemId() + "|" +
                nullToEmpty(fund.getTypeId()) + "|" +
                nullToEmpty(fund.getPayWayId()) + "|" +
                nullToEmpty(fund.getTaxRateId()) + "|" +
                nullToEmpty(fund.getPayTypeId()) + "|" +
                (fund.getStandAmount() != null ? fund.getStandAmount().stripTrailingZeros().toPlainString() : "0") + "|" +
                nullToEmpty(fund.getStandardId())+ "|" +
                fund.getIsHy()+ "|" +
                nullToEmpty(fund.getBcContractId());
    }
    //清单分组【费项ID，款项类型，付费方式ID，税率ID，付费类型ID，标准金额，收费标准ID】
    private String generateCompositeKey(ContractPayFundUpdateF fund) {
        return fund.getChargeItemId() + "|" +
                nullToEmpty(fund.getTypeId()) + "|" +
                nullToEmpty(fund.getPayWayId()) + "|" +
                nullToEmpty(fund.getTaxRateId()) + "|" +
                nullToEmpty(fund.getPayTypeId()) + "|" +
                (fund.getStandAmount() != null ? fund.getStandAmount().stripTrailingZeros().toPlainString() : "0") + "|" +
                nullToEmpty(fund.getStandardId())+ "|" +
                fund.getIsHy()+ "|" +
                nullToEmpty(fund.getBcContractId());
    }
    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private void checkStartTimeOnSupplement(ContractPayConcludeE concludeE) {
        // 补充协议判定开始时间落在 主合同的开始时间到结束时间+1天内
        ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(concludeE.getPid());
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
     *
     * @param form
     */
    @Transactional
    public Boolean modifySignDate(ContractPayEditF form) {
        //-- 校验合同ID是否正确
        ContractPayConcludeE e = contractPayConcludeService.getById(form.getId());
        if (Objects.isNull(e)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        e.setSignDate(form.getSignDate());
        e.setGmtModify(LocalDateTime.now());
        //-- 更新
        contractPayConcludeService.updateById(e);
        return Boolean.TRUE;
    }


    /**
     * 编辑支出合同数据
     *
     * @param form
     */
    @Transactional
    public ContractInfoV editPayContract(ContractPayEditF form) {
        if(CollectionUtils.isNotEmpty(form.getContractPayFundVList())){
            Map<String, List<ContractPayFundUpdateF>> fundMap = form.getContractPayFundVList().stream().filter(x->!"delete".equals(x.getActionCode()))
                    .collect(Collectors.groupingBy(
                            this::generateCompositeKey
                    ));
            boolean hasMultiValue = fundMap.values().stream()
                    .anyMatch(list -> list.size() > 1);
            if(hasMultiValue){
                throw new OwlBizException("该合同清单存在重复数据，请重新填写");
            }
        }
        //补充合同，前置校验清单金额
        if(form.getContractType().equals(ContractTypeEnum.补充协议.getCode()) && CollectionUtils.isNotEmpty(form.getContractPayFundVList())){
            checkFunList(form.getPid(),form.getContractPayFundVList().stream().filter(x->!x.getActionCode().equals("delete")).collect(Collectors.toList()));
        }
        ContractInfoV contractInfoV = new ContractInfoV();
        //-- 校验合同ID是否正确
        ContractPayConcludeE e = contractPayConcludeService.getById(form.getId());
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
        ContractPayConcludeE concludeE = Global.mapperFacade.map(form, ContractPayConcludeE.class);
        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }
        //-- 根据ID检索并赋值对应的名称字段
        fillNameForEdit(concludeE, e);

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

        BeanChangeUtil<ContractPayConcludeE> t = new BeanChangeUtil<>();
        ContractPayRecordF e1 = Global.mapperFacade.map(e, ContractPayRecordF.class);
        ContractPayRecordF e2 = Global.mapperFacade.map(concludeE, ContractPayRecordF.class);
        String str = t.contrastObj(e1, e2);

        boolean isEdit = false;
        if(form.getIsEditWps() != null && form.getIsEditWps()){
            isEdit = true;
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
            if(Objects.nonNull(contractRecordInfoE)){
                contractRecordInfoMapper.deleteById(contractRecordInfoE);
            }
        }

        if(StringUtils.isEmpty(contractInfoV.getLinkAdress())) {
            if (StringUtils.isNotBlank(concludeE.getTempId())) {
                FileVo fileVo = templateUtils.genTemplateInfo(concludeE.getTempId(), concludeE, concludeE.getName());
                concludeE.setTempFilekey(fileVo.getFileKey());
                //-- 把修改记录更新到表里
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

//        if(LocalDate.now().isBefore(form.getGmtExpireStart())){
//            concludeE.setStatus( ContractRevStatusEnum.尚未履行.getCode());
//        }else{
//            concludeE.setStatus( ContractRevStatusEnum.正在履行.getCode());
//        }
        //编辑同样的，同样是"未生效"
        concludeE.setStatus(ContractRevStatusEnum.未生效.getCode());

        //-- 更新
        contractPayConcludeService.updateById(concludeE);

        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.编辑.getCode());

        if (!Boolean.TRUE.equals(form.getIsStash()) && !isEdit) {
            contractInfoV.setSsoLinkAdress(postContract(concludeE.getId()));
        }

        //-- 处理范文文件
        dealContractTemp(concludeE, form.getPdffjxx());

        //-- 编辑拓展表
        form.setOppositeOne(concludeE.getOppositeOne());
        form.setOurParty(concludeE.getOurParty());
        dealEditContractExpand(concludeE.getId(),form);
        //编辑接口，如果该合同有CT码，那么不校验清单金额
        Boolean isCheckFundAmount = true;
        if (StringUtils.isNotBlank(e.getConmaincode())) {
            isCheckFundAmount = false;
        }
        if(concludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode()) && concludeE.getIsHy().equals(1) && CollectionUtils.isNotEmpty(form.getContractPayFundVList())){
            form.getContractPayFundVList().forEach(x->{x.setIsHy(1);x.setBcContractId(x.getContractId());});
        }
        contractPayFundService.dealBatch(form.getContractPayFundVList(), isCheckFundAmount);
        contractInfoV.setId(concludeE.getId());
        contractInfoV.setIsSucess(true);
        //-- 处理合同编号
        if (StringUtils.isNotBlank(e.getContractNo())) {
            ContractNoInfoV contractNoInfoV = contractBaseService.generatePayContractNo(concludeE, e);
            log.info("ContractPayBusinessService.editPayContract:contractNoInfoV:{}", JSON.toJSONString(contractNoInfoV));
            if (Objects.isNull(contractNoInfoV)){
                throw new OwlBizException("合同编号生成失败-未知异常");
            }
            if (contractNoInfoV.getCode() == 0){
                throw new OwlBizException(contractNoInfoV.getFailReason());
            }
            concludeE.setContractNo(contractNoInfoV.getContractNo());
            //-- 更新
            contractPayConcludeService.updateById(concludeE);
        }

        return contractInfoV;
    }
    @Transactional
    public ContractInfoV correctionPay(ContractPayEditF form) {
        ContractInfoV contractInfoV = new ContractInfoV();
        //-- 校验合同ID是否正确
        ContractPayConcludeE e = contractPayConcludeService.getById(form.getId());
        if (Objects.isNull(e)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }

        if(CollectionUtils.isNotEmpty(form.getContractPayFundVList()) && e.getContractType() == 0){
            List<ContractPayFundUpdateF> contractPayFundVList = form.getContractPayFundVList().stream().filter(record -> !"delete".equals(record.getActionCode())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(contractPayFundVList)){
                BigDecimal totalAmount = contractPayFundVList.stream()
                        .map(ContractPayFundUpdateF::getAmount)
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
        ContractPayConcludeE concludeE = Global.mapperFacade.map(form, ContractPayConcludeE.class);
        BeanUtils.copyProperties(e, concludeE);
        BeanUtils.copyProperties(form, concludeE, getNullPropertyNames(form));

        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }
        //-- 根据ID检索并赋值对应的名称字段
        fillNameForEdit(concludeE, e);

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

        BeanChangeUtil<ContractPayConcludeE> t = new BeanChangeUtil<>();
        ContractPayRecordF e1 = Global.mapperFacade.map(e, ContractPayRecordF.class);
        ContractPayRecordF e2 = Global.mapperFacade.map(concludeE, ContractPayRecordF.class);
        String str = t.contrastObj(e1, e2);

        boolean isEdit = false;
        if(form.getIsEditWps() != null && form.getIsEditWps()){
            isEdit = true;
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

        //-- 更新
        contractPayConcludeService.updateById(concludeE);

        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.编辑.getCode());

        if (!Boolean.TRUE.equals(form.getIsStash()) && !isEdit) {
            contractInfoV.setSsoLinkAdress(postContract(concludeE.getId()));
        }

        //-- 处理范文文件
        dealContractTemp(concludeE, form.getPdffjxx());

        //-- 编辑拓展表
        form.setOppositeOne(concludeE.getOppositeOne());
        form.setOurParty(concludeE.getOurParty());
        dealEditContractExpand(concludeE.getId(),form);
        //编辑接口，如果该合同有CT码，那么不校验清单金额
        Boolean isCheckFundAmount = true;
        if (StringUtils.isNotBlank(e.getConmaincode())) {
            isCheckFundAmount = false;
        }

        //变更前清单

        List<ContractPayFundE> oldFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, concludeE.getId())
                .eq(ContractPayFundE.IS_HY, 0)
                .eq(ContractPayFundE.DELETED, 0)
                .isNotNull("cbApportionId"));


        contractPayFundService.dealBatch(form.getContractPayFundVList(), isCheckFundAmount);
        contractInfoV.setId(concludeE.getId());
        contractInfoV.setIsSucess(true);

        if(CollectionUtils.isEmpty(oldFunList)){
            return contractInfoV;
        }

        extracted(e, oldFunList, concludeE);
        return contractInfoV;
    }

    public void extracted(ContractPayConcludeE oldPay, List<ContractPayFundE> oldFunList, ContractPayConcludeE concludeE) {
        //释放原占用数据
        DynamicCostIncurredReqF costSFReq = new DynamicCostIncurredReqF();
        costSFReq.setBillGuid(oldPay.getId());
        costSFReq.setBillCode(oldPay.getContractNo());
        costSFReq.setBillName(oldPay.getName());
        costSFReq.setBillTypeName(BillTypeEnum.CONTRACT_REJECT.getName());
        costSFReq.setBillTypeEnum(BillTypeEnum.CONTRACT_REJECT.getCode());
        costSFReq.setOperationType(OperationTypeEnum.RELEASE.getCode());
        costSFReq.setContactor(OperationTypeEnum.RELEASE.getName());
        costSFReq.setSystemType(1);
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredSfDataList = new ArrayList<>();
        List<String> costIdList = new ArrayList<>();
        for (ContractPayFundE mainFun : oldFunList) {
            List<ContractPayCostApportionE> costApportionEList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                    .eq(ContractPayCostApportionE.CONTRACT_ID, mainFun.getContractId())
                    .eq(ContractPayCostApportionE.DELETED, 0)
                    .and(wrapper -> wrapper
                            .eq(ContractPayCostApportionE.ID, mainFun.getCbApportionId())
                            .or()
                            .eq(ContractPayCostApportionE.PID, mainFun.getCbApportionId())
                    )
            );
            if(CollectionUtils.isEmpty(costApportionEList)){
                continue;
            }
            costIdList.addAll(costApportionEList.stream().map(ContractPayCostApportionE::getId).collect(Collectors.toList()));
            ContractPayCostApportionE mainCost = costApportionEList.stream().filter(x->mainFun.getCbApportionId().equals(x.getId())).findFirst().orElse(new ContractPayCostApportionE());
            costSFReq.setProjectGuid(mainCost.getProjectGuid());
            costSFReq.setBuGuid(mainCost.getBuGuid());
            costSFReq.setBusinessGuid(mainCost.getBusinessGuid());
            costSFReq.setBusinessUnitCode(mainCost.getBusinessUnitCode());

            List<ContractPayCostApportionE> yearCostList = costApportionEList.stream().filter(x->mainFun.getCbApportionId().equals(x.getPid()) && CostApportionTypeEnum.分摊金额.getCode().equals(x.getApportionType())).collect(Collectors.toList());
            for(ContractPayCostApportionE ftCost : yearCostList){
                DynamicCostIncurredReqF.IncurredDataListDTO incurredData = new DynamicCostIncurredReqF.IncurredDataListDTO();
                BeanUtils.copyProperties(ftCost, incurredData);
                incurredData.setAccountItemFullCode(mainFun.getAccountItemFullCode());
                incurredData.setDynamicCostGuid(ftCost.getDynamicCostGuid());
                incurredData.setShareYear(ftCost.getYear());
                incurredData.setYearShareAmount(this.getBigDecimalNegate(ftCost.getYearSurplus()));
                incurredData.setJanShareAmount(this.getBigDecimalNegate(ftCost.getJanSurplus()));
                incurredData.setFebShareAmount(this.getBigDecimalNegate(ftCost.getFebSurplus()));
                incurredData.setMarShareAmount(this.getBigDecimalNegate(ftCost.getMarSurplus()));
                incurredData.setAprShareAmount(this.getBigDecimalNegate(ftCost.getAprSurplus()));
                incurredData.setMayShareAmount(this.getBigDecimalNegate(ftCost.getMaySurplus()));
                incurredData.setJunShareAmount(this.getBigDecimalNegate(ftCost.getJunSurplus()));
                incurredData.setJulShareAmount(this.getBigDecimalNegate(ftCost.getJulSurplus()));
                incurredData.setAugShareAmount(this.getBigDecimalNegate(ftCost.getAugSurplus()));
                incurredData.setSepShareAmount(this.getBigDecimalNegate(ftCost.getSepSurplus()));
                incurredData.setOctShareAmount(this.getBigDecimalNegate(ftCost.getOctSurplus()));
                incurredData.setNovShareAmount(this.getBigDecimalNegate(ftCost.getNovSurplus()));
                incurredData.setDecShareAmount(this.getBigDecimalNegate(ftCost.getDecSurplus()));
                incurredSfDataList.add(incurredData);
            }

            mainFun.setCbApportionId(null);
        }
        contractPayFundMapper.deletedCostData(oldPay.getId());
        contractPayCostApportionMapper.deletedCostData(oldPay.getId(),costIdList);

        costSFReq.setIncurredAmount(incurredSfDataList.stream()
                .map(DynamicCostIncurredReqF.IncurredDataListDTO::getYearShareAmount)
                .filter(Objects::nonNull)  // 过滤掉null值
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredMeargeDataList = this.mergeIncurredData(incurredSfDataList);
        costSFReq.setIncurredDataList(incurredMeargeDataList);
        log.info("支出合同修正，成本数据释放入参：{}", JSONArray.toJSON(costSFReq));
        CostBaseResponse<List<DynamicCostIncurredRespF>> shifangData = externalFeignClient.getDynamicCostIncurred(costSFReq);
        log.info("setIncurredDataList，成本数据释放出参：{}", JSONArray.toJSON(shifangData));
        if(!shifangData.getSuccess()){
            throw new OwlBizException("支出合同修正，成本数据释放失败："+ shifangData.getMessage());
        }

        //变更后清单
        List<ContractPayFundE> newFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, concludeE.getId())
                .eq(ContractPayFundE.IS_HY, 0)
                .eq(ContractPayFundE.DELETED, 0)
                .isNotNull("accountItemFullCode"));

        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        //TODO-hhb调用成本校验
        DynamicCostIncurredReqF costReq = new DynamicCostIncurredReqF();
        costReq.setBillGuid(concludeE.getId());
        costReq.setBillCode(concludeE.getContractNo());
        costReq.setBillName(concludeE.getName());

        costReq.setBillTypeName(BillTypeEnum.CONTRACT_SIGN.getName());
        costReq.setBillTypeEnum(BillTypeEnum.CONTRACT_SIGN.getCode());
        costReq.setOperationType(OperationTypeEnum.CHECK.getCode());
        costReq.setContactor(OperationTypeEnum.CHECK.getName());
        costReq.setSystemType(1);
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList = new ArrayList<>();
        for(ContractPayFundE fun : newFunList){
            ContractPayCostPlanReqV funVo = new ContractPayCostPlanReqV();
            funVo.setCommunityId(concludeE.getCommunityId());
            funVo.setGmtExpireStart(concludeE.getGmtExpireStart());
            funVo.setGmtExpireEnd(concludeE.getGmtExpireEnd());
            funVo.setChargeItemId(fun.getChargeItemId());
            funVo.setAmountWithOutRate(fun.getAmountWithOutRate());
            funVo.setAccountItemFullCode(fun.getAccountItemFullCode());
            ContractPayCostApportionV funCostData = this.getCostApportionDetail(funVo);
            if(Objects.isNull(funCostData)){
                continue;
            }
            costReq.setBusinessUnitCode(funCostData.getBusinessUnitCode());
            costReq.setBuGuid(funCostData.getBuGuid());
            costReq.setProjectGuid(funCostData.getProjectGuid());
            costReq.setBusinessGuid(funCostData.getBusinessGuid());

            Map<String, List<ContractPayCostApportionDetailV>> apportionMap = funCostData.getApportionMap();
            ContractPayCostApportionE costApportionE = new ContractPayCostApportionE();
            BeanUtils.copyProperties(funCostData, costApportionE);
            BeanUtils.copyProperties(costReq, costApportionE);
            costApportionE.setContractId(concludeE.getId());
            costApportionE.setPid("0");
            costApportionE.setApportionAmount(fun.getAmountWithOutRate());
            costApportionE.setTenantId(identityInfo.getTenantId());
            costApportionE.setCreator(identityInfo.getUserId());
            costApportionE.setCreatorName(identityInfo.getUserName());
            costApportionE.setGmtCreate(now);
            costApportionE.setOperator(identityInfo.getUserId());
            costApportionE.setOperatorName(identityInfo.getUserName());
            costApportionE.setGmtModify(now);
            contractPayCostApportionMapper.insert(costApportionE);
            fun.setCbApportionId(costApportionE.getId());
            fun.setAccountItemCode(funCostData.getAccountItemCode());
            fun.setAccountItemName(funCostData.getAccountItemName());
            fun.setAccountItemFullCode(funCostData.getAccountItemFullCode());
            fun.setAccountItemFullName(funCostData.getAccountItemFullName());
            fun.setSummarySurplusAmount(funCostData.getSummarySurplusAmount());
            List<String> yearList = IntStream.rangeClosed(concludeE.getGmtExpireStart().getYear(), concludeE.getGmtExpireEnd().getYear())
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
            for(String year : yearList){
                ContractPayCostApportionDetailV useCost = apportionMap.get(year).stream().filter(
                                x->CostApportionTypeEnum.可用金额.getCode().equals(x.getApportionType())).findFirst() // 返回 Optional
                        .orElse(new ContractPayCostApportionDetailV());
                ContractPayCostApportionDetailV ftCost = apportionMap.get(year).stream().filter(
                                x->CostApportionTypeEnum.分摊金额.getCode().equals(x.getApportionType())).findFirst() // 返回 Optional
                        .orElse(new ContractPayCostApportionDetailV());
                DynamicCostIncurredReqF.IncurredDataListDTO incurredData = new DynamicCostIncurredReqF.IncurredDataListDTO();
                BeanUtils.copyProperties(ftCost, incurredData);
                incurredData.setAccountItemFullCode(funCostData.getAccountItemFullCode());
                incurredData.setDynamicCostGuid(useCost.getDynamicCostGuid());
                incurredData.setShareYear(year);
                incurredData.setYearShareAmount(ftCost.getYearSurplus());
                incurredData.setJanShareAmount(ftCost.getJanSurplus());
                incurredData.setFebShareAmount(ftCost.getFebSurplus());
                incurredData.setMarShareAmount(ftCost.getMarSurplus());
                incurredData.setAprShareAmount(ftCost.getAprSurplus());
                incurredData.setMayShareAmount(ftCost.getMaySurplus());
                incurredData.setJunShareAmount(ftCost.getJunSurplus());
                incurredData.setJulShareAmount(ftCost.getJulSurplus());
                incurredData.setAugShareAmount(ftCost.getAugSurplus());
                incurredData.setSepShareAmount(ftCost.getSepSurplus());
                incurredData.setOctShareAmount(ftCost.getOctSurplus());
                incurredData.setNovShareAmount(ftCost.getNovSurplus());
                incurredData.setDecShareAmount(ftCost.getDecSurplus());
                incurredDataList.add(incurredData);

                List<ContractPayCostApportionDetailV> apportionList = apportionMap.get(year);
                for(ContractPayCostApportionDetailV copyCost : apportionList){
                    ContractPayCostApportionE costApportionPidE = new ContractPayCostApportionE();
                    BeanUtils.copyProperties(copyCost, costApportionPidE);
                    if(CostApportionTypeEnum.分摊金额.getCode().equals(copyCost.getApportionType()) && Objects.nonNull(useCost)){
                        costApportionPidE.setDynamicCostGuid(useCost.getDynamicCostGuid());
                    }
                    costApportionPidE.setContractId(concludeE.getId());
                    costApportionPidE.setPid(costApportionE.getId());
                    costApportionPidE.setTenantId(identityInfo.getTenantId());
                    costApportionPidE.setCreator(identityInfo.getUserId());
                    costApportionPidE.setCreatorName(identityInfo.getUserName());
                    costApportionPidE.setGmtCreate(now);
                    costApportionPidE.setOperator(identityInfo.getUserId());
                    costApportionPidE.setOperatorName(identityInfo.getUserName());
                    costApportionPidE.setGmtModify(now);
                    contractPayCostApportionMapper.insert(costApportionPidE);
                }
            }
        }
        if(incurredDataList.size()>0){
            costReq.setIncurredAmount(incurredDataList.stream()
                    .map(DynamicCostIncurredReqF.IncurredDataListDTO::getYearShareAmount)
                    .filter(Objects::nonNull)  // 过滤掉null值
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredMeargeJxDataList = this.mergeIncurredData(incurredDataList);
            costReq.setIncurredDataList(incurredMeargeJxDataList);
            log.info("支出合同修正数据，成本数据校验入参：{}", JSONArray.toJSON(costReq));
            CostBaseResponse<List<DynamicCostIncurredRespF>> jiaoyanData = externalFeignClient.getDynamicCostIncurred(costReq);
            log.info("支出合同修正数据，成本数据校验出参：{}", JSONArray.toJSON(jiaoyanData));
            if(!jiaoyanData.getSuccess()){
                if(CollectionUtils.isNotEmpty(jiaoyanData.getData())){
                    List<String> errMsgList = jiaoyanData.getData().stream().map(DynamicCostIncurredRespF::getErrorMessage).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(errMsgList)){
                        String errMsg = errMsgList.stream()
                                .filter(msg -> msg != null && !msg.trim().isEmpty()) // 过滤空值
                                .distinct()
                                .collect(Collectors.joining(", "));
                        throw new OwlBizException("支出合同校验数据失败："+ errMsg);
                    }
                }
                throw new OwlBizException("支出合同校验数据失败："+ jiaoyanData.getMessage());
            }

            //TODO-hhb调用成本占用
            costReq.setContactor(OperationTypeEnum.OCCUPY.getName());
            costReq.setOperationType(OperationTypeEnum.OCCUPY.getCode());
            log.info("支出合同修正操作，成本数据占用入参：{}", JSONArray.toJSON(costReq));
            CostBaseResponse<List<DynamicCostIncurredRespF>> zhanyongData = externalFeignClient.getDynamicCostIncurred(costReq);
            log.info("支出合同修正操作，成本数据占用出参：{}", JSONArray.toJSON(zhanyongData));
            //TODO 成本问题，临时注释
            if(!zhanyongData.getSuccess()){
                throw new OwlBizException("该合同成本占用失败："+ zhanyongData.getMessage());
            }

            //查询有无NK合同，若有，对其清单数据进行修改
            ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(concludeE.getId());
            if(Objects.nonNull(nkConclude) && !NkStatusEnum.已关闭.getCode().equals(nkConclude.getNkStatus())){
                List<ContractPayFundE> nkFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                        .eq(ContractPayFundE.CONTRACT_ID, nkConclude.getId())
                        .eq(ContractPayFundE.DELETED, 0));
                List<ContractPayFundE> nkMainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                        .eq(ContractPayFundE.CONTRACT_ID, nkConclude.getPid())
                        .eq(ContractPayFundE.DELETED, 0));
                for(ContractPayFundE fun : nkFunList){
                    ContractPayFundE mainFun = nkMainFunList.stream()
                            .filter(x -> x.getId().equals(fun.getMainId()))
                            .findFirst()
                            .orElse(new ContractPayFundE());
                    fun.setAccountItemCode(mainFun.getAccountItemCode());
                    fun.setAccountItemName(mainFun.getAccountItemName());
                    fun.setAccountItemFullCode(mainFun.getAccountItemFullCode());
                    fun.setAccountItemFullName(mainFun.getAccountItemFullName());
                    fun.setCbApportionId(mainFun.getCbApportionId());
                }
                contractPayFundService.updateBatchById(nkFunList);
            }
        }
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
     *
     * @param form
     */
    @Transactional
    public ContractInfoV modifyPayContract(ContractPayAddF form) {
        if(CollectionUtils.isNotEmpty(form.getContractPayFundVList())){
            Map<String, List<ContractPayFundUpdateF>> fundMap = form.getContractPayFundVList().stream().filter(x->!"delete".equals(x.getActionCode()))
                    .collect(Collectors.groupingBy(
                            this::generateCompositeKey
                    ));
            boolean hasMultiValue = fundMap.values().stream()
                    .anyMatch(list -> list.size() > 1);
            if(hasMultiValue){
                throw new OwlBizException("该合同清单存在重复数据，请重新填写");
            }
        }
        //补充合同，前置校验清单金额
        if(form.getContractType().equals(ContractTypeEnum.补充协议.getCode()) && CollectionUtils.isNotEmpty(form.getContractPayFundVList())){
            checkFunList(form.getPid(),form.getContractPayFundVList().stream().filter(x->!x.getActionCode().equals("delete")).collect(Collectors.toList()));
        }

        //-- α.校验ID正确性
        ContractPayConcludeE oldConcludeE = contractPayConcludeService.getById(form.getId());
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
        ContractPayConcludeE concludeE = Global.mapperFacade.map(form, ContractPayConcludeE.class);
        if (Objects.nonNull(form.getServeType())) {
            concludeE.setContractServeType(form.getServeType().getCode());
        }

        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.selectById(form.getPid());

        if(ObjectUtils.isNotEmpty(contractPayConcludeE)){
            concludeE.setConmaincode(contractPayConcludeE.getConmaincode());
            concludeE.setFromid(contractPayConcludeE.getFromid());
            concludeE.setSignDate(contractPayConcludeE.getSignDate());
        }

        //-- 租户ID
        concludeE.setTenantId(tenantId());
        //-- 初始化相关字段 收款金额
        concludeE.setPayAmount(BigDecimal.ZERO)
                .setInvoiceAmount(BigDecimal.ZERO)
                .setChangContractAmount(oldConcludeE.getChangContractAmount());

        //-- 根据ID检索并赋值对应的名称字段
        fillNameForAdd(concludeE);

        /*if (StringUtils.isBlank(form.getContractNo())) {
            //-- 客户未输入合同编号后，自动生成合同编号
//            String code = contractConcludeAppService.revContractCode(tenantId(), form.getPid(), RevTypeEnum.支出合同.getCode());
            String code = contractUniqueCodeRuleAppService.genUniqueCode(form.getIsBackDate(),concludeE.getDepartName(),ContractAreaEnum.parseName(concludeE.getIsBackDate()),form.getConmanagetype(),concludeE.getCommunityName(),2,form.getConperformprovincesname());
            concludeE.setContractNo(code);
        }*/

        //-- 校验合同编码唯一性
        // judgeContractNoOnly(concludeE);

//        if(LocalDate.now().isBefore(form.getGmtExpireStart())){
//            concludeE.setStatus( ContractRevStatusEnum.尚未履行.getCode());
//        }else{
//            concludeE.setStatus( ContractRevStatusEnum.正在履行.getCode());
//        }
        //修改接口生成新合同，合同状态同样是"未生效"
        concludeE.setStatus(ContractRevStatusEnum.未生效.getCode());

        concludeE.setIsBackReview(1);
        //-- 审批状态 - 待提交
        concludeE.setReviewStatus(ReviewStatusEnum.待提交.getCode());
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
        contractPayConcludeMapper.insert(concludeE);
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
            if(form.getIsEditWps() != null && form.getIsEditWps()){
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
        form.getContractPayFundVList().stream().forEach( s-> {
            s.setContractId(concludeE.getId());
        });
        //修改接口中，若清单全部是新增的，那么需要进行清单金额的校验
        Boolean isCheckFundAmount = false;
        List<ContractPayFundUpdateF> notAddList = form.getContractPayFundVList().stream()
                .filter(item -> !StringUtils.equals("add", item.getActionCode()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(notAddList)) {
            isCheckFundAmount = true;
        }
        contractPayFundService.dealBatchModify(form.getContractPayFundVList(), isCheckFundAmount);
        List<AttachmentE> attachmentEList = attachmentService.listById(form.getPid());
        if(ObjectUtils.isNotEmpty(attachmentEList)){
            AttachmentE attachmentF = new AttachmentE();
            BeanUtils.copyProperties(attachmentEList.get(0),attachmentF);
            attachmentF.setId(null);
            attachmentF.setBusinessId(concludeE.getId());
            attachmentService.save(attachmentF);
        }
        //-- 处理合同编号 V2 修改合同和父合同编号保持一致 20241126
        ContractPayConcludeE parentConcludeE =  contractPayConcludeService.getById(form.getPid());
        if (Objects.isNull(parentConcludeE)){
            throw new OwlBizException("父合同不存在");
        }
        concludeE.setContractNo(parentConcludeE.getContractNo());
        ContractPayConcludeE contractPayConcludeE1 = new ContractPayConcludeE();
        contractPayConcludeE1.setId(concludeE.getId());
        contractPayConcludeE1.setContractNo(parentConcludeE.getContractNo());
        contractPayConcludeMapper.updateById(contractPayConcludeE1);
        return contractInfoV;
    }

    /**
     * 提交合同
     *
     * @param id 合同ID
     */
    @Transactional(rollbackFor = Exception.class)
    public String postContract(String id) {
        //-- α.校验ID正确性
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
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
        ContractNoInfoV contractNoInfoV = contractBaseService.generatePayContractNo(concludeE, null);
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
        // δ.业务相关
        contractPayConcludeService.updateById(concludeE);
            List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .eq(ContractPayFundE.CONTRACT_ID, concludeE.getId())
                    .eq(ContractPayFundE.IS_HY, 0)
                    .eq(ContractPayFundE.DELETED, 0));
            List<String> funIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(mainFunList)){
                List<Long> funList = mainFunList.stream().map(ContractPayFundE :: getChargeItemId).distinct().collect(Collectors.toList());
                List<ChargeItemRv> chargeList = financeFeignClient.getByIdList(funList);
                if(funList.size() != chargeList.size()){
                    throw new OwlBizException("该合同清单中含有不存在费项，请检查后重新提交");
                }
                for(ChargeItemRv item : chargeList){
                    //组装费项路径
                    List<Long> chargePath = JSON.parseArray(item.getPath(), Long.class);
                    chargePath.remove(chargePath.size() - 1);
                    if(chargePath.contains(166550662047102L) || chargePath.contains(1040901L)){
                        funIdList.addAll(mainFunList.stream().filter(i -> i.getChargeItemId().equals(item.getId())).map(ContractPayFundE :: getId).collect(Collectors.toList()));
                    }
                }
            }
            if(!this.getIsCorrelationCost(concludeE.getCommunityId(), concludeE.getDepartId(), concludeE.getId(), null,funIdList)){
                log.info("该合同无成本交互,直接发起审批");
                return createProcess(concludeE);
            }

            IdentityInfo identityInfo = curIdentityInfo();
            LocalDateTime now = LocalDateTime.now();
            //TODO-hhb调用成本校验
            DynamicCostIncurredReqF costReq = new DynamicCostIncurredReqF();
            costReq.setBillGuid(concludeE.getId());
            costReq.setBillCode(concludeE.getContractNo());
            costReq.setBillName(concludeE.getName());
            if(ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType()) || ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType())){
                ContractPayConcludeE concludeMainE = contractPayConcludeService.getById(concludeE.getPid());
                costReq.setBillGuid(concludeMainE.getId());
                costReq.setBillCode(concludeMainE.getContractNo());
                costReq.setBillName(concludeMainE.getName());
            }

            costReq.setBillTypeName(BillTypeEnum.CONTRACT_INIT.getName());
            costReq.setBillTypeEnum(BillTypeEnum.CONTRACT_INIT.getCode());
            if(ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType()) || ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType())){
                costReq.setBillTypeName(BillTypeEnum.CONTRACT_SIGN.getName());
                costReq.setBillTypeEnum(BillTypeEnum.CONTRACT_SIGN.getCode());
            }
            costReq.setOperationType(OperationTypeEnum.CHECK.getCode());
            costReq.setContactor(OperationTypeEnum.CHECK.getName());
            costReq.setSystemType(1);
            List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList = new ArrayList<>();
            for(ContractPayFundE fun : mainFunList.stream().filter(x->funIdList.contains(x.getId())).collect(Collectors.toList())){
                ContractPayCostPlanReqV funVo = new ContractPayCostPlanReqV();
                funVo.setCommunityId(concludeE.getCommunityId());
                funVo.setGmtExpireStart(concludeE.getGmtExpireStart());
                funVo.setGmtExpireEnd(concludeE.getGmtExpireEnd());
                funVo.setChargeItemId(fun.getChargeItemId());
                funVo.setAmountWithOutRate(fun.getAmountWithOutRate());
                funVo.setAccountItemFullCode(fun.getAccountItemFullCode());
                if(ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType())){
                    funVo.setContractMainId(concludeE.getPid());
                    funVo.setTypeId(fun.getTypeId());
                    funVo.setPayWayId(fun.getPayWayId());
                    funVo.setTaxRateId(fun.getTaxRateId());
                    funVo.setPayTypeId(fun.getPayTypeId());
                    funVo.setStandardId(fun.getStandardId());
                    funVo.setStandAmount(fun.getStandAmount());
                }

                ContractPayCostApportionV funCostData = this.getCostApportionDetail(funVo);
                if(Objects.isNull(funCostData)){
                    continue;
                }
                costReq.setBusinessUnitCode(funCostData.getBusinessUnitCode());
                costReq.setBuGuid(funCostData.getBuGuid());
                costReq.setProjectGuid(funCostData.getProjectGuid());
                costReq.setBusinessGuid(funCostData.getBusinessGuid());

                Map<String, List<ContractPayCostApportionDetailV>> apportionMap = funCostData.getApportionMap();
                ContractPayCostApportionE costApportionE = new ContractPayCostApportionE();
                BeanUtils.copyProperties(funCostData, costApportionE);
                BeanUtils.copyProperties(costReq, costApportionE);
                costApportionE.setContractId(concludeE.getId());
                costApportionE.setPid("0");
                costApportionE.setApportionAmount(fun.getAmountWithOutRate());
                costApportionE.setTenantId(identityInfo.getTenantId());
                costApportionE.setCreator(identityInfo.getUserId());
                costApportionE.setCreatorName(identityInfo.getUserName());
                costApportionE.setGmtCreate(now);
                costApportionE.setOperator(identityInfo.getUserId());
                costApportionE.setOperatorName(identityInfo.getUserName());
                costApportionE.setGmtModify(now);
                contractPayCostApportionMapper.insert(costApportionE);
                fun.setCbApportionId(costApportionE.getId());
                fun.setAccountItemCode(funCostData.getAccountItemCode());
                fun.setAccountItemName(funCostData.getAccountItemName());
                fun.setAccountItemFullCode(funCostData.getAccountItemFullCode());
                fun.setAccountItemFullName(funCostData.getAccountItemFullName());
                fun.setSummarySurplusAmount(funCostData.getSummarySurplusAmount());
                List<String> yearList = IntStream.rangeClosed(concludeE.getGmtExpireStart().getYear(), concludeE.getGmtExpireEnd().getYear())
                        .mapToObj(String::valueOf)
                        .collect(Collectors.toList());
                for(String year : yearList){
                    ContractPayCostApportionDetailV useCost = apportionMap.get(year).stream().filter(
                            x->CostApportionTypeEnum.可用金额.getCode().equals(x.getApportionType())).findFirst() // 返回 Optional
                            .orElse(new ContractPayCostApportionDetailV());
                    ContractPayCostApportionDetailV ftCost = apportionMap.get(year).stream().filter(
                            x->CostApportionTypeEnum.分摊金额.getCode().equals(x.getApportionType())).findFirst() // 返回 Optional
                            .orElse(new ContractPayCostApportionDetailV());
                    DynamicCostIncurredReqF.IncurredDataListDTO incurredData = new DynamicCostIncurredReqF.IncurredDataListDTO();
                    BeanUtils.copyProperties(ftCost, incurredData);
                    incurredData.setAccountItemFullCode(funCostData.getAccountItemFullCode());
                    incurredData.setDynamicCostGuid(useCost.getDynamicCostGuid());
                    incurredData.setShareYear(year);
                    incurredData.setYearShareAmount(ftCost.getYearSurplus());
                    incurredData.setJanShareAmount(ftCost.getJanSurplus());
                    incurredData.setFebShareAmount(ftCost.getFebSurplus());
                    incurredData.setMarShareAmount(ftCost.getMarSurplus());
                    incurredData.setAprShareAmount(ftCost.getAprSurplus());
                    incurredData.setMayShareAmount(ftCost.getMaySurplus());
                    incurredData.setJunShareAmount(ftCost.getJunSurplus());
                    incurredData.setJulShareAmount(ftCost.getJulSurplus());
                    incurredData.setAugShareAmount(ftCost.getAugSurplus());
                    incurredData.setSepShareAmount(ftCost.getSepSurplus());
                    incurredData.setOctShareAmount(ftCost.getOctSurplus());
                    incurredData.setNovShareAmount(ftCost.getNovSurplus());
                    incurredData.setDecShareAmount(ftCost.getDecSurplus());
                    incurredDataList.add(incurredData);

                    List<ContractPayCostApportionDetailV> apportionList = apportionMap.get(year);
                    for(ContractPayCostApportionDetailV copyCost : apportionList){
                        ContractPayCostApportionE costApportionPidE = new ContractPayCostApportionE();
                        BeanUtils.copyProperties(copyCost, costApportionPidE);
                        if(CostApportionTypeEnum.分摊金额.getCode().equals(copyCost.getApportionType()) && Objects.nonNull(useCost)){
                            costApportionPidE.setDynamicCostGuid(useCost.getDynamicCostGuid());
                        }
                        costApportionPidE.setContractId(concludeE.getId());
                        costApportionPidE.setPid(costApportionE.getId());
                        costApportionPidE.setTenantId(identityInfo.getTenantId());
                        costApportionPidE.setCreator(identityInfo.getUserId());
                        costApportionPidE.setCreatorName(identityInfo.getUserName());
                        costApportionPidE.setGmtCreate(now);
                        costApportionPidE.setOperator(identityInfo.getUserId());
                        costApportionPidE.setOperatorName(identityInfo.getUserName());
                        costApportionPidE.setGmtModify(now);
                        contractPayCostApportionMapper.insert(costApportionPidE);
                    }
                }
            }
            if(incurredDataList.size()>0){
                costReq.setIncurredAmount(incurredDataList.stream()
                        .map(DynamicCostIncurredReqF.IncurredDataListDTO::getYearShareAmount)
                        .filter(Objects::nonNull)  // 过滤掉null值
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredMeargeDataList = this.mergeIncurredData(incurredDataList);
                costReq.setIncurredDataList(incurredMeargeDataList);
                log.info("支出合同发起审批，成本数据校验入参：{}", JSONArray.toJSON(costReq));
                CostBaseResponse<List<DynamicCostIncurredRespF>> jiaoyanData = externalFeignClient.getDynamicCostIncurred(costReq);
                log.info("支出合同发起审批，成本数据校验出参：{}", JSONArray.toJSON(jiaoyanData));
                if(!jiaoyanData.getSuccess()){
                    if(CollectionUtils.isNotEmpty(jiaoyanData.getData())){
                        List<String> errMsgList = jiaoyanData.getData().stream().map(DynamicCostIncurredRespF::getErrorMessage).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(errMsgList)){
                            String errMsg = errMsgList.stream()
                                    .filter(msg -> msg != null && !msg.trim().isEmpty()) // 过滤空值
                                    .distinct()
                                    .collect(Collectors.joining(", "));
                            throw new OwlBizException("该合同成本占用校验失败："+ errMsg);
                        }
                    }
                    throw new OwlBizException("该合同成本占用校验失败："+ jiaoyanData.getMessage());
                }
            }
            String processDetail;
            try {
                processDetail= createProcess(concludeE);
            } catch (Exception e){
                // 回退合同编号的序号
                contractBaseService.callBackRedisCounter(contractNoInfoV);
                log.info("支出合同流程发起异常：{}",e);
                log.info("支出合同流程发起异常：{}",e.getMessage());
                throw new OwlBizException("OA流程发起超时，请稍后重试！");
            }
            if(incurredDataList.size()>0) {
                //TODO-hhb调用成本占用
                costReq.setContactor(OperationTypeEnum.OCCUPY.getName());
                costReq.setOperationType(OperationTypeEnum.OCCUPY.getCode());
                log.info("支出合同发起审批，成本数据占用入参：{}", JSONArray.toJSON(costReq));
                CostBaseResponse<List<DynamicCostIncurredRespF>> zhanyongData = externalFeignClient.getDynamicCostIncurred(costReq);
                log.info("支出合同发起审批，成本数据占用出参：{}", JSONArray.toJSON(zhanyongData));
                //TODO 成本问题，临时注释
                if (!zhanyongData.getSuccess()) {
                    throw new OwlBizException("该合同成本占用失败：" + zhanyongData.getMessage());
                }
                contractPayFundService.updateBatchById(mainFunList);
            }
            return processDetail;

    }

    public List<DynamicCostIncurredReqF.IncurredDataListDTO> mergeIncurredData(List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList) {
        return incurredDataList.stream()
                .collect(Collectors.groupingBy(
                        dto -> Arrays.asList(dto.getAccountItemFullCode(), dto.getDynamicCostGuid(), dto.getShareYear()),
                        Collectors.collectingAndThen(Collectors.toList(), this::mergeDTOs)
                ))
                .values().stream()
                .collect(Collectors.toList());
    }

    private DynamicCostIncurredReqF.IncurredDataListDTO mergeDTOs(List<DynamicCostIncurredReqF.IncurredDataListDTO> dtos) {
        if (dtos.isEmpty()) {
            return null;
        }

        DynamicCostIncurredReqF.IncurredDataListDTO result = new DynamicCostIncurredReqF.IncurredDataListDTO();
        DynamicCostIncurredReqF.IncurredDataListDTO first = dtos.get(0);

        // 设置关键字段
        result.setAccountItemFullCode(first.getAccountItemFullCode());
        result.setDynamicCostGuid(first.getDynamicCostGuid());
        result.setShareYear(first.getShareYear());

        // 合并金额
        result.setYearShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getYearShareAmount));
        result.setJanShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getJanShareAmount));
        result.setFebShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getFebShareAmount));
        result.setMarShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getMarShareAmount));
        result.setAprShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getAprShareAmount));
        result.setMayShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getMayShareAmount));
        result.setJunShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getJunShareAmount));
        result.setJulShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getJulShareAmount));
        result.setAugShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getAugShareAmount));
        result.setSepShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getSepShareAmount));
        result.setOctShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getOctShareAmount));
        result.setNovShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getNovShareAmount));
        result.setDecShareAmount(sumAmounts(dtos, DynamicCostIncurredReqF.IncurredDataListDTO::getDecShareAmount));

        return result;
    }

    private BigDecimal sumAmounts(List<DynamicCostIncurredReqF.IncurredDataListDTO> dtos, Function<DynamicCostIncurredReqF.IncurredDataListDTO, BigDecimal> amountExtractor) {
        return dtos.stream()
                .map(amountExtractor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String createProcess(ContractPayConcludeE concludeE){
        LambdaQueryWrapper<ContractProcessRecordE> queryWrappers = new LambdaQueryWrapper<>();
        queryWrappers.eq(ContractProcessRecordE::getContractId, concludeE.getId())
                .eq(ContractProcessRecordE::getType, ContractConcludeEnum.PAY.getCode())
                .eq(ContractProcessRecordE::getDeleted, 0);
        if(concludeE.getIsBackReview() != null && concludeE.getIsBackReview() == 1){
            queryWrappers.eq(ContractProcessRecordE::getSubType, 2);
        }else{
            queryWrappers.eq(ContractProcessRecordE::getSubType, 1);
        }
        ContractProcessRecordE sk = contractProcessRecordMapper.selectOne(queryWrappers);
        if (ObjectUtils.isNotEmpty(sk) && StringUtils.isNotBlank(sk.getProcessId())) {
            BusinessInfoF businessInfoF = buildBusinessInfoF(concludeE);
            businessInfoF.setProcessId(sk.getProcessId());
            log.info("支出合同更新审批表单数据:{}", JSON.toJSONString(businessInfoF));
            //响应结构保持不变
            ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
            if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                log.info("支出合同流程更新失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
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
        log.info("支出合同新增审批表单数据:{}", JSON.toJSONString(businessInfoF));
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
        // 若创建失败数据不入库
        if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
            log.info("支出合同流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
            reviewStatusCode = ReviewStatusEnum.已拒绝.getCode();
//            throw new OwlBizException("中建返回数据时,根据合同ID检索数据失败,流程创建失败");
        }

        // ζ.构造入库数据,能存的都存下来
        String requestid = processCreateV.getET_RESULT().getRequestid();
        ContractProcessRecordE contractProcessRecordE = Builder.of(ContractProcessRecordE::new)
                .with(ContractProcessRecordE::setProcessId, requestid) // 流程请求id
                .with(ContractProcessRecordE::setContractId, concludeE.getId()) // 合同ID
                .with(ContractProcessRecordE::setReviewStatus, reviewStatusCode) // 审核状态
                .with(ContractProcessRecordE::setTenantId, concludeE.getTenantId()) // 租户ID
                .with(ContractProcessRecordE::setCreator, concludeE.getCreator()) // 创建人
                .with(ContractProcessRecordE::setCreatorName, concludeE.getCreatorName()) // 创建人名称
                .with(ContractProcessRecordE::setType, ContractConcludeEnum.PAY.getCode()) // 类型（1合同订立支出2合同订立收入）
                .build();
        // η.非并发接口,为保证幂等性,无对应记录再插入数据 CHECK 代码逻辑不适用并发环境;暂不做redis缓存
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(ContractProcessRecordE::getProcessId, requestid).eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(recordE)) {
            contractProcessRecordMapper.updateById(contractProcessRecordE);
            log.info("返回的支出流程已存在,已更新数据库记录");
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
        log.info("返回的支出流程不存在,已插入数据库记录");

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

    private BusinessInfoF buildBusinessInfoF(ContractPayConcludeE concludeE){
        BusinessInfoF businessInfoF = new BusinessInfoF();
        //所属区域
        businessInfoF.setSsqy(convertRegion(concludeE));
        businessInfoF.setFormDataId(concludeE.getId());
        businessInfoF.setEditFlag(0);
        businessInfoF.setFormType(ContractConcludeEnum.PAY.getCode());
        if(concludeE.getIsBackReview() != null && concludeE.getIsBackReview() == 1){
            businessInfoF.setFlowType(bizCode);
        }else{
            businessInfoF.setFlowType(concludeE.getBizCode());
        }
        //合同总金额（含税）
        businessInfoF.setHtzjehs(concludeE.getContractAmountOriginalRate());
        //收支类型
        if(concludeE.getNoPaySign() != null && concludeE.getNoPaySign() == 1){
            businessInfoF.setSzlx(2);
            //合同总金额（含税）
            businessInfoF.setHtzjehs(null);
        }else{
            businessInfoF.setSzlx(0);
        }
        businessInfoF.setContractName(concludeE.getName());
        //合同类型
        if(StringUtils.isNotEmpty(concludeE.getBizCode())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.合同类型审批.getCode(), concludeE.getBizCode());
            if (CollectionUtils.isNotEmpty(value)) {
                businessInfoF.setHtywlx(Integer.parseInt(value.get(0).getName()));
            }
        }

        ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
        concludeExpandF.setContractId(concludeE.getId());
        ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
        //成本合同类别
        if(ObjectUtils.isNotEmpty(concludeExpandV) && StringUtils.isNotEmpty(concludeExpandV.getConmanagetype())){
            businessInfoF.setCbhtlb(Integer.parseInt(concludeExpandV.getConmanagetype()));
        }
        //支出合同增值类型
        if(ObjectUtils.isNotEmpty(concludeExpandV) && StringUtils.isNotEmpty(concludeExpandV.getConincrementype())){
            businessInfoF.setZzhtlx1(Integer.parseInt(concludeExpandV.getConincrementype()));
        }
        //是否满足集团利润刻度
        if(ObjectUtils.isNotEmpty(concludeExpandV) && concludeExpandV.getFmzjtzskd() != null){
            businessInfoF.setSfmzjtlrkdz(concludeExpandV.getFmzjtzskd());
        }
        if(ObjectUtils.isNotEmpty(concludeExpandV) && StringUtils.isNotEmpty(concludeExpandV.getFwlb())){
            businessInfoF.setFwlb(Integer.parseInt(concludeExpandV.getFwlb()));
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
        //是否补充协议
        businessInfoF.setSfbcxy(ContractTypeEnum.补充协议.getCode().equals(concludeE.getContractType()) ? 0 : 1);
        if(ContractBusinessLineEnum.物管.getCode().equals(concludeE.getContractBusinessLine())){
            businessInfoF.setGlzz(1);
            if(StringUtils.equals("总部", concludeE.getRegion())){
                businessInfoF.setGlzz(0);
            }
        }else if (ContractBusinessLineEnum.建管.getCode().equals(concludeE.getContractBusinessLine())){
            businessInfoF.setGlzz(3);
        }else{
            businessInfoF.setGlzz(2);
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
    private Integer convertRegion(ContractPayConcludeE concludeE) {
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
    public void judgeContractNoOnly(ContractPayConcludeE concludeE) {

    }

    /**
     * 提交前对合同数据进行校验
     *
     * @param concludeE
     * @return
     */
    public Boolean checkBeforePost(ContractPayConcludeE concludeE) {
        if (StringUtils.isBlank(concludeE.getOppositeOneId())) {
            throw new OwlBizException("对方单位1-为空，不可提交");
        }

        //-- 对方单位为供应商
        SupplierRv one = orgFeignClient.getSupplierVById(concludeE.getOppositeOneId());

        if (!one.getDisabled().equals(0)) {
            throw new OwlBizException("对方单位1 数据已被禁用，不可提交");
        }

        if (StringUtils.isNotBlank(concludeE.getOppositeTwoId())) {
            SupplierRv two = orgFeignClient.getSupplierVById(concludeE.getOppositeTwoId());

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
     *
     * @param concludeE 合同参数
     */
    public void checkValueBeforePost(ContractPayConcludeE concludeE) {
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

        List<ContractRecordInfoE> lists = contractRecordInfoService.list(new QueryWrapper<ContractRecordInfoE>()
                .eq(ContractRecordInfoE.TENANT_ID, tenantId())
                .eq(ContractRecordInfoE.CONTRACT_ID, concludeE.getId()));
        if(ObjectUtils.isEmpty(lists) ||  lists.size() != 1){
            throw new OwlBizException("合同范本文件只能一个，请检查后再提交");
        }
    }

    public ContractPayConcludeDetailV detail(ContractDetailV vo) {
        String id = vo.getId();
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("该合同数据不存在");
        }
        ContractPayConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractPayConcludeDetailV.class);
        detailV.setServeType(ServeTypeEnum.convert(concludeE.getContractServeType()));

        //-- 拓展字段回显处理
        ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV,concludeExpandV);

        //-- 字段赋值
        detailV.setContractNatureName(VirtuallyTypeEnum.parseName(detailV.getContractNature()))//-- 合同性质 1虚拟合同 0非虚拟合同
                .setContractTypeName(ContractTypeEnum.parseName(detailV.getContractType()))//-- 合同属性 0普通合同 1框架合同 2补充协议
                .setSigningMethodName(SigningMethodEnum.parseName(detailV.getSigningMethod()));//-- 签约方式 0 新签 1 补充协议 2 续签
        detailV.setDetailTableId(detailV.getId());//-- 合同详情的TABLE页面需要用到这个字段，与合同ID同值，用于分页查询关联数据

        //-- 金额字段处理
        if (Objects.nonNull(detailV.getContractAmountOriginalRate())) {
            detailV.setUnPayAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getPayAmount()));
            detailV.setUnInvoiceAmount(detailV.getContractAmountOriginalRate().subtract(detailV.getInvoiceAmount()));
        }

        //主合同金额处理
        if(StringUtils.isNotBlank(detailV.getPid()) && !detailV.getPid().equals("0")){
            ContractPayConcludeE mainConcludeE = contractPayConcludeService.getById(detailV.getPid());
            detailV.setPidName(mainConcludeE.getName());
            detailV.setMainContractAmount(mainConcludeE.getContractAmountOriginalRate());
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

        //-- 费项&税率信息数据列填充
        fillChargeAndTaxRateList(detailV,vo.getIsBc());

        //新增补充合同，重新获取合约规划信息
        if(vo.getIsBc()){
            //新增补充合同，重新获取合约规划信息
            if(CollectionUtils.isNotEmpty(detailV.getContractPayFundVList())){
                detailV.getContractPayFundVList().forEach(f -> {
                    if(StringUtils.isNotEmpty(f.getCbApportionId())){
                        //获取合约规划信息
                        ContractPayCostPlanReqV reqVo = new ContractPayCostPlanReqV();
                        reqVo.setCommunityId(detailV.getCommunityId());
                        reqVo.setDepartId(detailV.getDepartId());
                        reqVo.setChargeItemId(f.getChargeItemId());
                        reqVo.setGmtExpireStart(detailV.getGmtExpireStart());
                        reqVo.setGmtExpireEnd(detailV.getGmtExpireEnd());
                        reqVo.setContractId(detailV.getContractId());
                        reqVo.setAccountItemFullCode(f.getAccountItemFullCode());
                        ContractPayCostPlanV costPlan = this.getCostPlanByChargeItemId(reqVo);
                        if(Objects.nonNull(costPlan)){
                            f.setAccountItemCode(costPlan.getAccountItemCode());
                            f.setAccountItemName(costPlan.getAccountItemName());
                            f.setAccountItemFullCode(costPlan.getAccountItemFullCode());
                            f.setAccountItemFullName(costPlan.getAccountItemFullName());
                            f.setSummarySurplusAmount(costPlan.getSummarySurplusAmount());
                            f.setCbApportionId(null);
                        }

                    }
                });
            }
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

    public void fillChargeAndTaxRateList(ContractPayConcludeDetailV detailV,Boolean isBc) {
        List<ContractPayFundE> list = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, detailV.getId())
                .eq(ContractPayFundE.DELETED, 0));
        List<ContractPayFundV> sb = Global.mapperFacade.mapAsList(list,ContractPayFundV.class);
        List<String> charge = new ArrayList<>();
        List<String> taxRate = new ArrayList<>();
        BigDecimal fundAmount = BigDecimal.ZERO;
        for (ContractPayFundE fundE : list) {
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
            detailV.setSupplyContractChangeAmountPj(
                    Objects.nonNull(detailV.getMainContractBjAmount()) ? String.valueOf(detailV.getMainContractBjAmount().add(detailV.getContractAmountOriginalRate())) : detailV.getContractAmountOriginalRate().toString()
                            + "元");
        }
        if(detailV.getSigningMethod() == 1 && Objects.nonNull(detailV.getContractNature()) && detailV.getContractNature() != 2){
            detailV.setSupplyContractChangeAmountPj(detailV.getMainContractBjAmount().toString() + "元");
        }
        if(isBc){
            sb.forEach(x->x.setIsMain(1));
        }
        if(CollectionUtils.isNotEmpty(sb)){
            Map<String, List<ContractPayPlanConcludeE>> funCountMap = new HashMap<>();
            LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ContractPayPlanConcludeE::getContractId, detailV.getId())
                    .ne(ContractPayPlanConcludeE::getPid,"0")
                    .eq(ContractPayPlanConcludeE::getDeleted,0);
            List<ContractPayPlanConcludeE> concludeEList = contractPayPlanConcludeMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(concludeEList)){
                funCountMap = concludeEList.stream().collect(Collectors.groupingBy(ContractPayPlanConcludeE::getContractPayFundId));
            }
            Map<String, List<ContractPayPlanConcludeE>> finalFunCountMap = funCountMap;
            sb.forEach(x->{
                x.setIsLock(CollectionUtils.isEmpty(finalFunCountMap.get(x.getId())) ? Boolean.FALSE : Boolean.TRUE);
            });
        }
        detailV.setContractPayFundVList(sb);
    }


    //-- 新增支出合同订立信息表 根据ID检索并赋值对应的名称字段
    public void fillNameForEdit(ContractPayConcludeE concludeE, ContractPayConcludeE old) {

        //-- 合同分类
        if (Objects.nonNull(concludeE.getCategoryId()) && !concludeE.getCategoryId().equals(old.getCategoryId())) {
            Optional.ofNullable(contractCategoryAppService.queryById(concludeE.getCategoryId())).ifPresentOrElse(v -> {
                concludeE.setCategoryName(v.getName());
            }, () -> {
                throw new OwlBizException("根据合同分类ID检索分类数据失败");
            });
        }
        //-- 对方单位1
        if (StringUtils.isNotBlank(concludeE.getOppositeOneId()) && !concludeE.getOppositeOneId().equals(old.getOppositeOneId())) {
            Optional.ofNullable(orgFeignClient.getSupplierVById(concludeE.getOppositeOneId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位1数据已被禁用，请重新选择");
                }
                concludeE.setOppositeOne(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位1ID检索供应商数据失败");});
        }
        //-- 对方单位2
        if (StringUtils.isNotBlank(concludeE.getOppositeTwoId()) && !concludeE.getOppositeTwoId().equals(old.getOppositeTwoId())) {
            Optional.ofNullable(orgFeignClient.getSupplierVById(concludeE.getOppositeTwoId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位2-供应商数据已被禁用，请重新选择");
                }
                concludeE.setOppositeTwo(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位2ID检索供应商数据失败");});
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
            }, () -> {
                throw new OwlBizException("根据负责人ID检索名称失败");
            });
        }
        //-- 币种
        if (StringUtils.isNotBlank(concludeE.getCurrencyCode())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.币种.getCode(), concludeE.getCurrencyCode());
            if (CollectionUtils.isNotEmpty(value)) {
                concludeE.setCurrency(value.get(0).getName());
            }
        }

    }

    //校验补充合同金额和应付应收金额
    public void checkContractData(ContractPayConcludeE concludeE, List<ContractZffxxF> fkdwxx, List<ContractSrfxxF> skdwxx, boolean isFlag){
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
            ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(concludeE.getPid());
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
                QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(ContractPayConcludeE.PID, concludeE.getPid())
                        .eq(ContractPayConcludeE.DELETED,0);
                List<ContractPayConcludeE> contractPayConcludeEList = contractPayConcludeMapper.selectList(queryWrapper);
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

    //-- 新增支出合同订立信息表 根据ID检索并赋值对应的名称字段
    public void fillNameForAdd(ContractPayConcludeE concludeE) {

        //-- 合同分类
        if (Objects.nonNull(concludeE.getCategoryId())) {
            Optional.ofNullable(contractCategoryAppService.queryById(concludeE.getCategoryId())).ifPresentOrElse(v -> {
                concludeE.setCategoryName(v.getName());
            }, () -> {
                throw new OwlBizException("根据合同分类ID检索分类数据失败");
            });
        }
        //-- 对方单位1
        if (StringUtils.isNotBlank(concludeE.getOppositeOneId())) {
            Optional.ofNullable(orgFeignClient.getSupplierVById(concludeE.getOppositeOneId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位1-供应商数据已被禁用，请重新选择");
                }
                concludeE.setOppositeOne(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位1ID检索供应商数据失败");});
        }
        //-- 对方单位2
        if (StringUtils.isNotBlank(concludeE.getOppositeTwoId())) {
            Optional.ofNullable(orgFeignClient.getSupplierVById(concludeE.getOppositeTwoId())).ifPresentOrElse(v -> {
                if (!v.getDisabled().equals(0)) {
                    throw new OwlBizException("对方单位2-供应商数据已被禁用，请重新选择");
                }
                concludeE.setOppositeTwo(v.getName());
            }, () -> {throw new OwlBizException("根据对方单位2ID检索供应商数据失败");});
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

    /**
     * 下拉选择主合同
     * @param request
     * @return
     */
    public PageV<ContractPayConcludeV> getMainContractPay(PageF<SearchF<ContractPayConcludeE>> request) {
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();

        SearchF<ContractPayConcludeE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId())
                .orderByDesc(ContractPayConcludeE.GMT_CREATE);

        queryWrapper.in(ContractPayConcludeE.REVIEW_STATUS, 2);

        queryWrapper.eq(ContractPayConcludeE.SIGNING_METHOD,0);
        //这里已经限制了推送成功，等价于查询有CT码的合同
        queryWrapper.eq(ContractPayConcludeE.CONTRACT_NATURE,1);
        //增加条件：只查询普通合同
        queryWrapper.eq(ContractPayConcludeE.CONTRACT_TYPE,ContractTypeEnum.普通合同.getCode());
        //过滤终止合同
        queryWrapper.ne(ContractPayConcludeE.STATUS, ContractRevStatusEnum.合同终止.getCode());
        Page<ContractPayConcludeE> page = contractPayConcludeMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            LambdaQueryWrapper<ContractPaySettlementConcludeE> querySettWrapper = new LambdaQueryWrapper<>();
            querySettWrapper.in(ContractPaySettlementConcludeE::getContractId, page.getRecords().stream().map(ContractPayConcludeE::getId).collect(Collectors.toList()))
                    .ne(ContractPaySettlementConcludeE::getPid,"0")
                    .eq(ContractPaySettlementConcludeE::getSettlementType, SettlementTypeEnum.FINAL.getCode())
                    .eq(ContractPaySettlementConcludeE::getDeleted,0);
            List<ContractPaySettlementConcludeE> concludeSettList = contractPaySettlementConcludeMapper.selectList(querySettWrapper);
            List<String> finalSettleContractList = new ArrayList<>();
            finalSettleContractList.add("最终结算");
            concludeSettList.stream().forEach( s -> {
                finalSettleContractList.add(s.getContractId());
            });
            List<ContractPayConcludeV> resultList = Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeV.class);
            resultList.stream().forEach( s -> {
                s.setIsHaveFinalSettlement(finalSettleContractList.contains(s.getId()));
            });
            return PageV.of(request, page.getTotal(), resultList);
        }
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeV.class));
    }

    @Transactional
    public void endContract(ContractPayConcludeF form) {
        if (StringUtils.isBlank(form.getId())) {
            throw new OwlBizException("合同ID不可为空");
        }
        if (Objects.isNull(form.getEndDate())) {
            throw new OwlBizException("终止日期不可为空");
        }
        //-- 校验ID正确性
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(form.getId());
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }

        //-- 终止合同
        concludeE.setStatus(ContractRevStatusEnum.合同停用.getCode())
                .setEndDate(form.getEndDate());

        contractPayConcludeService.updateById(concludeE);

        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.终止.getCode());

        orgRelationService.mutualForPay(concludeE);

        ContractBasePullV s = noUsedContract(form.getId());
        if(s.getStatus() == 0){
            throw new OwlBizException(s.getMessage());
        }
    }

    /**
     * 处理合同的范本数据
     *
     * @param concludeE     合同
     * @param contractFjxxFList 范本文件的FileVo
     */
    public Boolean dealContractTemp(ContractPayConcludeE concludeE, List<ContractFjxxF> contractFjxxFList) {
        if (ObjectUtils.isEmpty(contractFjxxFList)) {
            if(!StringUtils.isNotBlank(concludeE.getTempId())){
                concludeE.setTempId("15497043922911");
                contractPayConcludeMapper.updateById(concludeE);
            }
            return false;
        }

        LambdaQueryWrapper<ContractRecordInfoE> payQueryWrapper = new LambdaQueryWrapper<>();
        payQueryWrapper.eq(ContractRecordInfoE::getContractId, concludeE.getId()).eq(ContractRecordInfoE::getDeleted, 0);
        ContractRecordInfoE contractRecordInfoE = contractRecordInfoMapper.selectOne(payQueryWrapper);
        //contractRecordInfoE不为空且模板id相同，说明合同正文没有更新，直接返回
        if (ObjectUtils.isNotEmpty(contractRecordInfoE) && StringUtils.isNotEmpty(contractRecordInfoE.getTemplateId())
                && contractRecordInfoE.getTemplateId().equals(contractFjxxFList.get(0).getFileId())) {
            return true;
        }
        //否则，说明更新了合同正文，走下面的逻辑
        if (ObjectUtils.isNotEmpty(contractRecordInfoE)) {
            contractRecordInfoMapper.deleteById(contractRecordInfoE);
        }
        contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),contractFjxxFList.get(0).getFileId(),contractFjxxFList.get(0).getFileKey(),null,contractFjxxFList.get(0).getName(),Integer.parseInt(contractFjxxFList.get(0).getSize()));
        concludeE.setTempId(contractFjxxFList.get(0).getFileId());
        contractPayConcludeMapper.updateById(concludeE);

//        if(ObjectUtils.isEmpty(contractRecordInfoE)){
//            contractRecordInfoService.insertOneRecord(concludeE.getId(),concludeE.getName(),contractFjxxFList.get(0).getFileId(),contractFjxxFList.get(0).getFileKey(),null,contractFjxxFList.get(0).getName(),Integer.parseInt(contractFjxxFList.get(0).getSize()));
//            concludeE.setTempId(contractFjxxFList.get(0).getFileId());
//            contractPayConcludeMapper.updateById(concludeE);
//        }else{
//            if (ObjectUtils.isNotEmpty(contractFjxxFList)) {
//                contractRecordInfoE.setTemplateId(contractFjxxFList.get(0).getFileId());
//                contractRecordInfoE.setTemplateName(contractFjxxFList.get(0).getFileKey());
//                contractRecordInfoE.setFileName(contractFjxxFList.get(0).getName());
//                contractRecordInfoE.setFileSize(Integer.parseInt(contractFjxxFList.get(0).getSize()));
//                contractRecordInfoService.updateById(contractRecordInfoE);
//                concludeE.setTempId(contractFjxxFList.get(0).getFileId());
//                contractPayConcludeMapper.updateById(concludeE);
//            }
//        }

        return true;
    }


    /**
     * 处理按钮展示
     *
     * @param map 入参
     */
    public Boolean dealBtnShowForDetail(ContractPayConcludeDetailV map, ContractPayConcludeE concludeE) {

        return true;
    }

    private void checkBlackList(ContractPayConcludeE concludeE) {
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
                .with(BlackListInfoF::setStepCode, "2") // *环节编码 目前对接合同只有新签才会用到 2023年8月11日
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
    public List<ContractPayConcludeV> getContractForBond(BondRelationF form) {

        List<Integer> status = new ArrayList<>();
        status.add(ContractRevStatusEnum.尚未履行.getCode());
        status.add(ContractRevStatusEnum.正在履行.getCode());

        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId())
                .in(ContractPayConcludeE.STATUS, status)
                .and(i -> i.eq(ContractPayConcludeE.OPPOSITE_ONE_ID, form.getId())
                        .or(m -> m.eq(ContractPayConcludeE.OPPOSITE_TWO_ID, form.getId())));
        if (StringUtils.isNotBlank(form.getName())) {
            queryWrapper.like(ContractPayConcludeE.NAME, form.getName());
        }
        List<ContractPayConcludeE> list = contractPayConcludeService.list(queryWrapper);

        return Global.mapperFacade.mapAsList(list, ContractPayConcludeV.class);
    }

    /**
     * 查询变更记录
     * @param id 合同ID
     * @return 变更记录
     */
    public RelationRecordDetailV changeRecord(String id) {

        List<ContractRelationRecordE> list = contractRelationRecordService.list(new QueryWrapper<ContractRelationRecordE>()
                .eq(ContractRelationRecordE.CONTRACT_TYPE, RevTypeEnum.支出合同.getCode())
                .eq(ContractRelationRecordE.ADD_ID, id)
                .eq(ContractRelationRecordE.TYPE, ActionTypeEnum.变更.getCode())
                .orderByDesc(ContractRelationRecordE.GMT_CREATE));

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        RelationRecordDetailV map = Global.mapperFacade.map(list.get(0), RelationRecordDetailV.class);

        Optional.ofNullable(contractPayConcludeService.getById(map.getAddId())).ifPresent(v -> {
            map.setAddName(v.getName())
                    .setAddContractNo(v.getContractNo());
        });

        Optional.ofNullable(contractPayConcludeService.getById(map.getOldId())).ifPresent(v -> {
            map.setOldName(v.getName())
                    .setOldContractNo(v.getContractNo());
        });

        return map;
    }

    /**
     * 反审
     * @param id 合同ID
     */
    public Boolean backReview(String id) {
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("检索合同数据失败");
        }

        return true;
    }


    /**
     * 获取合同页面金额数量展示数据
     * @param request
     * @return
     */
    public ContractNumShow getPageShowNum(PageF<SearchF<ContractPayConcludeQuery>> request) {
        QueryWrapper<ContractPayConcludeQuery> queryWrapper = new QueryWrapper<>();
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
        SearchF<ContractPayConcludeQuery> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId())
                .eq("cc." + ContractPayConcludeE.DELETED,0);;

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

        // List<ContractPayConcludeE> list = contractPayConcludeService.list(queryWrapper);
        List<ContractPayConcludeE> list = contractPayConcludeMapper.getPageShowNumV2(queryWrapper);

        ContractNumShow result = new ContractNumShow()
                .setContractAmount(BigDecimal.ZERO)
                .setPayAmount(BigDecimal.ZERO)
                .setUnPayAmount(BigDecimal.ZERO);

        for (ContractPayConcludeE concludeE : list) {
            if (Objects.nonNull(concludeE.getContractAmountOriginalRate()) && Objects.nonNull(concludeE.getPayAmount())) {
                result.setContractAmount(result.getContractAmount().add(concludeE.getContractAmountOriginalRate()))
                        .setPayAmount(concludeE.getPayAmount().add(result.getPayAmount()))
                        .setUnPayAmount(result.getUnPayAmount().add(concludeE.getContractAmountOriginalRate().subtract(concludeE.getPayAmount())));
            }
        }

        return result;
    }

    public PageV<ContractPayConcludeV> pageForSelect(PageF<ContractRevF> request) {
        ContractRevF conditions = request.getConditions();
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();

        List<Integer> status = new ArrayList<>();
        status.add(ContractRevStatusEnum.尚未履行.getCode());
        status.add(ContractRevStatusEnum.正在履行.getCode());

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeConcludeE.NAME, conditions.getName());
        }

        // 默认排序
        queryWrapper.orderByDesc(ContractPayConcludeE.GMT_CREATE)
                .eq(ContractPayConcludeE.TENANT_ID, tenantId())
                .in(ContractPayConcludeE.STATUS, status);
        Page<ContractPayConcludeE> page = contractPayConcludeMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeV.class));
    }


    /**
     * 拓展信息入库
     */
    private void dealContractExpand(String id, ContractPayAddF form) {
        ContractPayConcludeExpandSaveF data = new ContractPayConcludeExpandSaveF();
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
        contractPayConcludeExpandAppService.save(data);
    }


    /**
     * 拓展信息编辑
     */
    private void dealEditContractExpand(String id, ContractPayEditF form) {
        ContractPayConcludeExpandUpdateF data = new ContractPayConcludeExpandUpdateF();
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
        //PDF附件信息
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
        contractPayConcludeExpandAppService.updateByContractId(data);
    }

    /**
     * 合同拓展字段回显详情
     */
    private void dealContractExpandToDetailV(ContractPayConcludeDetailV detailV, ContractPayConcludeExpandV concludeExpandV) {
        if(null != detailV && null != concludeExpandV) {
            //基础字段处理
            BeanUtils.copyProperties(concludeExpandV,detailV);
            //字段翻译处理
            detailV.setConperformcountryname("中国");

            detailV.setConlanguagename("中文");

            //合同管理类别conmanagetype
            if(StringUtils.isNotEmpty(detailV.getConmanagetype())) {
                List<DictionaryCode> value = new ArrayList<>();
                if(ContractBusinessLineEnum.物管.getCode().equals(detailV.getContractBusinessLine())){
                    value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), detailV.getConmanagetype());
                }else if (ContractBusinessLineEnum.建管.getCode().equals(detailV.getContractBusinessLine())){
                    value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.建管合同管理类别.getCode(), detailV.getConmanagetype());
                }else{
                    value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.商管合同管理类别.getCode(), detailV.getConmanagetype());
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

            //服务类型fwlb
            if(StringUtils.isNotEmpty(detailV.getFwlb())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同服务类型.getCode(), detailV.getFwlb());
                if (CollectionUtils.isNotEmpty(value)) {
                    detailV.setFwlbname(value.get(0).getName());
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

                            // 收款人处理
                            if (StringUtils.isNotEmpty(srf.getPayee())) {
                                srfstr.add(srf.getPayee());
                                contractSrfxxPayeeExtendF.setName(srf.getPayee());
                                contractSrfxxPayeeExtendF.setId(srf.getPayeeid());
                                payeeName.add(contractSrfxxPayeeExtendF);
                                srf.setPayeeName(payeeName);
                            }


                            // 收款账号处理
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

                            // 实际收款账号信息回显
                            if (StringUtils.isNotEmpty(srf.getTruepayeeaccountid())) {
                                contractSrfxxTruePayAccountExtendF.setName(srf.getTruepayeeaccounname());
                                contractSrfxxTruePayAccountExtendF.setId(srf.getTruepayeeaccountid());
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
        ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
        concludeExpandF.setContractId(contractIncomeFjxxF.getConditions().getFields().get(0).getValue()+"");
        ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
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

    public void autoPullContract(){
        List<String> toPullContractIdList = contractPayConcludeMapper.queryToPullZjContractIds();
        if (CollectionUtils.isEmpty(toPullContractIdList)){
            return;
        }
        for (String id : toPullContractIdList) {
            try {
                pullContract(id);
            } catch (Exception e){
                log.error("contract:{} pullContract error:{}",id,e.getMessage());
                contractPayConcludeMapper.incrementPullZjFailCountById(id);
            }
        }
    }

    @Transactional
    public String pullContract(String id){
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
        if (Objects.nonNull(concludeE) && Objects.nonNull(concludeE.getContractNature()) && concludeE.getContractNature() == 1) {
            throw new OwlBizException("已推送成功，请点击查询按钮查看合同CT码");
        }
        LambdaQueryWrapper<AttachmentE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttachmentE::getBusinessId, id).eq(AttachmentE::getBusinessType,1002)
                .eq(AttachmentE::getDeleted, 0);
        List<AttachmentE> attachmentEList = attachmentMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(attachmentEList) && devFlag != 1){
            throw new OwlBizException("请上传合同扫描件");
        }
        //修改合同归档状态，只有非修改合同才处理归档状态
        /*if (!ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType())) {
            ContractPayConcludeE archivedConcludeE = new ContractPayConcludeE();
            archivedConcludeE.setId(id);
            archivedConcludeE.setArchived(1);
            contractPayConcludeService.updateById(archivedConcludeE);
        }*/
        //补充协议金额，同步至主合同逻辑上线时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedTime = LocalDateTime.parse("2025-09-04 21:00:00", formatter);
        if(Objects.nonNull(concludeE.getApprovalDate()) && concludeE.getApprovalDate().isAfter(parsedTime)
                && concludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())
                && (  (Objects.nonNull(concludeE.getContractNature())
                && concludeE.getContractNature() == 2)
                || Objects.isNull(concludeE.getContractNature()))){
            contractPayConcludeService.handleConcludeSupple(concludeE);
            concludeE = contractPayConcludeService.getById(id);
        }
        //保存合同推送时间
        ContractPayConcludeE pushDateE = new ContractPayConcludeE();
        pushDateE.setId(id);
        //归档状态，推送直接更改为已归档
        pushDateE.setArchived(1);
        pushDateE.setContractPushDate(LocalDateTime.now());
        contractPayConcludeService.updateById(pushDateE);

        logService.insertOneLog(concludeE.getId(), concludeE.getName(), LogActionTypeEnum.推送.getCode());
        ContractPayConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractPayConcludeDetailV.class);
        //直接从源头将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(detailV.getRemark())) {
            detailV.setRemark(detailV.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
        Integer type = 0;
        dealContractExpandToDetailV(detailV, concludeExpandV);
        if(StringUtils.isNotBlank(detailV.getConmaincode())){
            type = 1;
        }
        detailV.setIsPush(true);
        String requestBody = contractPayPullService.getJsonStr(detailV);
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
            contractPayConcludeMapper.updateById(concludeE);
            if(concludeE.getContractNature() == 1 && concludeE.getContractType() == 4){
                contractPayPullService.modifyContract(concludeE);
            }
            return "推送财务云报文："+requestBody;
        }
        return contractPayPullService.dealContractPull(requestBody, type, id);
    }

    public ContractBasePullV verifyContract(String id){
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
        ContractPayConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractPayConcludeDetailV.class);
        ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
        Integer type = 0;
        dealContractExpandToDetailV(detailV, concludeExpandV);
        if(StringUtils.isNotBlank(detailV.getConmaincode())){
            type = 1;
        }
        String requestBody = contractPayPullService.getJsonStr(detailV);
        return contractPayPullService.verifyContract(requestBody, type, id);
    }
    public ContractBasePullV noUsedContract(String id){
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
        ContractPayConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractPayConcludeDetailV.class);
        ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV, concludeExpandV);
        String requestBody = contractPayPullService.getJsonStrNoUsed(detailV);
        return contractPayPullService.noUsedContract(requestBody);
    }

    public List<ContractPayConcludeDetailV> getInfoListByIds(List<String> ids) {
        return contractPayConcludeService.getInfoListByIds(ids);
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

    /**
     * 成本合同台账mock数据
     * @return
     */
    public IPage<MockJson> mockJson(Integer pageNo, Integer pageSize) {
        Page<MockJson> pageF = Page.of(pageNo, pageSize, true);
        return contractPayConcludeMapper.mockJson(pageF);
    }

    public String getZjContractPullBody(String id) {
        ContractPayConcludeE concludeE = contractPayConcludeService.getById(id);
        ContractPayConcludeDetailV detailV = Global.mapperFacade.map(concludeE, ContractPayConcludeDetailV.class);
        //直接从源头将合同概述中的：制表符、回车、空格去掉
        if (StringUtils.isNotEmpty(detailV.getRemark())) {
            detailV.setRemark(detailV.getRemark().replaceAll("[\\t\\n\\r ]|\\u00A0", ""));
        }
        ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
        concludeExpandF.setContractId(id);
        ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
        dealContractExpandToDetailV(detailV, concludeExpandV);
        return contractPayPullService.getJsonStr(detailV);
    }

    public void updateContractInUse(LocalDate targetDate){
        contractPayConcludeMapper.updateContractInUse(targetDate);
    }

    //发起NK
    @Transactional
    public void startNK(String id){

        String userId = userId();
        String userName = userName();
        LocalDateTime dateTime = LocalDateTime.now();

        ContractPayConcludeE concludeE = contractPayConcludeMapper.selectById(id);
        if(Objects.isNull(concludeE)){
            throw new OwlBizException("合同不存在，请输入正确合同ID");
        }
        //查询结算单
        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPaySettlementConcludeE::getContractId, id)
                .notIn(ContractPaySettlementConcludeE::getPid,0)
                .eq(ContractPaySettlementConcludeE::getDeleted,0);
        List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(settlementList) &&
                CollectionUtils.isNotEmpty(settlementList.stream().filter(x->x.getReviewStatus().equals(ReviewStatusEnum.审批中.getCode())).collect(Collectors.toList()))){
            throw new OwlBizException("存在审批中结算申请时不可发起NK");
        }

        //将当前的合同ID快照为YJ
        ContractPayConcludeE concludeYJE = new ContractPayConcludeE();
        BeanUtils.copyProperties(concludeE, concludeYJE);
        concludeYJE.setDeleted(1);
        concludeYJE.setId(null);
        concludeYJE.setPid(concludeE.getId());
        concludeYJE.setApprovalDate(LocalDateTime.now());
        contractPayConcludeMapper.insert(concludeYJE);

        String concludeYjId = concludeYJE.getId();
        //拷贝清单数据
        List<ContractPayFundE> funList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, concludeE.getId())
                .eq(ContractPayFundE.DELETED, 0)
        );
        if(CollectionUtils.isNotEmpty(funList)){
            funList.forEach(x->{
                x.setId(null);
                x.setContractId(concludeYjId);
                x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);
            });
            contractPayFundService.saveBatch(funList);
        }

        //拷贝扩展表数据
        List<ContractPayConcludeExpandE> expandEList = contractPayConcludeExpandService.list(new QueryWrapper<ContractPayConcludeExpandE>()
                .orderByDesc(ContractPayConcludeExpandE.GMT_CREATE)
                .eq("contractId", concludeE.getId())
                .eq(ContractPayConcludeExpandE.DELETED, 0));
        if(CollectionUtils.isNotEmpty(expandEList)){
            expandEList.forEach(x->{
                x.setId(null);
                x.setContractId(concludeYjId);
                x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);
            });
            contractPayConcludeExpandService.saveBatch(expandEList);
        }

        //拷贝附件信息
        List<AttachmentE> attachmentEList = attachmentService.listById(concludeE.getId());
        if(ObjectUtils.isNotEmpty(attachmentEList)){
            for(AttachmentE file : attachmentEList){
                AttachmentE attachmentF = new AttachmentE();
                BeanUtils.copyProperties(file,attachmentF);
                attachmentF.setId(null);
                attachmentF.setBusinessId(concludeYjId);
                attachmentF.setCreator(userId);
                attachmentF.setCreatorName(userName);
                attachmentF.setGmtCreate(dateTime);
                attachmentF.setOperator(userId);
                attachmentF.setOperatorName(userName);
                attachmentF.setGmtModify(dateTime);
                attachmentService.save(attachmentF);
            }
        }

        //查询结算计划信息d
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
        queryPlanWrapper.eq(ContractPayPlanConcludeE::getContractId, id)
                .eq(ContractPayPlanConcludeE::getDeleted,0);
        List<ContractPayPlanConcludeE> concludePlanList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);
        concludePlanList.forEach(x->{
            x.setMainId(x.getId());
            x.setId(null);
            x.setContractId(concludeYjId);
            x.setCreator(userId);
            x.setCreatorName(userName);
            x.setGmtCreate(dateTime);
            x.setOperator(userId);
            x.setOperatorName(userName);
            x.setGmtModify(dateTime);
        });
        contractPayPlanConcludeService.saveBatch(concludePlanList);
        Map<String,String> planMap = concludePlanList.stream()
                .collect(Collectors.toMap(
                        ContractPayPlanConcludeE::getMainId,
                        ContractPayPlanConcludeE::getId,
                        (existing, replacement) -> replacement  // 冲突时用新值替换旧值
                ));
        //查询成本计划
        List<PayCostPlanE> payCostList = contractPayCostPlanService.list(Wrappers.<PayCostPlanE>lambdaQuery()
                .eq(PayCostPlanE::getContractId, id)
                .eq(PayCostPlanE::getDeleted, 0));
        if(CollectionUtils.isNotEmpty(payCostList)){
            payCostList.forEach(x->{
                x.setPlanId(planMap.get(x.getPlanId()));
                x.setId(null);
                x.setContractId(concludeYjId);
                x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);
            });
            contractPayCostPlanService.saveBatch(payCostList);
        }
        //若含有结算单，拷贝结算单对应数据
        if(CollectionUtils.isNotEmpty(settlementList)){
            settlementList.forEach(x->{
                x.setMainId(x.getId());
                x.setId(null);
                x.setContractId(concludeYjId);
                x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);
            });
            contractPaySettlementConcludeService.saveBatch(settlementList);
            Map<String,String> settlementMap = settlementList.stream()
                    .collect(Collectors.toMap(
                            ContractPaySettlementConcludeE::getMainId,
                            ContractPaySettlementConcludeE::getId,
                            (existing, replacement) -> replacement  // 冲突时用新值替换旧值
                    ));
            //查询结算明细数据
            List<ContractPaySettDetailsE> settDetailsList = contractPaySettDetailsMapper.selectList(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                    .in(ContractPaySettDetailsE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList())).eq(ContractPaySettDetailsE :: getDeleted, 0));
            if(CollectionUtils.isNotEmpty(settDetailsList)){
                settDetailsList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setId(null);
                    x.setCreator(userId);
                    x.setCreatorName(userName);
                    x.setGmtCreate(dateTime);
                    x.setOperator(userId);
                    x.setOperatorName(userName);
                    x.setGmtModify(dateTime);
                });
                contractPaySettDetailsService.saveBatch(settDetailsList);
            }

            //查询扣款明细数据
            List<ContractPayConcludeSettdeductionE> settdeductionEList = contractPayConcludeSettdeductionMapper.selectList(Wrappers.<ContractPayConcludeSettdeductionE>lambdaQuery()
                    .in(ContractPayConcludeSettdeductionE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList())).eq(ContractPayConcludeSettdeductionE :: getDeleted, 0));
            if(CollectionUtils.isNotEmpty(settdeductionEList)){
                settdeductionEList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setId(null);
                    x.setCreator(userId);
                    x.setCreatorName(userName);
                    x.setGmtCreate(dateTime);
                    x.setOperator(userId);
                    x.setOperatorName(userName);
                    x.setGmtModify(dateTime);
                });
                contractPayConcludeSettdeductionService.saveBatch(settdeductionEList);
            }

            //查询结算单与计划关联关系
            List<ContractPayConcludeSettlementPlanRelationE> planRelationList = settlementPlanRelationMapper
                    .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                            .in(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList())));
            if(CollectionUtils.isNotEmpty(planRelationList)){
                planRelationList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setPlanId(planMap.get(x.getPlanId()));
                    x.setId(null);
                });
                settlementPlanRelationMapper.insertBatch(planRelationList);
            }
            //查询结算单周期数据
            List<ContractPayConcludeSettlementPeriodE> periodList = contractPayConcludeSettlementPeriodMapper.selectList(Wrappers.<ContractPayConcludeSettlementPeriodE>lambdaQuery()
                    .in(ContractPayConcludeSettlementPeriodE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList())));
            if(CollectionUtils.isNotEmpty(periodList)){
                periodList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setId(null);
                });
                contractPayConcludeSettlementPeriodMapper.insertBatch(periodList);
            }
        }
    }

    //根据合同ID获取结算单列表
    public List<PayNkSettlementV> getPayNkSettlementList(String id){
        List<PayNkSettlementV> resultList = new ArrayList<>();
        //获取审批通过结算单
        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPaySettlementConcludeE::getContractId, id)
                .notIn(ContractPaySettlementConcludeE::getPid,0)
                .eq(ContractPaySettlementConcludeE::getReviewStatus,ReviewStatusEnum.已通过.getCode())
                .eq(ContractPaySettlementConcludeE::getDeleted,0)
                .orderByAsc(ContractPaySettlementConcludeE::getGmtCreate);  ;
        List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(settlementList)){
            return resultList;
        }
        //获取计量周期
        List<SettlementSimpleStr> periodList = contractPayConcludeSettlementPeriodMapper.getSimpleStrList(settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList()));
        Map<String, String> periodMap = periodList.stream()
                .collect(Collectors.toMap(
                        SettlementSimpleStr::getSettlementId,
                        SettlementSimpleStr::getStr
                ));
        for (int i = 0; i < settlementList.size(); i++) {
            PayNkSettlementV vo = new PayNkSettlementV();
            BeanUtils.copyProperties(settlementList.get(i), vo);
            vo.setSort(i);
            //计量周期
            vo.setPeriodStr(periodMap.get(vo.getId()));
            if (i == 0) {
                // 第一条数据，没有上一条数据
                vo.setDifferenceSettlementAmount(BigDecimal.ZERO);
                vo.setIsLessThanTenPercentOfThe(Boolean.FALSE);
            } else {
                ContractPaySettlementConcludeE previous = settlementList.get(i - 1);

                // 计算差值：当前值 - 上一条值
                BigDecimal difference = vo.getActualSettlementAmount()
                        .subtract(previous.getActualSettlementAmount());
                vo.setDifferenceSettlementAmount(difference);
                if(difference.compareTo(BigDecimal.ZERO) >= 0){
                    vo.setIsLessThanTenPercentOfThe(Boolean.FALSE);
                }else{
                    // 判断差值是否大于上一条值的10%
                    BigDecimal tenPercentOfPrevious = previous.getActualSettlementAmount()
                            .multiply(new BigDecimal("0.1"))
                            .abs(); // 取绝对值
                    boolean isLessThanTenPercent = difference.abs().compareTo(tenPercentOfPrevious) > 0;
                    vo.setIsLessThanTenPercentOfThe(isLessThanTenPercent);
                }
            }
            resultList.add(vo);
        }
        return resultList;
    }

    //开始NK
    @Transactional
    public void startNK(ContractIdV vo){
        String userName = userName();
        LocalDateTime dateTime = LocalDateTime.now();
        String userId = userId();

        //主合同ID
        String mainContractId = vo.getContractId();
        ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(mainContractId);
        if(Objects.isNull(mainContract)){
            throw new OwlBizException("合同不存在，请输入正确合同ID");
        }
        //变更主合同NK状态
        mainContract.setNkStatus(NkStatusEnum.已开启.getCode());
        contractPayConcludeMapper.updateById(mainContract);

        //-------将当前的合同ID快照为NK-------
        ContractPayConcludeE concludeNk = new ContractPayConcludeE();
        BeanUtils.copyProperties(mainContract, concludeNk);
        concludeNk.setDeleted(1);
        concludeNk.setNkStatus(NkStatusEnum.已开启.getCode());
        concludeNk.setId(null);
        concludeNk.setPid(mainContract.getId());
        concludeNk.setApprovalDate(LocalDateTime.now());
        contractPayConcludeMapper.insert(concludeNk);

        //NK合同ID
        String concludeNkId = concludeNk.getId();

        //-------拷贝清单数据-------
        List<ContractPayFundE> funList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, mainContractId)
                .eq(ContractPayFundE.DELETED, 0)
        );
        if(CollectionUtils.isNotEmpty(funList)){
            funList.forEach(x->{
                x.setMainId(x.getId());
                x.setId(null);
                x.setContractId(concludeNkId);
                /*x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);*/
            });
            contractPayFundService.saveBatch(funList);
        }
        Map<String, String> funMainMap = funList.stream()
                .collect(Collectors.toMap(
                        ContractPayFundE::getMainId,
                        ContractPayFundE::getId));

        //-------拷贝扩展表数据-------
        List<ContractPayConcludeExpandE> expandEList = contractPayConcludeExpandService.list(new QueryWrapper<ContractPayConcludeExpandE>()
                .orderByDesc(ContractPayConcludeExpandE.GMT_CREATE)
                .eq("contractId", mainContractId)
                .eq(ContractPayConcludeExpandE.DELETED, 0));
        if(CollectionUtils.isNotEmpty(expandEList)){
            expandEList.forEach(x->{
                x.setId(null);
                x.setContractId(concludeNkId);
                x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);
            });
            contractPayConcludeExpandService.saveBatch(expandEList);
        }

        //-------拷贝附件信息-------
        List<AttachmentE> attachmentEList = attachmentService.listById(mainContractId);
        if(ObjectUtils.isNotEmpty(attachmentEList)){
            for(AttachmentE file : attachmentEList){
                AttachmentE attachmentF = new AttachmentE();
                BeanUtils.copyProperties(file,attachmentF);
                attachmentF.setId(null);
                attachmentF.setBusinessId(concludeNkId);
                attachmentF.setCreator(userId);
                attachmentF.setCreatorName(userName);
                attachmentF.setGmtCreate(dateTime);
                attachmentF.setOperator(userId);
                attachmentF.setOperatorName(userName);
                attachmentF.setGmtModify(dateTime);
                attachmentService.save(attachmentF);
            }
        }

        //-------拷贝结算计划信息-------
        //拷贝结算计划pid为“0”数据
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanPidWrapper = new LambdaQueryWrapper<>();
        queryPlanPidWrapper.eq(ContractPayPlanConcludeE::getContractId, mainContractId)
                .eq(ContractPayPlanConcludeE::getPid,"0")
                .eq(ContractPayPlanConcludeE::getDeleted,0);
        List<ContractPayPlanConcludeE> concludePlanPidList = contractPayPlanConcludeMapper.selectList(queryPlanPidWrapper);
        concludePlanPidList.forEach(x->{
            x.setMainId(x.getId());
            x.setId(null);
            x.setContractId(concludeNkId);
            x.setCreator(userId);
            x.setCreatorName(userName);
            x.setGmtCreate(dateTime);
            x.setOperator(userId);
            x.setOperatorName(userName);
            x.setGmtModify(dateTime);
        });
        contractPayPlanConcludeService.saveBatch(concludePlanPidList);
        List<ContractPayConcludeSettlementPlanRelationE> planRelationList = new ArrayList<>();
        List<String> planIdList = new ArrayList<>();
        planIdList.add("结算计划ID");
        if(CollectionUtils.isNotEmpty(vo.getSettlementIdList())){
            planRelationList = settlementPlanRelationMapper
                    .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                            .in(ContractPayConcludeSettlementPlanRelationE::getSettlementId, vo.getSettlementIdList()));
            if(CollectionUtils.isNotEmpty(planRelationList)){
                planRelationList.forEach(x->planIdList.add(x.getPlanId()));
            }
        }

        Map<String, String> planPidMap = concludePlanPidList.stream()
                .collect(Collectors.toMap(
                        ContractPayPlanConcludeE::getMainId,
                        ContractPayPlanConcludeE::getId));
        //拷贝结算计划pid不为“0”数据
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
        queryPlanWrapper.eq(ContractPayPlanConcludeE::getContractId, mainContractId)
                .ne(ContractPayPlanConcludeE::getPid,"0")
                .eq(ContractPayPlanConcludeE::getDeleted,0);
        List<ContractPayPlanConcludeE> concludePlanList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);
        concludePlanList.forEach(x->{
            x.setSettlementAmount(planIdList.contains(x.getId()) ? x.getSettlementAmount() : BigDecimal.ZERO);
            x.setPaymentStatus(planIdList.contains(x.getId()) ? x.getPaymentStatus() : PaymentStatusEnum.未结算.getCode());
            x.setMainId(x.getId());
            x.setId(null);
            x.setPid(planPidMap.get(x.getPid()));
            x.setContractId(concludeNkId);
            /*x.setCreator(userId);
            x.setCreatorName(userName);
            x.setGmtCreate(dateTime);
            x.setOperator(userId);
            x.setOperatorName(userName);
            x.setGmtModify(dateTime);*/
            x.setContractPayFundId(funMainMap.get(x.getContractPayFundId()));
        });
        contractPayPlanConcludeService.saveBatch(concludePlanList);
        Map<String,String> planMap = concludePlanList.stream()
                .collect(Collectors.toMap(
                        ContractPayPlanConcludeE::getMainId,
                        ContractPayPlanConcludeE::getId
                ));
        //-------拷贝成本计划信息-------
        List<PayCostPlanE> payCostList = contractPayCostPlanService.list(Wrappers.<PayCostPlanE>lambdaQuery()
                .eq(PayCostPlanE::getContractId, mainContractId)
                .eq(PayCostPlanE::getDeleted, 0));
        if(CollectionUtils.isNotEmpty(payCostList)){
            payCostList.forEach(x->{
                x.setPlanId(planMap.get(x.getPlanId()));
                x.setId(null);
                x.setBillId(null);
                x.setBillCode(null);
                x.setContractId(concludeNkId);
                /*x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);*/
            });
            contractPayCostPlanService.saveBatch(payCostList);
        }
        //-------拷贝结算单相关信息-------
        if(CollectionUtils.isNotEmpty(vo.getSettlementIdList())){
            //拷贝结算单pid为“0”数据
            LambdaQueryWrapper<ContractPaySettlementConcludeE> queryMainWrapper = new LambdaQueryWrapper<>();
            queryMainWrapper.in(ContractPaySettlementConcludeE::getContractId, mainContractId)
                    .eq(ContractPaySettlementConcludeE::getPid,0)
                    .eq(ContractPaySettlementConcludeE::getDeleted,0);
            List<ContractPaySettlementConcludeE> settlemenMaintList = contractPaySettlementConcludeMapper.selectList(queryMainWrapper);
            settlemenMaintList.forEach(x->{
                x.setMainId(x.getId());
                x.setId(null);
                x.setContractId(concludeNkId);
                /*x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);*/
            });
            contractPaySettlementConcludeService.saveBatch(settlemenMaintList);

            Map<String, String> setPidMap = settlemenMaintList.stream()
                    .collect(Collectors.toMap(
                            ContractPaySettlementConcludeE::getMainId,
                            ContractPaySettlementConcludeE::getId));

            //拷贝结算单pid不为“0”数据
            LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ContractPaySettlementConcludeE::getContractId, mainContractId)
                    .in(ContractPaySettlementConcludeE::getId, vo.getSettlementIdList())
                    .eq(ContractPaySettlementConcludeE::getDeleted,0);
            List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
            settlementList.forEach(x->{
                x.setMainId(x.getId());
                x.setId(null);
                x.setPid(setPidMap.get(x.getPid()));
                x.setContractId(concludeNkId);
                /*x.setCreator(userId);
                x.setCreatorName(userName);
                x.setGmtCreate(dateTime);
                x.setOperator(userId);
                x.setOperatorName(userName);
                x.setGmtModify(dateTime);*/
            });
            contractPaySettlementConcludeService.saveBatch(settlementList);

            Map<String, String> settlementMap = settlementList.stream()
                    .collect(Collectors.toMap(
                            ContractPaySettlementConcludeE::getMainId,
                            ContractPaySettlementConcludeE::getId));

            //-------拷贝结算明细数据-------
            List<ContractPaySettDetailsE> settDetailsList = contractPaySettDetailsMapper.selectList(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                    .in(ContractPaySettDetailsE::getSettlementId, vo.getSettlementIdList()).eq(ContractPaySettDetailsE :: getDeleted, 0));
            if(CollectionUtils.isNotEmpty(settDetailsList)){
                settDetailsList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setId(null);
                    /*x.setCreator(userId);
                    x.setCreatorName(userName);
                    x.setGmtCreate(dateTime);
                    x.setOperator(userId);
                    x.setOperatorName(userName);
                    x.setGmtModify(dateTime);*/
                    x.setPayFundId(funMainMap.get(x.getPayFundId()));
                });
                contractPaySettDetailsService.saveBatch(settDetailsList);
            }

            //-------拷贝扣款明细数据-------
            List<ContractPayConcludeSettdeductionE> settdeductionEList = contractPayConcludeSettdeductionMapper.selectList(Wrappers.<ContractPayConcludeSettdeductionE>lambdaQuery()
                    .in(ContractPayConcludeSettdeductionE::getSettlementId, vo.getSettlementIdList()).eq(ContractPayConcludeSettdeductionE :: getDeleted, 0));
            if(CollectionUtils.isNotEmpty(settdeductionEList)){
                settdeductionEList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setId(null);
                    /*x.setCreator(userId);
                    x.setCreatorName(userName);
                    x.setGmtCreate(dateTime);
                    x.setOperator(userId);
                    x.setOperatorName(userName);
                    x.setGmtModify(dateTime);*/
                });
                contractPayConcludeSettdeductionService.saveBatch(settdeductionEList);
            }

            //-------拷贝结算单与计划关联关系-------
            if(CollectionUtils.isNotEmpty(planRelationList)){
                planRelationList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setPlanId(planMap.get(x.getPlanId()));
                    x.setId(null);
                });
                settlementPlanRelationMapper.insertBatch(planRelationList);
            }

            //-------拷贝结算单周期数据-------
            List<ContractPayConcludeSettlementPeriodE> periodList = contractPayConcludeSettlementPeriodMapper.selectList(Wrappers.<ContractPayConcludeSettlementPeriodE>lambdaQuery()
                    .in(ContractPayConcludeSettlementPeriodE::getSettlementId, vo.getSettlementIdList()));
            if(CollectionUtils.isNotEmpty(periodList)){
                periodList.forEach(x->{
                    x.setSettlementId(settlementMap.get(x.getSettlementId()));
                    x.setId(null);
                });
                contractPayConcludeSettlementPeriodMapper.insertBatch(periodList);
            }

            //-------拷贝附件信息-------
            for(String id : vo.getSettlementIdList()){
                List<AttachmentE> attachmentSettleEList = attachmentService.listById(id);
                if(CollectionUtils.isNotEmpty(attachmentSettleEList)){
                    for(AttachmentE file : attachmentSettleEList){
                        AttachmentE attachmentF = new AttachmentE();
                        BeanUtils.copyProperties(file,attachmentF);
                        attachmentF.setId(null);
                        attachmentF.setBusinessId(settlementMap.get(id));
                        attachmentF.setCreator(userId);
                        attachmentF.setCreatorName(userName);
                        attachmentF.setGmtCreate(dateTime);
                        attachmentF.setOperator(userId);
                        attachmentF.setOperatorName(userName);
                        attachmentF.setGmtModify(dateTime);
                        attachmentService.save(attachmentF);
                    }
                }
            }
        }
    }

    /**
     * 结束NK
     * @param id
     */
    @Transactional
    public void endNK(String id){
        ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(id);
        if(Objects.isNull(mainContract) ){
            throw new OwlBizException("合同不存在，请输入正确合同ID");
        }
        ContractPayConcludeE nkContract = contractPayConcludeMapper.queryNKContractById(id);
        if(ObjectUtil.isNotNull(nkContract)){
            //查询结算单
            LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ContractPaySettlementConcludeE::getContractId, nkContract.getId())
                    .notIn(ContractPaySettlementConcludeE::getPid,0)
                    .eq(ContractPaySettlementConcludeE::getReviewStatus,ReviewStatusEnum.审批中.getCode())
                    .eq(ContractPaySettlementConcludeE::getDeleted,0);
            List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(settlementList)){
                throw new OwlBizException("结束NK失败，当前合同存在未审批完成的NK结算流程");
            }
        }

        //暂时使用【合同报账单-通用收入-计提BPM审批】
        ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(mainContract.getCommunityId(),24);
        log.info("支出合同结束NK：获取审批规则,结果:{}", JSON.toJSONString(approveFilter));
        if (approveFilter.getApproveWay() == 1) {
            WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
            log.info("支出合同结束NK：获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));
            if (ObjectUtil.isNull(wflowModelHistorysV)) {
                throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
            }
            ProcessStartF processStartF = new ProcessStartF();
            Map<String, Object> formData = new HashMap<>();
            formData.put("flowType", "支出合同结束NK");
            formData.put("flowId", id);
            processStartF.setFormData(formData);
            processStartF.setBusinessKey(id);
            processStartF.setBusinessType("支出合同结束NK");
            processStartF.setSuitableTargetType("PROJECT");
            processStartF.setSuitableTargetId(mainContract.getCommunityId());
            processStartF.setSuitableTargetName(mainContract.getCommunityName());
            log.info("支出合同结束NK：发起审批流程入参,processStartF:{}", JSON.toJSONString(processStartF));
            String s = null;
            try {
                s = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
            } catch (Exception e) {
                log.info("流程发起异常：{}",e);
                log.error("流程发起异常：{}",e);
                throw new OwlBizException("流程发起超时，请稍后重试！");
            }
            mainContract.setBpmStatus(BPMStatusEnum.已发起.getCode());
            mainContract.setBpmProcInstId(s);
            mainContract.setNkStatus(NkStatusEnum.关闭中.getCode());
            contractPayConcludeMapper.updateById(mainContract);
            if(ObjectUtil.isNotNull(nkContract)){
                contractPayConcludeMapper.updateNKContractById(nkContract.getId(),NkStatusEnum.关闭中.getCode(),BPMStatusEnum.已发起.getCode(),s,null);
            }
        }else{
            //无需审批，直接关闭
            mainContract.setNkStatus(NkStatusEnum.已关闭.getCode());
            contractPayConcludeMapper.updateById(mainContract);
            if(ObjectUtil.isNotNull(nkContract)){
                contractPayConcludeMapper.updateNKContractById(nkContract.getId(),NkStatusEnum.已关闭.getCode(),null,null,null);
            }
        }
    }
    //HY合同列表清单
    public List<HyContractListV> getHyContractList(){
        List<HyContractListV> resultList = new ArrayList<>();
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPayConcludeE.PID, "0")
                .eq(ContractPayConcludeE.NK_STATUS,1)
                .eq(ContractPayConcludeE.DELETED,0);
        List<ContractPayConcludeE> contractPayConcludeEList = contractPayConcludeMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(contractPayConcludeEList)){
            return resultList;
        }
        contractPayConcludeEList.forEach(x->{
            HyContractListV hyContractListV = new HyContractListV();
            hyContractListV.setId(x.getId());
            hyContractListV.setName(x.getName());
            resultList.add(hyContractListV);
        });
        return resultList;
    }

    /**
     * 根据费项ID获取合约规划信息
     * @return
     */
    public ContractPayCostPlanV getCostPlanByChargeItemId(ContractPayCostPlanReqV reqVo){
        ContractPayCostPlanV result = new ContractPayCostPlanV();
        //查询快照数据
        if(StringUtils.isNotBlank(reqVo.getCbApportionId()) && StringUtils.isNotBlank(reqVo.getContractId())){
            ContractPayCostApportionE costApportionE = contractPayCostApportionMapper.selectOne(new QueryWrapper<ContractPayCostApportionE>()
                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                    .eq(ContractPayCostApportionE.CONTRACT_ID, reqVo.getContractId())
                    .eq(ContractPayCostApportionE.DELETED, 0)
                    .eq(ContractPayCostApportionE.ID, reqVo.getCbApportionId())
            );
            if(Objects.isNull(costApportionE)){
                return result;
            }
            BeanUtils.copyProperties(costApportionE,result);
        }else{
            ChargeItemRv chargeItemRv = financeFeignClient.chargeGetById(reqVo.getChargeItemId());
            if(Objects.isNull(chargeItemRv)){
                log.info("未查询到该费项：【{}】信息，不查询成本合约规划信息",reqVo.getChargeItemId());
                return result;
            }
            Boolean costResult = this.getIsCorrelationCost(reqVo.getCommunityId(),reqVo.getDepartId(), null, chargeItemRv, null);
            if(!costResult){
                return result;
            }
            DynamicCostSurplusInfo costData = this.costData(reqVo);
            if(Objects.nonNull(costData)){
                BeanUtils.copyProperties(costData,result);
            }
            /*log.info("查询成本费项映射信息入参：{}",chargeItemRv.getCode());
            CostBaseResponse<List<CostItemNode>> costNodeResult = externalFeignClient.getCodeMappingTree(chargeItemRv.getCode());
            log.info("查询成本费项映射信息出参：{}",JSONArray.toJSON(costNodeResult));
            if(Objects.isNull(costNodeResult) || !costNodeResult.getSuccess()){
                log.info("该项目：【{}】查询成本费项映射信息失败",reqVo.getCommunityId());
                return resultList;
            }
            if(CollectionUtils.isEmpty(costNodeResult.getData())){
                return resultList;
            }
            for(CostItemNode node : costNodeResult.getData()){
                if(CollectionUtils.isNotEmpty(node.getChildren())){
                    for(CostItemNode nodeChilder : node.getChildren()){
                        reqVo.setAccountItemFullCode(nodeChilder.getCbCode());
                        DynamicCostSurplusInfo costData = this.costData(reqVo);
                        if(Objects.nonNull(costData)){
                            ContractPayCostPlanV result = new ContractPayCostPlanV();
                            BeanUtils.copyProperties(costData,result);
                            resultList.add(result);
                        }

                    }
                }else{
                    reqVo.setAccountItemFullCode(node.getCbCode());
                    DynamicCostSurplusInfo costData = this.costData(reqVo);
                    if(Objects.nonNull(costData)){
                        ContractPayCostPlanV result = new ContractPayCostPlanV();
                        BeanUtils.copyProperties(costData,result);
                        resultList.add(result);
                    }
                }
            }*/
        }
        return result;
    }

    //获取成本规划数据
    private DynamicCostSurplusInfo costData(ContractPayCostPlanReqV reqVo){
        DynamicCostSurplusInfo costData = new DynamicCostSurplusInfo();
        ChargeItemRv chargeItemRv = financeFeignClient.chargeGetById(reqVo.getChargeItemId());
        if(Objects.isNull(chargeItemRv)){
            log.info("未查询到该费项：【{}】信息，不查询成本合约规划信息",reqVo.getChargeItemId());
            return null;
        }
        DynamicCostSurplusPropReqF costReq = new DynamicCostSurplusPropReqF();
        costReq.setCbCode(chargeItemRv.getCode());
        costReq.setMdmId(reqVo.getCommunityId());
        costReq.setSourceType(2);
        List<String> yearList = IntStream.rangeClosed(reqVo.getGmtExpireStart().getYear(), reqVo.getGmtExpireEnd().getYear())
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        costReq.setYears(yearList.stream().collect(Collectors.joining(",")))
                .setMonth(reqVo.getGmtExpireStart().getMonthValue());
        //查询项目下费项可用金额明细
        log.info("查询项目下费项可用金额明细入参：{}",JSON.toJSONString(costReq));
        CostBaseResponse<DynamicCostSurplusInfo> costBaseResponse = externalFeignClient.getDynamicCostSurplusProp(costReq);
        log.info("查询项目下费项可用金额明细出参：{}",JSON.toJSONString(costBaseResponse));
        if(Objects.isNull(costBaseResponse) || !costBaseResponse.getSuccess()){
            log.info("该项目：【{}】查询成本合约规划信息失败",reqVo.getCommunityId());
            return null;
        }
        costData = costBaseResponse.getData();
        if(Objects.isNull(costData)){
            log.info("该项目：【{}】查询成本合约规划信息无数据",reqVo.getCommunityId());
            return null;
        }

        return costData;
    }

    /**
     * 根据条件获取成本分摊明细
     * @return
     */
    public ContractPayCostApportionV getCostApportionDetail(ContractPayCostPlanReqV reqVo){
        ContractPayCostApportionV result = new ContractPayCostApportionV();
        //查询快照数据
        if(StringUtils.isNotBlank(reqVo.getCbApportionId()) && StringUtils.isNotBlank(reqVo.getContractId())){
            List<ContractPayCostApportionE> costApportionEList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                    .eq(ContractPayCostApportionE.CONTRACT_ID, reqVo.getContractId())
                    .eq(ContractPayCostApportionE.DELETED, 0)
                    .and(wrapper -> wrapper
                            .eq(ContractPayCostApportionE.ID, reqVo.getCbApportionId())
                            .or()
                            .eq(ContractPayCostApportionE.PID, reqVo.getCbApportionId())
                    )
            );
            if(CollectionUtils.isEmpty(costApportionEList)){
                return result;
            }
            ContractPayCostApportionE costPid = costApportionEList.stream().filter(x->reqVo.getCbApportionId().equals(x.getId())).findFirst().orElse(new ContractPayCostApportionE());
            //费项编码
            result.setAccountItemCode(costPid.getAccountItemCode());
            //费项名称
            result.setAccountItemName(costPid.getAccountItemName());
            //费项全码
            result.setAccountItemFullCode(costPid.getAccountItemFullCode());
            //费项全称
            result.setAccountItemFullName(costPid.getAccountItemFullName());
            //本次分摊金额
            result.setApportionAmount(costPid.getApportionAmount());
            result.setProjectGuid(costPid.getProjectGuid());
            result.setBuGuid(costPid.getBuGuid());
            result.setBusinessGuid(costPid.getBusinessGuid());
            result.setBusinessUnitCode(costPid.getBusinessUnitCode());
            Map<String, String> costControlTypeMap = new HashMap<>();
            List<ContractPayCostApportionDetailV> apportionList = new ArrayList<>();
            for (ContractPayCostApportionE costApportionE : costApportionEList.stream().filter(x->reqVo.getCbApportionId().equals(x.getPid())).collect(Collectors.toList())) {
                ContractPayCostApportionDetailV detailV = new ContractPayCostApportionDetailV();
                BeanUtils.copyProperties(costApportionE, detailV);
                detailV.setApportionTypeDesc(CostApportionTypeEnum.getEnum(costApportionE.getApportionType()).getName());
                detailV.setApportionTypePrompt(CostApportionTypeEnum.getEnum(costApportionE.getApportionType()).getPrompt());
                apportionList.add(detailV);
                if(CostApportionTypeEnum.可用金额.getCode().equals(costApportionE.getApportionType())){
                    costControlTypeMap.put(costApportionE.getYear(),costApportionE.getCostControlTypeName());
                }
            }
            result.setCostControlTypeMap(costControlTypeMap);
            Map<String, List<ContractPayCostApportionDetailV>> groupedAndSorted = apportionList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayCostApportionDetailV::getYear,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    list -> list.stream()
                                            .sorted(Comparator.comparing(ContractPayCostApportionDetailV::getApportionType))
                                            .collect(Collectors.toList())
                            )
                    ));
            result.setApportionMap(groupedAndSorted);
        }else{
            DynamicCostSurplusInfo costData = this.costData(reqVo);
            if(Objects.isNull(costData)){
                return null;
            }
            //费项编码
            result.setAccountItemCode(costData.getAccountItemCode());
            //费项名称
            result.setAccountItemName(costData.getAccountItemName());
            //费项全码
            result.setAccountItemFullCode(costData.getAccountItemFullCode());
            //费项全称
            result.setAccountItemFullName(costData.getAccountItemFullName());
            //本次分摊金额
            result.setApportionAmount(reqVo.getAmountWithOutRate());
            result.setProjectGuid(costData.getProjectGuid());
            result.setBuGuid(costData.getBuGuid());
            result.setBusinessGuid(costData.getBusinessGuid());
            result.setBusinessUnitCode(costData.getBusinessUnitCode());
            result.setSummarySurplusAmount(costData.getSummarySurplusAmount());

            //分摊明细列表
            List<ContractPayCostApportionDetailV> availableList = new ArrayList<>();

            List<DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO> costList = costData.getList();
            Map<String, String> costControlTypeMap = new HashMap<>();
            //获取可用金额
            if(CollectionUtils.isNotEmpty(costList)){
                if(CollectionUtils.isNotEmpty(costList)){

                    List<String> yearList = IntStream.rangeClosed(reqVo.getGmtExpireStart().getYear(), reqVo.getGmtExpireEnd().getYear())
                            .mapToObj(String::valueOf)
                            .collect(Collectors.toList());
                    List<String> costYearList = costList.stream().map(DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO::getBudgetYear).collect(Collectors.toList());
                    List<String> useYearList = new ArrayList<>();
                    useYearList.add("年份");
                    for(DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO cost : costList){
                        if(useYearList.contains(cost.getBudgetYear())){
                            continue;
                        }
                        costControlTypeMap.put(cost.getBudgetYear(),costList.get(0).getCostControlTypeName());
                        ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                        BeanUtils.copyProperties(cost.getDynamicCostSurplus(),apportionE);
                        apportionE.setDynamicCostGuid(cost.getDynamicCostGuid());
                        apportionE.setYear(cost.getBudgetYear());
                        apportionE.setCostControlTypeEnum(cost.getCostControlTypeEnum());
                        apportionE.setCostControlTypeName(cost.getCostControlTypeName());
                        apportionE.setApportionType(CostApportionTypeEnum.可用金额.getCode());
                        apportionE.setApportionTypeDesc(CostApportionTypeEnum.可用金额.getName());
                        apportionE.setApportionTypePrompt(CostApportionTypeEnum.可用金额.getPrompt());
                        availableList.add(apportionE);
                        useYearList.add(cost.getBudgetYear());
                    }
                    yearList.removeAll(costYearList);
                    //若成本未返回数据，则补位
                    if(CollectionUtils.isNotEmpty(yearList)){
                        for(String year : yearList){
                            ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                            apportionE.setYear(year);
                            apportionE.setApportionType(CostApportionTypeEnum.可用金额.getCode());
                            apportionE.setApportionTypeDesc(CostApportionTypeEnum.可用金额.getName());
                            apportionE.setApportionTypePrompt(CostApportionTypeEnum.可用金额.getPrompt());
                            availableList.add(apportionE);
                            costControlTypeMap.put(year,"");
                        }
                    }
                }
            }
            result.setCostControlTypeMap(costControlTypeMap);
            //若主合同ID不为空，查询有无对应清单数据
            if(StringUtils.isNotBlank(reqVo.getContractMainId())){
                List<ContractPayFundE> funList = contractPayFundMapper.selectList(new QueryWrapper<ContractPayFundE>()
                        .eq(ContractPayFundE.TENANT_ID, tenantId())
                        .eq(ContractPayFundE.CONTRACT_ID, reqVo.getContractMainId())
                        .eq(ContractPayFundE.DELETED, 0)
                        .eq(ContractPayFundE.TYPE_ID, reqVo.getTypeId())
                        .eq(ContractPayFundE.CHARGE_ITEM_ID, reqVo.getChargeItemId())
                        .eq(ContractPayFundE.PAY_WAY_ID, reqVo.getPayWayId())
                        .eq(ContractPayFundE.TAX_RATE_ID, reqVo.getTaxRateId())
                        .eq(ContractPayFundE.PAY_TYPE_ID, reqVo.getPayTypeId())
                        .eq(ContractPayFundE.STANDARD_ID, reqVo.getStandardId())
                        .eq(ContractPayFundE.STAND_AMOUNT, reqVo.getStandAmount())
                        .eq(ContractPayFundE.IS_HY, 0)
                );
                if(CollectionUtils.isNotEmpty(funList)){
                    //获取分摊金额
                    List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = contractProjectPlanMonthlyAllocationAppService.distributeCost(funList.get(0).getAmountWithOutRate(), reqVo.getGmtExpireStart(), reqVo.getGmtExpireEnd());
                    if(CollectionUtils.isNotEmpty(monthlyAllocationVS)){
                        for(ContractProjectPlanMonthlyAllocationV monthlyAllocationV : monthlyAllocationVS){
                            ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                            BeanUtils.copyProperties(monthlyAllocationV,apportionE);
                            apportionE.setApportionType(CostApportionTypeEnum.累计分摊金额.getCode());
                            apportionE.setApportionTypeDesc(CostApportionTypeEnum.累计分摊金额.getName());
                            apportionE.setApportionTypePrompt(CostApportionTypeEnum.累计分摊金额.getPrompt());
                            availableList.add(apportionE);
                        }
                    }
                }
            }
            //获取分摊金额
            List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = contractProjectPlanMonthlyAllocationAppService.distributeCost(reqVo.getAmountWithOutRate(), reqVo.getGmtExpireStart(), reqVo.getGmtExpireEnd());
            if(CollectionUtils.isNotEmpty(monthlyAllocationVS)){
                for(ContractProjectPlanMonthlyAllocationV monthlyAllocationV : monthlyAllocationVS){
                    ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                    BeanUtils.copyProperties(monthlyAllocationV,apportionE);
                    apportionE.setApportionType(CostApportionTypeEnum.分摊金额.getCode());
                    apportionE.setApportionTypeDesc(CostApportionTypeEnum.分摊金额.getName());
                    apportionE.setApportionTypePrompt(CostApportionTypeEnum.分摊金额.getPrompt());
                    availableList.add(apportionE);
                }
            }

            //获取分摊后剩余
            if(CollectionUtils.isNotEmpty(availableList)){
                List<ContractPayCostApportionDetailV> remainingList = this.processAvailableList(availableList,CostApportionTypeEnum.可用金额.getCode(),CostApportionTypeEnum.分摊金额.getCode(),Boolean.FALSE);
                remainingList.forEach(remaining -> {
                    remaining.setApportionType(CostApportionTypeEnum.分摊后剩余.getCode());
                    remaining.setApportionTypeDesc(CostApportionTypeEnum.分摊后剩余.getName());
                    remaining.setApportionTypePrompt(CostApportionTypeEnum.分摊后剩余.getPrompt());
                });
                availableList.addAll(remainingList);
            }

            Map<String, List<ContractPayCostApportionDetailV>> groupedAndSorted = availableList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayCostApportionDetailV::getYear,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    list -> list.stream()
                                            .sorted(Comparator.comparing(ContractPayCostApportionDetailV::getApportionType))
                                            .collect(Collectors.toList())
                            )
                    ));
            result.setApportionMap(groupedAndSorted);

        }


        return result;
    }
    //计算差值
    public List<ContractPayCostApportionDetailV> processAvailableList(List<ContractPayCostApportionDetailV> availableList,Integer apportionType,Integer apportionTypeTwo,Boolean isJs) {
        // 按年份分组
        Map<String, List<ContractPayCostApportionDetailV>> groupedByYear = new HashMap<>();
        for (ContractPayCostApportionDetailV detail : availableList) {
            groupedByYear.computeIfAbsent(detail.getYear(), k -> new ArrayList<>()).add(detail);
        }

        List<ContractPayCostApportionDetailV> result = new ArrayList<>();

        // 处理每个年份的数据
        for (Map.Entry<String, List<ContractPayCostApportionDetailV>> entry : groupedByYear.entrySet()) {
            String year = entry.getKey();
            List<ContractPayCostApportionDetailV> details = entry.getValue();

            // 找出apportionType=1和apportionType=3的记录
            ContractPayCostApportionDetailV type1Detail = null;
            ContractPayCostApportionDetailV type3Detail = null;

            for (ContractPayCostApportionDetailV detail : details) {
                if (detail.getApportionType() != null) {
                    if (detail.getApportionType() == apportionType) {
                        type1Detail = detail;
                    } else if (detail.getApportionType() == apportionTypeTwo) {
                        type3Detail = detail;
                    }
                }
            }

            // 创建结果对象（使用apportionType=3的记录作为基础，因为我们要修改它的值）
            ContractPayCostApportionDetailV resultDetail = new ContractPayCostApportionDetailV();
            if (type3Detail != null) {
                // 复制apportionType=3的记录的基本信息
                resultDetail.setApportionType(type3Detail.getApportionType());
                resultDetail.setApportionTypeDesc(type3Detail.getApportionTypeDesc());
                resultDetail.setYear(type3Detail.getYear());

                // 计算各月份的差值
                resultDetail.setJanSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getJanSurplus() : null,
                        type3Detail.getJanSurplus(),isJs));
                resultDetail.setFebSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getFebSurplus() : null,
                        type3Detail.getFebSurplus(),isJs));;
                resultDetail.setMarSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getMarSurplus() : null,
                        type3Detail.getMarSurplus(),isJs));
                resultDetail.setAprSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getAprSurplus() : null,
                        type3Detail.getAprSurplus(),isJs));
                resultDetail.setMaySurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getMaySurplus() : null,
                        type3Detail.getMaySurplus(),isJs));
                resultDetail.setJunSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getJunSurplus() : null,
                        type3Detail.getJunSurplus(),isJs));
                resultDetail.setJulSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getJulSurplus() : null,
                        type3Detail.getJulSurplus(),isJs));
                resultDetail.setAugSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getAugSurplus() : null,
                        type3Detail.getAugSurplus(),isJs));
                resultDetail.setSepSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getSepSurplus() : null,
                        type3Detail.getSepSurplus(),isJs));
                resultDetail.setOctSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getOctSurplus() : null,
                        type3Detail.getOctSurplus(),isJs));
                resultDetail.setNovSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getNovSurplus() : null,
                        type3Detail.getNovSurplus(),isJs));
                resultDetail.setDecSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getDecSurplus() : null,
                        type3Detail.getDecSurplus(),isJs));
                resultDetail.setYearSurplus(calculateDifference(
                        type1Detail != null ? type1Detail.getYearSurplus() : null,
                        type3Detail.getYearSurplus(),isJs));
                if(apportionType == 7){
                    resultDetail.setYearSurplus(
                            Stream.of( resultDetail.getJanSurplus(),resultDetail.getFebSurplus(), resultDetail.getMarSurplus(),
                                            resultDetail.getAprSurplus(),resultDetail.getMaySurplus(),resultDetail.getJunSurplus(),
                                            resultDetail.getJulSurplus(),resultDetail.getAugSurplus(),resultDetail.getSepSurplus(),
                                            resultDetail.getOctSurplus(),resultDetail.getNovSurplus(),resultDetail.getDecSurplus())
                                    .filter(Objects::nonNull) // 过滤null值
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    );
                }
                result.add(resultDetail);
            }
            // 如果type3Detail为null，则不处理（根据需求可能需要其他处理）
        }

        return result;
    }

    private BigDecimal calculateDifference(BigDecimal value1, BigDecimal value3,Boolean isJs) {
        if(isJs && Objects.isNull(value1)){
            return null;
        }
        BigDecimal val1 = value1 != null ? value1 : BigDecimal.ZERO;
        BigDecimal val3 = value3 != null ? value3 : BigDecimal.ZERO;
        return val1.subtract(val3);
    }

    //支出合同释放成本占用逻辑
    @Transactional(rollbackFor = Exception.class)
    public void releasePayContractCost(ContractPayConcludeE contractPayConcludeE){
        log.info("支出合同释放成本占用逻辑{}",contractPayConcludeE.getId());
        List<ContractPayFundE> mainFunCheckList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, contractPayConcludeE.getId())
                .eq(ContractPayFundE.IS_HY, 0)
                .eq(ContractPayFundE.DELETED, 0)
                .isNotNull("cbApportionId"));
        if(CollectionUtils.isEmpty(mainFunCheckList)){
            log.info("该合同无清单绑定成本数据，无需释放成本数据,跳出");
            return;
        }

        List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, contractPayConcludeE.getId())
                .eq(ContractPayFundE.IS_HY, 0)
                .eq(ContractPayFundE.DELETED, 0));
        List<String> funIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(mainFunList)){
            List<Long> funList = mainFunList.stream().map(ContractPayFundE :: getChargeItemId).distinct().collect(Collectors.toList());
            List<ChargeItemRv> chargeList = financeFeignClient.getByIdList(funList);
            if(funList.size() != chargeList.size()){
                throw new OwlBizException("该合同清单中含有不存在费项，请检查后重新提交");
            }
            for(ChargeItemRv item : chargeList){
                //组装费项路径
                List<Long> chargePath = JSON.parseArray(item.getPath(), Long.class);
                chargePath.remove(chargePath.size() - 1);
                if(chargePath.contains(166550662047102L) || chargePath.contains(1040901L)){
                    funIdList.addAll(mainFunList.stream().filter(i -> i.getChargeItemId().equals(item.getId())).map(ContractPayFundE :: getId).collect(Collectors.toList()));
                }
            }
        }
        if(!this.getIsCorrelationCost(contractPayConcludeE.getCommunityId(), contractPayConcludeE.getDepartId(), contractPayConcludeE.getId(), null,funIdList)){
            log.info("该合同无清单绑定成本数据，无需释放成本数据");
            return ;
        }

        DynamicCostIncurredReqF costReq = new DynamicCostIncurredReqF();
        costReq.setBillGuid(contractPayConcludeE.getId());
        costReq.setBillCode(contractPayConcludeE.getContractNo());
        costReq.setBillName(contractPayConcludeE.getName());
        costReq.setBillTypeName(BillTypeEnum.CONTRACT_REJECT.getName());
        costReq.setBillTypeEnum(BillTypeEnum.CONTRACT_REJECT.getCode());
        costReq.setOperationType(OperationTypeEnum.RELEASE.getCode());
        costReq.setContactor(OperationTypeEnum.RELEASE.getName());
        costReq.setSystemType(1);
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList = new ArrayList<>();
        List<String> costIdList = new ArrayList<>();
        for (ContractPayFundE mainFun : mainFunList.stream().filter(x->funIdList.contains(x.getId())).collect(Collectors.toList())) {
            List<ContractPayCostApportionE> costApportionEList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                    .eq(ContractPayCostApportionE.CONTRACT_ID, mainFun.getContractId())
                    .eq(ContractPayCostApportionE.DELETED, 0)
                    .and(wrapper -> wrapper
                            .eq(ContractPayCostApportionE.ID, mainFun.getCbApportionId())
                            .or()
                            .eq(ContractPayCostApportionE.PID, mainFun.getCbApportionId())
                    )
            );
            if(CollectionUtils.isEmpty(costApportionEList)){
                continue;
            }
            costIdList.addAll(costApportionEList.stream().map(ContractPayCostApportionE::getId).collect(Collectors.toList()));
            ContractPayCostApportionE mainCost = costApportionEList.stream().filter(x->mainFun.getCbApportionId().equals(x.getId())).findFirst().orElse(new ContractPayCostApportionE());
            costReq.setProjectGuid(mainCost.getProjectGuid());
            costReq.setBuGuid(mainCost.getBuGuid());
            costReq.setBusinessGuid(mainCost.getBusinessGuid());
            costReq.setBusinessUnitCode(mainCost.getBusinessUnitCode());

            List<ContractPayCostApportionE> yearCostList = costApportionEList.stream().filter(x->mainFun.getCbApportionId().equals(x.getPid()) && CostApportionTypeEnum.分摊金额.getCode().equals(x.getApportionType())).collect(Collectors.toList());
            for(ContractPayCostApportionE ftCost : yearCostList){
                DynamicCostIncurredReqF.IncurredDataListDTO incurredData = new DynamicCostIncurredReqF.IncurredDataListDTO();
                BeanUtils.copyProperties(ftCost, incurredData);
                incurredData.setAccountItemFullCode(mainFun.getAccountItemFullCode());
                incurredData.setDynamicCostGuid(ftCost.getDynamicCostGuid());
                incurredData.setShareYear(ftCost.getYear());
                incurredData.setYearShareAmount(this.getBigDecimalNegate(ftCost.getYearSurplus()));
                incurredData.setJanShareAmount(this.getBigDecimalNegate(ftCost.getJanSurplus()));
                incurredData.setFebShareAmount(this.getBigDecimalNegate(ftCost.getFebSurplus()));
                incurredData.setMarShareAmount(this.getBigDecimalNegate(ftCost.getMarSurplus()));
                incurredData.setAprShareAmount(this.getBigDecimalNegate(ftCost.getAprSurplus()));
                incurredData.setMayShareAmount(this.getBigDecimalNegate(ftCost.getMaySurplus()));
                incurredData.setJunShareAmount(this.getBigDecimalNegate(ftCost.getJunSurplus()));
                incurredData.setJulShareAmount(this.getBigDecimalNegate(ftCost.getJulSurplus()));
                incurredData.setAugShareAmount(this.getBigDecimalNegate(ftCost.getAugSurplus()));
                incurredData.setSepShareAmount(this.getBigDecimalNegate(ftCost.getSepSurplus()));
                incurredData.setOctShareAmount(this.getBigDecimalNegate(ftCost.getOctSurplus()));
                incurredData.setNovShareAmount(this.getBigDecimalNegate(ftCost.getNovSurplus()));
                incurredData.setDecShareAmount(this.getBigDecimalNegate(ftCost.getDecSurplus()));
                incurredDataList.add(incurredData);
            }

            mainFun.setCbApportionId(null);
        }
        contractPayFundMapper.deletedCostData(contractPayConcludeE.getId());
        contractPayCostApportionMapper.deletedCostData(contractPayConcludeE.getId(),costIdList);

        costReq.setIncurredAmount(incurredDataList.stream()
                .map(DynamicCostIncurredReqF.IncurredDataListDTO::getYearShareAmount)
                .filter(Objects::nonNull)  // 过滤掉null值
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredMeargeDataList = this.mergeIncurredData(incurredDataList);
        costReq.setIncurredDataList(incurredMeargeDataList);
        log.info("支出合同审批驳回，成本数据释放入参：{}", JSONArray.toJSON(costReq));
        CostBaseResponse<List<DynamicCostIncurredRespF>> shifangData = externalFeignClient.getDynamicCostIncurred(costReq);
        log.info("支出合同审批驳回，成本数据释放出参：{}", JSONArray.toJSON(shifangData));
        if(!shifangData.getSuccess()){
            throw new OwlBizException("支出合同审批驳回，成本数据释放失败："+ shifangData.getMessage());
        }
    }

    private BigDecimal getBigDecimalNegate(BigDecimal amount){
        if(Objects.isNull(amount)){
            return null;
        }
        return amount.negate();
    }

    //结算单根据条件查询成本费项分摊明细
    public ContractPayCostApportionV getSettlementCostPlan(ContractPayCostPlanReqV reqVo){
        ContractPayCostApportionV result = new ContractPayCostApportionV();
        //查询快照数据
        if(StringUtils.isNotBlank(reqVo.getCbApportionId()) && StringUtils.isNotBlank(reqVo.getContractId())){
            List<ContractPayCostApportionE> costApportionEList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                    .eq(ContractPayCostApportionE.CONTRACT_ID, reqVo.getContractId())
                    .eq(ContractPayCostApportionE.DELETED, 0)
                    .and(wrapper -> wrapper
                            .eq(ContractPayCostApportionE.ID, reqVo.getCbApportionId())
                            .or()
                            .eq(ContractPayCostApportionE.PID, reqVo.getCbApportionId())
                    )
            );
            if(CollectionUtils.isEmpty(costApportionEList)){
                ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(reqVo.getContractId());
                if(Objects.nonNull(nkConclude) && !NkStatusEnum.已关闭.getCode().equals(nkConclude.getNkStatus())){
                    nkConclude = contractPayConcludeMapper.queryByContractId(nkConclude.getPid());
                }
                costApportionEList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                        .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                        .eq(ContractPayCostApportionE.CONTRACT_ID, nkConclude.getId())
                        .eq(ContractPayCostApportionE.DELETED, 0)
                        .and(wrapper -> wrapper
                                .eq(ContractPayCostApportionE.ID, reqVo.getCbApportionId())
                                .or()
                                .eq(ContractPayCostApportionE.PID, reqVo.getCbApportionId())
                        )
                );
                if(CollectionUtils.isEmpty(costApportionEList)) {
                    return result;
                }
            }
            ContractPayCostApportionE costPid = costApportionEList.stream().filter(x->reqVo.getCbApportionId().equals(x.getId())).findFirst().orElse(new ContractPayCostApportionE());
            //费项编码
            result.setAccountItemCode(costPid.getAccountItemCode());
            //费项名称
            result.setAccountItemName(costPid.getAccountItemName());
            //费项全码
            result.setAccountItemFullCode(costPid.getAccountItemFullCode());
            //费项全称
            result.setAccountItemFullName(costPid.getAccountItemFullName());
            //本次分摊金额
            result.setApportionAmount(costPid.getApportionAmount());
            result.setProjectGuid(costPid.getProjectGuid());
            result.setBuGuid(costPid.getBuGuid());
            result.setBusinessGuid(costPid.getBusinessGuid());
            result.setBusinessUnitCode(costPid.getBusinessUnitCode());

            List<ContractPayCostApportionDetailV> apportionList = new ArrayList<>();
            Map<String, String> costControlTypeMap = new HashMap<>();
            for (ContractPayCostApportionE costApportionE : costApportionEList.stream().filter(x->reqVo.getCbApportionId().equals(x.getPid())).collect(Collectors.toList())) {
                ContractPayCostApportionDetailV detailV = new ContractPayCostApportionDetailV();
                BeanUtils.copyProperties(costApportionE, detailV);
                detailV.setApportionTypeDesc(CostApportionTypeEnum.getEnum(costApportionE.getApportionType()).getName());
                detailV.setApportionTypePrompt(CostApportionTypeEnum.getEnum(costApportionE.getApportionType()).getPrompt());
                apportionList.add(detailV);
                if(CostApportionTypeEnum.可用金额.getCode().equals(costApportionE.getApportionType())){
                    costControlTypeMap.put(costApportionE.getYear(),costApportionE.getCostControlTypeName());
                }
            }
            result.setCostControlTypeMap(costControlTypeMap);

            Map<String, List<ContractPayCostApportionDetailV>> groupedAndSorted = apportionList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayCostApportionDetailV::getYear,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    list -> list.stream()
                                            .sorted(Comparator.comparing(ContractPayCostApportionDetailV::getApportionType))
                                            .collect(Collectors.toList())
                            )
                    ));
            result.setApportionMap(groupedAndSorted);
        }else{
            ContractPayConcludeE payConcludeE = contractPayConcludeMapper.queryByContractId(reqVo.getContractId());
            if (Objects.isNull(payConcludeE)) {
                throw new OwlBizException("根据合同ID检索数据失败");
            }
            ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(payConcludeE.getId());
            if(Objects.nonNull(nkConclude) && !NkStatusEnum.已关闭.getCode().equals(nkConclude.getNkStatus())){
                payConcludeE = contractPayConcludeMapper.queryByContractId(nkConclude.getPid());
            }
            reqVo.setCommunityId(payConcludeE.getCommunityId());
            reqVo.setGmtExpireStart(reqVo.getGmtExpireStart());
            reqVo.setGmtExpireEnd(reqVo.getGmtExpireEnd());
            reqVo.setAccountItemFullCode(reqVo.getAccountItemFullCode());
            DynamicCostSurplusInfo costData = this.costData(reqVo);
            if(Objects.isNull(costData)){
                return null;
            }
            //费项编码
            result.setAccountItemCode(costData.getAccountItemCode());
            //费项名称
            result.setAccountItemName(costData.getAccountItemName());
            //费项全码
            result.setAccountItemFullCode(costData.getAccountItemFullCode());
            //费项全称
            result.setAccountItemFullName(costData.getAccountItemFullName());
            //本次分摊金额
            result.setApportionAmount(reqVo.getAmountWithOutRate());
            result.setProjectGuid(costData.getProjectGuid());
            result.setBuGuid(costData.getBuGuid());
            result.setBusinessGuid(costData.getBusinessGuid());
            result.setBusinessUnitCode(costData.getBusinessUnitCode());
            result.setSummarySurplusAmount(costData.getSummarySurplusAmount());

            //分摊明细列表
            List<ContractPayCostApportionDetailV> availableList = new ArrayList<>();

            List<DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO> costList = costData.getList();
            //获取可用金额
            Map<String, String> costControlTypeMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(costList)){
                if(CollectionUtils.isNotEmpty(costList)){
                    //管控方式名称
                    List<String> yearList = IntStream.rangeClosed(reqVo.getGmtExpireStart().getYear(), reqVo.getGmtExpireEnd().getYear())
                            .mapToObj(String::valueOf)
                            .collect(Collectors.toList());
                    List<String> costYearList = costList.stream().map(DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO::getBudgetYear).collect(Collectors.toList());
                    List<String> useYearList = new ArrayList<>();
                    useYearList.add("年份");
                    for(DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO cost : costList){
                        if(useYearList.contains(cost.getBudgetYear())){
                            continue;
                        }
                        costControlTypeMap.put(cost.getBudgetYear(),costList.get(0).getCostControlTypeName());
                        ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                        BeanUtils.copyProperties(cost.getDynamicCostSurplus(),apportionE);
                        apportionE.setDynamicCostGuid(cost.getDynamicCostGuid());
                        apportionE.setCostControlTypeEnum(cost.getCostControlTypeEnum());
                        apportionE.setCostControlTypeName(cost.getCostControlTypeName());
                        apportionE.setYear(cost.getBudgetYear());
                        apportionE.setApportionType(CostApportionTypeEnum.可用金额.getCode());
                        apportionE.setApportionTypeDesc(CostApportionTypeEnum.可用金额.getName());
                        apportionE.setApportionTypePrompt(CostApportionTypeEnum.可用金额.getPrompt());
                        availableList.add(apportionE);
                        useYearList.add(cost.getBudgetYear());
                    }
                    yearList.removeAll(costYearList);
                    //若成本未返回数据，则补位
                    if(CollectionUtils.isNotEmpty(yearList)){
                        for(String year : yearList){
                            ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                            apportionE.setYear(year);
                            costControlTypeMap.put(year,"");
                            apportionE.setApportionType(CostApportionTypeEnum.可用金额.getCode());
                            apportionE.setApportionTypeDesc(CostApportionTypeEnum.可用金额.getName());
                            apportionE.setApportionTypePrompt(CostApportionTypeEnum.可用金额.getPrompt());
                            availableList.add(apportionE);
                        }
                    }
                }
            }
            result.setCostControlTypeMap(costControlTypeMap);

            //获取累计分摊金额
            ContractPayFundE payFun = contractPayFundMapper.selectOne(new QueryWrapper<ContractPayFundE>()
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .eq(ContractPayFundE.CONTRACT_ID, reqVo.getContractId())
                    .eq(ContractPayFundE.DELETED, 0)
                    .eq(ContractPayFundE.ID, reqVo.getPayFundId())
            );
            List<String> yearZdList = IntStream.rangeClosed(reqVo.getGmtExpireStart().getYear(), reqVo.getGmtExpireEnd().getYear())
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
            List<ContractPayCostApportionE> costApportionEList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                    .eq(ContractPayCostApportionE.CONTRACT_ID, payConcludeE.getId())
                    .eq(ContractPayCostApportionE.DELETED, 0)
                    .eq(ContractPayCostApportionE.PID, payFun.getCbApportionId())
                    .in(ContractPayCostApportionE.YEAR, yearZdList)
            );
            // 先查找 apportionType = 2 的数据
            List<ContractPayCostApportionE> totalFunCostList = costApportionEList.stream()
                    .filter(e -> CostApportionTypeEnum.累计分摊金额.getCode().equals(e.getApportionType()))
                    .collect(Collectors.toList());

            // 如果没有找到 type=2 的数据，则查找 type=3 的数据
            if (totalFunCostList.isEmpty()) {
                totalFunCostList = costApportionEList.stream()
                        .filter(e -> CostApportionTypeEnum.分摊金额.getCode().equals(e.getApportionType()))
                        .collect(Collectors.toList());
            }
            for(ContractPayCostApportionE payContractCost: totalFunCostList){
                ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                BeanUtils.copyProperties(payContractCost,apportionE);
                apportionE.setApportionType(CostApportionTypeEnum.累计分摊金额.getCode());
                apportionE.setApportionTypeDesc(CostApportionTypeEnum.累计分摊金额.getName());
                apportionE.setApportionTypePrompt(CostApportionTypeEnum.累计分摊金额.getPrompt());
                availableList.add(apportionE);
            }
            //获取分摊金额
            List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = contractProjectPlanMonthlyAllocationAppService.distributeCost(reqVo.getAmountWithOutRate(), reqVo.getGmtExpireStart(), reqVo.getGmtExpireEnd());
            if(CollectionUtils.isNotEmpty(monthlyAllocationVS)){
                for(ContractProjectPlanMonthlyAllocationV monthlyAllocationV : monthlyAllocationVS){
                    ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                    BeanUtils.copyProperties(monthlyAllocationV,apportionE);
                    apportionE.setApportionType(CostApportionTypeEnum.分摊金额.getCode());
                    apportionE.setApportionTypeDesc(CostApportionTypeEnum.分摊金额.getName());
                    apportionE.setApportionTypePrompt(CostApportionTypeEnum.分摊金额.getPrompt());
                    availableList.add(apportionE);
                }
            }
            // 计算当前基金原始比例
            BigDecimal ratio = reqVo.getAmountWithOutRate().divide(reqVo.getAmountWithOutRateTotal(), 10, RoundingMode.HALF_UP);
            // 按比例计算分配金额
            BigDecimal distributedAmount = Objects.isNull(reqVo.getDeduction()) ? BigDecimal.ZERO : reqVo.getDeduction().multiply(ratio)
                    .setScale(2, RoundingMode.HALF_UP);
            List<ContractPayCostApportionDetailV> jsceList = new ArrayList<>();
            List<ContractProjectPlanMonthlyAllocationV> distributedList = contractProjectPlanMonthlyAllocationAppService.distributeCost(distributedAmount, reqVo.getGmtExpireStart(), reqVo.getGmtExpireEnd());
            if(CollectionUtils.isNotEmpty(distributedList)){
                for(ContractProjectPlanMonthlyAllocationV distributed : distributedList){
                    ContractPayCostApportionDetailV apportionE = new ContractPayCostApportionDetailV();
                    BeanUtils.copyProperties(distributed,apportionE);
                    apportionE.setApportionType(CostApportionTypeEnum.扣款金额.getCode());
                    apportionE.setApportionTypeDesc(CostApportionTypeEnum.扣款金额.getName());
                    apportionE.setApportionTypePrompt(CostApportionTypeEnum.扣款金额.getPrompt());
                    availableList.add(apportionE);
                    jsceList.add(apportionE);
                }
            }

            //获取分摊后剩余
            if(CollectionUtils.isNotEmpty(jsceList)){
                List<ContractPayCostApportionDetailV> remainingList = this.processAvailableList(availableList,CostApportionTypeEnum.分摊金额.getCode(),CostApportionTypeEnum.累计分摊金额.getCode(),Boolean.TRUE);
                remainingList.forEach(remaining -> {
                    remaining.setApportionType(7);
                });
                jsceList.addAll(remainingList);
                List<ContractPayCostApportionDetailV> remainingEndList = this.processAvailableList(jsceList,7,CostApportionTypeEnum.扣款金额.getCode(),Boolean.TRUE);
                remainingEndList.forEach(remaining -> {
                    remaining.setApportionType(CostApportionTypeEnum.结算差额.getCode());
                    remaining.setApportionTypeDesc(CostApportionTypeEnum.结算差额.getName());
                    remaining.setApportionTypePrompt(CostApportionTypeEnum.结算差额.getPrompt());

                });
                availableList.addAll(remainingEndList);
            }

            Map<String, List<ContractPayCostApportionDetailV>> groupedAndSorted = availableList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayCostApportionDetailV::getYear,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    list -> list.stream()
                                            .sorted(Comparator.comparing(ContractPayCostApportionDetailV::getApportionType))
                                            .collect(Collectors.toList())
                            )
                    ));
            result.setApportionMap(groupedAndSorted);

        }
        return result;
    }

    //结算单审批通过，释放成本占用
    @Transactional(rollbackFor = Exception.class)
    public void releasePayContractSettlementCost(ContractPaySettlementConcludeE pay){
        log.info("结算单审批通过，释放成本占用{}",JSONArray.toJSONString(pay));
        ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(pay.getContractId());
        Boolean isCost = Boolean.TRUE;
        Boolean isNK = Boolean.FALSE;
        if(Objects.nonNull(nkConclude) && !NkStatusEnum.已关闭.getCode().equals(nkConclude.getNkStatus())){
            isNK = Boolean.TRUE;
            if(!pay.getContractId().equals(nkConclude.getId())){
                log.info("结算单审批通过，释放成本占用，该合同含有NK结算，本YJ结算单不做成本占用");
                isCost = Boolean.FALSE;
            }
        }

        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(pay.getContractId());
        List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, contractPayConcludeE.getId())
                .eq(ContractPayFundE.IS_HY, 0)
                .eq(ContractPayFundE.DELETED, 0));
        List<String> funIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(mainFunList)){
            List<Long> funList = mainFunList.stream().map(ContractPayFundE :: getChargeItemId).distinct().collect(Collectors.toList());
            List<ChargeItemRv> chargeList = financeFeignClient.getByIdList(funList);
            if(funList.size() != chargeList.size()){
                throw new OwlBizException("该合同清单中含有不存在费项，请检查后重新提交");
            }
            for(ChargeItemRv item : chargeList){
                //组装费项路径
                List<Long> chargePath = JSON.parseArray(item.getPath(), Long.class);
                chargePath.remove(chargePath.size() - 1);
                if(chargePath.contains(166550662047102L) || chargePath.contains(1040901L)){
                    funIdList.addAll(mainFunList.stream().filter(i -> i.getChargeItemId().equals(item.getId())).map(ContractPayFundE :: getId).collect(Collectors.toList()));
                }
            }
            if(CollectionUtils.isEmpty(mainFunList.stream().filter(i -> StringUtils.isNotEmpty(i.getCbApportionId())).collect(Collectors.toList()))){
                log.info("该合同无清单绑定成本数据，无需释放成本数据");
                return ;
            }
        }
        if(!this.getIsCorrelationCost(contractPayConcludeE.getCommunityId(), contractPayConcludeE.getDepartId(), contractPayConcludeE.getId(), null,funIdList)){
            log.info("该合同校验无清单绑定成本数据，无需释放成本数据");
            return ;
        }
        //扣款明细详情
        LambdaQueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper10 = new LambdaQueryWrapper<>();
        queryWrapper10.eq(ContractPayConcludeSettdeductionE::getSettlementId, pay.getId())
                .eq(ContractPayConcludeSettdeductionE :: getDeleted, 0);
        List<ContractPayConcludeSettdeductionE> contractPayConcludeSettdeductionEList = contractPayConcludeSettdeductionMapper.selectList(queryWrapper10);
        //查询对应的结算单明细信息
        LambdaQueryWrapper<ContractPaySettDetailsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPaySettDetailsE::getSettlementId, pay.getId())
                .eq(ContractPaySettDetailsE::getDeleted,0);
        List<ContractPaySettDetailsE> contractPaySettlementConcludeEList = contractPaySettDetailsMapper.selectList(queryWrapper);

        Map<String, BigDecimal> amountMap = contractPaySettlementConcludeEList.stream()
                .filter(Objects::nonNull)
                .filter(e -> e.getPayFundId() != null)
                .collect(Collectors.groupingBy(
                        ContractPaySettDetailsE::getPayFundId,
                        Collectors.mapping(
                                e -> e.getAmount() != null ? e.getAmount() : BigDecimal.ZERO,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        PayPlanPeriodV period = contractPayConcludeSettlementPeriodMapper.getPeriodDate(pay.getId());
        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();

        //TODO-hhb调用成本释放
        DynamicCostIncurredReqF costReq = new DynamicCostIncurredReqF();
        costReq.setBillGuid(pay.getId());
        costReq.setBillCode(pay.getPayFundNumber());
        costReq.setBillName(pay.getTitle());
        costReq.setContractId(contractPayConcludeE.getId());
        costReq.setContractNo(contractPayConcludeE.getContractNo());
        costReq.setContractName(contractPayConcludeE.getName());
        if(isNK){
            ContractPayConcludeE mainPay = contractPayConcludeMapper.queryByContractId(nkConclude.getPid());
            costReq.setContractId(mainPay.getId());
            costReq.setContractNo(mainPay.getContractNo());
            costReq.setContractName(mainPay.getName());
        }
        costReq.setBillTypeName(BillTypeEnum.CONTRACT_SETTLE.getName());
        costReq.setBillTypeEnum(BillTypeEnum.CONTRACT_SETTLE.getCode());
        costReq.setOperationType(OperationTypeEnum.OCCUPY.getCode());
        costReq.setContactor(OperationTypeEnum.OCCUPY.getName());
        costReq.setSystemType(1);
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList = new ArrayList<>();
        for(ContractPayFundE fun : mainFunList.stream().filter(x->funIdList.contains(x.getId())).collect(Collectors.toList())){
            //该清单对应结算明细
            ContractPaySettDetailsE settDetails = contractPaySettlementConcludeEList.stream().filter(
                            x->fun.getId().equals(x.getPayFundId())).findFirst()
                    .orElse(new ContractPaySettDetailsE());
            //该清单对应扣款明细
            ContractPayConcludeSettdeductionE settdeduction = contractPayConcludeSettdeductionEList.stream().filter(
                            x->fun.getChargeItemId().equals(x.getChargeItemId()) && settDetails.getSettlementId().equals(x.getSettlementId())).findFirst()
                    .orElse(new ContractPayConcludeSettdeductionE());


            ContractPayCostPlanReqV funVo = new ContractPayCostPlanReqV();
            funVo.setContractId(contractPayConcludeE.getId());
            funVo.setChargeItemId(fun.getChargeItemId());
            funVo.setAccountItemFullCode(fun.getAccountItemFullCode());
            funVo.setAmountWithOutRate(settDetails.getAmountWithOutRate());
            funVo.setAmountWithOutRateTotal(amountMap.get(fun.getId()));
            funVo.setDeduction(settdeduction.getAmount());
            funVo.setPayFundId(fun.getId());
            funVo.setGmtExpireStart(period.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
            funVo.setGmtExpireEnd(period.getEndDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
            ContractPayCostApportionV funCostData = this.getSettlementCostPlan(funVo);
            if(Objects.isNull(funCostData)){
                continue;
            }
            costReq.setBusinessUnitCode(funCostData.getBusinessUnitCode());
            costReq.setBuGuid(funCostData.getBuGuid());
            costReq.setProjectGuid(funCostData.getProjectGuid());
            costReq.setBusinessGuid(funCostData.getBusinessGuid());

            Map<String, List<ContractPayCostApportionDetailV>> apportionMap = funCostData.getApportionMap();
            ContractPayCostApportionE costApportionE = new ContractPayCostApportionE();
            BeanUtils.copyProperties(funCostData, costApportionE);
            BeanUtils.copyProperties(costReq, costApportionE);
            costApportionE.setContractId(contractPayConcludeE.getId());
            costApportionE.setPid("0");
            costApportionE.setApportionAmount(fun.getAmountWithOutRate());
            costApportionE.setTenantId(identityInfo.getTenantId());
            costApportionE.setCreator(identityInfo.getUserId());
            costApportionE.setCreatorName(identityInfo.getUserName());
            costApportionE.setGmtCreate(now);
            costApportionE.setOperator(identityInfo.getUserId());
            costApportionE.setOperatorName(identityInfo.getUserName());
            costApportionE.setGmtModify(now);
            contractPayCostApportionMapper.insert(costApportionE);
            settDetails.setCbApportionId(costApportionE.getId());
            settDetails.setSummarySurplusAmount(funCostData.getSummarySurplusAmount());
            contractPaySettDetailsMapper.updateById(settDetails);

            List<String> yearList = IntStream.rangeClosed(funVo.getGmtExpireStart().getYear(), funVo.getGmtExpireEnd().getYear())
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
            for(String year : yearList){
                ContractPayCostApportionDetailV useCost = apportionMap.get(year).stream().filter(
                                x->CostApportionTypeEnum.可用金额.getCode().equals(x.getApportionType())).findFirst() // 返回 Optional
                        .orElse(new ContractPayCostApportionDetailV());
                ContractPayCostApportionDetailV ftCost = apportionMap.get(year).stream().filter(
                                x->CostApportionTypeEnum.结算差额.getCode().equals(x.getApportionType())).findFirst() // 返回 Optional
                        .orElse(new ContractPayCostApportionDetailV());
                DynamicCostIncurredReqF.IncurredDataListDTO incurredData = new DynamicCostIncurredReqF.IncurredDataListDTO();
                BeanUtils.copyProperties(ftCost, incurredData);
                incurredData.setAccountItemFullCode(funCostData.getAccountItemFullCode());
                incurredData.setDynamicCostGuid(useCost.getDynamicCostGuid());
                incurredData.setShareYear(year);
                incurredData.setYearShareAmount(ftCost.getYearSurplus());
                incurredData.setJanShareAmount(ftCost.getJanSurplus());
                incurredData.setFebShareAmount(ftCost.getFebSurplus());
                incurredData.setMarShareAmount(ftCost.getMarSurplus());
                incurredData.setAprShareAmount(ftCost.getAprSurplus());
                incurredData.setMayShareAmount(ftCost.getMaySurplus());
                incurredData.setJunShareAmount(ftCost.getJunSurplus());
                incurredData.setJulShareAmount(ftCost.getJulSurplus());
                incurredData.setAugShareAmount(ftCost.getAugSurplus());
                incurredData.setSepShareAmount(ftCost.getSepSurplus());
                incurredData.setOctShareAmount(ftCost.getOctSurplus());
                incurredData.setNovShareAmount(ftCost.getNovSurplus());
                incurredData.setDecShareAmount(ftCost.getDecSurplus());
                incurredDataList.add(incurredData);

                List<ContractPayCostApportionDetailV> apportionList = apportionMap.get(year);
                for(ContractPayCostApportionDetailV copyCost : apportionList){
                    ContractPayCostApportionE costApportionPidE = new ContractPayCostApportionE();
                    BeanUtils.copyProperties(copyCost, costApportionPidE);
                    if(CostApportionTypeEnum.结算差额.getCode().equals(copyCost.getApportionType())){
                        costApportionPidE.setDynamicCostGuid(useCost.getDynamicCostGuid());
                    }
                    costApportionPidE.setContractId(contractPayConcludeE.getId());
                    costApportionPidE.setPid(costApportionE.getId());
                    costApportionPidE.setTenantId(identityInfo.getTenantId());
                    costApportionPidE.setCreator(identityInfo.getUserId());
                    costApportionPidE.setCreatorName(identityInfo.getUserName());
                    costApportionPidE.setGmtCreate(now);
                    costApportionPidE.setOperator(identityInfo.getUserId());
                    costApportionPidE.setOperatorName(identityInfo.getUserName());
                    costApportionPidE.setGmtModify(now);
                    contractPayCostApportionMapper.insert(costApportionPidE);
                }
            }
        }
        if(incurredDataList.size()>0 && isCost){
            costReq.setIncurredAmount(incurredDataList.stream()
                    .map(DynamicCostIncurredReqF.IncurredDataListDTO::getYearShareAmount)
                    .filter(Objects::nonNull)  // 过滤掉null值
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredMeargeDataList = this.mergeIncurredData(incurredDataList);
            costReq.setIncurredDataList(incurredMeargeDataList);
            log.info("结算单审批通过，成本占用入参：{}", JSONArray.toJSON(costReq));
            CostBaseResponse<List<DynamicCostIncurredRespF>> shifangData = externalFeignClient.getDynamicCostIncurred(costReq);
            log.info("结算单审批通过，成本占用出参：{}", JSONArray.toJSON(shifangData));
            if(!shifangData.getSuccess()){
                throw new OwlBizException("结算单审批通过，调用成本失败："+ shifangData.getMessage());
            }
        }
    }

    //根据项目id，部门ID及费项ID哦判断是否交互成本合约规划数据
    public Boolean getIsCorrelationCost(String communityId, String departId, String contractId, ChargeItemRv chargeItemRv, List<String> funIdList){
        Boolean result = Boolean.FALSE;
        List<ContractPayCostCommunityE> communityList = contractPayCostCommunityMapper.selectList(new QueryWrapper<ContractPayCostCommunityE>());
        if(CollectionUtils.isEmpty(communityList)){
            return result;
        }
        List<String> communityIdList = communityList.stream().map(ContractPayCostCommunityE :: getCommunityId).collect(Collectors.toList());
        if(!communityIdList.contains(communityId)){
            log.info("该项目【{}】不在成本交互项目清单列表，不与成本进行合约规划",communityId);
            return result;
        }
        //获取项目详细信息
        SpaceCommunityRv spaceCommunity = spaceFeignClient.getById(communityId);
        if(Objects.isNull(spaceCommunity)){
            log.info("项目【{}】不存在，请输入正确项目ID",communityId);
            return result;
        }
        //虚拟项目直接返回
        Long xnId = 13590120111815L;
        if(xnId.equals(spaceCommunity.getType())){
            log.info("该项目：【{}】为虚拟项目，不查询成本合约规划信息",communityId);
            return result;
        }
        OrgIdsF orgIds = new OrgIdsF();
        List<Long> org = new ArrayList<>();
        org.add(Long.valueOf(departId));
        orgIds.setOrgIds(org);
        List<OrgInfoV> orgList = orgFeignClient.getOrgListByOrgIds(orgIds);
        if(CollectionUtils.isEmpty(orgList)){
            log.info("未查询到该部门：【{}】信息，不查询成本合约规划信息",departId);
            return result;
        }
        //中交集团,中交房地产,常设公司,中交地产,常设公司,中交服务,分公司
        if(!orgList.get(0).getStandardOrgPath().contains("101476177")){
            log.info("该部门：【{}】非指定部门下部门，不查询成本合约规划信息",departId);
            return result;
        }

        if(Objects.nonNull(chargeItemRv) && !chargeItemRv.getChargePath().contains(166550662047102L) && !chargeItemRv.getChargePath().contains(1040901L)){
            log.info("该费项：【{}】非指定费项，不查询成本合约规划信息",chargeItemRv.getName());
            return result;
        }
        if(StringUtils.isNotEmpty(contractId)){
            ContractPayConcludeE concludeE = contractPayConcludeMapper.queryByContractId(contractId);
            /*if(ContractTypeEnum.修改合同.getCode().equals(concludeE.getContractType())){
                log.info("修改合同，不同成本交互合约规划信息");
                return result;
            }*/
            if((Objects.nonNull(concludeE.getIsHy()) && concludeE.getIsHy() == 1)){
                log.info("HY合同，不同成本交互合约规划信息");
                return result;
            }
            if(CollectionUtils.isEmpty(funIdList)){
                log.info("无符合清单数据，不同成本交互合约规划信息");
                return result;
            }
            //获取该合同清单数据
            List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .eq(ContractPayFundE.CONTRACT_ID, contractId)
                    .eq(ContractPayFundE.IS_HY, 0)
                    .eq(ContractPayFundE.DELETED, 0)
                    .in(ContractPayFundE.ID, funIdList));
            if(CollectionUtils.isEmpty(mainFunList)){
                log.info("该合同无非HY清单数据，直接发起审批");
                return result;
            }
            //校验当前合同是否为主合同
            /*if(!concludeE.getPid().equals("0")){
                List<ContractPayFundE> mainNoHyFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                        .orderByDesc(ContractPayFundE.GMT_CREATE)
                        .eq(ContractPayFundE.TENANT_ID, tenantId())
                        .eq(ContractPayFundE.CONTRACT_ID, contractId)
                        .eq(ContractPayFundE.IS_HY, 0)
                        .eq(ContractPayFundE.DELETED, 0));
                Boolean isAllHaveCost = mainNoHyFunList.stream()
                        .anyMatch(item -> item.getAccountItemFullCode() == null ||
                                item.getAccountItemFullCode().isEmpty());
                if(isAllHaveCost){
                    log.info("当前合同主合同未关联成本合约规划信息，直接发起审批");
                    return result;
                }
            }*/

            // 判断是否存在accountItemFullCode为空的数据
      /*      Boolean isAllHaveCost = mainFunList.stream()
                    .anyMatch(item -> item.getAccountItemFullCode() == null ||
                            item.getAccountItemFullCode().isEmpty());
            if(isAllHaveCost){
                throw new OwlBizException("该合同清单中含无合约规划数据，请补充后再次提交");
            }*/
        }
        return Boolean.TRUE;
    }


    @Transactional(rollbackFor = Exception.class)
    public void releasePaySettlementCost(ContractPaySettlementConcludeE settlement, ContractPayConcludeE payConcludeE, List<String> apportionIdList){
        log.info("结算审批释放成本占用逻辑{}，",settlement.getId());

        DynamicCostIncurredReqF costReq = new DynamicCostIncurredReqF();
        costReq.setBillGuid(settlement.getId());
        costReq.setBillCode(settlement.getPayFundNumber());
        costReq.setBillName(settlement.getTitle());
        costReq.setContractId(payConcludeE.getId());
        costReq.setContractNo(payConcludeE.getContractNo());
        costReq.setContractName(payConcludeE.getName());
        costReq.setBillTypeName(BillTypeEnum.CONTRACT_SETTLE.getName());
        costReq.setBillTypeEnum(BillTypeEnum.CONTRACT_SETTLE.getCode());
        costReq.setOperationType(OperationTypeEnum.RELEASE.getCode());
        costReq.setContactor(OperationTypeEnum.RELEASE.getName());
        costReq.setSystemType(1);
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList = new ArrayList<>();
        List<String> costIdList = new ArrayList<>();
        List<ContractPayCostApportionE> costApportionEList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                .eq(ContractPayCostApportionE.CONTRACT_ID, payConcludeE.getId())
                .eq(ContractPayCostApportionE.DELETED, 0)
                .and(wrapper -> wrapper
                        .in(ContractPayCostApportionE.ID, apportionIdList)
                        .or()
                        .in(ContractPayCostApportionE.PID, apportionIdList)
                )
        );

        List<ContractPayCostApportionE> mainCostList = costApportionEList.stream().filter(x->x.getPid().equals("0")).collect(Collectors.toList());
        for(ContractPayCostApportionE cost : mainCostList){
            ContractPayCostApportionE mainCost = costApportionEList.stream().filter(x->cost.getId().equals(x.getId())).findFirst().orElse(new ContractPayCostApportionE());
            costReq.setProjectGuid(mainCost.getProjectGuid());
            costReq.setBuGuid(mainCost.getBuGuid());
            costReq.setBusinessGuid(mainCost.getBusinessGuid());
            costReq.setBusinessUnitCode(mainCost.getBusinessUnitCode());

            List<ContractPayCostApportionE> yearCostList = costApportionEList.stream().filter(x->cost.getId().equals(x.getPid()) && CostApportionTypeEnum.结算差额.getCode().equals(x.getApportionType())).collect(Collectors.toList());
            for(ContractPayCostApportionE ftCost : yearCostList){
                DynamicCostIncurredReqF.IncurredDataListDTO incurredData = new DynamicCostIncurredReqF.IncurredDataListDTO();
                BeanUtils.copyProperties(ftCost, incurredData);
                incurredData.setAccountItemFullCode(mainCost.getAccountItemFullCode());
                incurredData.setDynamicCostGuid(ftCost.getDynamicCostGuid());
                incurredData.setShareYear(ftCost.getYear());
                incurredData.setYearShareAmount(this.getBigDecimalNegate(ftCost.getYearSurplus()));
                incurredData.setJanShareAmount(this.getBigDecimalNegate(ftCost.getJanSurplus()));
                incurredData.setFebShareAmount(this.getBigDecimalNegate(ftCost.getFebSurplus()));
                incurredData.setMarShareAmount(this.getBigDecimalNegate(ftCost.getMarSurplus()));
                incurredData.setAprShareAmount(this.getBigDecimalNegate(ftCost.getAprSurplus()));
                incurredData.setMayShareAmount(this.getBigDecimalNegate(ftCost.getMaySurplus()));
                incurredData.setJunShareAmount(this.getBigDecimalNegate(ftCost.getJunSurplus()));
                incurredData.setJulShareAmount(this.getBigDecimalNegate(ftCost.getJulSurplus()));
                incurredData.setAugShareAmount(this.getBigDecimalNegate(ftCost.getAugSurplus()));
                incurredData.setSepShareAmount(this.getBigDecimalNegate(ftCost.getSepSurplus()));
                incurredData.setOctShareAmount(this.getBigDecimalNegate(ftCost.getOctSurplus()));
                incurredData.setNovShareAmount(this.getBigDecimalNegate(ftCost.getNovSurplus()));
                incurredData.setDecShareAmount(this.getBigDecimalNegate(ftCost.getDecSurplus()));
                incurredDataList.add(incurredData);
            }
        }

        //contractPayCostApportionMapper.deletedCostData(payConcludeE.getId(),apportionIdList);

        costReq.setIncurredAmount(incurredDataList.stream()
                .map(DynamicCostIncurredReqF.IncurredDataListDTO::getYearShareAmount)
                .filter(Objects::nonNull)  // 过滤掉null值
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredMeargeDataList = this.mergeIncurredData(incurredDataList);
        costReq.setIncurredDataList(incurredMeargeDataList);
        log.info("结算审批释放成本占用逻辑，成本数据释放入参：{}", JSONArray.toJSON(costReq));
        CostBaseResponse<List<DynamicCostIncurredRespF>> shifangData = externalFeignClient.getDynamicCostIncurred(costReq);
        log.info("结算审批释放成本占用逻辑，成本数据释放出参：{}", JSONArray.toJSON(shifangData));
        if(!shifangData.getSuccess()){
            throw new OwlBizException("结算审批释放成本占用逻辑，成本数据释放失败："+ shifangData.getMessage());
        }
    }

}
