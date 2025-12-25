package com.wishare.contract.apps.remote.fo.image;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/10/18/11:45
 */
@Data
@ApiModel("删除影像系统参数列表")
public class ImageFileDeleteParamF {
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
     * 附件唯一ID
     */
    @ApiModelProperty("附件唯一ID")
    @NotBlank(message = "附件唯一ID不能为空")
    private String fileuuid;
}
