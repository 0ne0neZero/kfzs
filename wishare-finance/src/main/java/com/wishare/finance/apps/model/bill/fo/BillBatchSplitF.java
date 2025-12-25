package com.wishare.finance.apps.model.bill.fo;

import com.wishare.finance.apps.model.bill.vo.ResidentEnterpriseV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author yyx
 * @date 2023/5/10 13:58
 */
@Getter
@Setter
@ApiModel("应收账单批量拆分")
public class BillBatchSplitF {

    @ApiModelProperty(value = "原账单id")
    @NotNull(message = "原账单id不能为空")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    @NotNull(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "拆分方式")
    @NotNull(message = "拆分方式不能为空")
    private Integer splitMethod;

    @ApiModelProperty(value = "拆分账单列表")
    @NotNull(message = "拆分账单列表不能为空")
    private List<SplitDetailF> splitDetailves;

    @ApiModelProperty(value = "收费对象信息")
    Map<String, List<ResidentEnterpriseV>> roomMessage;
}
