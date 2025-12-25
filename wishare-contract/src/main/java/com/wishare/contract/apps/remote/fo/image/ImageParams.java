package com.wishare.contract.apps.remote.fo.image;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 业务参数对象节点
 * 名称不能改动，需和影像系统一致
 *
 * @author 龙江锋
 * @date 2023/8/8 13:40
 */
@Data
@ApiModel("业务参数对象节点")
public class ImageParams {
    /**
     * 操作人帐号
     */
    @ApiModelProperty("操作人帐号")
    @NotBlank(message = "操作人帐号不能为空")
    @Length(message = "操作人帐号不能超过50位", max = 50)
    private String operatoruser;

    /**
     * 操作人名称
     */
    @NotBlank(message = "操作人名称不能为空")
    @Length(message = "操作人名称不能超过200位", max = 200)
    private String operatorname;

    /**
     * 文件base64内容
     */
    @ApiModelProperty("文件base64内容")
    @NotBlank(message = "文件base64内容不能为空")
    private String filecontent;

    /**
     * 文件名称，带后缀
     */
    @ApiModelProperty("文件名称，带后缀")
    @NotBlank(message = "文件名称不能为空")
    @Length(message = "文件名称长度不能超过100", max = 100)
    private String filename;

    /**
     * 是否需要OCR,
     * 0/不需要，1/需要
     * 默认：0
     */
    @ApiModelProperty("是否需要OCR")
    @Length(message = "是否需要OCR长度固定位1位", max = 1)
    private Integer isocr = 0;

    /**
     * 电子凭证验证类型，不传则默认为0。
     * 0-不做处理（适用于非电子凭证类的附件）
     * 1-只验签（验签失败则直接返回失败，验签成功后再继续进行发票真伪查验，真伪查验通过后才返回文件唯一ID、查验结果、发票信息给业务系统）
     * 2-只解析XBRL（解析失败则直接返回失败；解析成功再继续进行发票真伪查验，真伪查验通过后才返回文件唯一ID、查验结果、发票信息给业务系统）
     * 3-验签并解析XBRL（验签失败或解析XBRL失败则直接返回失败；验证成功且解析XBRL成功，再继续进行发票真伪查验，真伪查验通过后才返回文件唯一ID、查验结果、发票信息给业务系统）
     */
    @ApiModelProperty("电子凭证验证类型")
    private String verifytype;


}
