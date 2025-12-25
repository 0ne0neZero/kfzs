package com.wishare.finance.apps.process.fo;

import com.wishare.finance.apps.process.enums.BusinessProcessType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApprovalQueryF {

    @ApiModelProperty("业务主键id")
    private Long id;

    @ApiModelProperty("流程类型")
    private BusinessProcessType type;

}
