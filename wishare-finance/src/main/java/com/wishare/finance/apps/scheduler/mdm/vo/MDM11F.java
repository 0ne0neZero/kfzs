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
public class MDM11F {


    @ApiModelProperty(example = "2000-06-08T02:21:37.366+00:00")
    private String starttime;

    @ApiModelProperty(example = "2031-06-08T03:21:37.366+00:00")
    private String endtime;

    private String year;

    /**
     * 暂时只需要现金流量的数据,后续需要了再说
     * 01
     **/
    @JsonProperty("CusItemCategory")
    private String CusItemCategory;

    @ApiModelProperty(example = "1")
    @JsonProperty("PageNum")
    private Integer pageNum;


}
