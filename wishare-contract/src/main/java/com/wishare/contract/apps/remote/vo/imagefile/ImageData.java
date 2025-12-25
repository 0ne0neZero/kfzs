package com.wishare.contract.apps.remote.vo.imagefile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 附件上传接口返回值数据
 *
 * @author 龙江锋
 * @date 2023/8/8 17:29
 */
@Data
@ApiModel("附件上传接口返回值数据")
public class ImageData {
    /**
     * 文件唯一ID
     */
    @ApiModelProperty("文件唯一ID")
    private String fileuuid;

    /**
     * 识别结果返回
     */
    @ApiModelProperty("识别结果返回")
    private List<OcrInfo> ocrinfo;

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
