package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillOnAccountEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.HandAccountDto;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.support.PayWayJSONListTypeHandler;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 交账信息表
 *
 * @author dxclay
 * @since 2023-01-11
 */
@Slf4j
@Getter
@Setter
@TableName(value = TableNames.BILL_ACCOUNT_HAND, autoResultMap = true)
public class BillAccountHand {

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "费项类型：1常规收费类型 2临时收费类型 3押金收费类型")
    private Integer chargeItemType;

    @ApiModelProperty(value = "收费组织id")
    private String cpOrgId;

    @ApiModelProperty(value = "收费组织名称")
    private String cpOrgName;

    @ApiModelProperty(value = "上级收费单元id")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id")
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "账单类型：1.应收账单 2.预收账单 3.临时缴费账单")
    private Integer billType;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额")
    private Long totalAmount;

    @ApiModelProperty(value = "实际应收款金额")
    private Long payableAmount;

    @ApiModelProperty(value = "收款金额")
    private Long payAmount = 0L;

    @ApiModelProperty(value = "收费时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "税额")
    private Long taxAmount;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer customerLabel;

    @ApiModelProperty(value = "收费对象ID")
    private String customerId;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "票据编号列表")
    @TableField(typeHandler= JacksonTypeHandler.class)
    private List<String> invoiceNos;

    @ApiModelProperty(value = "开票金额")
    private Long invoiceAmount = 0L;

    @ApiModelProperty(value = "票据总额")
    private Long invoiceTotalAmount;

    @ApiModelProperty(value = "增值税普通发票	1: 增值税普通发票	2: 增值税专用发票	3: 增值税电子发票	4: 增值税电子专票	5: 收据	6：电子收据	7:纸质收据	8：全电普票")
    @TableField(typeHandler= JacksonTypeHandler.class)
    private List<Integer> invoiceTypes;

    @ApiModelProperty(value = "开票时间")
    private LocalDateTime invoiceTime;

    @ApiModelProperty(value = "是否挂账：0未挂账，1已挂账，2已销账")
    private Integer onAccount;

    @ApiModelProperty(value = "交账状态：0未交账，1部分交账，2已交账")
    private Integer accountHanded;

    @ApiModelProperty(value = "交账日期")
    private LocalDateTime handTime;

    @ApiModelProperty(value = "收款方式 [{\"payWay\": 1,\"payChannel\": \"ALIPAY\"}]")
    @TableField(typeHandler = PayWayJSONListTypeHandler.class, javaType = true)
    private List<PayWay> payWays;

    @ApiModelProperty("退款状态（0未退款，1退款中，2部分退款，已退款")
    private Integer refundState;

    @ApiModelProperty("结转状态：0未结转，1待结转，2部分结转，3已结转")
    private Integer carriedState;

    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;


    /**
     * 系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    private Integer sysSource;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;
    /**
     * 添加字段 状态
     */
    @ApiModelProperty("账单状态（0正常，1作废，2冻结）")
    private Integer state;

    public BillAccountHand() {
    }


    /**
     * 添加票据信息
     * @param invoiceNos 票据id
     * @param invoiceAmount 票据金额
     * @param invoiceTotalAmount 开票总额
     * @param invoiceTypes 票据类型
     * @param invoiceTime 开票最大时间
     */
    public void putInvoice(List<String> invoiceNos, Long invoiceAmount, Long invoiceTotalAmount, List<Integer> invoiceTypes, LocalDateTime invoiceTime) {
        this.invoiceNos = invoiceNos;
        this.invoiceAmount = invoiceAmount;
        this.invoiceTotalAmount = invoiceTotalAmount;
        this.invoiceTypes = invoiceTypes;
        this.invoiceTime = invoiceTime;
    }

    /**
     * 添加收款信息
     * @param payAmount 收款金额
     * @param payTime 最大收款时间
     * @param payWays 收款方式
     * @param payableAmount 实际应收款金额
     */
    public void putPayInfo(Long payAmount, LocalDateTime payTime, List<PayWay> payWays,Long payableAmount) {
        this.payableAmount = payableAmount;
        this.payAmount = payAmount;
        this.payTime = payTime;
        this.payWays = payWays;
    }

    /**
     * 合并预收
     *
     * @param advanceBill
     * @param billType
     * @return
     */
    public BillAccountHand mergeBill(AdvanceBill advanceBill, BillAction action, BillTypeEnum billType){
        ErrorAssertUtil.notNullThrow404(advanceBill, ErrorMessage.ACCOUNT_HAND_BILL_NOT_EXIST);
        //反审核的账单，同步账单状态
        if (action != null && action.equals(BillAction.REVERSE_APPROVED)){
            Long mid = this.id;
            Global.mapperFacade.map(advanceBill, this);
            this.id = mid;
            this.payAmount = advanceBill.getSettleAmount();
            this.payWays = List.of(new PayWay(advanceBill.getPayWay(), advanceBill.getPayChannel()));
            this.billType = billType.getCode();
            this.payableAmount = advanceBill.getActualSettleAmount();
        }
        if (Objects.nonNull(advanceBill) && BillApproveStateEnum.已审核.equalsByCode(advanceBill.getApprovedState())){
            Long mid = this.id;
            Global.mapperFacade.map(advanceBill, this);
            this.id = mid;
            this.payAmount = advanceBill.getSettleAmount();
            this.payWays = List.of(new PayWay(advanceBill.getPayWay(), advanceBill.getPayChannel()));
            this.billType = billType.getCode();
            this.payableAmount = advanceBill.getActualSettleAmount();
        }
        return this;
    }

    /**
     * 合并应收
     *
     * @param receivableBill
     * @param billType
     * @return
     */
    public BillAccountHand mergeBill(ReceivableBill receivableBill, BillAction action, BillTypeEnum billType){
        ErrorAssertUtil.notNullThrow404(receivableBill, ErrorMessage.ACCOUNT_HAND_BILL_NOT_EXIST);
        //反审核的账单，同步账单状态
        if (action != null && action.equals(BillAction.REVERSE_APPROVED)){
            //合并
            Long mid = this.id;
            Global.mapperFacade.map(receivableBill, this);
            this.id = mid;
            this.billType =  billType.getCode();
        }
        if (Objects.nonNull(receivableBill) && BillApproveStateEnum.已审核.equalsByCode(receivableBill.getApprovedState())){
            //合并
            Long mid = this.id;
            Global.mapperFacade.map(receivableBill, this);
            this.id = mid;
            this.billType = billType.getCode();
        }
        return this;
    }

    /**
     * 发起交账
     * @return
     */
    public HandAccountDto handAccount(){
        ErrorAssertUtil.isFalseThrow402(payAmount == 0 && invoiceAmount == 0, ErrorMessage.ACCOUNT_HAND_STATE_ERROR);
        //账单账票一致或已挂账则可交账成功
        if (BillAccountHandedStateEnum.已交账.equalsByCode(accountHanded)
                || (payAmount.compareTo(invoiceAmount) == 0 && invoiceAmount != 0)
                || BillOnAccountEnum.已挂账.equalsByCode(onAccount)){
            accountHanded = BillAccountHandedStateEnum.已交账.getCode();
            if (payAmount.compareTo(invoiceAmount) == 0 && invoiceAmount != 0 && payAmount < totalAmount){
                accountHanded = BillAccountHandedStateEnum.部分交账.getCode();
            }
            handTime = LocalDateTime.now();
            return new HandAccountDto().setSuccess(true);
        }
        long differenceAmount = payAmount - invoiceAmount;
        //金额多收
        return new HandAccountDto()
                .setSuccess(false)
                .setErrType(differenceAmount > 0 ? 1 : 0)
                .setDifferenceAmount(Math.abs(differenceAmount));
    }

}
