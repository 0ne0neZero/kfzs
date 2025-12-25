package com.wishare.finance.domains.reconciliation.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.ContractFlowBillV;
import com.wishare.finance.apps.model.reconciliation.consts.enums.ClaimIdTypeEnum;
import com.wishare.finance.apps.model.reconciliation.consts.enums.ClaimTypeEnum;
import com.wishare.finance.apps.model.reconciliation.fo.UpdateFlowF;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordPageV;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordStatisticsV;
import com.wishare.finance.apps.service.acl.AclSpaceClientService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.GatherTypeEnum;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.PayDetail;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.facade.CostCenterFacade;
import com.wishare.finance.domains.configure.organization.repository.StatutoryBodyAccountRepository;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.reconciliation.command.ClaimFlowCommand;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimDetailStatusEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimStatusEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowTypeStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.base.MsgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.enums.MsgCardCodeTypeEnum;
import com.wishare.finance.infrastructure.remote.enums.MsgModelCodeEnum;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeBusinessD;
import com.wishare.finance.infrastructure.remote.vo.space.UserInfoRawV;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流水领域
 *
 * @author yancao
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FlowClaimRecordDomainService {

    private final FlowDetailRepository flowDetailRepository;

    private final FlowClaimRecordRepository flowClaimRecordRepository;

    private final FlowClaimDetailRepository flowClaimDetailRepository;
    private final CostCenterFacade costCenterFacade;

    private final StatutoryBodyAccountRepository statutoryBodyAccountRepository;
    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;
    private final GatherDetailRepository gatherDetailRepository;
    private final PayDetailRepository payDetailRepository;
    private final GatherBillRepository gatherBillRepository;
    private final PayBillRepository payBillRepository;
    private final ReceivableBillRepository receivableBillRepository;
    private final AdvanceBillRepository advanceBillRepository;

    private final MsgClient msgClient;



    private final AclSpaceClientService aclSpaceClientService;
    /**
     * 分页查询领用记录
     *
     * @param queryF queryF
     * @return IPage
     */
    public IPage<FlowClaimRecordE> queryRecordPage(PageF<SearchF<FlowClaimRecordE>> queryF) {
        RepositoryUtil.convertSearch(queryF.getConditions(), "settle_amount");
        if (!costCenterFacade.changeNodeIdSearch(queryF.getConditions(), "ba.cost_org_id")) {
            return new Page<>();
        }
        if (!costCenterFacade.changeNodeIdSearchByStatutoryBodyAccountId(queryF.getConditions(), "sba.statutory_body_id")) {
            return new Page<>();
        }
        QueryWrapper<FlowClaimRecordE> queryWrapper = queryF.getConditions().getQueryModel();
        Page<FlowClaimRecordE> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        queryWrapper.orderByDesc("gmt_create");
        return flowClaimRecordRepository.queryRecordPage(page, queryWrapper);
//        return flowClaimRecordRepository.page(page, queryWrapper);
    }

    /**
     * 撤销认领
     *
     * @param flowIdList      流水明细id集合
     * @param flowClaimIdList 认领记录id集合
     * @return Boolean
     */
    public Boolean revoked(List<Long> flowClaimIdList, List<Long> flowIdList) {
        //更新流水为未认领
        flowDetailRepository.updateClaimStatusByIdList(flowIdList, FlowClaimStatusEnum.未认领.getCode());
        return this.revokedByFlowClaimIdList(flowClaimIdList);
    }

    /**
     * 根据流水记录ID集合撤销
     * 1. 删除流水认领记录
     * 2. 修改流水认领对应的 流水认领明细 为已撤销
     * @param flowClaimIdList 流水认领记录ID集合
     * @return
     */
    public Boolean revokedByFlowClaimIdList(List<Long> flowClaimIdList) {
        //更新流水认领明细为已撤销
        flowClaimDetailRepository.updateStateByFlowClaimIdList(flowClaimIdList, FlowClaimDetailStatusEnum.已撤销.getCode());
        //删除认领记录
        return flowClaimRecordRepository.removeBatchByIds(flowClaimIdList);
    }

    /**
     * 获取并校验流水明细
     * @param flowIdList
     * @param claimStatusValid 是否校验已认领状态（新认领需要校验，修改则不需要校验）
     * @return
     */
    public List<FlowDetailE> getAndValidFlowDetail(List<Long> flowIdList, boolean claimStatusValid) {
        List<FlowDetailE> flowDetailByIdList = flowDetailRepository.listByIdsOrderBySettleAmountDesc(flowIdList);
        if (CollectionUtils.isEmpty(flowDetailByIdList)) {
            throw BizException.throw400(ErrMsgEnum.FLOW_DETAIL_NOT_FOUNT.getErrMsg());
        }
        // 已挂起的流水明细
        List<FlowDetailE> suspendFlowDetailList = new ArrayList<>();
        // 已认领的流水明细
        List<FlowDetailE> claimedFlowDetailList = new ArrayList<>();
        for (FlowDetailE flowDetailE : flowDetailByIdList) {
            if (FlowClaimStatusEnum.已认领.getCode() == flowDetailE.getClaimStatus()) {
                claimedFlowDetailList.add(flowDetailE);
            }
            if (FlowClaimStatusEnum.已挂起.getCode() == flowDetailE.getClaimStatus()) {
                suspendFlowDetailList.add(flowDetailE);
            }
        }
        // 校验是否已被挂起
        ErrorAssertUtil.isEmptyThrow400(suspendFlowDetailList, ErrorMessage.FLOW_SUSPEND,
                suspendFlowDetailList.stream().map(FlowDetailE::getSerialNumber).collect(Collectors.joining(",")));
        // 判断是否校验已被认领
        if (claimStatusValid) {
            ErrorAssertUtil.isEmptyThrow400(claimedFlowDetailList, ErrorMessage.FLOW_CLAIMED,
                    claimedFlowDetailList.stream().map(FlowDetailE::getSerialNumber).collect(Collectors.joining(",")));
        }
        return flowDetailByIdList;
    }

    /**
     * 校验银行账户
     * @param ourAccount
     * @param oppositeAccount
     */
    public void validBankAccount(Set<String> ourAccount, Set<String> oppositeAccount) {
        // 所有的本方账号信息、对方账号信息要一致（一次认领中）
//        if (CollectionUtils.isNotEmpty(ourAccount) && ourAccount.size() != 1) {
//            throw BizException.throw400(ErrMsgEnum.FLOW_OUR_ACCOUNT_NOT_SAME.getErrMsg());
//        }
//
//        if (CollectionUtils.isNotEmpty(oppositeAccount) && oppositeAccount.size() != 1) {
//            throw BizException.throw400(ErrMsgEnum.FLOW_OPPOSITE_ACCOUNT_NOT_SAME.getErrMsg());
//        }
    }

    /**
     * 通过流水明细计算各项数据
     * @param flowDetailList
     * @param command
     * @param ourAccount
     * @param oppositeAccount
     */
    public void countByFlowDetailList(List<FlowDetailE> flowDetailList,
                                      ClaimFlowCommand command,
                                      Set<String> ourAccount, Set<String> oppositeAccount, List<String> tradingPlatform) {

        for (FlowDetailE flowDetailE : flowDetailList) {
            if (FlowTypeStatusEnum.收入.equalsByCode(flowDetailE.getType())) {
//                incomeFlowDetailList.add(flowDetailE);
                command.setFlowIncomeFlowAmount(command.getFlowIncomeFlowAmount() + flowDetailE.getSettleAmount());
                ourAccount.add(flowDetailE.getOurName() + "-" + flowDetailE.getOurBank() + "-" + flowDetailE.getOurAccount());
            } else if (FlowTypeStatusEnum.退款.equalsByCode(flowDetailE.getType())) {
//                refundFlowDetailList.add(flowDetailE);
                command.setFlowRefundFlowAmount(command.getFlowRefundFlowAmount() + flowDetailE.getSettleAmount());
                ourAccount.add(flowDetailE.getOurName() + "-" + flowDetailE.getOurBank() + "-" + flowDetailE.getOurAccount());
                // 只有退款单需要更新对方账号，所以可以在只有退款流水中记录对方账号用以校验，收入流水的对方账号不一致暂时不影响业务流程
                oppositeAccount.add(flowDetailE.getOppositeName() + "-" + flowDetailE.getOppositeBank() + "-" + flowDetailE.getOppositeAccount());
            }
            tradingPlatform.add(flowDetailE.getTradingPlatform());
        }
    }

    /**
     * 校验 发票/单据的金额 是否和流水的金额一致，一致才可以认领
     * @param command
     */
    public void validAmount(ClaimFlowCommand command) {
        long flowIncomeFlowAmount = command.getFlowIncomeFlowAmount();
        long flowRefundFlowAmount = command.getFlowRefundFlowAmount();
        if (ClaimTypeEnum.发票认领.equalsByCode(command.getClaimType())) {
            //校验退款流水和红冲金额是否一致
            if(command.getRefundAmount().compareTo(flowRefundFlowAmount) != 0 ){
                throw BizException.throw400(ErrMsgEnum.FLOW_RED_AMOUNT_NOT_SAME.getErrMsg());
            }
            //校验流水金额和票据金额是否一致
            if(command.getInvoiceAmount().compareTo(flowIncomeFlowAmount - flowRefundFlowAmount) != 0
                    || command.getClaimAmount().compareTo(flowIncomeFlowAmount - flowRefundFlowAmount) != 0){
                throw BizException.throw400(ErrMsgEnum.FLOW_CLAIM_AMOUNT_NOT_SAME.getErrMsg());
            }
        } else if (ClaimTypeEnum.账单认领.equalsByCode(command.getClaimType())) {
            //校验退款流水和退款金额是否一致
            if(command.getRefundAmount().compareTo(flowRefundFlowAmount) != 0 ){
                throw BizException.throw400(ErrMsgEnum.FLOW_REFUND_AMOUNT_NOT_SAME.getErrMsg());
            }
            //校验流水金额和账单金额是否一致
            if(command.getInvoiceAmount().compareTo(flowIncomeFlowAmount - flowRefundFlowAmount) != 0
                    || command.getClaimAmount().compareTo(flowIncomeFlowAmount - flowRefundFlowAmount) != 0){
                throw BizException.throw400(ErrMsgEnum.FLOW_BILL_AMOUNT_CLAIM_AMOUNT_NOT_SAME.getErrMsg());
            }
        }
    }

    /**
     * 获取银行账户，得到一条流水，该流水只用来存储银行账户信息
     * @param ourAccount
     * @param oppositeAccount
     */
    public FlowDetailE getBankAccountFlowDetail(Set<String> ourAccount, Set<String> oppositeAccount, List<String> tradingPlatform) {
        FlowDetailE accountFlowDetail = new FlowDetailE();
        if (CollectionUtils.isNotEmpty(ourAccount)) {
            String incomeAccount = ourAccount.iterator().next();
            String[] accountArr = incomeAccount.split("-");
            accountFlowDetail.setOurName(accountArr[0]);
            accountFlowDetail.setOurBank(accountArr[1]);
            accountFlowDetail.setOurAccount(accountArr[2]);
        }
        if (CollectionUtils.isNotEmpty(oppositeAccount)) {
            String refundAccount = oppositeAccount.iterator().next();
            String[] accountArr = refundAccount.split("-");
            accountFlowDetail.setOppositeName(accountArr[0]);
            accountFlowDetail.setOppositeBank(accountArr[1]);
            accountFlowDetail.setOppositeAccount(accountArr[2]);
        }
        if (CollectionUtils.isNotEmpty(tradingPlatform)) {
            String platform = tradingPlatform.get(0);
            accountFlowDetail.setTradingPlatform(platform);
        }
        return accountFlowDetail;
    }

    /**
     * 获取认领记录
     * @param command
     * @param bankAccountFlowDetail
     * @return
     */
    public FlowClaimRecordE getFlowClaimRecord(ClaimFlowCommand command, FlowDetailE bankAccountFlowDetail) {
        FlowClaimRecordE flowClaimRecordE = new FlowClaimRecordE().setClaimDate(LocalDateTime.now())
                .setClaimAmount(command.getClaimAmount())
                .setSettleAmount(command.getSettleAmount())
                .setOurName(bankAccountFlowDetail.getOurName())
                .setOurAccount(bankAccountFlowDetail.getOurAccount())
                .setOurBank(bankAccountFlowDetail.getOurBank())
                .setTradingPlatform(bankAccountFlowDetail.getTradingPlatform())
                .setSysSource(command.getSysSource())
                .setClaimType(command.getClaimType())
                .setSupCpUnitId(command.getSupCpUnitId())
                .setSupCpUnitName(command.getSupCpUnitName())
                .setClaimName(ApiData.API.getUserName().get())
                .setReconcileFlag(0)
                .setFlowFiles(JSONObject.toJSONString(command.getFlowFileVos()))
                .setReportFiles(JSONObject.toJSONString(command.getReportFileVos()))
                // 差额认领标识
                // 0 差额认领 1或者其他 全额认领
                .setDifferenceFlag(null != command.getDifferenceFlag() && command.getDifferenceFlag().equals(0) ? 0 : 1)
                // 是否差额认领 差额认领则审核中   否则已审核
                // 审核状态: 流水审核状态 0 已审核 1 审核中 2 差额认领审批驳回 3差额认领待审核
                .setApproveState(null != command.getDifferenceFlag() && command.getDifferenceFlag().equals(0) ?  3 : 0)
                .setDifferenceReason(null != command.getDifferenceReason() ? Integer.valueOf(command.getDifferenceReason()) : null)
                .setDifferenceRemark(null != command.getDifferenceRemark()? command.getDifferenceRemark() : "");
        flowClaimRecordE.init();
        return flowClaimRecordE;
    }



    public Boolean difClaim(ClaimFlowCommand command, FlowClaimRecordE flowClaimRecord, List<FlowDetailE> flowDetailList) {
        flowClaimRecordRepository.save(flowClaimRecord);
        ArrayList<FlowClaimDetailE> flowClaimDetailList = createDifFlowClaimDetail(command.getClaimType(), flowClaimRecord.getId(),
                command.getInvoiceReceiptList(), command.getFlowBillList(), flowDetailList);
        return flowClaimDetailRepository.saveBatch(flowClaimDetailList);
    }


    private ArrayList<FlowClaimDetailE> createDifFlowClaimDetail(Integer claimType, Long flowClaimId, List<InvoiceReceiptE> invoiceReceiptList, List<ContractFlowBillV> flowBillList, List<FlowDetailE> flowDetailByIdList) {
        List<FlowDetailE> incomeFlowCollect = flowDetailByIdList.stream()
                .sorted(Comparator.comparing(FlowDetailE::getSettleAmount).reversed())
                .collect(Collectors.toList());
        log.info("收入流水:" + JSON.toJSON(incomeFlowCollect));
        List<ContractFlowBillV> collect = flowBillList.stream().sorted(Comparator.comparing(ContractFlowBillV::getTotalAmount).reversed()).collect(Collectors.toList());
        ArrayList<FlowClaimDetailE> flowClaimDetailList= new ArrayList<>();
        // 判断两个list的长度
        if (incomeFlowCollect.size() > collect.size()) {
            for (int i = 0; i < incomeFlowCollect.size(); i++) {
                if(i < collect.size()){
                    FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
                    flowClaimDetailE.init();
                    flowClaimDetailE.setInvoiceId(collect.get(i).getId());
                    flowClaimDetailE.setInvoiceAmount(collect.get(i).getTotalAmount());
                    flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
                    flowClaimDetailE.setSysSource(collect.get(i).getSysSource());
                    flowClaimDetailE.setClaimType(claimType);
                    flowClaimDetailE.setClaimIdType(3);
                    flowClaimDetailE.setFlowId(incomeFlowCollect.get(i).getId());
                    flowClaimDetailE.setFlowAmount(incomeFlowCollect.get(i).getSettleAmount());
                    flowClaimDetailList.add(flowClaimDetailE);
                } else {
                    FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
                    flowClaimDetailE.init();
                    flowClaimDetailE.setInvoiceId(collect.get(collect.size() -1).getId());
                    flowClaimDetailE.setInvoiceAmount(collect.get(collect.size() -1).getTotalAmount());
                    flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
                    flowClaimDetailE.setSysSource(collect.get(collect.size() -1).getSysSource());
                    flowClaimDetailE.setClaimType(claimType);
                    flowClaimDetailE.setClaimIdType(3);
                    flowClaimDetailE.setFlowId(incomeFlowCollect.get(i).getId());
                    flowClaimDetailE.setFlowAmount(incomeFlowCollect.get(i).getSettleAmount());
                    flowClaimDetailList.add(flowClaimDetailE);
                }
            }
        } else if (incomeFlowCollect.size() < collect.size()){
            for (int i = 0; i < collect.size(); i++) {
                if (i < incomeFlowCollect.size()){
                    FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
                    flowClaimDetailE.init();
                    flowClaimDetailE.setInvoiceId(collect.get(i).getId());
                    flowClaimDetailE.setInvoiceAmount(collect.get(i).getTotalAmount());
                    flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
                    flowClaimDetailE.setSysSource(collect.get(i).getSysSource());
                    flowClaimDetailE.setClaimType(claimType);
                    flowClaimDetailE.setClaimIdType(3);
                    flowClaimDetailE.setFlowId(incomeFlowCollect.get(i).getId());
                    flowClaimDetailE.setFlowAmount(incomeFlowCollect.get(i).getSettleAmount());
                    flowClaimDetailList.add(flowClaimDetailE);
                } else {
                    FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
                    flowClaimDetailE.init();
                    flowClaimDetailE.setInvoiceId(collect.get(i).getId());
                    flowClaimDetailE.setInvoiceAmount(collect.get(i).getTotalAmount());
                    flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
                    flowClaimDetailE.setSysSource(collect.get(i).getSysSource());
                    flowClaimDetailE.setClaimType(claimType);
                    flowClaimDetailE.setClaimIdType(3);
                    flowClaimDetailE.setFlowId(incomeFlowCollect.get(incomeFlowCollect.size() -1).getId());
                    flowClaimDetailE.setFlowAmount(incomeFlowCollect.get(incomeFlowCollect.size() -1).getSettleAmount());
                    flowClaimDetailList.add(flowClaimDetailE);
                }
            }
        }else {
            for (int i = 0; i < incomeFlowCollect.size(); i++) {
                FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
                flowClaimDetailE.init();
                flowClaimDetailE.setInvoiceId(collect.get(i).getId());
                flowClaimDetailE.setInvoiceAmount(collect.get(i).getTotalAmount());
                flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
                flowClaimDetailE.setSysSource(collect.get(i).getSysSource());
                flowClaimDetailE.setClaimType(claimType);
                flowClaimDetailE.setClaimIdType(3);
                flowClaimDetailE.setFlowId(incomeFlowCollect.get(i).getId());
                flowClaimDetailE.setFlowAmount(incomeFlowCollect.get(i).getSettleAmount());
                flowClaimDetailList.add(flowClaimDetailE);
            }
        }
        return flowClaimDetailList;
    }


        /**
         * 认领流水
         *
         * @param command command
         * @return Boolean
         */
    public Boolean claim(ClaimFlowCommand command, FlowClaimRecordE flowClaimRecord, List<FlowDetailE> flowDetailList) {

        // 保存流水认领记录
        flowClaimRecordRepository.save(flowClaimRecord);
        log.info("流水认领单据/发票信息" + JSON.toJSON(command.getInvoiceReceiptList()));
        //新增流水领用明细
        ArrayList<FlowClaimDetailE> flowClaimDetailList = createFlowClaimDetail(command.getClaimType(), flowClaimRecord.getId(),
                command.getInvoiceReceiptList(), command.getFlowBillList(), flowDetailList);
        log.info("flowClaimDetailRepository.saveBatch(flowClaimDetailList)" + JSON.toJSON(flowClaimDetailList));
        return flowClaimDetailRepository.saveBatch(flowClaimDetailList);
    }

    /**
     * 根据流水认领记录id获取认领详情
     *
     * @param recordId 流水认领记录id
     * @return Boolean
     */
    public FlowClaimRecordE queryByRecordId(Long recordId) {
        FlowClaimRecordE flowClaimRecordE = flowClaimRecordRepository.getById(recordId);
        if (Objects.isNull(flowClaimRecordE)) {
            throw BizException.throw400("未找到对应的流水认领记录，可能以删除");
        }
        //获取关联的领用明细
        List<FlowClaimDetailE> flowClaimDetailList = flowClaimDetailRepository.queryByFlowClaimRecordId(Collections.singletonList(recordId));
        List<Long> flowIdList = flowClaimDetailList.stream().map(FlowClaimDetailE::getFlowId).distinct().collect(Collectors.toList());
        List<Long> invoiceIdList = flowClaimDetailList.stream().map(FlowClaimDetailE::getInvoiceId).distinct().collect(Collectors.toList());
        flowClaimRecordE.setInvoiceIdList(invoiceIdList);
        flowClaimRecordE.setFlowIdList(flowIdList);
        return flowClaimRecordE;
    }

    /**
     * 根据流水领用记录id获取领用记录
     *
     * @param flowClaimIdList 流水领用id
     * @return List
     */
    public List<FlowClaimRecordE> queryByIdList(List<Long> flowClaimIdList) {
        if (CollectionUtils.isEmpty(flowClaimIdList)){
            return new ArrayList<>();
        }
        List<FlowClaimRecordE> flowClaimRecordList = flowClaimRecordRepository.listByIds(flowClaimIdList);
        List<FlowClaimDetailE> allFlowClaimDetailList = flowClaimDetailRepository.queryByFlowClaimRecordId(flowClaimIdList);
        Map<Long, List<FlowClaimDetailE>> collect = allFlowClaimDetailList.stream().collect(Collectors.groupingBy(FlowClaimDetailE::getFlowClaimRecordId));

        //设置流水id和发票id
        for (FlowClaimRecordE flowClaimRecordE : flowClaimRecordList) {
            List<FlowClaimDetailE> flowClaimDetailList = collect.get(flowClaimRecordE.getId());
            List<Long> invoiceIdList = flowClaimDetailList.stream().map(FlowClaimDetailE::getInvoiceId).distinct().collect(Collectors.toList());
            List<Long> flowIdList = flowClaimDetailList.stream().map(FlowClaimDetailE::getFlowId).distinct().collect(Collectors.toList());
            flowClaimRecordE.setInvoiceIdList(invoiceIdList);
            flowClaimRecordE.setFlowIdList(flowIdList);
        }
        return flowClaimRecordList;
    }

    /**
     * 创建认领详情
     *
     * @param claimType          认领类型
     * @param flowClaimId        流水认领记录id
     * @param invoiceReceiptList 发票信息
     * @param flowBillList       合同流水账单信息
     * @param flowDetailByIdList 流水信息
     */
    private ArrayList<FlowClaimDetailE> createFlowClaimDetail(Integer claimType, Long flowClaimId, List<InvoiceReceiptE> invoiceReceiptList, List<ContractFlowBillV> flowBillList, List<FlowDetailE> flowDetailByIdList) {
        ArrayList<FlowClaimDetailE> flowClaimDetailList = new ArrayList<>();
        //收入流水
        List<FlowDetailE> incomeFlowCollect = flowDetailByIdList.stream()
                .filter(s -> FlowTypeStatusEnum.收入.equalsByCode(s.getType()))
                .sorted(Comparator.comparing(FlowDetailE::getSettleAmount).reversed())
                .collect(Collectors.toList());
        log.info("收入流水:" + JSON.toJSON(incomeFlowCollect));
        //
        List<FlowDetailE> refundFlowCollect = flowDetailByIdList.stream()
                .filter(s -> FlowTypeStatusEnum.退款.equalsByCode(s.getType()))
                .sorted(Comparator.comparing(FlowDetailE::getSettleAmount).reversed())
                .collect(Collectors.toList());
        log.info("退款流水:" + JSON.toJSON(incomeFlowCollect));
        if(ClaimTypeEnum.发票认领.equalsByCode(claimType)){
            //缴费流水进行发票的开票金额和红冲金额认领
            chargeFlowClaim(flowClaimId, claimType, invoiceReceiptList, flowClaimDetailList, incomeFlowCollect, refundFlowCollect);
        }else if(ClaimTypeEnum.账单认领.equalsByCode(claimType)){
            //合同流水进行收入账单的金额和退款账单的金额认领
            contractFlowClaim(flowClaimId, claimType, flowBillList, flowClaimDetailList, incomeFlowCollect, refundFlowCollect);
        }
        return flowClaimDetailList;
    }

    /**
     * 合同流水进行收入账单的金额和退款账单的金额认领
     *
     * @param flowClaimId         认领记录id
     * @param flowBillList        发票信息
     * @param flowClaimDetailList 认领详情集合
     * @param incomeFlowCollect   收入流水
     * @param refundFlowCollect   退款流水
     */
    private void contractFlowClaim(Long flowClaimId, Integer claimType, List<ContractFlowBillV> flowBillList, ArrayList<FlowClaimDetailE> flowClaimDetailList, List<FlowDetailE> incomeFlowCollect, List<FlowDetailE> refundFlowCollect) {
        //退款账单
        List<ContractFlowBillV> refundBillList = flowBillList.stream().filter(s -> BillTypeEnum.退款账单.equalsByCode(s.getBillType())).collect(Collectors.toList());
        //非退款账单
        // 后续新增了负的收款单  导致这个地方有问题
        // 流水金额大于账单金额 会直接移除该流水  导致负的收款单关联不到流水
        // 进行排序 这样收款单金额小于流水金额。

        List<ContractFlowBillV> incomeBillList = flowBillList.stream().filter(s -> !BillTypeEnum.退款账单.equalsByCode(s.getBillType())).collect(Collectors.toList())
                .stream().sorted(Comparator.comparingLong(ContractFlowBillV::getTotalAmount)).collect(Collectors.toList());

        //未对应退款流水的退款账单
        List<ContractFlowBillV> notClaimRefundBillList = new ArrayList<>(refundBillList);
        log.info("未对应退款流水的退款账单" + JSON.toJSON(notClaimRefundBillList));
        //未对应收入流水的收入账单
        List<ContractFlowBillV> notClaimIncomeBillList = new ArrayList<>(incomeBillList);
        log.info("未对应收入流水的收入账单" + JSON.toJSON(notClaimIncomeBillList));
        //一、首先对收入流水和收入账单进行对应
        //1、首先对账单金额和收入流水金额一致的作为一条认领明细
        for (ContractFlowBillV contractFlowBillV : incomeBillList) {
            findContractFlowAndCostSame(flowClaimId, claimType, flowClaimDetailList, incomeFlowCollect, notClaimIncomeBillList, contractFlowBillV, false);
        }
        //2、未对应流水的发票进行多收入流水对应或者部分收入流水对应
        for (ContractFlowBillV contractFlowBillV : notClaimIncomeBillList) {
            findContractFlowAndCostNotSame(flowClaimId, claimType, flowClaimDetailList, incomeFlowCollect, contractFlowBillV, false);
        }

        //二、对退款账单金额和退款流水金额对应
        //1、首先对退款金额和退款流水金额一致的作为一条认领明细
        for (ContractFlowBillV contractFlowBillV : refundBillList) {
            findContractFlowAndCostSame(flowClaimId, claimType, flowClaimDetailList, refundFlowCollect, notClaimRefundBillList, contractFlowBillV, true);
        }
        //2、未对应流水的发票进行多退款流水对应或者部分退款流水对应
        for (ContractFlowBillV contractFlowBillV : notClaimRefundBillList) {
            findContractFlowAndCostNotSame(flowClaimId, claimType, flowClaimDetailList, refundFlowCollect, contractFlowBillV, true);
        }

    }

    /**
     * 缴费流水进行发票的开票金额和红冲金额认领
     *
     * @param flowClaimId         认领记录id
     * @param invoiceReceiptList  发票信息
     * @param flowClaimDetailList 认领详情集合
     * @param incomeFlowCollect   收入流水
     * @param refundFlowCollect   退款流水
     */
    private void chargeFlowClaim(Long flowClaimId, Integer claimType, List<InvoiceReceiptE> invoiceReceiptList, ArrayList<FlowClaimDetailE> flowClaimDetailList, List<FlowDetailE> incomeFlowCollect, List<FlowDetailE> refundFlowCollect) {
        //存在红冲金额并且未对应退款流水的发票
        List<InvoiceReceiptE> redNotClaimInvoiceReceiptList = invoiceReceiptList.stream().filter(s -> s.getRedTaxAmount() != 0).collect(Collectors.toList());
        //未对应收入流水的发票
        List<InvoiceReceiptE> notClaimInvoiceReceiptList = new ArrayList<>(invoiceReceiptList);

        //一、首先对收入流水和发票金额进行对应
        //1、首先对发票金额和收入流水金额一致的作为一条认领明细
        for (InvoiceReceiptE invoiceReceiptE : invoiceReceiptList) {
            findChargeFlowAndCostSame(flowClaimId, claimType, flowClaimDetailList, incomeFlowCollect, notClaimInvoiceReceiptList, invoiceReceiptE, false);
        }

        //2、未对应流水的发票进行多收入流水对应或者部分收入流水对应
        for (InvoiceReceiptE invoiceReceiptE : notClaimInvoiceReceiptList) {
            findChargeFlowAndCostNotSame(flowClaimId, claimType, flowClaimDetailList, incomeFlowCollect, invoiceReceiptE,false);
        }

        //二、对红冲金额和退款流水金额对应
        //1、首先对红冲金额和退款流水金额一致的作为一条认领明细
        for (InvoiceReceiptE invoiceReceiptE : invoiceReceiptList) {
            //红冲金额为 0 不做处理
            if(invoiceReceiptE.getRedTaxAmount() == 0){
                continue;
            }
            findChargeFlowAndCostSame(flowClaimId, claimType, flowClaimDetailList, refundFlowCollect, redNotClaimInvoiceReceiptList, invoiceReceiptE, true);
        }
        //2、未对应流水的发票进行多退款流水对应或者部分退款流水对应
        for (InvoiceReceiptE invoiceReceiptE : redNotClaimInvoiceReceiptList) {
            findChargeFlowAndCostNotSame(flowClaimId, claimType, flowClaimDetailList, refundFlowCollect, invoiceReceiptE,true);
        }
    }

    /**
     * 找到流水金额和账单金额未一一对应的数据
     *
     * @param flowClaimId 认领记录id
     * @param flowClaimDetailList 认领明细集合
     * @param flowCollect 流水数据
     * @param invoiceReceiptE 发票信息
     * @param refund 是否退款账单
     */
    private void findChargeFlowAndCostNotSame(Long flowClaimId,
                                              Integer claimType,
                                              ArrayList<FlowClaimDetailE> flowClaimDetailList,
                                              List<FlowDetailE> flowCollect,
                                              InvoiceReceiptE invoiceReceiptE,
                                              boolean refund) {
        Long taxAmount = refund ? invoiceReceiptE.getRedTaxAmount() : invoiceReceiptE.getPriceTaxAmount();
        long flowAmount = taxAmount;
        for (FlowDetailE flowDetailE : flowCollect) {
            FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
            flowClaimDetailE.init();
            flowClaimDetailE.setInvoiceId(invoiceReceiptE.getId());
            flowClaimDetailE.setInvoiceAmount(refund? -taxAmount : taxAmount);
            flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
            flowClaimDetailE.setSysSource(invoiceReceiptE.getSysSource());
            flowClaimDetailE.setClaimType(claimType);
            flowClaimDetailE.setClaimIdType(refund ? ClaimIdTypeEnum.红票.getCode() : ClaimIdTypeEnum.蓝票.getCode());
            //跳过已处理的流水
            if (flowDetailE.getDealFlag()) {
                continue;
            }
            long currentFlowAmount = flowDetailE.getSettleAmount();
            if (currentFlowAmount > flowAmount) {
                //流水金额大于发票金额的情况设置认领金额和流水id，更新当前流水金额，剩余的下次进行对比
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund? -flowAmount : flowAmount);
                flowDetailE.setSettleAmount(currentFlowAmount - flowAmount);
                flowClaimDetailList.add(flowClaimDetailE);
                break;
            } else if (currentFlowAmount == flowAmount) {
                //等于的情况设置认领金额和流水id，并移除已认领的流水
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund? -flowAmount : flowAmount);
                //移除已对应发票的流水
                flowCollect.remove(flowDetailE);
                flowClaimDetailList.add(flowClaimDetailE);
                break;
            } else {
                //小于的情况设置认领金额和流水id，移除已认领的流水，更新发票金额，进行下次认领
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund? -currentFlowAmount : currentFlowAmount);
                flowAmount -= currentFlowAmount;
                //移除已对应发票的流水
                flowDetailE.setDealFlag(Boolean.TRUE);
                flowClaimDetailList.add(flowClaimDetailE);
            }
        }
    }

    /**
     * 找到流水金额和账单金额未一一对应的数据
     *
     * @param flowClaimId 认领记录id
     * @param flowClaimDetailList 认领明细集合
     * @param flowCollect 流水数据
     * @param contractFlowBillV 合同账单
     * @param refund 是否退款账单
     */
    private void findContractFlowAndCostNotSame(Long flowClaimId,
                                                Integer claimType,
                                                ArrayList<FlowClaimDetailE> flowClaimDetailList,
                                                List<FlowDetailE> flowCollect,
                                                ContractFlowBillV contractFlowBillV,
                                                boolean refund) {
        Long totalAmount = contractFlowBillV.getTotalAmount();
        long flowAmount = totalAmount;
        for (FlowDetailE flowDetailE : flowCollect) {
            FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
            flowClaimDetailE.init();
            flowClaimDetailE.setInvoiceId(contractFlowBillV.getId());
            flowClaimDetailE.setInvoiceAmount(refund ? -totalAmount : totalAmount);
            flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
            flowClaimDetailE.setSysSource(contractFlowBillV.getSysSource());
            flowClaimDetailE.setClaimType(claimType);
            flowClaimDetailE.setClaimIdType(refund ? ClaimIdTypeEnum.退款单.getCode() : ClaimIdTypeEnum.收款单.getCode());
            //跳过已处理的流水
            if (flowDetailE.getDealFlag()) {
                continue;
            }
            long currentFlowAmount = flowDetailE.getSettleAmount();
            if (currentFlowAmount > flowAmount) {
                //流水金额大于账单金额的情况设置认领金额和流水id，更新当前流水金额，剩余的下次进行对比
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund ? -flowAmount : flowAmount);
                flowDetailE.setSettleAmount(currentFlowAmount - flowAmount);
                flowClaimDetailList.add(flowClaimDetailE);
                break;
            } else if (currentFlowAmount == flowAmount) {
                //等于的情况设置认领金额和流水id，并移除已认领的流水
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund ? -currentFlowAmount : currentFlowAmount);
                //移除已对应发票的流水
                flowCollect.remove(flowDetailE);
                flowClaimDetailList.add(flowClaimDetailE);
                break;
            } else {
                //小于的情况设置认领金额和流水id，移除已认领的流水，更新账单金额，进行下次认领
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund ? -currentFlowAmount : currentFlowAmount);
                flowAmount -= currentFlowAmount;
                //移除已对应发票的流水
                flowDetailE.setDealFlag(Boolean.TRUE);
                flowClaimDetailList.add(flowClaimDetailE);
            }
        }
    }


    /**
     * 找到合同流水与账单金额一一对应的数据作为一条认领明细
     *
     * @param flowClaimId 认领记录id
     * @param flowClaimDetailList 认领明细集合
     * @param flowCollect 流水数据
     * @param notClaimBillList 未一一对应的账单
     * @param contractFlowBillV 合同流水账单
     * @param refund 是否退款账单
     */
    private void findContractFlowAndCostSame(Long flowClaimId,
                                             Integer claimType,
                                             ArrayList<FlowClaimDetailE> flowClaimDetailList,
                                             List<FlowDetailE> flowCollect,
                                             List<ContractFlowBillV> notClaimBillList,
                                             ContractFlowBillV contractFlowBillV,
                                             boolean refund) {
        FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
        Long totalAmount = contractFlowBillV.getTotalAmount();
        flowClaimDetailE.init();
        flowClaimDetailE.setInvoiceId(contractFlowBillV.getId());
        flowClaimDetailE.setInvoiceAmount(refund ? -totalAmount : totalAmount);
        flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
        flowClaimDetailE.setSysSource(contractFlowBillV.getSysSource());
        flowClaimDetailE.setClaimType(claimType);
        flowClaimDetailE.setClaimIdType(refund ? ClaimIdTypeEnum.退款单.getCode() : ClaimIdTypeEnum.收款单.getCode());
        //找到对应的流水
        for (FlowDetailE flowDetailE : flowCollect) {
            if (flowDetailE.getSettleAmount().equals(totalAmount)) {
                //流水金额和账单金额一致
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund ? -totalAmount : totalAmount);
                //移除已对应账单的流水
                flowCollect.remove(flowDetailE);
                flowClaimDetailList.add(flowClaimDetailE);
                //移除已存在对应流水金额的账单
                notClaimBillList.remove(contractFlowBillV);
                break;
            }
        }
    }

    /**
     * 找到缴费流水与账单金额一一对应的数据作为一条认领明细
     *
     * @param flowClaimId 认领记录id
     * @param flowClaimDetailList 认领明细集合
     * @param flowCollect 流水数据
     * @param notClaimInvoiceReceiptList 未一一对应的账单
     * @param invoiceReceiptE 缴费发票信息
     * @param refund 是否红冲
     */
    private void findChargeFlowAndCostSame(Long flowClaimId,
                                           Integer claimType,
                                           ArrayList<FlowClaimDetailE> flowClaimDetailList,
                                           List<FlowDetailE> flowCollect,
                                           List<InvoiceReceiptE> notClaimInvoiceReceiptList,
                                           InvoiceReceiptE invoiceReceiptE,
                                           boolean refund) {
        FlowClaimDetailE flowClaimDetailE = new FlowClaimDetailE();
        Long taxAmount = refund ? invoiceReceiptE.getRedTaxAmount() : invoiceReceiptE.getPriceTaxAmount();
        flowClaimDetailE.init();
        flowClaimDetailE.setInvoiceId(invoiceReceiptE.getId());
        flowClaimDetailE.setInvoiceAmount(refund? -taxAmount: taxAmount);
        flowClaimDetailE.setFlowClaimRecordId(flowClaimId);
        flowClaimDetailE.setSysSource(invoiceReceiptE.getSysSource());
        flowClaimDetailE.setClaimType(claimType);
        // 目前不管流水认领的是红票还是蓝票，流水认领明细中都存储的蓝票ID
        flowClaimDetailE.setClaimIdType(refund ? ClaimIdTypeEnum.红票.getCode() : ClaimIdTypeEnum.蓝票.getCode());
        //找到对应的流水
        for (FlowDetailE flowDetailE : flowCollect) {
            if (flowDetailE.getSettleAmount().equals(taxAmount)) {
                //流水金额和发票金额一致
                flowClaimDetailE.setFlowId(flowDetailE.getId());
                flowClaimDetailE.setFlowAmount(refund? -taxAmount: taxAmount);
                //移除已对应发票的流水
                flowCollect.remove(flowDetailE);
                flowClaimDetailList.add(flowClaimDetailE);
                //移除已存在对应流水金额的发票
                notClaimInvoiceReceiptList.remove(invoiceReceiptE);
                break;
            }
        }
    }

    /**
     * 更新账单的银行账户信息
     * @param ourBankAccount 我方银行账号
     * @param oppositeBankAccount 对方银行账号
     * @param flowBillList 收/退款单
     * @param invoiceReceiptList 发票（蓝票）
     */
    public void getAndUpdateBankAccount(String ourBankAccount, String oppositeBankAccount,
                                  List<ContractFlowBillV> flowBillList, List<InvoiceReceiptE> invoiceReceiptList, String supCpUnitId) {
        // 获取我方账户的账户id和对方账户的账户id
        Long ourBankAccountId = this.getOurBankAccountId(ourBankAccount);
        Long oppositeBankAccountId = this.getOppoBankAccountId(oppositeBankAccount);

        // 应收、临时账单ID集合
        List<Long> receivableBillIdList = new ArrayList<>();
        // 预收账单ID集合
        List<Long> advanceBillIdList = new ArrayList<>();
        // 收款单ID集合
        List<Long> gatherBillIdList = new ArrayList<>();
        // 退款单ID集合
        List<Long> payBillIdList = new ArrayList<>();
        // 如果发票列表不为空，处理发票，获取其下的发票详情，然后分组对应的账单ID信息
        this.fillBillIdsByInvoiceReceiptList(invoiceReceiptList, receivableBillIdList, advanceBillIdList);

        // 如果账单列表不为空，处理账单，这里获取到的是gather_bill或者pay_bill
        this.fillBillIdsByFlowBillList(flowBillList, receivableBillIdList, advanceBillIdList, gatherBillIdList, payBillIdList, supCpUnitId);

        // 最后更新账单的银行账号
        this.updateBankAccount(ourBankAccountId, oppositeBankAccountId, receivableBillIdList, advanceBillIdList, gatherBillIdList, payBillIdList, supCpUnitId);
    }

    /**
     * 获取我方银行账户ID
     * @param ourBankAccount
     * @return
     */
    public Long getOurBankAccountId(String ourBankAccount) {
        Long ourBankAccountId = null;
        List<StatutoryBodyAccountE> accountByOurBankAccount = statutoryBodyAccountRepository.listByAccount(ourBankAccount);
        if (CollectionUtils.isNotEmpty(accountByOurBankAccount)) {
            ourBankAccountId = accountByOurBankAccount.get(0).getId();
        }
        return ourBankAccountId;
    }

    /**
     * 获取对方银行账户ID
     * @param oppositeBankAccount
     * @return
     */
    public Long getOppoBankAccountId(String oppositeBankAccount) {
        Long oppositeBankAccountId = null;
        List<StatutoryBodyAccountE> accountByOppositeBankAccount = statutoryBodyAccountRepository.listByAccount(oppositeBankAccount);
        if (CollectionUtils.isNotEmpty(accountByOppositeBankAccount)) {
            oppositeBankAccountId = accountByOppositeBankAccount.get(0).getId();
        }
        return oppositeBankAccountId;
    }

    /**
     * 发票列表补充各种账单ID
     * @param invoiceReceiptList
     * @param receivableBillIdList
     * @param advanceBillIdList
     */
    private void fillBillIdsByInvoiceReceiptList(List<InvoiceReceiptE> invoiceReceiptList, List<Long> receivableBillIdList, List<Long> advanceBillIdList) {
        // 如果发票列表不为空，处理发票，获取其下的发票详情，然后分组对应的账单ID信息
        if (CollectionUtils.isEmpty(invoiceReceiptList)) {
            return;
        }
        List<Long> invoiceReceiptIds = invoiceReceiptList.stream().map(InvoiceReceiptE::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(invoiceReceiptIds)) {
            return;
        }
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(invoiceReceiptIds);
        if (CollectionUtils.isEmpty(invoiceReceiptDetailES)) {
            return;
        }
        for (InvoiceReceiptDetailE invoiceReceiptDetailE : invoiceReceiptDetailES) {
            if (Objects.isNull(invoiceReceiptDetailE.getBillId())) continue;
            if (BillTypeEnum.应收账单.getCode() == invoiceReceiptDetailE.getBillType() || BillTypeEnum.临时收费账单.getCode() == invoiceReceiptDetailE.getBillType()) {
                receivableBillIdList.add(invoiceReceiptDetailE.getBillId());
            } else if (BillTypeEnum.预收账单.getCode() == invoiceReceiptDetailE.getBillType()) {
                advanceBillIdList.add(invoiceReceiptDetailE.getBillId());
            }
        }
    }

    /**
     * 账单列表补充各种账单ID
     * @param flowBillList
     * @param receivableBillIdList
     * @param advanceBillIdList
     * @param gatherBillIdList
     * @param payBillIdList
     */
    private void fillBillIdsByFlowBillList(List<ContractFlowBillV> flowBillList,
                                           List<Long> receivableBillIdList, List<Long> advanceBillIdList,
                                           List<Long> gatherBillIdList, List<Long> payBillIdList, String supCpUnitId) {
        if (CollectionUtils.isEmpty(flowBillList)) {
            return;
        }
        this.fillBillIdsByGatherBillList(flowBillList, receivableBillIdList, advanceBillIdList, gatherBillIdList, supCpUnitId);
        this.fillBillIdsByPayBillList(flowBillList, receivableBillIdList, advanceBillIdList, payBillIdList);
    }

    private void fillBillIdsByGatherBillList(List<ContractFlowBillV> flowBillList,
                                           List<Long> receivableBillIdList, List<Long> advanceBillIdList,
                                           List<Long> gatherBillIdList, String supCpUnitId) {
        // 收款单ID
        List<Long> gatherBillIds = flowBillList.stream().filter(e -> BillTypeEnum.收款单.getCode() == e.getBillType()).map(ContractFlowBillV::getId).collect(Collectors.toList());
        // 根据收款单获取收款详情里面的账单信息然后分别获取应收、临时表账单ID
        if (CollectionUtils.isEmpty(gatherBillIds)) return;
        List<GatherDetail> gatherDetails = gatherDetailRepository.getByGatherBillIds(gatherBillIds, supCpUnitId);
        if (CollectionUtils.isEmpty(gatherDetails)) {
            return;
        }
        for (GatherDetail gatherDetail : gatherDetails) {
            if (Objects.isNull(gatherDetail.getRecBillId())) continue;
            if (GatherTypeEnum.应收.getCode() == gatherDetail.getGatherType()) {
                receivableBillIdList.add(gatherDetail.getRecBillId());
            } else if (GatherTypeEnum.预收.getCode() == gatherDetail.getGatherType()) {
                advanceBillIdList.add(gatherDetail.getRecBillId());
            }
        }
        gatherBillIdList.addAll(gatherBillIds);
    }

    private void fillBillIdsByPayBillList(List<ContractFlowBillV> flowBillList,
                                             List<Long> receivableBillIdList, List<Long> advanceBillIdList,
                                             List<Long> payBillIdList) {
        // 退款账单ID
        List<Long> payBillIds = flowBillList.stream().filter(e -> BillTypeEnum.退款账单.getCode() == e.getBillType()).map(ContractFlowBillV::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(payBillIds)) {
            return;
        }
        // 根据退款单获取退款详情
        List<PayDetail> payDetails = payDetailRepository.queryByPayBillIdList(payBillIds);
        if (CollectionUtils.isEmpty(payDetails)) {
            return;
        }
        // 这里将退款单详情对应的账单ID都加入到两个集合中
        for (PayDetail payDetail : payDetails) {
            if (Objects.isNull(payDetail.getPayableBillId())) continue;
            receivableBillIdList.add(payDetail.getPayableBillId());
            advanceBillIdList.add(payDetail.getPayableBillId());
        }
        // 添加账单ID
        payBillIdList.addAll(payBillIds);
    }

    /**
     * 更新银行账户信息
     * @param ourBankAccountId
     * @param oppositeBankAccountId
     * @param receivableBillIdList
     * @param advanceBillIdList
     * @param gatherBillIdList
     * @param payBillIdList
     */
    private void updateBankAccount(Long ourBankAccountId, Long oppositeBankAccountId,
                                   List<Long> receivableBillIdList, List<Long> advanceBillIdList,
                                   List<Long> gatherBillIdList, List<Long> payBillIdList,
                                   String supCpUnitId) {
        if (Objects.isNull(ourBankAccountId)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(gatherBillIdList)) {
            gatherBillRepository.updateSbAccountId(ourBankAccountId, gatherBillIdList, supCpUnitId);
        }
        if (CollectionUtils.isNotEmpty(receivableBillIdList)) {
            receivableBillRepository.updateSbAccountId(ourBankAccountId, receivableBillIdList, supCpUnitId);
        }
        if (CollectionUtils.isNotEmpty(advanceBillIdList)) {
            advanceBillRepository.updateSbAccountId(ourBankAccountId, advanceBillIdList);
        }
        if (CollectionUtils.isNotEmpty(payBillIdList) && Objects.nonNull(oppositeBankAccountId)) {
            payBillRepository.updateAccountId(ourBankAccountId, oppositeBankAccountId, payBillIdList);
        }
    }

    /**
     * 根据流水ID获取流水认领记录ID
     * @param flowId
     * @return
     */
    public Long queryIdByFlowId(Long flowId) {
        return flowClaimRecordRepository.queryIdByFlowId(flowId);
    }

    /**
     * 根据流水认领记录ID获取流水认领明细对应的发票ID
     * @param flowClaimRecordId
     * @return
     */
    public List<Long> queryInvoiceIdsByFlowClaimRecordId(Long flowClaimRecordId) {
        return flowClaimDetailRepository.queryInvoiceIdsByFlowClaimRecordId(flowClaimRecordId);
    }


    /**
     * 根据收款单ID列表获取关联流水认领明细
     * @param gatherIds 收款单ID列表
     * @param supCpUnitId 项目ID
     * @return {@link ReconciliationDetailE}
     */
    public List<FlowClaimDetailE> getRecGatherIdFlowClaimRecord(List<Long> gatherIds,String supCpUnitId){
        return flowClaimDetailRepository.list(new LambdaQueryWrapper<FlowClaimDetailE>()
                .in(FlowClaimDetailE::getInvoiceId,gatherIds)
                .eq(FlowClaimDetailE::getClaimType,ClaimTypeEnum.账单认领.getCode())
                .eq(FlowClaimDetailE::getState, Const.State._0)
                .eq(FlowClaimDetailE::getDeleted,Const.State._0));
    }

    public IPage<FlowRecordPageV> getPageRecord(PageF<SearchF<FlowRecordPageV>> queryF) {
        QueryWrapper<FlowRecordPageV> queryWrapper = queryF.getConditions().getQueryModel();
        Page<FlowClaimRecordE> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        return flowClaimRecordRepository.getPageRecord(page, queryWrapper);
    }

    public FlowRecordStatisticsV getFlowRecordStatistics(PageF<SearchF<?>> queryF){
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        return flowClaimRecordRepository.getFlowRecordStatistics(queryWrapper);
    }

    public  boolean updateFlowByApprove(UpdateFlowF updateFlowF) {
        FlowClaimRecordE flowClaimRecordE = flowClaimRecordRepository.getById(updateFlowF.getId());
        List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(Collections.singletonList(updateFlowF.getId()));
        List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getFlowId).collect(Collectors.toList());
        List<FlowDetailE> flowDetailES = flowDetailRepository.listByIds(collect);
        // 判断审核标识
        if (updateFlowF.getFlag().equals(1)){
            // 1  审核拒绝
            // 更新流水认领记录
            flowClaimRecordE.setApproveState(2);
            flowClaimRecordE.setReviewComments(updateFlowF.getReviewComments());
            flowClaimRecordE.setRefuseName(ApiData.API.getUserName().get());
            flowClaimRecordE.setRefuseTime(LocalDateTime.now());
            // 更新流水状态
            for (FlowDetailE flowDetailE : flowDetailES) {
                flowDetailE.setClaimStatus(FlowClaimStatusEnum.已认领.getCode());
            }
            String  content = "认领批次号:" + flowClaimRecordE.getSerialNumber() +", 驳回原因：" +updateFlowF.getReviewComments();
            noticeBusiness(content,flowClaimRecordE, MsgModelCodeEnum.FLOW_CLAIM);
        } else {
            // 0  审核通过
            // 更新流水认领记录
            flowClaimRecordE.setApproveState(0);
            flowClaimRecordE.setReviewComments(updateFlowF.getReviewComments());
            // 更新流水状态
            for (FlowDetailE flowDetailE : flowDetailES) {
                flowDetailE.setClaimStatus(FlowClaimStatusEnum.已认领.getCode());
            }
        }
        flowClaimRecordRepository.updateById(flowClaimRecordE);
        flowDetailRepository.updateBatchById(flowDetailES);
        return true;
    }

    // 发送站内信
    private void noticeBusiness(String content, FlowClaimRecordE flowClaimRecordE, MsgModelCodeEnum msgModelCodeEnum) {
        NoticeBusinessD noticeBusinessD = new NoticeBusinessD();
        noticeBusinessD.setCardCode(MsgCardCodeTypeEnum.FLOW_CLAIM_REFUSE.name())
                .setNoticeCardType(1)
                .setNoticeCardTypeName("文章消息卡片")
                .setTitle("流水差额认领申请已驳回")
                .setContent(content)
                .setModelCode(msgModelCodeEnum.getCode())
                .setModelCodeName(msgModelCodeEnum.getName())
                .setNoticeTime(LocalDateTime.now())
                .setUserId(Collections.singletonList(flowClaimRecordE.getCreator()));
        msgClient.add(noticeBusinessD);
    }


}
