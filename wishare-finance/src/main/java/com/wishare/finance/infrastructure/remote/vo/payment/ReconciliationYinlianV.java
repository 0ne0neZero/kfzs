package com.wishare.finance.infrastructure.remote.vo.payment;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2023/2/19
 * @Description:
 */
@Getter
@Setter
public class ReconciliationYinlianV {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("清算日期")
    private String clearDate;

    @ApiModelProperty("商户号")
    private String mid;

    @ApiModelProperty("终端号")
    private String tid;

    @ApiModelProperty("消息类型")
    private String msgType;

    @ApiModelProperty("处理码")
    private String handleCode;

    @ApiModelProperty("卡号/付款码")
    private String paymentCode;

    @ApiModelProperty("交易金额")
    private String tradeAmount;

    @ApiModelProperty("交易流水号")
    private String seqId;

    @ApiModelProperty("交易日期")
    private String tradeDate;

    @ApiModelProperty("服务点条件码")
    private String servicePointConditionCode;

    @ApiModelProperty("服务码")
    private String serviceCode;

    @ApiModelProperty("发卡机构标识")
    private String issuerIdent;

    @ApiModelProperty("检索参考号")
    private String searchReferenceNo;

    @ApiModelProperty("原交易流水号")
    private String oldSeqId;

    @ApiModelProperty("原交易日期(MMDDhhmmss)")
    private String oldTradeDate;

    @ApiModelProperty("原交易参考号")
    private String oldTradeReferenceNo;

    @ApiModelProperty("商户手续费")
    private String commission;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("交易名称（中文）")
    private String tradeName;

    @ApiModelProperty("卡类型标识")
    private String cardType;

    @ApiModelProperty("外卡内用标识")
    private String extCardUseToIn;

    @ApiModelProperty("交易渠道")
    private String  tradeChannel;

    @ApiModelProperty("对账状态：0未核对，1已核对")
    private Integer state;

    @ApiModelProperty("备用字段1")
    private String  extField1;

    @ApiModelProperty("备用字段2")
    private String  extField2;

    @ApiModelProperty("备用字段3")
    private String  extField3;

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

}
