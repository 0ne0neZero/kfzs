package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("批量审核账单信息")
public class ApproveBatchBillF {

    @ApiModelProperty(value = "检索条件（全部批量处理时必填）")
    private PageF<SearchF<?>> query;

    @ApiModelProperty(value = "账单id列表（多条账单处理时必填）")
    private List<Long> billIds;

    @ApiModelProperty(value = "审核状态：0未审核，1审核中，2已审核，3未通过", required = true)
    @NotNull(message = "审核状态不能为空")
    private Integer approveState;

    @ApiModelProperty(value = "驳回理由")
    private String rejectReason;

    @ApiModelProperty("上级收费单元id")
    @NotNull(message = "上级收费单元id不能为空")
    private String supCpUnitId;

}
