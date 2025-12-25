package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 流水领用记录
 *
 * @author yancao
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("flow_claim_record")
@ToString
public class FlowClaimRecordE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 领用金额
     */
    private Long claimAmount;

    /**
     * 实收金额
     */
    private Long settleAmount;

    /**
     * 领用时间
     */
    private LocalDateTime claimDate;

    /**
     * 本方账户
     */
    private String ourAccount;

    /**
     * 本方名称
     */
    private String ourName;

    /**
     * 本方开户行
     */
    private String ourBank;

    /**
     * 交易平台
     */
    private String tradingPlatform;

    /**
     * 房号id
     */
    private String roomId;

    /**
     * 系统来源 1收费系统，2合同系统
     */
    private Integer sysSource;

    /**
     * 认领类型 1:发票认领;2:账单认领;
     */
    private Integer claimType;
    /**
     *  上级收费单元ID 项目id
     */
    private String supCpUnitId;


    private String supCpUnitName;
    /**
     * 对账标识，是否对账  0 未对账  1 已对账  2 部分对账
     */
    private Integer reconcileFlag;

    @ApiModelProperty("日报表附件集合")
    private String reportFiles;

    @ApiModelProperty("银行回单附件集合")
    private String flowFiles;

    @ApiModelProperty("流水审核状态 0 已审核 1 审核中 2 差额认领审批驳回 3差额认领待审核")
    private Integer approveState;

    @ApiModelProperty("资金收款单推送 0未生成资金收款单 1待推送 2已推送 3 推送失败 4 已驳回")
    private Integer pushState;

    @ApiModelProperty("是否差额认领 0 差额认领 1或者其他 全额认领 若是差额认领必须传0")
    private Integer differenceFlag;

    @ApiModelProperty("差额认领原因 差额认领必传")
    private Integer differenceReason;

    @ApiModelProperty("差额认领备注")
    private String differenceRemark;

    @ApiModelProperty("审核意见")
    private String reviewComments;

    @ApiModelProperty("驳回人")
    private String refuseName;

    @ApiModelProperty("驳回时间")
    private LocalDateTime refuseTime;

    @ApiModelProperty("支付方式 0 现金  1 非现金")
    private Integer payChannelType;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 认领人
     */
    private String claimName;


    private String voucherBillNo;

    private Long voucherBillId;


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

    /**
     * 关联的发票id
     */
    @TableField(exist = false)
    private List<Long> invoiceIdList;

    /**
     * 关联的流水id
     */
    @TableField(exist = false)
    private List<Long> flowIdList;

    /**
     * 单据备注
     */
    private String receiptRemark;

    /**
     * 到期日期
     */
    private LocalDateTime gmtExpire;

    /**
     * 推送部门code
     */
    private String externalDepartmentCode;

    public Long compareAmount(){
       return claimAmount -settleAmount;
    };


    public void init() {
        if (Objects.isNull(id)){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("flow_claim_record_id"));
        }
        if(StringUtils.isBlank(serialNumber)){
            setSerialNumber(IdentifierFactory.getInstance().serialNumber("flow_claim_record_serial_number", "FCR", 21));
        }
    }

}
