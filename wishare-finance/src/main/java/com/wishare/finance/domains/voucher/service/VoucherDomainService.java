package com.wishare.finance.domains.voucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.voucher.fo.SetDetailsF;
import com.wishare.finance.apps.model.voucher.vo.BillPostedStatusV;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.model.voucher.vo.VoucherV;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.repository.AccountBookRepository;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitRepository;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.repository.CashFlowRepository;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectRuleInOutEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectRuleMapTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.SubjectCashFlow;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import com.wishare.finance.domains.configure.subject.repository.SubjectCashFlowRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectMapUnitDetailRepository;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherCashFlowTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherLoanTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherSystemEnum;
import com.wishare.finance.domains.voucher.entity.*;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.repository.*;
import com.wishare.finance.domains.voucher.repository.yuanyang.YyNccCustomerRelRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AssF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.CashFlowF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.DetailsF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.UfinterfaceF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.VoucherF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.VoucherHeadF;
import com.wishare.finance.infrastructure.remote.vo.yonyounc.SendresultV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/24 20:08
 * @version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherDomainService {

    private final VoucherFacade voucherFacade;
    private final VoucherRepository voucherRepository;
    private final CashFlowRepository cashFlowRepository;
    private final VoucherInfoRepository voucherInfoRepository;
    private final VoucherBillRepository voucherBillRepository;

    private final SubjectCashFlowRepository subjectCashFlowRepository;

    private final SubjectMapUnitDetailRepository subjectMapUnitDetailRepository;
    private final VoucherBusinessDetailRepository voucherBusinessDetailRepository;
    private final VoucherInferenceRecordRepository voucherInferenceRecordRepository;
    private final BusinessUnitRepository businessUnitRepository;
    private final AccountBookRepository accountBookRepository;
    protected final CloseAccountRepository closeAccountRepository;
    private final YyNccCustomerRelRepository yyNccCustomerRelRepository;
    @Value("${wishare.finance.voucher.ncc.sender:001}")
    private String nccSender;

    /**
     * 制作凭证
     * @param voucher 凭证信息
     * @return
     */
    public Long makeVoucher(Voucher voucher){
        voucher.init();
        voucherInfoRepository.save(voucher);
        return voucher.getId();
    }

    /**
     * 批量制作凭证
     * @param vouchers 凭证列表
     * @return
     */
    public boolean makeVouchers(List<Voucher> vouchers){
        vouchers.forEach(Voucher::init);
        return voucherInfoRepository.saveBatch(vouchers);
    }


    /**
     * 同步
     * @param voucherIds
     * @param voucherSystem
     * @return
     */
    public SyncBatchVoucherResultV syncBatchVoucher(List<Long> voucherIds, VoucherSystemEnum voucherSystem){
        List<Voucher> vouchers = voucherInfoRepository.listByIds(voucherIds);
        SyncBatchVoucherResultV result = new SyncBatchVoucherResultV();
        List<Long> errorList = new ArrayList<>();
        int successCount = vouchers.size();
        for (Voucher voucher : vouchers) {
            try {
                SendresultV sendresultV = syncToNcc(voucher);
                voucher.setSyncSystem(voucherSystem.getCode());
                if (null != sendresultV && 1 == sendresultV.getResultcode()) {
                    voucher.setState(VoucherStateEnum.成功.getCode());
                    voucher.setSyncSystemVoucherNo(sendresultV.getNo());
                } else {
                    voucher.setState(VoucherStateEnum.失败.getCode());
                    //voucher.setErrorReason(dealReason(sendresultV.getResultdescription()));
                    voucher.setErrorReason(sendresultV.getResultdescription());
                    errorList.add(voucher.getId());
                    successCount --;
                }
            }catch (Exception e){
                log.error("凭证推送失败 ---------> : ", e);
                voucher.setState(VoucherStateEnum.失败.getCode());
                voucher.setErrorReason("系统内部错误，请重新发起同步，多次错误请联系系统管理员");
                errorList.add(voucher.getId());
                successCount --;
            }
            voucherInfoRepository.updateById(voucher);
        }

        String level = "success";
        if (successCount > 0){
            level = "warn";
        }else if (successCount <= 0){
            level = "error";
        }
        result.setLevel(level);
        result.setErrorTotal(vouchers.size() - successCount);
        result.setSuccessTotal(successCount);
        result.setErrorList(errorList);
        return result;
    }

    /**
     * 作废
     * @param voucherIds
     * @return
     */
    public Map<String,Object> cancelBatch(List<Long> voucherIds){
        Map<String, Object> returnMap = new HashMap<>();
        List<Voucher> vouchers = voucherInfoRepository.listByIds(voucherIds);
        SyncBatchVoucherResultV result = new SyncBatchVoucherResultV();
        List<Long> errorList = new ArrayList<>();
        int successCount = vouchers.size();
        StringBuilder stringBuilder = new StringBuilder("");
        for (Voucher voucher : vouchers) {
            if(!voucher.getState().equals(0) && !voucher.getState().equals(2)){
                stringBuilder.append("报账凭证编号:").append(voucher.getVoucherNo()).append("作废失败,失败原因:只支持待同步和同步失败状态凭证作废;");
                errorList.add(voucher.getId());
                successCount --;
                continue;
            }
            boolean update = voucherInfoRepository.update(new LambdaUpdateWrapper<Voucher>().eq(Voucher::getId, voucher.getId()).set(Voucher::getState, 4));
            if (!update){
                stringBuilder.append("报账凭证编号:").append(voucher.getVoucherNo()).append("作废失败,失败原因:凭证状态修改失败;");
                errorList.add(voucher.getId());
                successCount --;
            }
        }
        if (successCount==0){
            returnMap.put("success",false);
        }else {
            returnMap.put("success",true);
        }
        returnMap.put("errorMessage",stringBuilder.toString());
        returnMap.put("successTotal",successCount);
        returnMap.put("errorTotal",vouchers.size()-successCount);
        returnMap.put("errorList",errorList);
        return returnMap;
    }

    public BillPostedStatusV checkPostedStatus(Long billId, BillTypeEnum billTypeEnum) {
        List<Voucher> vouchers = voucherInfoRepository.listByBusinessId(billId, billTypeEnum.getCode());
        BillPostedStatusV billPostedStatusV = new BillPostedStatusV();
        billPostedStatusV.setIsPosted(false);
        vouchers.forEach(voucher -> {
            if (VoucherStateEnum.成功.getCode() == voucher.getState()) {
                billPostedStatusV.setIsPosted(true);
            }
        });
        return billPostedStatusV;
    }

    public Boolean setDetails(SetDetailsF setDetailsF) {
        Voucher voucher = voucherInfoRepository.getById(setDetailsF.getVoucherId());
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",voucher.getId());
        updateWrapper.set("details",setDetailsF.getDetails());

        return voucherInfoRepository.update(updateWrapper);
    }


    /**
     * 同步凭证
     * @param voucherId
     * @param voucherSystem
     * @return
     */
    public Voucher syncVoucher(Long voucherId, VoucherSystemEnum voucherSystem){
        Voucher voucher = voucherInfoRepository.getById(voucherId);
        ErrorAssertUtil.notNullThrow403(voucher, ErrorMessage.VOUCHER_NOT_EXIST);
        if(VoucherStateEnum.成功.equalsByCode(voucher.getState())){
            //如果已经成功过，则不需要再次推送
            return voucher;
        }
        try {
            SendresultV sendresultV = syncToNcc(voucher);
            voucher.setSyncSystem(voucherSystem.getCode());
            voucher.setSyncTime(LocalDateTime.now());
            if (null != sendresultV && 1 == sendresultV.getResultcode()) {
                voucher.setState(VoucherStateEnum.成功.getCode());
                voucher.setSyncSystemVoucherNo(sendresultV.getNo());
            } else {
                voucher.setState(VoucherStateEnum.失败.getCode());
                //voucher.setErrorReason(dealReason(sendresultV.getResultdescription()));
                voucher.setErrorReason(sendresultV.getResultdescription());
            }
        }catch (Exception e){
            log.error("凭证推送失败 ---------> : ", e);
            voucher.setState(VoucherStateEnum.失败.getCode());
            voucher.setErrorReason("系统内部错误，请重新发起同步，多次错误请联系系统管理员");
        }
        voucherInfoRepository.updateById(voucher);
        return voucher;
    }

    public Long insert(VoucherE voucherE) {
        voucherRepository.save(voucherE);
        return voucherE.getId();
    }

    /**
     * 批量插入凭证明细
     * @param list
     */
    public void batchInsert(List<VoucherE> list) {
        voucherRepository.saveBatch(list);
    }

    public PageV<VoucherV> queryPage(PageF<SearchF<?>> form) {
        Page<VoucherE> page;
        boolean match = false;
        if (!CollectionUtils.isEmpty(form.getConditions().getFields())) {
            match = form.getConditions().getFields().stream()
                .anyMatch(field -> "recordId".equals(field.getName()));
        }
        if (match) {
            Optional<Field> record = form.getConditions().getFields().stream().
                filter(field -> "recordId".equals(field.getName())).findFirst();
            form.getConditions().setFields(form.getConditions().getFields().stream().
                filter(field -> !"recordId".equals(field.getName())).collect(Collectors.toList()));

            if (record.isPresent()) {
                if (Objects.nonNull(record.get().getValue())) {
                    return queryPageByRecordId(form, Long.parseLong(record.get().getValue().toString()));
                }
            }
        }
        page = voucherRepository.queryPage(form);

        if (Optional.ofNullable(page).isEmpty()) {
            return new PageV<>();
        }
        List<VoucherV> list = new ArrayList<>();
        for (VoucherE record : page.getRecords()) {
            VoucherV voucherV = Global.mapperFacade.map(record, VoucherV.class);
            if (StringUtils.isNotBlank(record.getBillList())) {
                //voucherV.setBillLists(JSONArray.parseArray(record.getBillList(), SimpleVoucherBill.class));
            }
            list.add(voucherV);
        }
        return PageV.of(form, page.getTotal(), list);
    }

    /**
     * 通过推凭记录查询推凭明细
     * @param form
     * @param recordId
     * @return
     */
    public PageV<VoucherV> queryPageByRecordId(PageF<SearchF<?>> form, Long recordId) {
        VoucherInferenceRecordE record = voucherInferenceRecordRepository.getById(recordId);
        Page<VoucherE> page = voucherRepository.queryPageByRecordId(form, JSONArray.parseArray(record.getVoucherIds(), Long.class));
        if (Optional.ofNullable(page).isEmpty()) {
            return new PageV<>();
        }
        return PageV.of(form, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), VoucherV.class));
    }

    /**
     * 批量保存推凭的账单快照
     * @param list
     * @param billTypeEnum
     * @param voucherIds
     */
    public void batchInsertVoucherBills(List<BillInferenceV> list, BillTypeEnum billTypeEnum, List<Long> voucherIds) {
        List<VoucherBillDetailE> voucherBillES = Global.mapperFacade.mapAsList(list, VoucherBillDetailE.class);
        voucherBillES.forEach(voucherBillDetailE -> {
            voucherBillDetailE.setVoucherId(voucherIds.get(0));
            voucherBillDetailE.setType(billTypeEnum.getCode());
        });
        voucherBillRepository.saveBatch(voucherBillES);
    }

    /**
     * 通过账单编号和推凭事件类型获取最后一次推凭的明细
     * @param billNo
     * @param eventTypeEnum
     * @return
     */
    public VoucherE getLastVoucher(String billNo, EventTypeEnum eventTypeEnum) {
        LambdaQueryWrapper<VoucherE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VoucherE::getBillNo, billNo);
        wrapper.eq(VoucherE::getEvenType, eventTypeEnum.getEvent());
        wrapper.orderByDesc(VoucherE::getGmtCreate);
        wrapper.last(" limit 1");
        return voucherRepository.getOne(wrapper);
    }

    /**
     * 通过账单编号获取最后一次推凭的明细
     * @param billNo
     * @return
     */
    public VoucherE getLastVoucherByBillNo(String billNo) {
        LambdaQueryWrapper<VoucherE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VoucherE::getBillNo, billNo);
        wrapper.orderByDesc(VoucherE::getGmtCreate);
        wrapper.last(" limit 1");
        return voucherRepository.getOne(wrapper);
    }

    public Long staticVoucherAmount(PageF<SearchF<?>> form) {
        boolean match = false;
        if (!CollectionUtils.isEmpty(form.getConditions().getFields())) {
            match = form.getConditions().getFields().stream()
                .anyMatch(field -> "recordId".equals(field.getName()));
        }
        if (match) {
            Optional<Field> record = form.getConditions().getFields().stream().
                filter(field -> "recordId".equals(field.getName())).findFirst();
            form.getConditions().setFields(form.getConditions().getFields().stream().
                filter(field -> !"recordId".equals(field.getName())).collect(Collectors.toList()));

            if (record.isPresent()) {
                if (Objects.nonNull(record.get().getValue())) {
                    VoucherInferenceRecordE recordE = voucherInferenceRecordRepository.
                        getById(Long.parseLong(record.get().getValue().toString()));
                    if (Objects.nonNull(recordE) && StringUtils.isNotBlank(recordE.getVoucherIds())) {
                        form.getConditions().getFields().add(new Field("id", JSONArray.parseArray(recordE.getVoucherIds(), Long.class), 15));
                    }
                }
            }
        }
       return voucherRepository.staticVoucherAmount(form);
    }

    /**
     * 提供ids批量修改凭证号和状态
     *
     * @param ids
     * @param reason
     * @param success   结果
     */
    public void updateVoucherNoByIds(List<Long> ids, String reason, boolean success) {
        voucherRepository.updateVoucherNoByIds(ids, reason, success);
    }

    /**
     * 根据账单id获取凭证明细列表
     *
     * @param billId
     * @param billTypeEnum
     * @return
     */
    public List<Voucher> listByBillId(Long billId, BillTypeEnum billTypeEnum) {
        return voucherInfoRepository.listByBusinessId(billId, billTypeEnum.getCode());
    }

    public List<VoucherE> listById(List<Long> voucherIds) {
        return voucherRepository.listByIds(voucherIds);
    }

    /**
     * 批量修改凭证的状态
     * @param ids
     * @param inferState
     */
    public void updateVoucherNoInferStateByIds(List<Long> ids, int inferState) {
        voucherRepository.updateVoucherNoInferStateByIds(ids, inferState);
    }

    protected String dateCheck(LocalDate date){
        LocalDate fiscalPeriod = date;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
        String dateStr = fiscalPeriod.format(df);
        return dateStr;
    }
    /**
     * 推送凭证至用友ncc
     * @param voucher
     * @return
     */
    public SendresultV syncToNcc(Voucher voucher){
        //根据账簿查询账簿所关联的业务单元信息
        AccountBookE accountBookE = accountBookRepository.getById(voucher.getAccountBookId());
        BusinessUnitE businessUnitE = businessUnitRepository.getById(accountBookE.getStatutoryBodyId());
        if (Objects.isNull(businessUnitE)) {
            int codeIndex = accountBookE.getCode().lastIndexOf("-");
            businessUnitE = businessUnitRepository.getOne(
                    new LambdaUpdateWrapper<BusinessUnitE>().eq(BusinessUnitE::getCode,
                            accountBookE.getCode().substring(0, codeIndex)));
        }
        UfinterfaceF form = new UfinterfaceF().create();
        form.setSender(nccSender);
        VoucherF voucherF = new VoucherF();
        voucherF.setId(String.valueOf(voucher.getId()));
        form.setVoucher(voucherF);
        VoucherHeadF voucherHead = new VoucherHeadF().create();
        //2023-05-19 用友ncc开发人员说不需要传这个参数
        //voucherHead.setPk_voucher(voucher.getVoucherNo());
        voucherHead.setYear(String.valueOf(voucher.getFiscalPeriod().getYear())); // 归纳的年份
        voucherHead.setPk_system("GL");  // 来源系统 默认GL
        voucherHead.setPk_accountingbook(voucher.getAccountBookCode());

        //判断当前账簿是否关账且凭证会计期间在这个内
        CloseAccount closeAccount = closeAccountRepository.getBookIdBuyAccount(voucher.getAccountBookId(),
                dateCheck(voucher.getFiscalPeriod()));
        // 说明关账了。
        if (ObjectUtil.isNotNull(closeAccount)){
            // 处理会计月
            LocalDate d = LocalDate.parse(closeAccount.getAccountingPeriod() + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            voucher.setFiscalPeriod(d);
            voucher.setFiscalYear(d.getYear());
        }


        voucherHead.setPeriod(DateTimeUtil.formatMonthNoc(voucher.getFiscalPeriod())); // 凭证所属的月份
        voucherHead.setPrepareddate(voucher.getFiscalPeriod().toString()+" 01:00:00");
        voucherHead.setPk_prepared(voucher.getMakerId());
        voucherHead.setTallydate(""); // 记账日期
        voucherHead.setPk_manager(""); // 记账人
        int pkOrgIndex = voucher.getAccountBookCode().lastIndexOf("-");
        if (pkOrgIndex > 1){
            voucherHead.setPk_org(voucher.getAccountBookCode().substring(0,  pkOrgIndex));
        }else {
            voucherHead.setPk_org(voucher.getAccountBookCode());
        }
        voucherHead.setPk_org_v("");
        voucherHead.setPk_group("G");
        int index = 0;
        List<DetailsF> details = new ArrayList<>();
        for (VoucherDetailOBV detail : voucher.getDetails()) {
            DetailsF detailsF = new DetailsF();
            detailsF.setDetailindex((index++) + "");
            detailsF.setExplanation(detail.getSummary());
            //detailsF.setVerifydate("");
            //detailsF.setPrice("");
            //detailsF.setExcrate2("1");
            detailsF.setPk_accasoa(detail.getSubjectCode());
            detailsF.setDebitquantity("");
            if (VoucherLoanTypeEnum.借方.equalsByCode(detail.getType())){
                detailsF.setDebitamount(AmountUtils.toStringAmount(detail.getDebitAmount()));
                detailsF.setLocaldebitamount(detailsF.getDebitamount());
            }else {
                detailsF.setCreditamount(AmountUtils.toStringAmount(detail.getCreditAmount()));
                detailsF.setLocalcreditamount(detailsF.getCreditamount());
            }
            detailsF.setGroupdebitamount("");
            detailsF.setGlobaldebitamount("");
            detailsF.setPk_currtype("CNY");                    //
            detailsF.setPk_unit("");
            detailsF.setPk_unit_v("");
            List<AssisteItemOBV> assisteItems = detail.getAssisteItems();
            if (CollectionUtils.isNotEmpty(assisteItems)){
                detailsF.setAss(assisteItems.stream().map(i -> {
                    AssF assF = new AssF();
                    assF.setPk_Checktype(i.getAscCode());
                    assF.setPk_Checkvalue(i.getCode());
                    return assF;
                }).collect(Collectors.toList()));
            }
            //现金流量
            List<CashFlowOBV> cashFlows = detail.getCashFlows();
            if (CollectionUtils.isNotEmpty(cashFlows)){
                BusinessUnitE finalBusinessUnitE = businessUnitE;
                detailsF.setCashFlow(cashFlows.stream().map(item -> {
                    CashFlowF cashFlowF = new CashFlowF();
                    BigDecimal amount = AmountUtils.toDecimal(item.getMoney());
                    cashFlowF.setPk_innercorp(finalBusinessUnitE.getCode());
                    cashFlowF.setPk_cashflow(item.getCode());
                    cashFlowF.setMoneymain(amount);
                    cashFlowF.setMoney(amount);
                    cashFlowF.setMoneygroup(amount);
                    cashFlowF.setMoneyglobal(amount);
                    cashFlowF.setM_pk_currtype("CNY");
                    return cashFlowF;
                }).collect(Collectors.toList()));
            }

            details.add(detailsF);
        }
        voucherHead.setDetails(details);
        voucherF.setVoucher_head(voucherHead);

        //推送nc
        log.info("开始发起凭证推送请求，凭证[" + voucher.getVoucherNo()+ "] 参数: {}", JSON.toJSONString(form));
        SendresultV sendresultV = voucherFacade.pushVoucher(form);
        log.info("成功发起凭证推送请求，凭证[" + voucher.getVoucherNo() +"] 结果: {}", JSON.toJSONString(sendresultV));
        return sendresultV;
    }

    /**
     * 截取需要的异常
     * @param resultdescription
     * @return
     */
    private String dealReason(String resultdescription) {
        if (resultdescription.contains("异常信息")) {
            return resultdescription.substring(resultdescription.indexOf("异常信息:") + 5);
        } else if (resultdescription.contains("处理错误")) {
            return resultdescription.substring(resultdescription.indexOf("处理错误:") + 5);
        } else {
            return resultdescription;
        }
    }

    /**
     * 设置
     * @param details
     * @param subjectMap
     */
    public void putVoucherCashFlow(List<VoucherDetailOBV> details, Map<String, SubjectE> subjectMap){
        Map<String, List<VoucherDetailOBV>> loanGroup = details.stream().collect(Collectors.groupingBy(VoucherDetailOBV::getType));
        List<VoucherDetailOBV> creditDetails = loanGroup.get(VoucherLoanTypeEnum.贷方.getCode());
        List<VoucherDetailOBV> debitDetails = loanGroup.get(VoucherLoanTypeEnum.借方.getCode());
        //##2023-05-10 现金流直接去分录科目费项所对应的现金流
        //###设置借方现金流
        //判断贷方科目是否存在现金流科目，如果存在现金流科目，则借方设置现金流信息
        if (hasCashFlowSubject(creditDetails, subjectMap)){
            setVoucherCashFlow(debitDetails, SubjectRuleInOutEnum.流入, subjectMap,VoucherLoanTypeEnum.贷方.getCode());
        }
        //###设置贷方现金流
        //判断借方科目是否存在现金流科目，如果存在现金流科目，则贷方设置现金流信息
        if (hasCashFlowSubject(debitDetails, subjectMap)){
            setVoucherCashFlow(creditDetails, SubjectRuleInOutEnum.流出, subjectMap,VoucherLoanTypeEnum.借方.getCode());
        }
    }

    public void setVoucherCashFlow(List<VoucherDetailOBV> details, SubjectRuleInOutEnum inOut, Map<String, SubjectE> subjectMap,String loanType) {
        if (CollectionUtils.isEmpty(details)) {
            return;
        }
        List<SubjectCashFlow> subjectCashFlows;
        for (VoucherDetailOBV detail : details) {
            SubjectE subject = subjectMap.get(detail.getSubjectCode());
            //2023-05-15 现金流添加逻辑，如果是税费科目则不需要填写现金流信息
            if (Objects.nonNull(subject.getExistTax()) && subject.getExistTax() == 1){
                continue;
            }
                        //2023-05-15 如果是现金科目则不需要加现金流信息
            if (subject.verifyCashSubject()){
                continue;
            }
            if (Objects.nonNull(detail.getChargeItemId())){
                // 根据费项获取现金流信息
                SubjectMapUnitDetailE mapUnitDetail = subjectMapUnitDetailRepository.getByUnitId(detail.getChargeItemId(), SubjectRuleMapTypeEnum.现金流量.getCode());
                if (Objects.nonNull(mapUnitDetail)){
                    CashFlowE cashFlow = cashFlowRepository.getById(mapUnitDetail.getSubjectLevelLastId());
                    if (Objects.nonNull(cashFlow)){
                        CashFlowOBV cashFlowOBV = new CashFlowOBV()
                                .setType(Integer.valueOf(cashFlow.getItemType()))
                                .setCurrency("CNY")
                                .setMoney(extracted(Integer.valueOf(cashFlow.getItemType()), detail,loanType));
                        cashFlowOBV.setCode(cashFlow.getCode());
                        cashFlowOBV.setName(cashFlow.getName());
                        detail.setCashFlows(List.of(cashFlowOBV)); //设置现金流量
                    }
                }
            } else if (Objects.nonNull(detail.getCashFlowId())) {
                CashFlowE cashFlow = cashFlowRepository.getById(detail.getCashFlowId());
                if (Objects.nonNull(cashFlow)){
                    CashFlowOBV cashFlowOBV = new CashFlowOBV()
                            .setType(Integer.valueOf(cashFlow.getItemType()))
                            .setCurrency("CNY")
                            .setMoney(extracted(Integer.valueOf(cashFlow.getItemType()), detail,loanType));
                    cashFlowOBV.setCode(cashFlow.getCode());
                    cashFlowOBV.setName(cashFlow.getName());
                    detail.setCashFlows(List.of(cashFlowOBV)); //设置现金流量
                }
            } else {
                //1.科目编码和费项获取借方现金流量
                if(Objects.isNull(detail.getChargeItemId())){
                    //1.根据科目id
                    subjectCashFlows = subjectCashFlowRepository.listBySubjectId(detail.getSubjectId());
                }else {
                    subjectCashFlows = subjectCashFlowRepository.listByMapDetail(detail.getSubjectId(),
                            detail.getChargeItemId(), SubjectRuleMapTypeEnum.现金流量.getCode());
                }
                if (CollectionUtils.isNotEmpty(subjectCashFlows)) {
                    //如果对方科目是资产类别,则借：流入， 贷：流出
                    SubjectCashFlow subjectCashFlow = subjectCashFlows.stream().filter(i -> inOut.equalsByCode(i.getInOut())).findAny().orElse(null);
                    if (Objects.nonNull(subjectCashFlow)){
                        CashFlowOBV cashFlowOBV = new CashFlowOBV()
                                .setType(inOut.getCode())
                                .setCurrency("CNY")
                                .setMoney(extracted(inOut.getCode(), detail,loanType));
                        if (Objects.nonNull(subjectCashFlow.getMasterCode())){
                            cashFlowOBV.setCode(subjectCashFlow.getMasterCode());
                            cashFlowOBV.setName(subjectCashFlow.getMasterName());
                        } else if (Objects.nonNull(subjectCashFlow.getAttachedCode())) {
                            cashFlowOBV.setCode(subjectCashFlow.getAttachedCode());
                            cashFlowOBV.setName(subjectCashFlow.getAttachedName());
                        }
                        detail.setCashFlows(List.of(cashFlowOBV)); //设置现金流量
                    }
                }
            }
        }
    }

    /**
     * 根据流入/流出以及借方/贷方来判断是否把本币转为负数
     * @param cashFlowType
     * @param detail
     * @return
     */
    private long extracted(Integer cashFlowType, VoucherDetailOBV detail,String loanType) {
        if(Objects.nonNull(detail.getIncTaxAmount()) && SubjectRuleInOutEnum.流入.getCode() == cashFlowType){
            if(VoucherLoanTypeEnum.借方.getCode().equals(loanType)){
                return Math.abs(detail.getIncTaxAmount());
            }
        }else if(Objects.nonNull(detail.getIncTaxAmount()) && SubjectRuleInOutEnum.流出.getCode() == cashFlowType){
            if(VoucherLoanTypeEnum.贷方.getCode().equals(loanType)){
                return Math.abs(detail.getIncTaxAmount());
            }
        }
        return  -Math.abs(detail.getIncTaxAmount());
    }


    /**
     * 现金流量
     * @param details 借方/贷方分录详情
     * @param inOut 流入流出方向
     * @return 现金流量列表
     *//*
    public List<CashFlowOBV> getVoucherCashFlow(List<VoucherDetailOBV> details, SubjectRuleInOutEnum inOut) {
        if (CollectionUtils.isEmpty(details)) {
            return null;
        }
        List<CashFlowOBV> cashFlows = new ArrayList<>();
        List<SubjectCashFlow> subjectCashFlows;
        for (VoucherDetailOBV debitDetail : details) {
            if (Objects.nonNull(debitDetail.getChargeItemId())){
                //根据费项获取现金流信息
                SubjectMapUnitDetailE mapUnitDetail = subjectMapUnitDetailRepository.getByUnitId(debitDetail.getChargeItemId(), SubjectRuleMapTypeEnum.现金流量.getCode());
                if (Objects.nonNull(mapUnitDetail)){
                    CashFlowE cashFlow = cashFlowRepository.getById(mapUnitDetail.getSubjectLevelLastId());
                    if (Objects.nonNull(cashFlow)){
                        CashFlowOBV cashFlowOBV = new CashFlowOBV()
                                .setType(inOut  == SubjectRuleInOutEnum.流入 ? VoucherCashFlowTypeEnum.流入.getCode() :  VoucherCashFlowTypeEnum.流出.getCode())
                                .setCurrency("CNY")
                                .setMoney(debitDetail.getIncTaxAmount());
                        cashFlowOBV.setCode(cashFlow.getCode());
                        cashFlowOBV.setName(cashFlow.getName());
                        cashFlows.add(cashFlowOBV);
                    }
                }
            }else {
                //1.借方科目编码和借方费项获取借方现金流量
                if(Objects.isNull(debitDetail.getChargeItemId())){
                    //1.根据科目id
                    subjectCashFlows = subjectCashFlowRepository.listBySubjectId(debitDetail.getSubjectId());
                }else {
                    subjectCashFlows = subjectCashFlowRepository.listByMapDetail(debitDetail.getSubjectId(),
                            debitDetail.getChargeItemId(), SubjectRuleMapTypeEnum.现金流量.getCode());
                }
                if (CollectionUtils.isNotEmpty(subjectCashFlows)) {
                    //如果对方科目是资产类别,则借：流入， 贷：流出
                    SubjectCashFlow subjectCashFlow = subjectCashFlows.stream().filter(i -> inOut.equalsByCode(i.getInOut())).findAny().orElse(null);
                    if (Objects.nonNull(subjectCashFlow)){
                        CashFlowOBV cashFlowOBV = new CashFlowOBV()
                                .setType(inOut  == SubjectRuleInOutEnum.流入 ? VoucherCashFlowTypeEnum.流入.getCode() :  VoucherCashFlowTypeEnum.流出.getCode())
                                .setCurrency("CNY")
                                .setMoney(debitDetail.getIncTaxAmount());
                        if (Objects.nonNull(subjectCashFlow.getMasterCode())){
                            cashFlowOBV.setCode(subjectCashFlow.getMasterCode());
                            cashFlowOBV.setName(subjectCashFlow.getMasterName());
                        } else if (Objects.nonNull(subjectCashFlow.getAttachedCode())) {
                            cashFlowOBV.setCode(subjectCashFlow.getAttachedCode());
                            cashFlowOBV.setName(subjectCashFlow.getAttachedName());
                        }
                        cashFlows.add(cashFlowOBV);
                    }
                }
            }
        }
        return cashFlows;
    }*/

    /**
     * 保存凭证业务单据信息
     * @param voucherBusinessDetails
     * @return
     */
    public boolean saveBusinessDetail(List<VoucherBusinessDetail> voucherBusinessDetails) {
        return  voucherBusinessDetailRepository.saveBatch(voucherBusinessDetails);
    }

    /**
     * 获取现金流信息
     * @param chargeItemId 费项
     * @param excTaxAmount 不含税金额
     * @param inOut 流入流出
     */
    public CashFlowOBV getCashFlow(Long chargeItemId, Long excTaxAmount, SubjectRuleInOutEnum inOut) {
        //1.根据费项id获取现金流信息
        SubjectMapUnitDetailE mapUnitDetail = subjectMapUnitDetailRepository.getByUnitId(chargeItemId, SubjectRuleMapTypeEnum.现金流量.getCode());
        CashFlowOBV cashFlowOBV = null;
        if (Objects.nonNull(mapUnitDetail)){
            cashFlowOBV = new CashFlowOBV()
                    .setType(inOut  == SubjectRuleInOutEnum.流入 ? VoucherCashFlowTypeEnum.流入.getCode() :  VoucherCashFlowTypeEnum.流出.getCode())
                    .setCurrency("CNY")
                    .setName(mapUnitDetail.getSubjectLevelLastName())
                    .setCode(mapUnitDetail.getSubjectLevelLastId().toString())
                    .setMoney(excTaxAmount);
        }
        return cashFlowOBV;
    }

    /**
     * 判断是否存在现金流量
     * @param details 分录详情
     * @return true：存在， false：不存在
     */
    private boolean hasCashFlowSubject(List<VoucherDetailOBV> details, Map<String, SubjectE> subjectMap){
        if (CollectionUtils.isNotEmpty(details)){
            for (VoucherDetailOBV detail : details) {
                SubjectE subject = subjectMap.get(detail.getSubjectCode());
                if (Objects.nonNull(subject) && subject.verifyCashSubject()){
                    return true;
                }
            }
        }
        return false;
    }

    public Integer deleteByIds(List<Long> ids) {
        return voucherInfoRepository.deleteByIds(ids);
    }

    public Integer deleteBusinessDetailByIds(List<Long> voucherIds, Long accountBookId){
        return voucherInfoRepository.deleteBusinessDetailByIds(voucherIds,accountBookId);
    }

    public boolean selectVoucherInfoAndBusinessDetail(List<Long> ids) {
        return voucherInfoRepository.selectVoucherInfoAndBusinessDetail(ids);
    }
}
