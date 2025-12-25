package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("空间中心资产信息")
public class SAssetV {
    @ApiModelProperty("资产id")
    private String id;
    @ApiModelProperty("空间id")
    private Long spaceId;
    @ApiModelProperty("空间标准名称")
    private String spaceName;
    @ApiModelProperty("空间类型名称")
    private String spaceTypeName;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("项目id")
    private String communityId;
    @ApiModelProperty("项目名称")
    private String communityName;
    @ApiModelProperty(value = "与户主的关系 0 户主 1 配偶 2 子女 3 孙辈 4 父母 5 祖父母 6 外祖父母 7 兄弟姊妹 8 旁系亲属 9 其他")
    private Integer relation;
    @ApiModelProperty(value = "是否为缴费主体 0 否 1 是 2 未知")
    private Integer isPayer;
    @ApiModelProperty("用户电话")
    private String mobileNum;
    @ApiModelProperty("是否经过确认：0待确认，1已确认，2未通过")
    private Integer isExamine;
    @ApiModelProperty("身份id")
    private String identityId;
    @ApiModelProperty("身份")
    private String identity;
    @ApiModelProperty("身份类型：1房东，2代理人，3租客，4业主，5家属，0租户自定义")
    private Integer identityType;

    //用作排序
    private Integer isExamineSort;
}