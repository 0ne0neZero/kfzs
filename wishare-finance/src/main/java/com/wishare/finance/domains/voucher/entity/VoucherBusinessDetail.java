package com.wishare.finance.domains.voucher.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.reconciliation.enums.ExPayChannelEnums;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 凭证业务明细表
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/30
 */
@Getter
@Setter
@TableName(TableNames.VOUCHER_BUSINESS_DETAIL)
public class VoucherBusinessDetail {


    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    private Long accountBookId;

    @ApiModelProperty(value = "业务单据id")
    private Long businessBillId;

    @ApiModelProperty(value = "业务单据编码")
    private String businessBillNo;

    @ApiModelProperty(value = "业务单据类型: 1应收单, 2预收单, 3临时单, 4应付单, 5退款单, 6付款单, 7收款单, 8发票, 9收据, 10银行流水, 11清分流水")
    private Integer businessBillType;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "业务单据详情（快照）")
    private String businessBillDetails;

    @ApiModelProperty(value = "业务场景记录ID")
    private String sceneId;

    @ApiModelProperty(value = "场景类型(事件类型 1应收计提 2收款结算 3预收应收核销 4账单调整 5账单开票 6冲销作废 7未认领暂收款 8应付计提 9付款结算 )")
    private Integer sceneType;


    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
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
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;
    @TableField(exist = false)
    private Long receivableBillId;

    @TableField(exist = false)
    private Long advanceId;
    @ApiModelProperty(value = "支付方式-编码")
    private String payChannel;
    @ApiModelProperty(value = "支付方式-中文")
    private String payChannelValue;
    public VoucherBusinessDetail() {
    }

    public VoucherBusinessDetail(Long voucherId, Long businessBillId, String businessBillNo, Integer businessBillType,
                                 String sceneId, Integer sceneType, Object businessBillDetails, String supCpUnitId,
                                 String supCpUnitName,Long receivableBillId,Long advanceId,Long accountBookId,String payChannel) {
        this.voucherId = voucherId;
        this.businessBillId = businessBillId;
        this.businessBillNo = businessBillNo;
        this.businessBillType = businessBillType;
        this.sceneId = sceneId;
        this.sceneType = sceneType;
        if (Objects.nonNull(businessBillDetails)) {
            this.businessBillDetails = JSON.toJSONString(businessBillDetails);
        }
        this.supCpUnitId = supCpUnitId;
        this.supCpUnitName = supCpUnitName;
        this.receivableBillId = receivableBillId;
        this.advanceId = advanceId;
        this.accountBookId = accountBookId;
        this.payChannel = payChannel;
        this.payChannelValue = SettleChannelEnum.valueNameOfByCode(payChannel);
    }
}
