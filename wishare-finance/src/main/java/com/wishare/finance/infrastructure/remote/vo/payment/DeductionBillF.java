package com.wishare.finance.infrastructure.remote.vo.payment;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/6/13 15:56
 */
@Getter
@Setter
@ApiModel("减免账单")
public class DeductionBillF{

    @NotNull(message = "减免场景不能为空")
    @ApiModelProperty(value = "减免场景：5减免-空置房减免，6减免-优惠券，7减免-开发减免，8减免-其他 16-减免-零头减免", required = true)
    private Integer adjustWay;

    @ApiModelProperty(value = "调整类型： 1减免，2调高，3调低")
    private Integer adjustType = 1;

    @ApiModelProperty(value = "减免原因 1：物业服务终止、失联，2：破产或房屋拍卖、易主房屋，" +
            "3: 销售承诺、房屋质量问题,4: 服务质量瑕疵,5: 法院判决类、属地法规规定的空置减免类, 99: 其他", required = true)
    @NotNull(message = "请选择减免原因")
    private Integer reason;

    @ApiModelProperty("申请时间")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "优惠单据号码", required = true)
    private String voucher;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("附件集合")
    private List<FileVo> fileVos;

    private Long billId;

    @ApiModelProperty(value = "减免金额")
    private BigDecimal adjustAmount;

    @ApiModelProperty("上级收费单元id")
    @NotNull(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "调整比例，区间[0.01, 100]")
    private BigDecimal adjustRatio;

    @NotNull(message = "减免方式不能为空")
    @ApiModelProperty(value = "减免方式：1.按固定金额减免，2.按金额均摊减免，3.按折扣减免，4.按权重减免", required = true)
    private Integer derateStrategy;

    @NotNull(message = "减免形式不能为空")
    @ApiModelProperty(value = "减免形式：1.应收减免；2实收减免；3不减免", required = true)
    private Integer deductionMethod;
}