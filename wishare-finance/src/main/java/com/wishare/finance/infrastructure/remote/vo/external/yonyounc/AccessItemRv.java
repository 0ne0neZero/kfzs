package com.wishare.finance.infrastructure.remote.vo.external.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/26
 * @Description:
 */
@ApiModel("辅助核算列表")
@Getter
@Setter
public class AccessItemRv {

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 数据对象
     */
    @ApiModelProperty("数据对象")
    private String object;

    /**
     * 参照名称
     */
    @ApiModelProperty("参照名称")
    private String refnodename;

}
