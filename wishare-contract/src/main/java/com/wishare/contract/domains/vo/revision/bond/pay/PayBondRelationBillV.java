package com.wishare.contract.domains.vo.revision.bond.pay;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 缴纳保证金改版关联单据明细表视图对象
* </p>
*
* @author chenglong
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "缴纳保证金改版关联单据明细表视图对象", description = "缴纳保证金改版关联单据明细表视图对象")
public class PayBondRelationBillV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 保证金计划ID
    */
    @ApiModelProperty("保证金计划ID")
    private String bondId;
    /**
    * 编号
    */
    @ApiModelProperty("编号")
    private String code;
    /**
    * 业务类型编码（收款，收据，结转，退款）
    */
    @ApiModelProperty("业务类型编码（收款，收据，结转，退款）")
    private String typeCode;
    /**
    * 业务类型名称（收款，收据，结转，退款）
    */
    @ApiModelProperty("业务类型名称（收款，收据，结转，退款）")
    private String type;
    /**
    * 金额（收款，收据，结转，退款）
    */
    @ApiModelProperty("金额（收款，收据，结转，退款）")
    private BigDecimal amount;
    /**
    * 交易方式编码（现金，微信，支付宝，网上转账等）
    */
    @ApiModelProperty("交易方式编码（现金，微信，支付宝，网上转账等）")
    private String dealWayCode;
    /**
    * 交易方式名称（现金，微信，支付宝，网上转账等）
    */
    @ApiModelProperty("交易方式名称（现金，微信，支付宝，网上转账等）")
    private String dealWay;
    /**
    * 业务费项ID
    */
    @ApiModelProperty("业务费项ID")
    private String chargeItemId;
    /**
    * 业务费项名称
    */
    @ApiModelProperty("业务费项名称")
    private String chargeItem;
    /**
    * （收款，收据，结转，退款）交易日期
    */
    @ApiModelProperty("（收款，收据，结转，退款）交易日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dealDate;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    private String remark;
    /**
    * 原因
    */
    @ApiModelProperty("原因")
    private String reason;
    /**
     * 附件凭证
     */
    @ApiModelProperty("附件凭证")
    private AttachmentE filesRecord;
    /**
     * fileKey
     */
    @ApiModelProperty("附件凭证fileKey")
    private String fileKey;
    /**
     * fileKey
     */
    @ApiModelProperty("附件凭证fileName")
    private String fileName;
    /**
    * 剩余金额
    */
    @ApiModelProperty("剩余金额")
    private BigDecimal residueAmount;
    /**
    * 审批流id
    */
    @ApiModelProperty("审批流id")
    private Long procId;
    /**
     * 转履约关联数据ID
     */
    @ApiModelProperty("转履约关联数据ID")
    private String volumUpId;
    /**
    * 状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）
    */
    @ApiModelProperty("状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）")
    private Integer status;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人
    */
    @ApiModelProperty("操作人")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
     * bankAccount
     */
    @ApiModelProperty("银行账户")
    @Length(message = "银行账户不可超过 40 个字符",max = 50)
    private String bankAccount;

}
