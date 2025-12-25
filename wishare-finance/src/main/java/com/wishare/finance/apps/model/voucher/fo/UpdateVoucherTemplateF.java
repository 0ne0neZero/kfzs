package com.wishare.finance.apps.model.voucher.fo;

import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 凭证模板表
 * </p>
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Getter
@Setter
@ApiModel("凭证模板更新信息")
public class UpdateVoucherTemplateF {

    @ApiModelProperty(value = "凭证模板id", required = true)
    @NotNull(message = "凭证模板id不能为空")
    private Long id;

    @ApiModelProperty(value = "模板类型：1常规  2BPM")
    private Integer type;

    @ApiModelProperty(value = "业务类型编码(BPM特有)")
    private String bizCode;

    @ApiModelProperty(value = "模板序号")
    private Integer templateNum;

    @ApiModelProperty(value = "模板名称")
    @NotBlank(message = "模板名称不能为空")
    private String name;

    @ApiModelProperty(value = "币种：默认CNY人民币")
    private String currency;

    @ApiModelProperty(value = "记账日期类型： 1凭证生成日期，2凭证同步成功日期")
    private Integer bookDateType;

    @ApiModelProperty(value = "科目体系id")
    @NotNull(message = "科目体系id不能为空")
    private Long subjectSystemId;

    @ApiModelProperty(value = "科目体系名称")
    @NotBlank(message = "科目体系名称不能为空")
    private String subjectSystemName;

    @ApiModelProperty(value = "借贷分录")
    private List<VoucherTemplateEntryOBV> entries;

    @ApiModelProperty(value = "是否启用：0启用，1禁用， 默认启用")
    private Integer disabled;

    @ApiModelProperty(value = "是否红字凭证： 0否  1是， 默认否")
    private Integer redVoucher;
}
