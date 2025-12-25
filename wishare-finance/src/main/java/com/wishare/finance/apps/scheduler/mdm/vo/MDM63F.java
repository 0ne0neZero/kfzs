package com.wishare.finance.apps.scheduler.mdm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/10:44
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MDM63F {

    @ApiModelProperty(example = "CT12312321321")
    @JsonProperty("CONTRACTCODE")
    private String contractCode;

    @ApiModelProperty(example = "2000-06-08T02:21:37.366+00:00")
    private String starttime;

    @ApiModelProperty(example = "2031-06-08T03:21:37.366+00:00")
    private String endtime;

    @ApiModelProperty(example = "1")
    @JsonProperty("PageNum")
    private Integer pageNum;


}
