package com.wishare.contract.domains.vo.revision.projectInitiation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Props {

    @ApiModelProperty("明细表列组件")
    private List<FormItem> columns;

}
