package com.wishare.finance.apps.service.reconciliation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.finance.apps.model.bill.vo.ContractFlowBillV;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptStatisticsDto;
import com.wishare.finance.apps.model.reconciliation.consts.enums.ClaimTypeEnum;
import com.wishare.finance.apps.model.reconciliation.dto.FlowStatisticsDto;
import com.wishare.finance.apps.model.reconciliation.fo.*;
import com.wishare.finance.apps.model.reconciliation.vo.*;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJV;
import com.wishare.finance.apps.service.bill.AllBillAppService;
import com.wishare.finance.apps.service.bill.BillAppServiceOld;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.AllBillPageDto;
import com.wishare.finance.domains.bill.dto.FlowBillPageDto;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.BankAccountCostOrgRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.domains.reconciliation.command.AddFlowDetailCommand;
import com.wishare.finance.domains.reconciliation.command.ClaimFlowCommand;
import com.wishare.finance.domains.reconciliation.dto.FlowReconciliationDetailDto;
import com.wishare.finance.domains.reconciliation.entity.*;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimStatusEnum;
import com.wishare.finance.domains.reconciliation.enums.StaCostEnum;
import com.wishare.finance.domains.reconciliation.facade.ReconciliationOrgFacade;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimGatherDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.domains.reconciliation.service.FlowClaimRecordDomainService;
import com.wishare.finance.domains.reconciliation.service.FlowDetailDomainService;
import com.wishare.finance.domains.voucher.entity.VoucherBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherBillDetailRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileLoadVo;
import com.wishare.tools.starter.vo.FileVo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.mail.FetchProfile;
import javax.servlet.http.HttpServletResponse;

import jdk.security.jarsigner.JarSigner;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 流水领用应用层
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RefreshScope
public class FlowClaimAppService implements ApiBase {

    private final FlowClaimRecordDomainService flowClaimRecordDomainService;

    private final FlowDetailDomainService flowDetailDomainService;

    private final InvoiceDomainService invoiceDomainService;

    private final ReconciliationOrgFacade reconciliationOrgFacade;

    private final BillAppServiceOld billAppServiceOld;

    private final AllBillAppService allBillAppService;

    private final FlowClaimDetailRepository flowClaimDetailRepository;
    private final FlowClaimRecordRepository flowClaimRecordRepository;

    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;
    private final GatherDetailRepository gatherDetailRepository;

    private final VoucherBillDetailRepository voucherBillDetailRepository;
    private final SpaceClient spaceClient;
    @Value("${wishare.file.template.flow:}")
    private String fileKey;

    private final FileStorage fileStorage;

    private final FlowDetailRepository flowDetailRepository;

    private final FlowClaimGatherDetailRepository flowClaimGatherDetailRepository;

    private final FlowTempAppService flowTempAppService;

    private final GatherBillRepository gatherBillRepository;

    private final VoucherPushBillZJRepository voucherPushBillZJRepository;

    private final OrgClient orgClient;

    private final BankAccountCostOrgRepository bankAccountCostOrgRepository;
    private final ConfigClient configClient;

    /**
     * 分页查询领用记录
     *
     * @param queryF queryF
     * @return PageV
     */
    public PageV<FlowClaimRecordPageV> pageRecord(PageF<SearchF<FlowClaimRecordE>> queryF) {
        IPage<FlowClaimRecordE> flowDetailPage = flowClaimRecordDomainService.queryRecordPage(queryF);
        List<FlowClaimRecordPageV> pageDataList = Global.mapperFacade.mapAsList(flowDetailPage.getRecords(), FlowClaimRecordPageV.class);
        return PageV.of(flowDetailPage.getCurrent(), flowDetailPage.getSize(), flowDetailPage.getTotal(), pageDataList);
    }


    /**
     * 流水认领-差额认领实现方法
     *
     * @param claimFlowF
     * @return
     */
    public FlowClaimRecordE differenceClaim(ClaimFlowF claimFlowF) {
        FlowClaimRecordE formerFlowClaimRecord = null;
        if (Objects.nonNull(claimFlowF.getFlowClaimRecordId()) && claimFlowF.getFlowClaimRecordId() > 0) {
            // 修改功能
            formerFlowClaimRecord = flowClaimRecordDomainService.queryByRecordId(claimFlowF.getFlowClaimRecordId());
            // 将上次认领的流水记录ID设置为本次认领的流水记录ID
            claimFlowF.setFlowIdList(formerFlowClaimRecord.getFlowIdList());
        }
        // 收款单id
        List<Long> invoiceIdList = claimFlowF.getInvoiceIdList();
        // 流水ID
        List<Long> flowIdList = claimFlowF.getFlowIdList();

        ClaimFlowCommand command = Global.mapperFacade.map(claimFlowF, ClaimFlowCommand.class);
        CommunityShortRV communityInfo = spaceClient.getCommunityInfo(claimFlowF.getSupCpUnitId());
        command.setSupCpUnitName(communityInfo.getName());
        // 金额不一致 取消金额校验
        Set<String> ourAccount = new HashSet<>();
        // 对方账号
        Set<String> oppositeAccount = new HashSet<>();
        // 交易平台，原来的取值方式也有问题，没有对其校验，取得是第一个
        List<String> tradingPlatform = new ArrayList<>();
        // 通过流水明细计算金额和银行账户
        List<FlowDetailE> flowDetailList = flowClaimRecordDomainService.getAndValidFlowDetail(flowIdList, Objects.isNull(formerFlowClaimRecord));
        // 通过流水明细计算金额和银行账户
        flowClaimRecordDomainService.countByFlowDetailList(flowDetailList, command, ourAccount, oppositeAccount, tradingPlatform);
        // 获取 收/退款单
        List<ContractFlowBillV> flowBillList = this.getFlowBill(invoiceIdList, claimFlowF.getSupCpUnitId());
        // 收/退款单填充金额
        this.fillFlowBillAmount(flowBillList, command);
        // 获取银行账号信息
        FlowDetailE bankAccountFlowDetail = flowClaimRecordDomainService.getBankAccountFlowDetail(ourAccount, oppositeAccount, tradingPlatform);
        // 获取领用记录
        FlowClaimRecordE flowClaimRecord = flowClaimRecordDomainService.getFlowClaimRecord(command, bankAccountFlowDetail);

        if (Objects.nonNull(formerFlowClaimRecord)) {
            flowClaimRecordDomainService.revokedByFlowClaimIdList(Collections.singletonList(formerFlowClaimRecord.getId()));
        }
        flowDetailDomainService.updateClaimStatusByIdList(flowIdList, FlowClaimStatusEnum.认领审核中.getCode());
        // 更新账单的银行账户信息
        flowClaimRecordDomainService.getAndUpdateBankAccount(bankAccountFlowDetail.getOurAccount(), bankAccountFlowDetail.getOppositeAccount(),
                command.getFlowBillList(), command.getInvoiceReceiptList(), claimFlowF.getSupCpUnitId());
        flowClaimRecordDomainService.difClaim(command, flowClaimRecord, flowDetailList);
        return flowClaimRecord;
    }


    /**
     * 认领流水
     *
     * @param claimFlowF flowClaimF
     * @return Boolean
     */
    @Transactional
    public FlowClaimRecordE claim(ClaimFlowF claimFlowF) {
        // 差额认领逻辑
        if (null != claimFlowF.getDifferenceFlag() && claimFlowF.getDifferenceFlag().equals(0) ) {
            FlowClaimRecordE flowClaimRecordE = differenceClaim(claimFlowF);
            return flowClaimRecordE;
        } else {
            // 上次认领记录
            FlowClaimRecordE formerFlowClaimRecord = null;
            if (Objects.nonNull(claimFlowF.getFlowClaimRecordId()) && claimFlowF.getFlowClaimRecordId() > 0) {
                // 修改功能
                formerFlowClaimRecord = flowClaimRecordDomainService.queryByRecordId(claimFlowF.getFlowClaimRecordId());
                // 将上次认领的流水记录ID设置为本次认领的流水记录ID
                claimFlowF.setFlowIdList(formerFlowClaimRecord.getFlowIdList());
            }

            // 发票ID或收/退款单ID
            List<Long> invoiceIdList = claimFlowF.getInvoiceIdList();
            // 流水ID
            List<Long> flowIdList = claimFlowF.getFlowIdList();
            // 记录认领的发票/单据的金额和数据
            ClaimFlowCommand command = Global.mapperFacade.map(claimFlowF, ClaimFlowCommand.class);
            CommunityShortRV communityInfo = spaceClient.getCommunityInfo(claimFlowF.getSupCpUnitId());
            command.setSupCpUnitName(communityInfo.getName());
            // 获取流水明细（新增或修改），
            // Objects.isNull(formerFlowClaimRecord) 重新认领为false，新认领为true
            List<FlowDetailE> flowDetailList = flowClaimRecordDomainService.getAndValidFlowDetail(flowIdList, Objects.isNull(formerFlowClaimRecord));
            // 我方账号
            Set<String> ourAccount = new HashSet<>();
            // 对方账号
            Set<String> oppositeAccount = new HashSet<>();
            // 交易平台，原来的取值方式也有问题，没有对其校验，取得是第一个
            List<String> tradingPlatform = new ArrayList<>();
            // 通过流水明细计算金额和银行账户
            flowClaimRecordDomainService.countByFlowDetailList(flowDetailList, command, ourAccount, oppositeAccount, tradingPlatform);
            // 校验银行账号
            flowClaimRecordDomainService.validBankAccount(ourAccount, oppositeAccount);

            if (ClaimTypeEnum.发票认领.equalsByCode(claimFlowF.getClaimType())) {
                // 获取蓝票
                List<InvoiceReceiptE> invoiceReceiptList = this.getAndValidInvoiceList(invoiceIdList, claimFlowF.getFlowClaimRecordId());
                // 获取发票对应账单金额并将发票金额填充到command
                this.fillInvoiceAmount(invoiceIdList, invoiceReceiptList, command);
                //更新票据领用状态
                sendMqForCharge(invoiceIdList, claimFlowF.getSupCpUnitId());
            } else if (ClaimTypeEnum.账单认领.equalsByCode(claimFlowF.getClaimType())) {
                // 获取 收/退款单
                List<ContractFlowBillV> flowBillList = this.getFlowBill(invoiceIdList, claimFlowF.getSupCpUnitId());
                // 收/退款单填充金额
                this.fillFlowBillAmount(flowBillList, command);
            } else {
                return formerFlowClaimRecord;
            }
            // 校验金额  中交环境金额前置校验
            if(!EnvConst.ZHONGJIAO.equals(EnvData.config)){
                flowClaimRecordDomainService.validAmount(command);
            }
            // 获取银行账号信息
            FlowDetailE bankAccountFlowDetail = flowClaimRecordDomainService.getBankAccountFlowDetail(ourAccount, oppositeAccount, tradingPlatform);
            // 获取领用记录
            FlowClaimRecordE flowClaimRecord = flowClaimRecordDomainService.getFlowClaimRecord(command, bankAccountFlowDetail);
            // 如果是修改，则先删除之前的流水记录；如果之前是发票认领，则修改上次认领的发票为未认领
            if (Objects.nonNull(formerFlowClaimRecord)) {
                // 上次认领为发票认领
                if (ClaimTypeEnum.发票认领.equalsByCode(formerFlowClaimRecord.getClaimType())) {
                    invoiceDomainService.updateClaimStatusByIdList(formerFlowClaimRecord.getInvoiceIdList(), FlowClaimStatusEnum.未认领.getCode());
                }
                flowClaimRecordDomainService.revokedByFlowClaimIdList(Collections.singletonList(formerFlowClaimRecord.getId()));
            }
            // 更新发票状态为已认领(发票认领时)
            if (ClaimTypeEnum.发票认领.equalsByCode(claimFlowF.getClaimType())) {
                invoiceDomainService.updateClaimStatusByIdList(invoiceIdList, FlowClaimStatusEnum.已认领.getCode());
            }
            // 如果是新认领，非修改，将流水状态修改为已认领
            if (Objects.isNull(formerFlowClaimRecord)) {
                flowDetailDomainService.updateClaimStatusByIdList(flowIdList, FlowClaimStatusEnum.已认领.getCode());
            }
            // 更新账单的银行账户信息
            flowClaimRecordDomainService.getAndUpdateBankAccount(bankAccountFlowDetail.getOurAccount(), bankAccountFlowDetail.getOppositeAccount(),
                    command.getFlowBillList(), command.getInvoiceReceiptList(), claimFlowF.getSupCpUnitId());
            //更新流水明细领用状态并添加领用记录
            Boolean claim = flowClaimRecordDomainService.claim(command, flowClaimRecord, flowDetailList);
            sendMqForAccountHand(invoiceIdList, claimFlowF.getSupCpUnitId());
            return flowClaimRecord;
        }
    }

    public Boolean claimForZJ(ClaimFlowZJF claimFlowZJF) {
        //到期日期是必填的
        if (Objects.isNull(claimFlowZJF.getGmtExpire())) {
            throw BizException.throw400("请填写到期日期");
        }
        // 因为暂存原因， 前置校验 是否被认领
        StringBuilder stringBuilder = new StringBuilder();
        for (FlowBillPageDto flowBillPageDto : claimFlowZJF.getGatherBillList()) {
            String gatherDetailIds = flowBillPageDto.getGatherDetailIds();
            stringBuilder.append(gatherDetailIds);
            stringBuilder.append(",");
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        String string = stringBuilder.toString();
        List<Long> collect1 = Arrays.asList(string.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());

        List<FlowClaimGatherDetailE> list1 = flowClaimGatherDetailRepository.list(new LambdaQueryWrapper<FlowClaimGatherDetailE>().in(FlowClaimGatherDetailE::getGatherDetailId, collect1));
        if (CollectionUtils.isNotEmpty(list1) && Objects.isNull(claimFlowZJF.getFlowClaimRecordId())){
            throw BizException.throw400("收款单已被认领!");
        }
        List<Long> billIdList = claimFlowZJF.getGatherBillList().stream().map(FlowBillPageDto::getBillId).collect(Collectors.toList());

        List<GatherBill> gatherBills = gatherBillRepository.list(new LambdaQueryWrapper<GatherBill>()
                .in(GatherBill::getId, billIdList)
                .eq(GatherBill::getReversed, BillReverseStateEnum.已冲销.getCode())
                .eq(GatherBill::getSupCpUnitId, claimFlowZJF.getSupCpUnitId()));

        if (CollectionUtils.isNotEmpty(gatherBills)){
            throw BizException.throw400("收款单已被冲销!");
        }

        List<GatherDetail> gatherDetailList = gatherDetailRepository.list(new LambdaQueryWrapper<GatherDetail>()
                .in(GatherDetail::getId, collect1)
                .eq(GatherDetail::getAvailable, 1)
                .eq(GatherDetail::getSupCpUnitId, claimFlowZJF.getSupCpUnitId()));
        if (CollectionUtils.isNotEmpty(gatherDetailList)){
            throw BizException.throw400("收款明细已被冲销!");
        }


        long sum = claimFlowZJF.getGatherBillList().stream().mapToLong(FlowBillPageDto::getTotalAmount).sum();

        // 中交前置金额校验
        if (!(null != claimFlowZJF.getDifferenceFlag() && claimFlowZJF.getDifferenceFlag().equals(0))){
            BigDecimal divide = new BigDecimal(String.valueOf(sum)).divide(new BigDecimal("100"));
            if (divide.compareTo(claimFlowZJF.getClaimAmount()) != 0) {
                throw BizException.throw400(ErrMsgEnum.FLOW_BILL_AMOUNT_CLAIM_AMOUNT_NOT_SAME.getErrMsg());
            }
        }
        // 前置校验  现金不能和其他支付方式一起认领
        // 判断支付方式是否是一种, 如果不是一种, 是否包含现金
        List<String> payChannelList = claimFlowZJF.getGatherBillList().stream().map(FlowBillPageDto::getPayChannel).distinct().collect(Collectors.toList());
        if(payChannelList.size() > 1 && payChannelList.contains("CASH")) {
            throw BizException.throw400("请选择收款方式同为现金或非现金的收款单认领!");
        }
        // 前置判断  是否是修改 如果是修改   先修改之前的状态
        if (Objects.nonNull(claimFlowZJF.getFlowClaimRecordId())){
            flowClaimGatherDetailRepository.remove(new LambdaQueryWrapper<FlowClaimGatherDetailE>().eq(FlowClaimGatherDetailE::getFlowClaimRecordId, claimFlowZJF.getFlowClaimRecordId()));
        }
        ClaimFlowF map = Global.mapperFacade.map(claimFlowZJF, ClaimFlowF.class);
        List<FlowBillPageDto> gatherBillList = claimFlowZJF.getGatherBillList();
        List<Long> collect = gatherBillList.stream().map(FlowBillPageDto::getBillId).collect(Collectors.toList());
        map.setInvoiceIdList(collect);
        FlowClaimRecordE claim = claim(map);
        ArrayList<FlowClaimGatherDetailE> flowClaimGatherDetailES = new ArrayList<>();

        for (FlowBillPageDto flowBillPageDto : gatherBillList) {
            List<String> list = Arrays.asList(flowBillPageDto.getGatherDetailIds().split(","));
            for (String s : list) {
                FlowClaimGatherDetailE flowClaimGatherDetailE = new FlowClaimGatherDetailE();
                flowClaimGatherDetailE.setFlowClaimRecordId(claim.getId());
                flowClaimGatherDetailE.setFlowClaimRecordNumber(claim.getSerialNumber());
                flowClaimGatherDetailE.setGatherBillId(flowBillPageDto.getBillId());
                flowClaimGatherDetailE.setGatherDetailId(Long.valueOf(s));
                flowClaimGatherDetailE.init();
                flowClaimGatherDetailES.add(flowClaimGatherDetailE);
            }
        }
        claim.setSettleAmount(sum);
        if(payChannelList.size() == 1 && payChannelList.contains("CASH")) {
            claim.setPayChannelType(0);
        } else {
            claim.setPayChannelType(1);
        }
        //新增单据备注和到期日期
        claim.setReceiptRemark(claimFlowZJF.getReceiptRemark());
        claim.setGmtExpire(claimFlowZJF.getGmtExpire().atTime(23, 59, 59));
        flowClaimRecordRepository.updateById(claim);
        flowClaimGatherDetailRepository.saveBatch(flowClaimGatherDetailES);

        if (null != claimFlowZJF.getFlowTempRecordId()){
            flowTempAppService.deleteFlowTemp(claimFlowZJF.getFlowTempRecordId());
        }
        return true;
    }

    /**
     * 查询4A组织id
     *
     * @param orgOidQueryF
     * @return
     */
    public OrgOidV queryOids(OrgOidQueryF orgOidQueryF) {
        OrgOidV orgOidV = new OrgOidV();
        orgOidV.setOids(new ArrayList<>());
        if (Objects.isNull(orgOidQueryF.getType()) || CollectionUtils.isEmpty(orgOidQueryF.getIds())) {
            return orgOidV;
        }
        List<Long> staIds = orgOidQueryF.getIds();
        //如果是"成本单位"，需要先查询成本单位所在的法定单位id，然后再查询4A信息
        if (StaCostEnum.COST.equals(orgOidQueryF.getType())) {
            List<StatutoryBodyAccountE> accountES = bankAccountCostOrgRepository.queryStaIdByCostIds(orgOidQueryF.getIds());
            staIds = accountES.stream().map(StatutoryBodyAccountE::getStatutoryBodyId).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(staIds)) {
            return orgOidV;
        }
        staIds = staIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(staIds)) {
            return orgOidV;
        }

        List<String> oids = orgClient.queryOids(staIds);
        orgOidV.setOids(oids);
        return orgOidV;
    }

    public BankAccountV queryBankAccounts(BankAccountQueryF bankAccountQueryF) {
        BankAccountV bankAccountV = new BankAccountV();
        if (CollectionUtils.isEmpty(bankAccountQueryF.getCostIds())) {
            return bankAccountV;
        }
        List<StatutoryBodyAccountE> accountES = bankAccountCostOrgRepository.queryStaIdByCostIds(bankAccountQueryF.getCostIds());
        List<String> bankAccounts = accountES.stream().map(StatutoryBodyAccountE::getBankAccount).distinct().collect(Collectors.toList());
        bankAccountV.setBankAccounts(bankAccounts);
        return bankAccountV;
    }


    /**
     * 获取发票信息、校验，并补充红冲金额
     *
     * @param invoiceIdList
     * @return
     */
    private List<InvoiceReceiptE> getAndValidInvoiceList(List<Long> invoiceIdList, Long flowClaimRecordId) {
        //获取蓝票信息
        List<InvoiceReceiptE> invoiceReceiptList = invoiceDomainService.queryByIdListOrderByPriceTaxAmountDesc(invoiceIdList);
        // 如果是修改，可以选择之前认领的发票记录
        List<Long> claimedInvoiceIds;
        if (Objects.nonNull(flowClaimRecordId)) {
            claimedInvoiceIds = flowClaimRecordDomainService.queryInvoiceIdsByFlowClaimRecordId(flowClaimRecordId);
        } else {
            claimedInvoiceIds = new ArrayList<>();
        }
        // 蓝票中有已认领的发票就抛错(该认领记录之前认领的发票除外)
        List<InvoiceReceiptE> claimedInvoice = invoiceReceiptList.stream().filter(
                s -> s.getClaimStatus() == FlowClaimStatusEnum.已认领.getCode() && !claimedInvoiceIds.contains(s.getId())
        ).collect(Collectors.toList());
        ErrorAssertUtil.isEmptyThrow400(claimedInvoice, ErrorMessage.INVOICE_CLAIMED, claimedInvoice.stream().map(InvoiceReceiptE::getInvoiceReceiptNo).collect(Collectors.joining(",")));
        //获取蓝票关联的红票金额并设置到发票明细中
        List<InvoiceAndReceiptDto> redInvoiceList = invoiceDomainService.getRedInvoiceByBlueInvoiceId(invoiceIdList);
        Map<Long, List<InvoiceAndReceiptDto>> collect = redInvoiceList.stream().collect(Collectors.groupingBy(InvoiceAndReceiptDto::getId));
        // 计算蓝票对应的红票红冲金额填充到蓝票
        for (InvoiceReceiptE record : invoiceReceiptList) {
            List<InvoiceAndReceiptDto> invoiceAndReceiptDtoList = collect.get(record.getId());
            if (CollectionUtils.isNotEmpty(invoiceAndReceiptDtoList)) {
                record.setRedTaxAmount(Math.abs(invoiceAndReceiptDtoList.get(0).getRedTaxAmount()));
            } else {
                record.setRedTaxAmount(0L);
            }
        }
        return invoiceReceiptList;
    }

    /**
     * 填充发票金额和发票数据
     *
     * @param invoiceIdList
     * @param invoiceReceiptList
     * @param command
     */
    private void fillInvoiceAmount(List<Long> invoiceIdList, List<InvoiceReceiptE> invoiceReceiptList, ClaimFlowCommand command) {
        // 获取发票对应的账单的各项金额
        InvoiceAndReceiptStatisticsDto invoiceAndReceiptStatisticsDto = invoiceDomainService.statisticsAmount(invoiceIdList, null, command.getSupCpUnitId());
        // 红冲金额
        long redAmount = invoiceReceiptList.stream().mapToLong(InvoiceReceiptE::getRedTaxAmount).sum();
        command.setInvoiceAmount(invoiceAndReceiptStatisticsDto.getTotalAmount() - Math.abs(redAmount));
        command.setRefundAmount(Math.abs(redAmount));
        command.setSettleAmount(invoiceAndReceiptStatisticsDto.getSettleAmount());
        command.setInvoiceReceiptList(invoiceReceiptList);
    }

    /**
     * 获取收入/退款单信息
     *
     * @param invoiceIdList
     * @return
     */
    private List<ContractFlowBillV> getFlowBill(List<Long> invoiceIdList, String supCpUnitId) {
        //获取合同流水账单信息
        List<AllBillPageDto> allBillPageDtoList = allBillAppService.flowContractIdList(invoiceIdList, supCpUnitId);
        return Global.mapperFacade.mapAsList(allBillPageDtoList, ContractFlowBillV.class);
    }

    /**
     * 填充单据金额到command
     *
     * @param contractFlowBillList
     * @param command
     */
    private void fillFlowBillAmount(List<ContractFlowBillV> contractFlowBillList, ClaimFlowCommand command) {
        long billAmount = contractFlowBillList.stream().mapToLong(ContractFlowBillV::getTotalAmount).sum();
        long refundAmount = contractFlowBillList.stream().filter(s -> BillTypeEnum.退款账单.equalsByCode(s.getBillType())).mapToLong(ContractFlowBillV::getTotalAmount).sum();
        long incomeAmount = billAmount - refundAmount;
        command.setInvoiceAmount(incomeAmount - refundAmount);
        command.setRefundAmount(Math.abs(refundAmount));
        command.setSettleAmount(incomeAmount - refundAmount);
        command.setFlowBillList(contractFlowBillList);
    }

    /**
     * 根据缴费系统的票据生成凭证
     *
     * @param list
     */
    @Async
    public void sendMqForCharge(List<Long> list, String supCpUnitId) {

        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceDomainService.listByInvoiceReceiptId(list);
        invoiceReceiptDetailES.forEach(item -> {
            // 发送mq消息，即时生成凭证
            if (BillTypeEnum.收款单.getCode() == item.getBillType() || BillTypeEnum.付款单.getCode() == item.getBillType()) {
                EventLifecycle.apply(EventMessage.builder().headers("action", BillAction.SETTLED).payload(
                        BillActionEvent.settle(item.getBillId(), item.getBillType(), "结算", supCpUnitId)).build());
            }
        });


    }

    /**
     * 流水认领成功后自动交账
     *
     * @param invoiceIdList
     */
    @Async
    public void sendMqForAccountHand(List<Long> invoiceIdList, String supCpUnitId) {
        // 流水认领成功后，发送mq消息
        invoiceIdList.forEach(item -> {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.FLOW_CLAIM)
                    .payload(BillActionEvent.flowClaim(item, 0, "流水认领", supCpUnitId))   //billType 是随便取的值
                    .build());
        });
        log.info("流水认领,交账账单id：{}", JSON.toJSONString(invoiceIdList));
    }

    /**
     * 根据合同生成的账单  生成凭证
     *
     * @param contractFlowBillList
     */
    @Async
    public void sendMqForContract(List<ContractFlowBillV> contractFlowBillList, String supCpUnitId) {

        contractFlowBillList.forEach(item -> {
            // 发送mq消息，即时生成凭证
            if (BillTypeEnum.收款单.getCode() == item.getBillType() || BillTypeEnum.付款单.getCode() == item.getBillType()) {
                EventLifecycle.apply(
                        EventMessage.builder().headers("action", BillAction.SETTLED).payload(
                                        BillActionEvent.settle(item.getId(), item.getBillType(), "结算", supCpUnitId))
                                .build());
            }
        });

    }
    public FlowDetailRevokedV  revokedForRecon(RevokedFlowF revokedFlowF) {
        List<FlowClaimRecordE> flowClaimRecordList = flowClaimRecordDomainService.queryByIdList(revokedFlowF.getFlowClaimIdList());
        if (CollectionUtils.isEmpty(flowClaimRecordList)) {
            throw BizException.throw400(ErrMsgEnum.FLOW_CLAIM_RECORDS_NOT_EXIST.getErrMsg());
        }
        return revokedImpl(flowClaimRecordList, revokedFlowF);
    }


    /**
     * 根据收款单ID列表获取关联流水认领明细
     * @param gatherIds 收款单ID列表
     * @param supCpUnitId 项目ID
     * @return {@link ReconciliationDetailE}
     */
    public List<FlowClaimDetailV> getRecGatherIdFlowClaimRecord(List<Long> gatherIds,String supCpUnitId){
        return Optional.ofNullable(flowClaimRecordDomainService.getRecGatherIdFlowClaimRecord(gatherIds,supCpUnitId))
                .map(a->Global.mapperFacade.mapAsList(a,FlowClaimDetailV.class)).orElse(Lists.newArrayList());
    }

    /**
     * 撤销认领
     *
     * @param revokedFlowF 撤销认领参数
     * @return Boolean
     */
    @Transactional
    public FlowDetailRevokedV revoked(RevokedFlowF revokedFlowF) {
        List<FlowClaimRecordE> flowClaimRecordList = flowClaimRecordDomainService.queryByIdList(revokedFlowF.getFlowClaimIdList());


        if (CollectionUtils.isEmpty(flowClaimRecordList)) {
            throw BizException.throw400(ErrMsgEnum.FLOW_CLAIM_RECORDS_NOT_EXIST.getErrMsg());
        }
        List<Integer> list = flowClaimRecordList.stream().map(FlowClaimRecordE::getReconcileFlag).collect(Collectors.toList())
                .stream().distinct().collect(Collectors.toList());
        // 判断是否已经对账如果已经对账,则不允许解除。
        if (list.contains(1) || list.contains(2)) {
            throw BizException.throw400(ErrMsgEnum.FLOW_CLAIM_IS_RECONCILE.getErrMsg());
        }
        List<Integer> pushState = flowClaimRecordList.stream().map(FlowClaimRecordE::getPushState).collect(Collectors.toList())
                .stream().distinct().collect(Collectors.toList());
        if (pushState.contains(4)) {
            throw BizException.throw400(ErrMsgEnum.FLOW_CLAIM_IS_APPROVE.getErrMsg());
        }
        List<Integer> pushStateList = flowClaimRecordList.stream().map(FlowClaimRecordE::getPushState).collect(Collectors.toList())
                .stream().distinct().collect(Collectors.toList());
        if (pushStateList.contains(1) || pushStateList.contains(2) || pushStateList.contains(3)) {
            throw BizException.throw400(ErrMsgEnum.FLOW_CLAIM_IS_PUSH.getErrMsg());
        }
        // 中交环境  删除flow_claim_gather_detail 表数据

        if(EnvConst.ZHONGJIAO.equals(EnvData.config)){
            // 判断是否解除报账单， 强制校验
            for (Long l : revokedFlowF.getFlowClaimIdList()) {
                List<VoucherBillZJ> voucherBillZJS = voucherPushBillZJRepository.list(new LambdaQueryWrapper<VoucherBillZJ>().eq(VoucherBillZJ::getBillEventType, 2).like(VoucherBillZJ::getRecordIdList, l));
                if (CollectionUtils.isNotEmpty(voucherBillZJS)) {
                    throw BizException.throw400("不删除报账单则不允许解除流水!");
                }
            }

            flowClaimGatherDetailRepository.remove(new LambdaQueryWrapper<FlowClaimGatherDetailE>().in(FlowClaimGatherDetailE::getFlowClaimRecordId, revokedFlowF.getFlowClaimIdList()));
        }

        return revokedImpl(flowClaimRecordList, revokedFlowF);
    }
    private FlowDetailRevokedV revokedImpl(List<FlowClaimRecordE> flowClaimRecordList, RevokedFlowF revokedFlowF){
        // 方圆环境特有如果是已经推凭 或者是  推凭成功，则不能接触让认领
        if (EnvConst.FANGYUAN.equals(EnvData.config)) {
            String supCpUnitId = flowClaimRecordList.get(0).getSupCpUnitId();
            // 根据流水记录id 获取流水认领明细
            List<FlowClaimDetailE> allFlowClaimDetailList = flowClaimDetailRepository.queryByFlowClaimRecordId(revokedFlowF.getFlowClaimIdList());
            // 根据明细获取收退款单id 或者是发票id
            Map<Integer, List<FlowClaimDetailE>> claimIdTypeMap = allFlowClaimDetailList.stream().collect(Collectors.groupingBy(FlowClaimDetailE::getClaimIdType));
            // 蓝票id集合
            List<Long> blueInvoiceIdList = null;
            // 红票id集合
            List<Long> RedInvoiceIdList = null;
            List<Long> gatherBillIdList = null;
            List<Long> invoiceIdList = new ArrayList<>();
            List<Long> billIdList = new ArrayList<>();
            if (null != claimIdTypeMap.get(1)) {
                blueInvoiceIdList = claimIdTypeMap.get(1).stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
                invoiceIdList.addAll(blueInvoiceIdList);
            }
            if (null != claimIdTypeMap.get(2)) {
                RedInvoiceIdList = claimIdTypeMap.get(2).stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
                invoiceIdList.addAll(RedInvoiceIdList);
            }
            if (null != claimIdTypeMap.get(3)) {
                gatherBillIdList = claimIdTypeMap.get(3).stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
            }
            // 根据收退款单id  或者是发票id 获取对应的账单id
            if (null != invoiceIdList && invoiceIdList.size() > 0) {
                List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(invoiceIdList);
                billIdList.addAll(invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getBillId).collect(Collectors.toList()));
            }
            if (null != gatherBillIdList && gatherBillIdList.size() > 0) {
                List<GatherDetail> gatherDetails = gatherDetailRepository.queryByPayBillIdList(gatherBillIdList, supCpUnitId);
                billIdList.addAll(gatherDetails.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()));
            }
            // 根据账单id  获取推凭状态
            List<VoucherPushBillDetail> voucherPushBillDetails = null;
            if (null != billIdList && billIdList.size() > 0) {
                voucherPushBillDetails = voucherBillDetailRepository.queryVoucherPushBillDetailInfo(billIdList);
            }
            // 根据推凭状态  判断是否能接触认领
            if (null != voucherPushBillDetails && voucherPushBillDetails.size() > 0) {
                throw BizException.throw400("已推凭账单不能接触流水认领！");
            }

        }
        //解除票和流水的认领状态
        ArrayList<Long> flowIdList = new ArrayList<>();
        ArrayList<Long> invoiceIdList = new ArrayList<>();
//        Integer sysSource = null;
        Integer claimType = null;
        for (FlowClaimRecordE flowClaimRecordE : flowClaimRecordList) {
            if (CollectionUtils.isNotEmpty(flowClaimRecordE.getFlowIdList())) {
                flowIdList.addAll(flowClaimRecordE.getFlowIdList());
            }
            if (CollectionUtils.isNotEmpty(flowClaimRecordE.getInvoiceIdList())) {
                invoiceIdList.addAll(flowClaimRecordE.getInvoiceIdList());
            }
            claimType = flowClaimRecordE.getClaimType();
//            sysSource = flowClaimRecordE.getSysSource();
        }

        //更新票据领用状态
        FlowDetailRevokedV result = new FlowDetailRevokedV();
        if (CollectionUtils.isNotEmpty(invoiceIdList)) {
            if (Objects.nonNull(claimType) && ClaimTypeEnum.发票认领.equalsByCode(claimType)) {
                invoiceDomainService.updateClaimStatusByIdList(invoiceIdList, FlowClaimStatusEnum.未认领.getCode());
            }
        }
        if (CollectionUtils.isNotEmpty(flowIdList)) {
            flowClaimRecordDomainService.revoked(revokedFlowF.getFlowClaimIdList(), flowIdList);
        }
        result.setFlowDetailSum(flowIdList.size());
        result.setInvoiceDetailSum(invoiceIdList.size());

        return result;
    }


   public PageV<ContractFlowBillV> queryByFlowDetail(PageF<SearchF<?>> queryF) {
        return null;
   }

    /**
     * 认领详情
     *
     * @param flowId
     */
    public FlowClaimRecordDetailV claimDetail(Long flowId, String supCpUnitId) {
        Long flowClaimRecordId = flowClaimRecordDomainService.queryIdByFlowId(flowId);
        // 特殊处理，返回当前的流水的状态
        FlowClaimRecordDetailV flowClaimRecordDetailV = queryDetailByRecordId(flowClaimRecordId, supCpUnitId);
        List<FlowDetailPageV> flowDetailList = flowClaimRecordDetailV.getFlowDetailList();
        if (CollectionUtils.isEmpty(flowDetailList)) {
            return flowClaimRecordDetailV;
        }
        for (FlowDetailPageV detailPageV : flowDetailList) {
            if (detailPageV.getId().equals(flowId.toString())) {
                flowClaimRecordDetailV.setCurrentFlowDetailState(detailPageV.getClaimStatus());
                break;
            }
        }
        return flowClaimRecordDetailV;
    }

    /**
     * 根据流水认领记录id获取认领详情
     *
     * @param recordId 流水认领记录id
     * @return Boolean
     */
    public FlowClaimRecordDetailV queryDetailByRecordId(Long recordId, String supCpUnitId) {
        //获取领用记录
        FlowClaimRecordE flowClaimRecordE = flowClaimRecordDomainService.queryByRecordId(recordId);
        FlowClaimRecordDetailV flowClaimRecordDetailV = Global.mapperFacade.map(flowClaimRecordE, FlowClaimRecordDetailV.class);
        if (flowClaimRecordE.getDifferenceFlag().equals(0)) {
            flowClaimRecordDetailV.setFlowType("0");
        }  else {
            flowClaimRecordDetailV.setFlowType("1");
        }
        flowClaimRecordDetailV.setReportFilesJson(JSONObject.parseArray(flowClaimRecordE.getReportFiles()));
        flowClaimRecordDetailV.setFlowFilesJson(JSONObject.parseArray(flowClaimRecordE.getFlowFiles()));
        long l = flowClaimRecordDetailV.getSettleAmount() - flowClaimRecordDetailV.getClaimAmount();
        flowClaimRecordDetailV.setCompareAmount(new BigDecimal(String.valueOf(l)).divide(new BigDecimal("100")));
        //获取流水明细
        List<FlowDetailE> flowDetailList = flowDetailDomainService.getByIdList(flowClaimRecordE.getFlowIdList());
        flowClaimRecordDetailV.setFlowDetailList(Global.mapperFacade.mapAsList(flowDetailList, FlowDetailPageV.class));
        if (ClaimTypeEnum.账单认领.equalsByCode(flowClaimRecordE.getClaimType())) {
            //获取关联的账单列表
            List<AllBillPageDto> allBillPageDtoList = allBillAppService.flowContractIdList(flowClaimRecordE.getInvoiceIdList(), supCpUnitId);
            List<ContractFlowBillV> contractFlowBillList = Global.mapperFacade.mapAsList(allBillPageDtoList, ContractFlowBillV.class);
            flowClaimRecordDetailV.setFlowBillDetailList(contractFlowBillList);
        } else if (ClaimTypeEnum.发票认领.equalsByCode(flowClaimRecordE.getClaimType())) {
            //获取关联的发票列表
            List<InvoiceReceiptE> invoiceReceiptList = invoiceDomainService.queryByIdList(flowClaimRecordE.getInvoiceIdList());
            List<FlowInvoiceDetailV> flowInvoiceDetailList = Global.mapperFacade.mapAsList(invoiceReceiptList, FlowInvoiceDetailV.class);
            flowClaimRecordDetailV.setFlowInvoiceDetailList(flowInvoiceDetailList);
        }
        return flowClaimRecordDetailV;
    }

    /**
     * 新增流水明细
     *
     * @param addFlowDetailF addFlowDetailF
     * @return Boolean
     */
    public Boolean addDetail(AddFlowDetailF addFlowDetailF) {
        OrgTenantRv orgTenantRv = reconciliationOrgFacade.tenantGetById(getTenantId().get());
        AddFlowDetailCommand command = Global.mapperFacade.map(addFlowDetailF, AddFlowDetailCommand.class);
        return flowDetailDomainService.addDetail(command, orgTenantRv.getEnglishName());
    }

    /**
     * 分页查询流水明细
     *
     * @param queryF queryF
     * @return PageV
     */
    public PageV<FlowDetailPageV> pageDetail(PageF<SearchF<FlowDetailE>> queryF) {
        RepositoryUtil.convertSearch(queryF.getConditions(), "fd.settle_amount");
        RepositoryUtil.convertSearchType(queryF.getConditions());
        IPage<FlowDetailComplexV> flowDetailPage = flowDetailDomainService.queryFlowDetailComplexPage(queryF);
        Map<Long, OrgFinanceCostV> financeCostVMap = queryOrgFinanceCostMap(queryF);
        List<FlowDetailPageV> pageDataList = Global.mapperFacade.mapAsList(flowDetailPage.getRecords(), FlowDetailPageV.class);
        for (FlowDetailPageV data : pageDataList) {
            BigDecimal se = new BigDecimal(data.getSettleAmount());
            BigDecimal res = se.divide(new BigDecimal(100), 2, RoundingMode.CEILING);
            data.setSettleAmountUnitYuan(res.toString());
            //设置所属项目id
            if (financeCostVMap.containsKey(data.getCostOrgId())) {
                data.setCommunityId(financeCostVMap.get(data.getCostOrgId()).getCommunityId());
            }
        }
        List<String> communityIdList = pageDataList.stream().filter(x->StringUtils.isNotEmpty(x.getCommunityId()))
                .map(FlowDetailPageV::getCommunityId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(communityIdList)){
            //获取所有项目下部门信息
            List<CfgExternalDeportData> communityList = configClient.getDeportList(communityIdList);
            if(CollectionUtils.isNotEmpty(communityList)){
                Map<String, List<CfgExternalDeportData>> communityMap = communityList.stream().collect(Collectors.groupingBy(CfgExternalDeportData::getCommunityId));
                for (FlowDetailPageV data : pageDataList) {
                    data.setDepartmentList(StringUtils.isNotEmpty(data.getCommunityId()) ? communityMap.get(data.getCommunityId()) : null);
                }
            }
        }
        return PageV.of(flowDetailPage.getCurrent(), flowDetailPage.getSize(), flowDetailPage.getTotal(), pageDataList);
    }

    private Map<Long, OrgFinanceCostV> queryOrgFinanceCostMap(PageF<SearchF<FlowDetailE>> queryF) {
        Map<Long, OrgFinanceCostV> financeCostVMap = new HashMap<>();
        List<Field> costOrgIdFields = queryF.getConditions().getFields().stream()
                .filter(field -> StringUtils.equals("ba.cost_org_id", field.getName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(costOrgIdFields)) {
            return financeCostVMap;
        }
        List<Long> ids = (List<Long>) costOrgIdFields.get(0).getValue();
        if (CollectionUtils.isEmpty(ids)) {
            return financeCostVMap;
        }

        List<OrgFinanceCostV> orgFinanceCosts = orgClient.getCommunityIds(ids);
        if (CollectionUtils.isEmpty(orgFinanceCosts)) {
            return financeCostVMap;
        }
        return orgFinanceCosts.stream()
                .collect(Collectors.toMap(OrgFinanceCostV::getId, Function.identity(), (v1, v2) -> v1));
    }

    /**
     * 分页查询流水明细(汇款认领时)
     *
     * @param queryF
     * @return
     */
    public PageV<FlowDetailPageV> remitPageDetail(PageF<SearchF<FlowDetailE>> queryF) {
//        RepositoryUtil.convertSearch(queryF.getConditions(), "settle_amount");
        RepositoryUtil.remitConvertSearchType(queryF.getConditions());
        IPage<FlowDetailE> flowDetailPage = flowDetailDomainService.remitQueryDetailPage(queryF);
        List<FlowDetailPageV> pageDataList = Global.mapperFacade.mapAsList(flowDetailPage.getRecords(), FlowDetailPageV.class);
        return PageV.of(flowDetailPage.getCurrent(), flowDetailPage.getSize(), flowDetailPage.getTotal(), pageDataList);
    }

    public List<FlowDetailPageV> listDetail(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        List<FlowDetailE> flowDetailPage = flowDetailDomainService.queryDetailList(id);
        List<FlowDetailPageV> pageDataList = Global.mapperFacade.mapAsList(flowDetailPage, FlowDetailPageV.class);
        return pageDataList;
    }

    /**
     * 导入流水明细
     *
     * @param file 导入文件
     * @return FlowImportV
     */
    public FlowImportV importFlow(MultipartFile file) {
        OrgTenantRv orgTenantRv = reconciliationOrgFacade.tenantGetById(getTenantId().get());
        return flowDetailDomainService.importFlow(file, orgTenantRv.getEnglishName());
    }

    /**
     * 获取流水合计明细
     *
     * @return Long
     */
    public FlowStatisticsV statisticsAmount(PageF<SearchF<FlowDetailE>> form) {
        RepositoryUtil.convertSearch(form.getConditions(), "settle_amount");
        RepositoryUtil.convertSearchType(form.getConditions());
        FlowStatisticsDto flowStatisticsDto = flowDetailDomainService.statisticsAmount2(form);
        return Global.mapperFacade.map(flowStatisticsDto, FlowStatisticsV.class);
    }

    /**
     * 统计已勾选流水明细信息
     */
    public FlowStatisticsClaimInfoV statisticsClaimInfo(List<Long> flowIdList) {
        return flowDetailDomainService.statisticsClaimInfo(flowIdList);
    }

    /**
     * 获取流水导入模板文件
     *
     * @return String
     */
    public String queryFile() {
        return fileKey;
    }

    /**
     * 下载流水导入模板
     *
     * @param response response
     */
    public void download(HttpServletResponse response) {
        FileLoadVo fileLoadVo = fileStorage.get(new FileVo(fileKey, "流水导入模板", "xlsx", 0L, 1), curIdentityInfo().getTenantId());
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("流水导入模板.xlsx", StandardCharsets.UTF_8));
            bis = new BufferedInputStream(fileLoadVo.getInputStream());
            bos = new BufferedOutputStream(response.getOutputStream());
            int len;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量删除流水
     *
     * @param flowIdList 流水id集合
     * @return Boolean
     */
    public Boolean deleteBatch(List<Long> flowIdList) {
        return flowDetailDomainService.deleteBatch(flowIdList);
    }

    /**
     * 统计批量删除流水信息
     *
     * @param flowIdList 流水id集合
     * @return Boolean
     */
    public Map<String, Object> groupInfoDeleteBatch(List<Long> flowIdList) {
        return flowDetailDomainService.groupInfoDeleteBatch(flowIdList);
    }

    public PageV<FlowDetailPageRV> pageOutDetail(PageF<SearchF<FlowDetailE>> queryF) {
        RepositoryUtil.convertSearch(queryF.getConditions(), "settle_amount");
        IPage<FlowDetailE> flowDetailPage = flowDetailDomainService.queryDetailPage(queryF);
        List<FlowDetailPageRV> pageDataList = Global.mapperFacade.mapAsList(flowDetailPage.getRecords(), FlowDetailPageRV.class);
        return PageV.of(flowDetailPage.getCurrent(), flowDetailPage.getSize(), flowDetailPage.getTotal(), pageDataList);
    }

    /**
     * 挂起流水
     *
     * @param flowId
     * @return
     */
    public Boolean suspend(Long flowId) {
        return flowDetailDomainService.suspend(flowId);
    }

    /**
     * @param flowId
     * @return
     */
    public Boolean unsuspend(Long flowId) {
        return flowDetailDomainService.unsuspend(flowId);
    }

    /**
     * 汇款认领
     * @param remitClaimFlowF
     * @return
     */
//    @Transactional
//    public Boolean remitClaim(RemitClaimFlowF remitClaimFlowF) {
//        long remitAmount = remitClaimFlowF.getRemitAmount().longValue();
//        // 获取账单
//        ReceivableBill receivableBill = flowClaimRecordDomainService.queryReceivableBillById(remitClaimFlowF.getBillId());
//        if (Objects.isNull(receivableBill)) {
//            throw BizException.throw400(ErrMsgEnum.BILL_NOT_FOUND.getErrMsg());
//        }
//        // 获取流水明细
//        List<FlowDetailE> flowDetailList = flowClaimRecordDomainService.getAndValidFlowDetail(remitClaimFlowF.getFlowIdList(), true);
//        // 收入流水金额
//        long flowIncomeFlowAmount = 0L;
//        // 退款流水金额
//        long flowRefundFlowAmount = 0L;
//        // 我方账号
//        Set<String> ourAccount = new HashSet<>();
//        // 对方账号
//        Set<String> oppositeAccount = new HashSet<>();
//        // 交易平台，原来的取值方式也有问题，没有对其校验，取得是第一个
//        List<String> tradingPlatform = new ArrayList<>();
//        // 通过流水明细计算金额和银行账户
//        flowClaimRecordDomainService.countByFlowDetailList(flowDetailList, flowIncomeFlowAmount, flowRefundFlowAmount, ourAccount, oppositeAccount, tradingPlatform);
//        // 校验银行账号
//        flowClaimRecordDomainService.validBankAccount(ourAccount, oppositeAccount);
//        // 校验金额
//        this.remitAmountValid(remitAmount, flowIncomeFlowAmount, flowRefundFlowAmount);
//        // 获取银行账号信息
//        FlowDetailE bankAccountFlowDetail = flowClaimRecordDomainService.getBankAccountFlowDetail(ourAccount, oppositeAccount, tradingPlatform);
//        // 获取领用记录
//        ClaimFlowCommand command = new ClaimFlowCommand();
//        command.setClaimAmount(remitAmount);
//        command.setSettleAmount(remitAmount);
//        command.setClaimType(ClaimTypeEnum.汇款认领.getCode());
//        command.setSysSource(receivableBill.getSysSource());
//        FlowClaimRecordE flowClaimRecord = flowClaimRecordDomainService.getFlowClaimRecord(command, bankAccountFlowDetail);
//        // 获取我方银行账户信息
//        Long ourBankAccountId = flowClaimRecordDomainService.getOurBankAccountId(bankAccountFlowDetail.getOurAccount());
//        // TODO 修改账单结算状态，已结算？部分结算？应该是需要计算
////        if (receivableBill.getReceivableAmount().compareTo(receivableBill.getSettleAmount() + remitAmount) <= 0) {
////            receivableBill.setSettleState(BillSettleStateEnum.已结算.getCode());
////        } else {
////            receivableBill.setSettleState(BillSettleStateEnum.部分结算.getCode());
////        }
//        receivableBill.setSbAccountId(ourBankAccountId);
//        // 修改账单
//        flowClaimRecordDomainService.updateReceivableBill(receivableBill);
//        // 流水明细修改为已认领状态
//        flowDetailDomainService.updateClaimStatusByIdList(remitClaimFlowF.getFlowIdList(), FlowClaimStatusEnum.已认领.getCode());
//        // 最后生成流水认领记录和流水认领明细
//        ContractFlowBillV contractFlowBillV = this.receivableBillToContractFlowBillV(receivableBill, remitAmount);
//        command.setFlowBillList(Collections.singletonList(contractFlowBillV));
//        return flowClaimRecordDomainService.claim(command, flowClaimRecord, flowDetailList);
//    }

    /**
     * 汇款认领时校验金额    (收入流水金额-退款流水金额)=汇款金额
     * @param remitAmount
     * @param flowIncomeFlowAmount
     * @param flowRefundFlowAmount
     */
//    private void remitAmountValid(long remitAmount, long flowIncomeFlowAmount, long flowRefundFlowAmount) {
//        if (flowIncomeFlowAmount - flowRefundFlowAmount != remitAmount) {
//            throw BizException.throw400(ErrMsgEnum.REMIT_AMOUNT_CLAIM_NOT_SAME.getErrMsg());
//        }
//    }
//
//    private ContractFlowBillV receivableBillToContractFlowBillV(ReceivableBill bill, Long remitAmount) {
//        ContractFlowBillV contractFlowBillV = new ContractFlowBillV();
//        contractFlowBillV.setId(bill.getId());
//        // 这里的金额应该设置为本次汇款的金额
//        contractFlowBillV.setTotalAmount(remitAmount);
//        contractFlowBillV.setBillNo(bill.getBillNo());
//        // 这里是汇款，应该是收款账单，内部计算流水认领明细时使用该类型亦可
//        contractFlowBillV.setBillType(bill.getBillType());
//        contractFlowBillV.setChargeItemName(bill.getChargeItemName());
//        contractFlowBillV.setPayeeName(bill.getPayeeName());
//        contractFlowBillV.setPayTime(LocalDateTime.now());
//        return contractFlowBillV;
//    }

    /**
     * 流水认领获取当前登录用户项目权限
     *
     * @return
     */
    public List<SpaceCommunityShortV> getCommunityInfo() {
        return spaceClient.perCommunitys();
    }

    public FlowClaimRecordE getFlowClaimRecord(String flowId) {
        return flowDetailDomainService.getFlowClaimRecord(flowId);
    }


    public Set<String> querySerialNumbers() {
        return flowDetailRepository.querySerialNumbers();
    }

    public Boolean importFlowClaim(List<FlowClaimDetailImportT> list) {
        OrgTenantRv orgTenantRv = reconciliationOrgFacade.tenantGetById(getTenantId().get());
        flowDetailDomainService.importFlowClaim(list, orgTenantRv.getEnglishName());
        log.info("流水导入数据：" + JSON.toJSON(list));
        return true;
    }


    public Boolean syncFlowClaim( List<FlowDetailV> list){
      return flowDetailDomainService.syncFlowClaim(list);
    }


    /**
     *根据流水流水认领记录表id获取认领详情
     * @param id
     * @param supCpUnitId
     * @return
     */
    public FlowClaimRecordDetailV getClaimDetailByRecordId(Long id, String supCpUnitId) {
        FlowClaimRecordDetailV flowClaimRecordDetailV = queryDetailByRecordId(id, supCpUnitId);
        flowClaimRecordDetailV.setCurrentFlowDetailState(1);
        return flowClaimRecordDetailV;
    }


    public FlowClaimRecordE getClaimDetailBySerialNumber(String serialNumber) {
        return flowClaimRecordRepository.getOne(new LambdaQueryWrapper<FlowClaimRecordE>()
                .eq(FlowClaimRecordE::getSerialNumber, serialNumber)
                .eq(FlowClaimRecordE::getDeleted, 0));
    }

    public PageV<FlowRecordPageV> getPageRecord(PageF<SearchF<FlowRecordPageV>> queryF) {
        IPage<FlowRecordPageV> pageRecord = flowClaimRecordDomainService.getPageRecord(queryF);

        for (FlowRecordPageV record : pageRecord.getRecords()) {
            List<FlowClaimDetailE> flowClaimDetailList = flowClaimDetailRepository.queryByFlowClaimRecordId(Collections.singletonList(record.getId()));
            List<Long> flowIdList = flowClaimDetailList.stream().map(FlowClaimDetailE::getFlowId).distinct().collect(Collectors.toList());
            List<Long> invoiceIdList = flowClaimDetailList.stream().map(FlowClaimDetailE::getInvoiceId).distinct().collect(Collectors.toList());
            record.setFlowDetailIdList(flowIdList);
            List<AllBillPageDto> allBillPageDtoList =   allBillAppService.flowContractIdList(invoiceIdList, record.getSupCpUnitId());
            List<ContractFlowBillV> contractFlowBillList = Global.mapperFacade.mapAsList(allBillPageDtoList, ContractFlowBillV.class);
            record.setFlowBillDetailList(contractFlowBillList);
            List<FlowClaimGatherDetailE> list = flowClaimGatherDetailRepository.list(new LambdaQueryWrapper<FlowClaimGatherDetailE>().eq(FlowClaimGatherDetailE::getFlowClaimRecordId, record.getId()));
            if (CollectionUtils.isNotEmpty(list)) {
                List<FlowBillPageDto> flowBillPageDtos = allBillAppService.flowPageZJ(list.stream().map(FlowClaimGatherDetailE::getGatherDetailId).collect(Collectors.toList()), record.getSupCpUnitId());
                record.setFlowBillPageDtos(flowBillPageDtos);
            }
            if (StringUtils.isNotBlank(record.getFlowDetailSimpleStr())){
                String[] flowDetailSimple = record.getFlowDetailSimpleStr().split(",");
                List<FlowDetailSimpleVo> flowDetailSimpleVoList = Lists.newArrayList();
                List<String> flowDetailSerialNumbers = Lists.newArrayList();
                for (String singleFlowDetailSimple : flowDetailSimple) {
                    String[] split = singleFlowDetailSimple.split(" ");
                    flowDetailSimpleVoList.add(FlowDetailSimpleVo.builder().flowDetailId(Long.parseLong(split[0])).serialNumber(split[1]).build());
                    flowDetailSerialNumbers.add(split[1]);
                }
                record.setFlowDetailSimpleVoList(flowDetailSimpleVoList);
                record.setFlowDetailSimpleStr(StringUtils.join(flowDetailSerialNumbers, ","));
            }
        }
        return PageV.of(pageRecord.getCurrent(), pageRecord.getSize(), pageRecord.getTotal(), pageRecord.getRecords());
    }

    public FlowRecordStatisticsV getFlowRecordStatistics(PageF<SearchF<?>> queryF) {
            return flowClaimRecordDomainService.getFlowRecordStatistics(queryF);
    }


    @Transactional
    public  boolean updateFlowByApprove(UpdateFlowF updateFlowF) {
       return flowClaimRecordDomainService.updateFlowByApprove(updateFlowF);
    }

}
