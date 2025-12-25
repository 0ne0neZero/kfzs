package com.wishare.contract.apps.remote.vo.imagefile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 附件上传接口返回值类型
 *
 * @author 龙江锋
 * @date 2023/8/8 17:22
 */
@Data
@ApiModel("附件上传接口返回值类型")
public class ImageFileDeleteV {
    /**
     * 执行是否成功
     */
    @ApiModelProperty("执行是否成功")
    private boolean status;

    /**
     * 错误编码
     */
    @ApiModelProperty("错误编码")
    private String errorCode;

    /**
     * 错误信息
     */
    @ApiModelProperty("错误信息")
    private String errorMessage;
}
