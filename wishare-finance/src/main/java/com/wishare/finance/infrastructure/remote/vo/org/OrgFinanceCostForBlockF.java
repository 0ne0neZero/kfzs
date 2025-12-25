package com.wishare.finance.infrastructure.remote.vo.org;


import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
* <p>
*  根据项目期区ids 获取期区成本中心集合
* </p>
*
* @author wangrui
* @since 2022-08-15
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("根据期区查成本中心集合")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrgFinanceCostForBlockF {
    @ApiModelProperty("项目id")
    @NotBlank
    String communityId;
    @ApiModelProperty("期区ids")
    @NotNull
    List<String> blockIds;
}
