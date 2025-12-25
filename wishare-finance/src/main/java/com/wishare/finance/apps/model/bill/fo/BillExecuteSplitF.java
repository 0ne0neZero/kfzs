package com.wishare.finance.apps.model.bill.fo;

import com.wishare.finance.apps.model.bill.vo.ResidentEnterpriseV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author yyx
 * @date 2023/5/8 14:35
 * @description:账单拆分 执行参数
 */
@Getter
@Setter
@ApiModel("账单拆分")
public class BillExecuteSplitF {

    @ApiModelProperty(value = "原账单id")
    @NotNull(message = "原账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "账单类型")
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "拆分方式")
    private Integer splitMethod;

    @ApiModelProperty(value = "拆分账单方式列表")
    @NotEmpty(message = "拆分账单方式列表不能为空")
    private List<BillExecuteSplitDetailF> executeSplitDetailves;

    @ApiModelProperty(value = "收费对象信息")
    Map<String, List<ResidentEnterpriseV>> roomMessage;
}
