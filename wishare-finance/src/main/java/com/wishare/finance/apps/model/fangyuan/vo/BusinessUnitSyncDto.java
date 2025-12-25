package com.wishare.finance.apps.model.fangyuan.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author fengxiaolin
 * @date 2023/6/1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessUnitSyncDto {
    @ApiModelProperty(value = "业务单元id")
    private Long id;


    @ApiModelProperty("业务单元编码")
    private String code;

    @ApiModelProperty("业务单元名称")
    private String name;

    private List<Long> path;


}
