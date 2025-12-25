package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author :lethe
 * @Date : 2022/7/20 19:23
 */
@Getter
@Setter
@ApiModel
public class ResidentEnterpriseV {
    @ApiModelProperty(value = "每个对象ID")
    private String id;

    @ApiModelProperty(value = "居民ID")
    private String residentId;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "居民姓名")
    private String name;

    @ApiModelProperty(value = "居民电话")
    private String phone;

    @ApiModelProperty(value = "身份名称")
    private String identity;

    @ApiModelProperty(value = "身份id")
    private String identityId;

    @ApiModelProperty(value = "企业id")
    private String enterpriseId;

    @ApiModelProperty(value = "企业名")
    private String enterpriseName;

    @ApiModelProperty(value = "企业电话")
    private String enterprisePhone;

    @ApiModelProperty(value = "性别 0 女 1 男")
    private Byte sex;

    @ApiModelProperty(value="与户主的关系 0 户主 1 配偶 2 子女 3 孙辈 4 父母 5 祖父母 6 外祖父母 7 兄弟姊妹 8 旁系亲属 9 其他")
    private Integer relation;
}
