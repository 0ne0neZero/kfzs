package com.wishare.contract.apps.service.revision.bond.pay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.fo.revision.attachment.AttachmentSaveF;
import com.wishare.contract.apps.fo.revision.bond.BondVolumUpF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondBillF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayAddF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayEditF;
import com.wishare.contract.apps.remote.clients.*;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.BondRefundStatusEnum;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.enums.revision.FileSaveTypeEnum;
import com.wishare.contract.domains.enums.revision.bond.BondActionTypeEnum;
import com.wishare.contract.domains.enums.revision.bond.BondDealWayEnum;
import com.wishare.contract.domains.enums.revision.bond.BondStatusEnum;
import com.wishare.contract.domains.enums.revision.bond.BondTypeEnum;
import com.wishare.contract.domains.mapper.revision.bond.pay.PayBondRelationBillMapper;
import com.wishare.contract.domains.mapper.revision.bond.pay.RevisionBondPayMapper;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.bond.pay.PayBondRelationBillService;
import com.wishare.contract.domains.service.revision.bond.pay.RevisionBondPayService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.vo.revision.bond.BondNumShowV;
import com.wishare.contract.domains.vo.revision.bond.pay.BondPayDetailV;
import com.wishare.contract.domains.vo.revision.bond.pay.VolumeUpV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.owl.util.OptionalCollection;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/28  10:39
 */
@Service
@Slf4j
public class BondPayBusinessService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeService contractIncomeConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondPayService revisionBondPayService;

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondPayMapper revisionBondPayMapper;

    @Setter(onMethod_ = {@Autowired})
    private PayBondRelationBillMapper bondRelationBillMapper;

    @Setter(onMethod_ = {@Autowired})
    private PayBondRelationBillService bondRelationBillService;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private SpaceFeignClient spaceFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private AttachmentService attachmentService;

    /**
     * 新增方法
     * @param form 新增请求参数
     * @return 主键ID
     */
    @Transactional
    public String add(RevisionBondPayAddF form) {
        RevisionBondPayE map = Global.mapperFacade.map(form, RevisionBondPayE.class);

        //-- 处理参数赋值信息
        dealValueForAdd(map);
        //-- 计划编码
        map.setCode(getCodeForAdd(tenantId()));
        //-- 状态
        map.setStatus(BondStatusEnum.待提交.getCode())
                .setTenantId(tenantId());

        //-- 校验保证金总额与计划金额是否相等
        if (map.getBondAmount().compareTo(map.getPlannedPayAmount()) != 0) {
            throw new OwlBizException("保证金总额须等于计划付款金额，请重新编辑");
        }

        map.setPayAmount(BigDecimal.ZERO)
                .setReceiptAmount(BigDecimal.ZERO)
                .setSettleTransferAmount(BigDecimal.ZERO)
                .setCollectAmount(BigDecimal.ZERO);

        revisionBondPayMapper.insert(map);

        //-- 非暂存，则执行提交操作
        if (!Boolean.TRUE.equals(form.getIsStash())) {
            postBondPlan(map.getId());
        }

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF().setBusinessType(FileSaveTypeEnum.保证金附件凭证.getCode())
                    .setFileVo(form.getFileVo())
                    .setBusinessId(map.getId()));
        }

        return map.getId();
    }

    public Boolean edit(RevisionBondPayEditF form) {
        RevisionBondPayE map = Global.mapperFacade.map(form, RevisionBondPayE.class);

        //-- 校验保证金总额与计划金额是否相等
        if (map.getBondAmount().compareTo(map.getPlannedPayAmount()) != 0) {
            throw new OwlBizException("保证金总额须等于计划付款金额，请重新编辑");
        }

        //-- 处理参数赋值信息
        dealValueForEdit(map);

        revisionBondPayService.updateById(map);

        //-- 非暂存，则执行提交操作
        if (!Boolean.TRUE.equals(form.getIsStash())) {
            postBondPlan(map.getId());
        }

        return Boolean.TRUE;
    }


    public void dealValueForAdd(RevisionBondPayE map) {
        //-- 保证金类型
        map.setType(BondTypeEnum.parseName(map.getTypeCode()));
        //-- 客户名称
        Optional.ofNullable(orgFeignClient.getCustomerVById(map.getCustomerId())).ifPresentOrElse(v -> {
            if (!v.getDisabled().equals(0)) {
                throw new OwlBizException("客户数据已被禁用，请重新选择");
            }
            map.setCustomer(v.getName());
        }, () -> {throw new OwlBizException("根据客户ID检索客户数据失败");});
        //-- 合同数据检索赋值
        if (StringUtils.isNotBlank(map.getContractId())) {
            ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeService.getById(map.getContractId());
            if (Objects.isNull(incomeConcludeE)) {
                throw new OwlBizException("根据合同ID检索数据失败");
            }
            map.setContractCode(incomeConcludeE.getContractNo())
                        .setContractName(incomeConcludeE.getName());
        }
        //-- 成本中心
        if (StringUtils.isNotBlank(map.getCostCenterId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceCostId(Long.parseLong(map.getCostCenterId()))).ifPresentOrElse(v -> {
                map.setCostCenterName(v.getNameCn());
                if (Objects.nonNull(v.getCommunityId())) {
                    map.setCommunityId(v.getCommunityId());
                    Optional.ofNullable(spaceFeignClient.getById(v.getCommunityId())).ifPresent(c -> {
                        map.setCommunityName(c.getName());
                    });
                }
            }, () -> {throw new OwlBizException("根据成本中心ID检索成本中心数据失败");});
        }
        //-- 所属项目
        Optional.ofNullable(spaceFeignClient.getById(map.getCommunityId())).ifPresentOrElse(c -> {
            map.setCommunityName(c.getName());
        }, () -> {throw new OwlBizException("根据所属项目ID检索项目数据失败");});
        //-- 所属部门
        if (StringUtils.isNotBlank(map.getOrgId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(map.getOrgId()))).ifPresentOrElse(v -> {
                map.setOrgName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属部门ID检索组织名称失败");});
        } else {
            throw new OwlBizException("所属部门不可为空");
        }
        //-- 负责人
        Optional.ofNullable(userFeignClient.getUsreInfoByUserId(map.getChargeManId())).ifPresentOrElse(v -> {
            map.setChargeMan(v.getUserName());
        }, () -> {throw new OwlBizException("根据负责人ID检索名称失败");});
    }

    public void dealValueForEdit(RevisionBondPayE map) {
        //-- 保证金类型
        map.setType(BondTypeEnum.parseName(map.getTypeCode()));
        //-- 客户名称
        Optional.ofNullable(orgFeignClient.getCustomerVById(map.getCustomerId())).ifPresentOrElse(v -> {
            if (!v.getDisabled().equals(0)) {
                throw new OwlBizException("客户数据已被禁用，请重新选择");
            }
            map.setCustomer(v.getName());
        }, () -> {throw new OwlBizException("根据客户ID检索客户数据失败");});
        //-- 合同数据检索赋值
        if (StringUtils.isNotBlank(map.getContractId())) {
            ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeService.getById(map.getContractId());
            if (Objects.isNull(incomeConcludeE)) {
                throw new OwlBizException("根据合同ID检索数据失败");
            }
            map.setContractCode(incomeConcludeE.getContractNo())
                    .setContractName(incomeConcludeE.getName());
        }
        //-- 成本中心
        if (StringUtils.isNotBlank(map.getCostCenterId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceCostId(Long.parseLong(map.getCostCenterId()))).ifPresentOrElse(v -> {
                map.setCostCenterName(v.getNameCn());
                if (Objects.nonNull(v.getCommunityId())) {
                    map.setCommunityId(v.getCommunityId());
                    Optional.ofNullable(spaceFeignClient.getById(v.getCommunityId())).ifPresent(c -> {
                        map.setCommunityName(c.getName());
                    });
                }
            }, () -> {throw new OwlBizException("根据成本中心ID检索成本中心数据失败");});
        }
        //-- 所属项目
        Optional.ofNullable(spaceFeignClient.getById(map.getCommunityId())).ifPresentOrElse(c -> {
            map.setCommunityName(c.getName());
        }, () -> {throw new OwlBizException("根据所属项目ID检索项目数据失败");});
        //-- 所属部门
        if (StringUtils.isNotBlank(map.getOrgId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(map.getOrgId()))).ifPresentOrElse(v -> {
                map.setOrgName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属部门ID检索组织名称失败");});
        } else {
            throw new OwlBizException("所属部门不可为空");
        }
        //-- 负责人
        Optional.ofNullable(userFeignClient.getUsreInfoByUserId(map.getChargeManId())).ifPresentOrElse(v -> {
            map.setChargeMan(v.getUserName());
        }, () -> {throw new OwlBizException("根据负责人ID检索名称失败");});
    }

    public String getCodeForAdd(String tenantId) {
        String code = null;
        //生成保证金编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());


        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        String nature = "";

        Integer count = 0;

        nature = "BZJJN";
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(RevisionBondPayE.TENANT_ID, tenantId)
                .ge(RevisionBondPayE.GMT_CREATE, LocalDate.now().atStartOfDay());
        List<RevisionBondPayE> list = revisionBondPayService.list(queryWrapper);
        count = list.size();

        //编号
        String code1 = String.format("%0" + 4 + "d", count + 1);
        if (null != tenantInfoRv && org.springframework.util.StringUtils.hasText(tenantInfoRv.getEnglishName())) {
            code = tenantInfoRv.getEnglishName() + nature + year + code1;
        } else {
            code = nature + year + code1;
        }

        return code;
    }

    public Boolean postBondPlan(String id){
        RevisionBondPayE bondE = revisionBondPayService.getById(id);
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("该保证金计划数据不存在");
        }

        //-- 默认审批通过
        bondE.setStatus(BondStatusEnum.未完成.getCode())
                .setPayAmount(BigDecimal.ZERO)
                .setReceiptAmount(BigDecimal.ZERO)
                .setSettleTransferAmount(BigDecimal.ZERO)
                .setCollectAmount(BigDecimal.ZERO);

        revisionBondPayService.updateById(bondE);

        return true;
    }

    public BondPayDetailV detail(String id) {

        RevisionBondPayE entity = revisionBondPayService.getById(id);

        if (Objects.isNull(entity)) {
            return null;
        }

        BondPayDetailV detailV = Global.mapperFacade.map(entity, BondPayDetailV.class);

        detailV.setBondId(detailV.getId())
                .setFiles(attachmentService.listById(id))
                .setResidueAmount((detailV.getPayAmount().subtract(detailV.getSettleTransferAmount())).subtract(detailV.getCollectAmount()));

        revisionBondPayService.dealBtnShow(detailV);

        return detailV;
    }

    public String getBillCode(String actionType, String suffix) {

        String code = null;
        //生成保证金编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());


        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId());

        Integer count = 0;
        List<PayBondRelationBillE> list = bondRelationBillService.list(new QueryWrapper<PayBondRelationBillE>()
                .eq(PayBondRelationBillE.TENANT_ID, tenantId())
                .eq(PayBondRelationBillE.TYPE_CODE, actionType)
                .ge(PayBondRelationBillE.GMT_CREATE, LocalDate.now().atStartOfDay()));
        count = list.size();

        String code1 = String.format("%0" + 4 + "d", count + 1);
        if (null != tenantInfoRv && org.springframework.util.StringUtils.hasText(tenantInfoRv.getEnglishName())) {
            code = tenantInfoRv.getEnglishName() + suffix + year + code1;
        } else {
            code = suffix + year + code1;
        }

        return code;
    }

    @Transactional
    public Boolean pay(PayBondBillF form) {
        PayBondRelationBillE map = Global.mapperFacade.map(form, PayBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondPayE bondE = revisionBondPayService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getPayAmount().compareTo(bondE.getPlannedPayAmount()) == 0 || BondStatusEnum.已完成.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金已经完成付款，无法进行付款操作");
        }
        if (bondE.getPayAmount().add(map.getAmount()).compareTo(bondE.getPlannedPayAmount()) > 0) {
            throw new OwlBizException("已付金额 + 本次付款金额 大于计划付款金额，无法进行付款操作");
        }

        //-- 生成明细记录
        map.setCode(getBillCode(BondActionTypeEnum.缴纳类付款.getCode(), "BZJFKD"))
                .setTypeCode(BondActionTypeEnum.缴纳类付款.getCode())
                .setType(BondActionTypeEnum.缴纳类付款.getName())
                .setStatus(BondStatusEnum.已完成.getCode());

        OptionalCollection.ofNullable(configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.付款方式.getCode(), map.getDealWayCode())).ifNotEmptyOrElse(v -> {
            map.setDealWay(v.get(0).getName());
        }, () -> {throw new OwlBizException("交易方式信息存在错误");});

        map.setTenantId(tenantId());

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setPayAmount(bondE.getPayAmount().add(map.getAmount()));
        if (bondE.getPayAmount().compareTo(bondE.getPlannedPayAmount()) == 0) {
            bondE.setStatus(BondStatusEnum.已完成.getCode());
        }
        //-- 更新退款状态
        if (!BondRefundStatusEnum.未退款.getCode().equals(bondE.getRefundStatus())) {
            bondE.setRefundStatus(bondE.getPayAmount().subtract(bondE.getCollectAmount().add(bondE.getSettleTransferAmount())).compareTo(BigDecimal.ZERO) == 0
                    ? BondRefundStatusEnum.已退款.getCode() : BondRefundStatusEnum.部分退款.getCode());
        }
        revisionBondPayService.updateById(bondE);

        return true;
    }

    @Transactional
    public Boolean transfer(PayBondBillF form) {
        PayBondRelationBillE map = Global.mapperFacade.map(form, PayBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondPayE bondE = revisionBondPayService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getPayAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getPayAmount().compareTo(bondE.getSettleTransferAmount().add(bondE.getCollectAmount())) == 0) {
            throw new OwlBizException("保证金计划已付金额为0 或 保证金可结转金额为0， 无法进行结转操作");
        }
        if (bondE.getCollectAmount().add(bondE.getSettleTransferAmount().add(map.getAmount())).compareTo(bondE.getPayAmount()) > 0) {
            throw new OwlBizException("已结转金额 + 已收款金额 + 本次结转金额大于已付款金额，无法进行结转操作");
        }

        //-- 生成明细记录
        map.setCode(getBillCode(BondActionTypeEnum.缴纳类结转.getCode(), "BZJJZD"))
                .setTypeCode(BondActionTypeEnum.缴纳类结转.getCode())
                .setType(BondActionTypeEnum.缴纳类结转.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getPayAmount().subtract(bondE.getSettleTransferAmount().add(map.getAmount())));
        if (StringUtils.isBlank(map.getChargeItemId())) {
            throw new OwlBizException("结转费项信息不可为空");
        }
        Optional.ofNullable(financeFeignClient.chargeName(Long.parseLong(map.getChargeItemId()))).ifPresentOrElse(v -> {
            map.setChargeItem(v);
        }, () -> {throw new OwlBizException("根据结转费项ID检索费项信息失败");});

        map.setTenantId(tenantId());

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setSettleTransferAmount(bondE.getSettleTransferAmount().add(map.getAmount()));
        revisionBondPayService.updateById(bondE);

        return true;
    }

    public Boolean collect(PayBondBillF form) {
        PayBondRelationBillE map = Global.mapperFacade.map(form, PayBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondPayE bondE = revisionBondPayService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getPayAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getPayAmount().compareTo(bondE.getSettleTransferAmount().add(bondE.getCollectAmount())) == 0) {
            throw new OwlBizException("保证金计划已付金额为0 或 保证金可收款金额为0， 无法进行收款操作");
        }
        if (bondE.getCollectAmount().add(bondE.getSettleTransferAmount().add(map.getAmount())).compareTo(bondE.getPayAmount()) > 0) {
            throw new OwlBizException("已结转金额 + 已收款金额 + 本次收款金额大于已付款金额，无法进行收款操作");
        }

        //-- 生成明细记录
        map.setCode(getBillCode(BondActionTypeEnum.缴纳类收款.getCode(), "BZJSKD"))
                .setTypeCode(BondActionTypeEnum.缴纳类收款.getCode())
                .setType(BondActionTypeEnum.缴纳类收款.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getPayAmount().subtract(bondE.getCollectAmount().add(map.getAmount())));

        OptionalCollection.ofNullable(configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收款方式.getCode(), map.getDealWayCode())).ifNotEmptyOrElse(v -> {
            map.setDealWay(v.get(0).getName());
        }, () -> {throw new OwlBizException("交易方式信息存在错误");});

        map.setTenantId(tenantId());

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setCollectAmount(bondE.getCollectAmount().add(map.getAmount()))
                .setRefundAmount(bondE.getCollectAmount().add(map.getAmount()));//-- 已退款金额
        bondE.setRefundStatus(bondE.getPayAmount().subtract(bondE.getCollectAmount().add(bondE.getSettleTransferAmount())).compareTo(BigDecimal.ZERO) == 0
                ? BondRefundStatusEnum.已退款.getCode() : BondRefundStatusEnum.部分退款.getCode());
        revisionBondPayService.updateById(bondE);

        return true;
    }

    public Boolean receipt(PayBondBillF form) {
        PayBondRelationBillE map = Global.mapperFacade.map(form, PayBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondPayE bondE = revisionBondPayService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getPayAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getPayAmount().compareTo(bondE.getReceiptAmount()) == 0) {
            throw new OwlBizException("保证金计划已付金额为0 或 保证金可收据金额为0， 无法进行收据操作");
        }
        if (bondE.getReceiptAmount().add(map.getAmount()).compareTo(bondE.getPayAmount()) > 0) {
            throw new OwlBizException("已收据金额 + 本次收据金额大于已付款金额，无法进行收据操作");
        }

        map.setCode(getBillCode(BondActionTypeEnum.缴纳类收据.getCode(), "BZJSJD"))
                .setTypeCode(BondActionTypeEnum.缴纳类收据.getCode())
                .setType(BondActionTypeEnum.缴纳类收据.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getPayAmount().subtract(bondE.getReceiptAmount().add(map.getAmount())));

        map.setTenantId(tenantId());

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setReceiptAmount(bondE.getReceiptAmount().add(map.getAmount()));
        revisionBondPayService.updateById(bondE);

        return true;
    }


    public BondNumShowV getNumShowForPage(PageF<SearchF<RevisionBondPayE>> request) {

        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        SearchF<RevisionBondPayE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.eq(RevisionBondCollectE.TENANT_ID, tenantId())
                .orderByAsc(RevisionBondCollectE.STATUS)
                .orderByDesc(RevisionBondCollectE.GMT_CREATE);

        List<RevisionBondPayE> list = revisionBondPayService.list(queryWrapper);

        if (CollectionUtils.isEmpty(list)) {
            return new BondNumShowV()
                    .setAmount(BigDecimal.ZERO)
                    .setPlanAmount(BigDecimal.ZERO);
        }

        BigDecimal planAmount = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;

        for (RevisionBondPayE bondE : list) {
            planAmount = planAmount.add(bondE.getPlannedPayAmount());
            amount = amount.add(bondE.getPayAmount());
        }

        return new BondNumShowV().setPlanAmount(planAmount).setAmount(amount);
    }


    /**
     * 转履约
     * 对原保证金的数据进行更新
     * 生成一条新的保证金记录
     * 生成一条结转记录，并关联相关保证金记录数据
     * @param form 转履约传参
     * @return
     */
    @Transactional
    public Boolean volumeUp (BondVolumUpF form) {
        PayBondRelationBillE map = Global.mapperFacade.map(form, PayBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondPayE bondE = revisionBondPayService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getPayAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getPayAmount().compareTo(bondE.getSettleTransferAmount().add(bondE.getCollectAmount())) == 0) {
            throw new OwlBizException("保证金计划已付金额为0 或 保证金可结转金额为0， 无法进行结转操作");
        }
        if (bondE.getCollectAmount().add(bondE.getSettleTransferAmount().add(map.getAmount())).compareTo(bondE.getPayAmount()) > 0) {
            throw new OwlBizException("已结转金额 + 已收款金额 + 本次结转金额大于已付款金额，无法进行结转操作");
        }

        //-- 对原保证金计划进行更新
        bondE.setSettleTransferAmount(bondE.getSettleTransferAmount().add(map.getAmount()));
        revisionBondPayService.updateById(bondE);

        //-- 生成一条新的保证金记录
        RevisionBondPayE addF = Global.mapperFacade.map(bondE, RevisionBondPayE.class);
        addF.setTypeCode(BondTypeEnum.履约保证金.getCode())
                .setType(BondTypeEnum.履约保证金.getName())
                .setId(null)
                .setGmtCreate(null)
                .setGmtModify(null)
                .setCreator(null)
                .setCreatorName(null)
                .setOperator(null)
                .setOperatorName(null)
                .setTenantId(tenantId())
                .setCode(getCodeForAdd(tenantId()))
                .setStatus(BondStatusEnum.待提交.getCode())
                .setPayAmount(BigDecimal.ZERO)
                .setReceiptAmount(BigDecimal.ZERO)
                .setSettleTransferAmount(BigDecimal.ZERO)
                .setCollectAmount(BigDecimal.ZERO)
                .setBondAmount(map.getAmount())
                .setPlannedPayAmount(map.getAmount());
        //-- 关联投标保证金相关字段赋值
        addF.setIsTender(1)
                .setTenderBondId(bondE.getId())
                .setTenderBondAmount(bondE.getBondAmount());

        //-- 合同数据检索赋值
        if (StringUtils.isNotBlank(form.getContractId())) {
            ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeService.getById(form.getContractId());
            if (Objects.isNull(incomeConcludeE)) {
                throw new OwlBizException("根据合同ID检索数据失败");
            }
            addF.setContractId(form.getContractId())
                    .setContractCode(incomeConcludeE.getContractNo())
                    .setContractName(incomeConcludeE.getName());
        }
        revisionBondPayMapper.insert(addF);
        postBondPlan(addF.getId());

        //-- 生成一条结转记录
        map.setCode(getBillCode(BondActionTypeEnum.缴纳类结转.getCode(), "BZJJZD"))
                .setTypeCode(BondActionTypeEnum.缴纳类结转.getCode())
                .setType(BondActionTypeEnum.缴纳类结转.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getPayAmount().subtract(bondE.getSettleTransferAmount().add(map.getAmount())));
        if (StringUtils.isNotBlank(map.getChargeItemId())) {
            Optional.ofNullable(financeFeignClient.chargeName(Long.parseLong(map.getChargeItemId()))).ifPresentOrElse(v -> {
                map.setChargeItem(v);
            }, () -> {throw new OwlBizException("根据结转费项ID检索费项信息失败");});
        }
        map.setTenantId(tenantId())
                .setVolumUpId(addF.getId());
        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }


        return true;
    }

    /**
     * 获取保证金转履约记录
     * @param id 保证金ID
     * @return 转履约记录
     */
    public List<VolumeUpV> getVolumRecord(String id) {
        //-- 校验关联保证金计划
        RevisionBondPayE bondE = revisionBondPayService.getById(id);
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        List<PayBondRelationBillE> list = bondRelationBillService.list(new QueryWrapper<PayBondRelationBillE>()
                .eq(PayBondRelationBillE.BOND_ID, id)
                .isNotNull(PayBondRelationBillE.VOLUM_UP_ID)
                .orderByDesc(PayBondRelationBillE.GMT_CREATE));

        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        List<VolumeUpV> result = new ArrayList<>();

        for (PayBondRelationBillE billE : list) {
            VolumeUpV volume = new VolumeUpV();
            volume.setId(bondE.getId())
                    .setCode(bondE.getCode())
                    .setGmtCreate(bondE.getGmtCreate())
                    .setCreator(bondE.getCreator())
                    .setCreatorName(bondE.getCreatorName())
                    .setDate(billE.getGmtCreate().toLocalDate());
            RevisionBondPayE e = revisionBondPayService.getById(billE.getVolumUpId());
            if (Objects.nonNull(e)) {
                volume.setTransferId(e.getId())
                        .setTransferCode(e.getCode())
                        .setAmount(e.getPlannedPayAmount());
            }
            result.add(volume);
        }

        return result;
    }

}
