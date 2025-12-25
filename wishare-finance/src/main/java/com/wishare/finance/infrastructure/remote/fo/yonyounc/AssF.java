package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("辅助核算")
public class AssF {

    @ApiModelProperty(value = "辅助核算类型    (会计科目辅助核算)  ")
    private String pk_Checktype;

    @ApiModelProperty(value = " 辅助核算值    （档案转换）")
    private String pk_Checkvalue;
}
