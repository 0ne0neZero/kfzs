package com.wishare.finance.apps.model.bill.fo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("批量减免记录")
@Getter
@Setter
public class DeductionRecordDetailF {


    @ApiModelProperty("减免规则id")
    private Long ruleId;

    @ApiModelProperty("账单id")
    private List<Long> billIds;

    @ApiModelProperty(value = "调整方式：1应收调整-单价，2应收调整-应收金额，3应收调整-实测面积，4实收调整-实测面积，5实收调整-空置房打折，6实收调整-优惠券，7实收调整-开发减免，8实收调整-其他", required = true)
    private Integer adjustWay;

    @ApiModelProperty(value = "减免方式：1.按固定金额减免，2.按金额均摊减免，3.按折扣减免，4.按权重减免", required = true)
    private Integer derateStrategy;

    @ApiModelProperty(value = "调整金额(减免时为负数，调高时为正数)", required = true)
    private BigDecimal adjustAmount;

    @ApiModelProperty(value = "减免形式：1.应收减免；2实收减免")
    private Integer deductionMethod;

    @ApiModelProperty(value = "减免原因 1：物业服务终止、失联，2：破产或房屋拍卖、易主房屋，3: 销售承诺、房屋质量问题,4: 服务质量瑕疵,5: 法院判决类、属地法规规定的空置减免类, 99: 其他", required = true)
    private Integer reason;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）,可多选")
    private List<Integer> settleState;

    @ApiModelProperty("附件信息")
    private List<FileVo> fileVos;

    @ApiModelProperty(value = "指定计费周期的开始时间", hidden = true)
    private LocalDate startDate;

    @ApiModelProperty(value = "指定计费周期的结束时间", hidden = true)
    private LocalDate endDate;


    @ApiModelProperty("删除状态：0 未删除，1 已删除")
    protected Byte deleted;

    @ApiModelProperty("租户id")
    protected String tenantId;

    @ApiModelProperty("创建人id")
    protected String creator;

    @ApiModelProperty("创建人名字")
    protected String creatorName;

    @ApiModelProperty("操作人id")
    protected String operator;

    @ApiModelProperty("操作人姓名")
    protected String operatorName;

    @ApiModelProperty("创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    protected LocalDateTime gmtCreate;

    @ApiModelProperty("最后修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    protected LocalDateTime gmtModify;


}
