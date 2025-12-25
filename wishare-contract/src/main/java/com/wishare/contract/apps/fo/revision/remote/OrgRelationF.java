package com.wishare.contract.apps.fo.revision.remote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/11  15:25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "OrgRelationF请求参数", description = "OrgRelationF请求参数")
public class OrgRelationF {

    /**
     * ID
     */
    @ApiModelProperty("业务主键ID")
    private String id;

}
