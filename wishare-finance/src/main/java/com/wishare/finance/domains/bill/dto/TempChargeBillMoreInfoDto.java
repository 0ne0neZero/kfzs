package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/8
 * @Description:
 */
@Getter
@Setter
@ApiModel("临时收费信息反参")
public class TempChargeBillMoreInfoDto {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 外部账单编号
     */
    private String outBillNo;

    /**
     * 外部业务单号
     */
    private String outBusNo;

    /**
     * 外部业务id
     */
    private String outBusId;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    /**
     * 账单说明
     */
    private String description;

    /**
     * CNY	币种(货币代码)（CNY:人民币）
     */
    private String currency;

    /**
     * 收款账号id
     */
    private Long sbAccountId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer type;

    /**
     * 类型账单id，对应账单类型的账单id
     */
    private Long typeBillId;

    /**
     * 账单金额
     */
    private Long totalAmount;

    /**
     * 应收金额
     */
    private Long receivableAmount;

    /**
     * 应收减免金额
     */
    private Long deductibleAmount;

    /**
     * 违约金金额
     */
    private Long overdueAmount;

    /**
     * 实收减免金额
     */
    private Long discountAmount;

    /**
     * 结算金额
     */
    private Long settleAmount;


    /**
     * 结转金额
     */
    private Long carriedAmount;

    /**
     * 实收金额
     */
    private Long actualPayAmount;

    /**
     * 开票金额
     */
    private Long invoiceAmount;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 收款方ID
     */
    private String payeeId;

    /**
     * 收款方名称
     */
    private String payeeName;

    /**
     * 付款方ID
     */
    private String payerId;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 增值税普通发票
     *   1: 增值税普通发票
     *   2: 增值税专用发票
     *   3: 增值税电子发票
     *   4: 增值税电子专票
     *   5: 收据
     *   6：电子收据
     */
    private String invoiceType;

    /**
     * 扩展参数
     */
    private String attachParams;

    /**
     * 账单来源
     */
    private String source;

    /**
     * 账单状态（0正常，1作废，2冻结，3挂账）
     */
    private Integer state;

    /**
     * 结算状态（0未结算，1部分结算，2已结算）
     */
    private Integer settleState;

    /**
     * 退款状态（0未退款，1退款中，2部分退款，已退款）
     */
    private Integer refundState;

    /**
     * 核销状态（0未核销，1已核销）
     */
    private Integer verifyState;

    /**
     * 审核状态：0未审核，1审核中，2已审核，3未通过
     */
    private Integer approvedState;

    /**
     *  开票状态：0未开票，1开票中，2部分开票，3已开票
     */
    private Integer invoiceState;

    /**
     * 是否交账：0未交账，1已交账
     */
    private Integer accountHanded;

    /**
     * 是否拆单：0未拆单，1已拆单
     */
    private Integer separated;

    /**
     * 是否冲销：0未冲销，1已冲销
     */
    private Integer reversed;

    /**
     * 是否调整：0未调整，1已调整
     */
    private Integer adjusted;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;

    /**
     * 项目ID
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 费项类型：1常规收费类型、2临时收费类型、3押金收费类型、4常规付费类型、5押金付费类型
     */
    private Integer chargeItemType;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 缴费人手机号
     */
    private String payerPhone;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactPhone;

    /**
     * 收费时间
     */
    private LocalDateTime payTime;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;


    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 修改人姓名
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    private Integer sysSource;

    /**
     * 应用名称
     */
    private String appName;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount;

    @ApiModelProperty("应收账单的结算信息")
    private List<BillSettleDto> billSettleDtoList;

}
