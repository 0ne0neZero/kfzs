package com.wishare.finance.apps.model.voucher.vo;

import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 凭证模板信息
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Getter
@Setter
@ApiModel("凭证模板信息")
public class VoucherTemplateV {

    @ApiModelProperty(value = "凭证模板id")
    private Long id;

    @ApiModelProperty(value = "模板类型：1常规  2BPM")
    private Integer type;

    @ApiModelProperty(value = "业务类型编码(BPM特有)")
    private String bizCode;

    @ApiModelProperty(value = "模板序号")
    private Integer templateNum;

    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "币种：CNY人民币")
    private String currency;

    @ApiModelProperty(value = "记账日期类型： 1凭证生成日期，2凭证同步成功日期")
    private Integer bookDateType;

    @ApiModelProperty(value = "科目体系id")
    private Long subjectSystemId;

    @ApiModelProperty(value = "科目体系名称")
    private String subjectSystemName;

    @ApiModelProperty(value = "借贷分录")
    private List<VoucherTemplateEntryOBV> entries;

    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "是否红字凭证： 0否  1是")
    private Integer redVoucher;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    private String operator;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

}
