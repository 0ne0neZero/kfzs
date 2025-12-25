package com.wishare.finance.apps.service.invoicereceipt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import cn.hutool.core.lang.hash.Hash;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apis.common.FinanceCommonUtils;
import com.wishare.finance.apps.model.invoice.invoice.dto.*;
import com.wishare.finance.apps.model.invoice.invoice.fo.*;
import com.wishare.finance.apps.model.invoice.invoice.vo.ReceiptV;
import com.wishare.finance.apps.model.invoice.invoice.vo.SignExternalSealVo;
import com.wishare.finance.apps.model.signature.EsignResultV;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.apps.service.bill.prepay.BillPrepayInfoAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.apps.service.strategy.ReceiptTenant;
import com.wishare.finance.apps.service.strategy.receipt.ReceiptTenantZJ;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillMethodEnum;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.DeveloperPayEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.RedisConst;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceSpaceExpandExternalV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceV;
import com.wishare.finance.infrastructure.utils.ChineseNumber;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.finance.infrastructure.utils.MapperFacadeUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const.State;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Service
@RefreshScope
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceiptAppService {

    private final ReceiptDomainService receiptDomainService;
    private final ReceiptRepository receiptRepository;
    private final SpacePermissionAppService spacePermissionAppService;

    private final BillPrepayInfoAppService prepayPaymentService;

    private final SharedBillAppService sharedBillAppService;
    private final ReceivableBillRepository receivableBillRepository;
    private final  BillFacade billFacade;
    private final BillAdjustRepository billAdjustRepository;
    private final SpaceClient spaceFeignClient;

    /**
     * 开具收据
     *
     * @param form
     * @return
     */
    public Long invoiceBatch(ReceiptBatchF form) {
        //账单支付校验
        prepayPaymentService.checkPrepay(form.getBillIds(),form.getSupCpUnitId());
        AddReceiptCommand command = Global.mapperFacade.map(form, AddReceiptCommand.class);
        //租户
        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        Map<String, Object> map = new HashMap<>();
        Boolean skip = form.getSkip();
        //是否跳过 中交有当前标识 则只从数据源获取数据不对数据源做任何变动改动
        if(Objects.nonNull(skip)){
            map.put("skip",skip);
        }
        String clerkStatus = form.getClerkStatus();
        if(Objects.nonNull(clerkStatus)){
            map.put("clerkStatus",clerkStatus);
        }

        // do
        final Long invoiceReceiptEId = receiptDomainService.invoiceBatch(command,receiptTenant,map);
        //生成pdf-签章-推送
        receiptDomainService.pushMessage(invoiceReceiptEId,form.getSupCpUnitId(),receiptTenant,map);
        return invoiceReceiptEId;
    }



    /**
     * 收据下发
     * @param invoiceReceiptId 主键id
     * @param invoiceReceiptNo 票据编号

     * @param url pdf地址    receipt
     * @param statutoryBodyName 法定单位名称
     * @param roomName  空间名称
     * @param communityId 项目id
     * @param communityName 项目名称
     *
     * @param pushMode 填写 推送方式
     * @param buyerPhone 填写 推送手机号
     * @param email 填写 推送邮箱
     * @param shortName 租户简称
     */
    public String receiptSend(ReceiptSendF receiptSendF) {
        //获取收据信息
        ReceiptVDto receiptVDto = receiptDomainService.queryByInvoiceReceiptId(receiptSendF.getInvoiceReceiptId());
        //如果是走e签宝的那么发送的时候校验e签宝状态以及e签宝pdf
        ErrorAssertUtils.isFalseThrow400(receiptVDto.getSignStatus() == 0 && Objects.isNull(receiptVDto.getSignReceiptUrl()),"签章文件还未生成请稍后再试");
        /**如果用户填写了手机号，则进行手机号替换 */
        receiptVDto.setPhone(receiptSendF.getBuyerPhone());
        //额外信息
        final HashMap<String, Object> map = new HashMap<>() {{
            put("email", receiptSendF.getEmail());
        }};
        // 如果有作废收据 那么认定为发送作废收据 这里入口只有一个前端发起。可以只单单去判断申请编号就可以了，不需要判断是否存在作废收据文件，前端会把控
        if(StringUtils.isNotBlank(receiptVDto.getVoidSignApplyNo())){
            /** 发送作废收据标识 */
            map.put("voidNo", receiptVDto.getInvoiceReceiptNo());
        }
        //进行通知
        int way = receiptDomainService.receiptSend(receiptSendF.getPushMode(), receiptVDto,map);
        //付款方通知之后 进行部分数据变更
        receiptDomainService.afterReceiptSend(receiptVDto.getId(),way,receiptVDto);
        //主动发起 不可能存在0的情况
        return way!=-1?"成功":"失败";
    }


    /**
     * 是否需要重新制作收据源文件
     * @return true:重新制作 false:未重新制作
     */
    private boolean reDoPdf(ReceiptVDto receiptVDto){
        if(CollectionUtils.isNotEmpty(receiptVDto.getScriptFileVos())){
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("endPdf","endPdf");
        //生成pdf
        receiptDomainService.doPdf(receiptVDto.getInvoiceReceiptId(),receiptVDto.getCommunityId(),
                Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName(),ReceiptTenant.class),map);
        return true;
    }


    /***
     * 查询签章情况
     * @return
     */
    public void eSignReceipt(Integer type,Long invoiceReceiptId){
        ReceiptVDto receiptVDto = receiptDomainService.queryByInvoiceReceiptId(invoiceReceiptId);
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(receiptVDto),"当前收据信息不存在");
        String signApplyNo = receiptVDto.getSignApplyNo();
        String voidSignApplyNo = receiptVDto.getVoidSignApplyNo();
        log.info("eSignReceipt[start{}]", JSONObject.toJSON(receiptVDto));
        ErrorAssertUtils.isFalseThrow400(type == 1&&StringUtils.isNotBlank(signApplyNo),"当前收据已经发起签署，不可重复操作");
        ErrorAssertUtils.isFalseThrow400(type == 1&&StringUtils.isNotBlank(receiptVDto.getSignReceiptUrl()),"当前收据已经签署成功请勿重复操作");
        ErrorAssertUtils.isFalseThrow400(type == 2&&StringUtils.isNotBlank(voidSignApplyNo),"当前收据已经发起作废签署，不可重复操作");
        ErrorAssertUtils.isFalseThrow400(type == 2&&CollectionUtils.isNotEmpty(receiptVDto.getVoidFileVos()),"当前收据已经成功作废签署，请勿重复操作");

        ReceiptE receiptE = Global.ac.getBean(ReceiptRepository.class)
                .getById(receiptVDto.getId());
        InvoiceReceiptE invoiceReceiptE = Global.ac.getBean(InvoiceReceiptRepository.class)
                .getById(receiptVDto.getInvoiceReceiptId());
        if(type == 1){
            //是否需要重新发起制作签署源文件
            if(this.reDoPdf(receiptVDto)){
                //重新获取最新数据
                receiptVDto = receiptDomainService.queryByInvoiceReceiptId(invoiceReceiptId);
            }
            Global.ac.getBean(ReceiptTenantZJ.class).signExternalSeal(
                    SignExternalSealVo.builder().signStatus(0)
                            .fileVo(receiptVDto.getScriptFileVos().get(0))
                            .fileHost(receiptVDto.getReceiptUrl()).receiptE(receiptE)
                            .invoiceReceiptE(invoiceReceiptE).map(null)
                            .build());
            //再次调用接口获取结果
            ReceiptVDto receiptVDtoAgain = receiptDomainService.queryByInvoiceReceiptId(invoiceReceiptId);
            log.info("eSignReceipt[agin{}type{}]", JSONObject.toJSON(receiptVDtoAgain),type);
            ErrorAssertUtils.isFalseThrow400(StringUtils.isEmpty(receiptVDtoAgain.getSignApplyNo()),
                    RedisHelper.get(RedisConst.SIGN + invoiceReceiptId));
            return;
        }
        //发起作废
        Global.ac.getBean(ReceiptTenantZJ.class).voidReceiptV(receiptVDto);
        //再次调用接口获取结果
        ReceiptVDto receiptVDtoAgain = receiptDomainService.queryByInvoiceReceiptId(invoiceReceiptId);
        log.info("eSignReceipt[agin{}type{}]", JSONObject.toJSON(receiptVDtoAgain),type);
        ErrorAssertUtils.isFalseThrow400(StringUtils.isBlank(receiptVDtoAgain.getVoidSignApplyNo()),
                RedisHelper.get(RedisConst.ZF_SIGN + invoiceReceiptId));

    }



    /***
     * 查询签章情况
     * @return
     */
    public EsignResultV querySignResult(Long invoiceReceiptId){
        return receiptDomainService.getByInvoiceReceiptId(invoiceReceiptId);
    }

    /***
     * 查询作废情况
     * @return
     */
    public EsignResultV queryVoidResult(Long invoiceReceiptId){
        return receiptDomainService.getByInvoiceReceiptIdVoid(invoiceReceiptId);
    }


    /**
     * 根据收款明细ids获取收据信息
     * @param gatherDetailIds
     * @param supCpUnitId
     * @return
     */
    public InvoiceReceiptDto getReceiptByGatherDetailIds(List<Long> gatherDetailIds, String supCpUnitId){
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        return receiptDomainService.getReceiptByGatherDetailIds(gatherDetailIds, gatherDetailName);
    }


    /**
     * 分页查询收据列表
     *
     * @param form
     * @return
     */
    public PageV<ReceiptV> queryPage(PageF<SearchF<?>> form) {
        List<Field> fields = form.getConditions().getFields();
        // 处理项目树参数
        if (TenantUtil.bf20()) {
            spaceQueryOpr(form);
        }
        for(Field field : fields){
            if("ir.billing_time".equals(field.getName())){
                field.setName("DATE_FORMAT(ir.billing_time, '%Y-%m-%d')");
            }
        }
        Page<ReceiptDto> pageResult = receiptDomainService.queryPage(form);
        if (CollectionUtils.isNotEmpty(pageResult.getRecords())) {
            pageResult.getRecords().forEach(receiptDto -> {
                if (StringUtils.isNotBlank(receiptDto.getRoomName())) {
                    receiptDto.setRoomName(receiptDto.getCommunityName() + "-" +
                            receiptDto.getRoomName().replaceAll(">", ""));
                }
            });
        }
        return PageV.of(form, pageResult.getTotal(), MapperFacadeUtil.getMoneyMapperFacade().mapAsList(pageResult.getRecords(), ReceiptV.class));
    }

    /**
     * 处理项目树参数
     * @param form 分页参数
     */
    public void spaceQueryOpr(PageF<SearchF<?>> form) {
        // 获取属性值
        Field flag = spacePermissionAppService.getField(form, "flag");
        Field spaceTreeId = spacePermissionAppService.getField(form, "spaceTreeId");
        if (Objects.isNull(flag) || Objects.isNull(spaceTreeId)) {
            throw BizException.throw300("缺少房产树信息");
        }

        // 去除表多余字段
        SpacePermissionAppService.removeField(form, flag);
        SpacePermissionAppService.removeField(form, spaceTreeId);

        // path值处理
        String spaceId = (String) spaceTreeId.getValue();
        if ((Integer) flag.getValue() == State._1) {
            // 项目层
            form.getConditions().getFields().add(new Field("ir.community_id", spaceId, 1));
        }else if ((Integer) flag.getValue() == State._2){
            SpaceV spaceInfo = spaceFeignClient.getSpaceInfo(Long.valueOf(spaceId));
            String spacePath = StringUtils
                    .join(Optional.ofNullable(spaceInfo.getPath()).orElse(new ArrayList<>()), ",");
            form.getConditions().getFields().add(new Field("ird.path", spacePath, 8));
        }
    }

    /**
     * 统计收据信息
     *
     * @param form
     * @return
     */
    public ReceiptStatisticsDto statistics(PageF<SearchF<?>> form) {
        if (TenantUtil.bf20()) {
            spaceQueryOpr(form);
        }
        return receiptDomainService.statistics(form);
    }

    /**
     * 作废收据
     *
     * @param invoiceReceiptId
     * @return
     */
    public Boolean voidReceipt(Long invoiceReceiptId,String supCpUnitId) {
        final Boolean aBoolean = receiptDomainService.voidReceipt(invoiceReceiptId, supCpUnitId);
        //作废成功回收收据
        if(aBoolean){
            ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
            ReceiptVDto receiptVDto = receiptDomainService.queryByInvoiceReceiptId(invoiceReceiptId);
            receiptTenant.voidReceiptV(receiptVDto);
        }
        //中交e签宝，需要作废收据,收据状态已回收 进行短信推送
        return aBoolean;
    }

    /**
     * 作废收据 目前针对中交e签宝
     *
     * @param form
     * @return
     */
    public Boolean voidReceiptV(ReceiptVoidF form) {
        return receiptDomainService.voidReceiptV(form);
    }


    /**
     * 获取收据详情
     *
     * @param form
     * @return
     */
    public ReceiptDetailDto detail(ReceiptDetailF form) {
        return receiptDomainService.detail(form);
    }


    /**
     * 编辑收据
     *
     * @param form
     * @return
     */
    public Boolean editReceipt(EditReceiptF form) {
        return receiptDomainService.editReceipt(form);
    }

    /**
     * 修复收据错乱数据
     *
     * @return
     */
    public Boolean repairReceipt() {
        return receiptDomainService.repairReceipt();
    }

    /**
     * 修复收据错乱数据
     *
     * @return
     */
    public Boolean updateBillPayTime() {
        return receiptDomainService.updateBillPayTime();
    }

    private static String getBillPeriod(List<ReceivableBill> billDetailList){
        List<LocalDateTime> startList = billDetailList.stream().filter(ObjectUtil::isNotNull)
                .sorted(Comparator.comparing(ReceivableBill::getStartTime))
                .map(ReceivableBill::getStartTime).collect(Collectors.toList());

        List<LocalDateTime> endList = billDetailList.stream().filter(ObjectUtil::isNotNull)
                .sorted(Comparator.comparing(ReceivableBill::getEndTime).reversed())
                .map(ReceivableBill::getStartTime).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(startList) && CollUtil.isNotEmpty(endList)){
            DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return startList.get(0).format(d) +"-"+endList.get(0).format(d);
        }
        return "";
    }

    private String handleRemark(List<ReceivableBill> billDetailList,  AddInvoiceCommand command,BigDecimal total) {

        String room = "【" + billDetailList.get(0).getRoomName() + "】业主本次应缴纳";
        String period = getBillPeriod(billDetailList);
        String billPeriod = "【" + period + "】期间（简称”该期间“）的";
        Set<String> allItem = billDetailList.stream().map(ReceivableBill::getChargeItemName).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        String chargeItemName =  "【" +StrUtil.join("、", allItem)+"】费用";
        String sub = FinanceCommonUtils.F2Y(command.getOldDeveloperPayBill().stream().mapToLong(ReceivableBill::getReceivableAmount).sum());
        BigDecimal all = total.add(new BigDecimal(sub)).setScale(2, RoundingMode.UP);

        String sumStr =  "【" + all+"】元（简称“开发商代付优惠”），";
        // 开发商代付优惠凭证号，没有，不进行拼接
        String favorableNo= "";
        String favorablePeriod= "开发商代付优惠对应期间为【"+period+"】，开发商代付后业主需缴纳该期间剩余";
        String  remainingItem =  "【"+StrUtil.join("、", allItem)+"】费用";
        String remainingAmount ="【"+ sub+"】元。";

        return room+billPeriod+chargeItemName+sumStr+favorablePeriod+remainingItem+remainingAmount;


    }
    private List<ReceivableBill> verifyDeveloperPayConsistency(AddInvoiceCommand command){

        //校验 收费对象为开发商的应收账单，账单状态为正常，未冲销
        List<ReceivableBill> count = receivableBillRepository.list(Wrappers.<ReceivableBill>lambdaQuery()
                .eq(ReceivableBill::getSupCpUnitId, command.getSupCpUnitId())
                .eq(ReceivableBill::getState, 0)
                .eq(ReceivableBill::getPayerType, 1)
                .eq(ReceivableBill::getReversed, 0)
                .in(ReceivableBill::getId, command.getBillIds())
        );

        ErrorAssertUtils.ifTrueThrow300(command.getBillIds().size() != count.size(),
                "请选择账单状态为正常，未冲销且收费对象为开发商的账单");

        //校检账单中法定单位是否相同
        Set<Long> bodys = count.stream().map(ReceivableBill::getStatutoryBodyId).collect(Collectors.toSet());
        ErrorAssertUtils.ifTrueThrow300( bodys.size() != 1,"请先选择同法定单位下的账单打印开发代付凭据");

        //校检账单中项目是否相同
        Set<String> communityIds = count.stream().map(ReceivableBill::getCommunityId).collect(Collectors.toSet());
        ErrorAssertUtils.ifTrueThrow300( communityIds.size() != 1,  "请先选择相同项目的账单打印开发代付凭据");
        // 校验账单房号
        Set<String> roomId = count.stream().map(ReceivableBill::getRoomId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        ErrorAssertUtils.ifTrueThrow300(roomId.size() != 1,"请先选择相同房间号的账单打印开发代付凭据");
        return count;
    }


    private String getPropertyOwnerName(String roomId){
        try {
            List<SpaceSpaceExpandExternalV> spaceExternals = spaceFeignClient.getSpaceExternals(List.of(Long.parseLong(roomId)));
            if(CollectionUtils.isNotEmpty(spaceExternals)){
                return (spaceExternals.get(0).getPropertyOwner());
            }
            return "";
        } catch (NumberFormatException e) {

        }
        return "";
    }
    /** 后期可优化，查询
     * @param form
     * @return
     */
    public DeveloperPayV invoiceBatchDeveloperPay(ReceiptBatchF form) {
        AddInvoiceCommand command = Global.mapperFacade.map(form, AddInvoiceCommand.class);

        //根据账单id获取账单信息
        List<ReceivableBill> billList = verifyDeveloperPayConsistency(command);


        List<Long> oldBillIdList = billAdjustRepository.listBySeparateBillNo(billList.stream()
                .map(ReceivableBill::getBillNo).collect(Collectors.toSet()));
        if (CollUtil.isEmpty(oldBillIdList)){
            throw new BizException(403,"开发商代付，未查询到原账单");
        }

        List<ReceivableBill> oldBill = receivableBillRepository.list(Wrappers.<ReceivableBill>lambdaQuery()
                .eq(ReceivableBill::getSupCpUnitId, command.getSupCpUnitId())
                .in(ReceivableBill::getId, oldBillIdList)
        );
        command.setOldDeveloperPayBill(oldBill);

        ReceivableBill billDetailMoreV = billList.get(0);


        DeveloperPayV receiptDetailDto = new DeveloperPayV();

        receiptDetailDto.setStatutoryBodyName(billDetailMoreV.getStatutoryBodyName());
        receiptDetailDto.setReceiptNo(IdentifierFactory.getInstance().serialNumber("invoice_no",
                DeveloperPayEnum.YES.getReceiptNoPrefix(), 20));
        receiptDetailDto.setCommunityName(billDetailMoreV.getCommunityName());
        receiptDetailDto.setRoomName(billDetailMoreV.getRoomName());
        receiptDetailDto.setCustomerName(getPropertyOwnerName(billDetailMoreV.getRoomId()));

        // 中间部分
        Map<String, List<ReceivableBill>> billDetailMoreVMap = getBillDetailMoreVMap(billList);
        ArrayList<DeveloperPayDetailV> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (List<ReceivableBill> list : billDetailMoreVMap.values()) {
            DeveloperPayDetailV item = new DeveloperPayDetailV();
            BigDecimal chargingAreaSum = BigDecimal.ZERO;

            for (ReceivableBill b : list) {
                item.setChargeItemName(b.getChargeItemName());
                if (Objects.nonNull(b.getUnitPrice())){
                    item.setPrice(FinanceCommonUtils.F2Y(b.getUnitPrice()));
                }
                if (b.getChargingArea() != null) {
                    chargingAreaSum = chargingAreaSum.add(b.getChargingArea());
                }

            }
            item.setNumStr(handleNumStr(list.get(0).getBillMethod(),list,chargingAreaSum,list.get(0).getChargingCount()));
            BigDecimal sum = NumberUtil.div(new BigDecimal(list.stream().mapToLong(ReceivableBill::getReceivableAmount).sum()),
                    (new BigDecimal("100")), 2, RoundingMode.UP);
            item.setTotal(sum.toString());
             total = total.add(sum);
            details.add(item);
        }

        receiptDetailDto.setInvoiceReceiptDetail(details);


        // 底部
        receiptDetailDto.setInvoiceAmountTotalUppercase(ChineseNumber.number2CNMontrayUnit(total.setScale(2,RoundingMode.UP)));
        receiptDetailDto.setInvoiceAmountTotal(total.setScale(2,RoundingMode.UP).toString());

        String remark = handleRemark(billList, command,total.setScale(2,RoundingMode.UP));
        receiptDetailDto.setRemark(remark);
        return receiptDetailDto;
    }

    /**
     * 构造计量描述
     *
     * @param billMethod
     * @param bills
     * @param chargingAreaSum
     * @return
     */
    public static String handleNumStr(Integer billMethod, List<ReceivableBill> bills, BigDecimal chargingAreaSum, Integer chargeCount) {
        String numStr = "";
        if (null == billMethod) {
            return numStr;
        }
        if (chargingAreaSum != null) {
            chargingAreaSum = chargingAreaSum.setScale(2, RoundingMode.HALF_UP);
        }
        BillMethodEnum billMethodEnum = valueOfByCode(billMethod);
        switch (billMethodEnum) {
            //单价*天
            case PRICE_DAY:
                //单价*月
            case PRICE_MONTH:
                //固定金额
            case PRICE_FIXED:
                break;
            //单价*吨
            case PRICE_TON:
                numStr = chargingAreaSum.toString() + "吨" + handleMeterReading(bills);
                break;
            //单价*度
            case PRICE_DEGREE:
                numStr = chargingAreaSum.toString()+"度" + handleMeterReading(bills);
                break;
            //单价*千瓦时
            case PRICE_KWH:
                numStr = chargingAreaSum.toString()+"度" + handleMeterReading(bills);
                break;
            //单价*面积*天
            case PRICE_AREA_DAY:
                //单价*面积*月
            case PRICE_AREA_MONTH:
                numStr = chargingAreaSum + "m²";
                break;
            //单价*m³
            case PRICE_CUBIC_METER:
                numStr = chargingAreaSum.toString() +"m³" + handleMeterReading(bills);
                break;
            case PRICE_OVERDUE: //违约单价*用量*违约比率*天
                //numStr = "1*" + size + "";
                break;
            case PRICE_COUNT: //单价*数量
                numStr = chargeCount + "个";
                break;
        }
        return numStr;
    }

    public static BillMethodEnum valueOfByCode(int code) {
        for (BillMethodEnum ee : BillMethodEnum.values()) {
            if (ee.getType() == code) {
                return ee;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_METHOD_NOT_SUPPORT.msg());
    }
    /**
     * 处理账单中的表读数信息
     * @param bills
     * @return
     */
    public static String handleMeterReading(List<ReceivableBill> bills) {
        String reading = "";
        try {
            BigDecimal min = null;
            BigDecimal max = null;
            for (ReceivableBill billDetailMoreV : bills) {
                String extField3 = billDetailMoreV.getExtField3();
                String extField4 = billDetailMoreV.getExtField4();
                if (StringUtils.isNotBlank(extField3)) {
                    BigDecimal tempMin = new BigDecimal(extField3);
                    if (min == null || min.compareTo(tempMin) > 0) {
                        min = tempMin;
                    }
                    BigDecimal temMax = new BigDecimal(extField3);
                    if (max == null || max.compareTo(temMax) < 0) {
                        max = temMax;
                    }
                }
                if (StringUtils.isNotBlank(extField4)) {
                    BigDecimal tempMin = new BigDecimal(extField4);
                    if (min == null || min.compareTo(tempMin) > 0) {
                        min = tempMin;
                    }
                    BigDecimal temMax = new BigDecimal(extField4);
                    if (max == null || max.compareTo(temMax) < 0) {
                        max = temMax;
                    }
                }
            }
            if (min != null && max != null) {
                //当前2个字段可能存在读出处理反的情况
                reading = "(" + min.min(max) + "-" + min.max(max) + ")";
            }
        } catch (Exception e) {
            log.error("处理表读数失败", e);
        }
        return reading;
    }
    private Map<String, List<ReceivableBill>> getBillDetailMoreVMap(List<ReceivableBill> billDetailMoreVList) {
        if (EnvConst.HUIXIANGYUN.equals(EnvData.config) || EnvConst.YUANYANG.equals(EnvData.config)
                || EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            return billDetailMoreVList.stream().collect(
                    Collectors.groupingBy(
                            bill -> bill.getChargeItemId() + "-" + bill.getUnitPrice() + "-" + bill.getBillMethod()+"-"+bill.getChargingArea() + hasTime(bill)
                    ));
        }
        return billDetailMoreVList.stream().collect(
                Collectors.groupingBy(
                        bill -> bill.getChargeItemId() + "-" + bill.getUnitPrice() + "-" + bill.getBillMethod() + hasTime(bill)
                ));
    }

    private String hasTime(ReceivableBill billDetailMoreV) {
        if (billDetailMoreV.getStartTime() == null || billDetailMoreV.getEndTime() == null){
            return "N";
        } else {
            return "Y";
        }
    }






    /**
     * 根据收据id查询收据信息
     * @param invoiceReceiptId
     * @return
     */
    public ReceiptVDto queryByInvoiceReceiptId(Long invoiceReceiptId){
        return receiptDomainService.queryByInvoiceReceiptId(invoiceReceiptId);
    }

    public void batchDownloadZip(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        receiptDomainService.batchDownloadZip(queryF,response);
    }

    /**
     * 收据文件下载
     * @param id 发票收据主表ID
     * @param response
     */
    public void getDownloadReceipt(Long id, HttpServletResponse response) {
        // 获取发票url
        ReceiptE receiptE = receiptRepository.getByInvoiceReceiptId(id);
        Assert.validate(()->Objects.nonNull(receiptE),()->BizException.throw400("收据信息不存在"));
        Assert.validate(()-> ObjectUtils.anyNotNull(receiptE.getSignReceiptUrl(),receiptE.getReceiptUrl()),
                ()->BizException.throw400("收据文件路径不存在"));

        String fileName = "receipt_" + UUID.randomUUID() + ".pdf";

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        Boolean result = Boolean.TRUE;
        try {
            result = FileUtil.downLoadPdf(response, receiptE.getSignReceiptUrl());
        }catch (Exception e){
            FileUtil.downLoadPdf(response, receiptE.getReceiptUrl());
        }
        if (!result){
            FileUtil.downLoadPdf(response, receiptE.getReceiptUrl());
        }

    }
}
