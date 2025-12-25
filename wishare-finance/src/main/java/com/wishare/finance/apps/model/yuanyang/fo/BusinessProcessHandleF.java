package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.apps.model.bill.fo.SceneF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Date;
import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/1
 */
@Getter
@Setter
@ApiModel("BPM凭证流程统一入参")
public class BusinessProcessHandleF {

    /**
     * 单据标题
     */
    @NotBlank(message="单据标题不能为空")

    @ApiModelProperty(value = "单据标题", required = true)
    private String title;

    @NotBlank(message="业务单据id不能为空")
    @Length(max= 64,message="业务单据id长度不能超过64")
    @ApiModelProperty(value = "业务单据id", required = true)
    private String businessId;

    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("业务单据编号（用与前端页面展示）")
    private String businessNo;

    @NotBlank(message="单据类型不能为空")
    @Length(max= 32,message="单据类型长度不能超过32")
    @ApiModelProperty(value = "单据类型：资金上缴、资金下拨、能源费预付款、能源费分摊", required = true)
    private String billType;

    @NotBlank(message="单据类型编码不能为空")
    @Length(max= 32,message="单据类型编码长度不能超过32")
    @ApiModelProperty(value = "单据类型编码", required = true)
    private String billTypeCode;

    @NotNull(message="单据开始时间不能为空")
    @ApiModelProperty(value = "单据开始时间", required = true)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "请求超时时间 格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @ApiModelProperty("单据描述")
    private String description;

    @Length(max= 64,message="制单人编码长度不能超过64")
    @NotBlank(message="制单人编码不能为空")
    @ApiModelProperty(value = "制单人编码", required = true)
    private String makerCode;

    @Length(max= 100,message="制单人名称不能超过100")
    @ApiModelProperty(value = "制单人名称")
    private String makerName;

    @ApiModelProperty("记账会计")
    private String accountant;

    @ApiModelProperty("财务出纳")
    private String casher;

    @ApiModelProperty("申请人")
    private String applicant;

    @ApiModelProperty("申请人编码")
    private String applicantCode;

    @ApiModelProperty("申请日期")
    private LocalDate applyTime;

    @ApiModelProperty("实际付款日期")
    private LocalDate payTime;

    @ApiModelProperty("支付信息")
    private ProcessBankInfoF bankInfo;

    @Valid
    @ApiModelProperty(value = "支付场景信息")
    private SceneF scene;

    @ApiModelProperty(value = "消息通知地址")
    @Length(max = 256, message = "消息通知地址格式不正确")
    private String notifyUrl;

    @ApiModelProperty(value = "扩展参数")
    private String attachParam;

    @ApiModelProperty(value = "推凭标识：0不推凭，1推凭")
    private Integer voucherFlag;

    @ApiModelProperty("来源系统：默认102BPM系统")
    private String sysSource = "102";

    @ApiModelProperty("来源说明")
    private String source;

    @Valid
    @ApiModelProperty("账套信息")
    private List<ProcessAccountBookF> accountBooks;


}
