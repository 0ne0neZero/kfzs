package com.wishare.finance.infrastructure.remote.vo.space;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/11/3
 * @Description:
 */
@Getter
@Setter
@ApiModel("租户项目关联表反参")
public class SpaceCommunityMtmTenantCommunityRV {

    @ApiModelProperty("记录唯一ID")
    private String id;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("标识码")
    private String code;

    @ApiModelProperty("租户项目ID")
    private String tenantCommunityId;


}
