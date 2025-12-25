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
public class MDM63F2 {

    //主数据合同编码
    @ApiModelProperty(example = "CT12312321321")
    @JsonProperty("CONTRACTCODE")
    private String contractCode;

    //单位ID
    @ApiModelProperty(example = "101276372")
    private String acctUnitID;

    //往来单位编号
    @JsonProperty("PARTNERID")
    private String PARTNERID;

    //查询开始时间
    @ApiModelProperty(example = "2000-06-08T02:21:37.366+00:00")
    private String starttime;

    //查询结束时间
    @ApiModelProperty(example = "2031-06-08T03:21:37.366+00:00")
    private String endtime;

    //查询页码
    @ApiModelProperty(example = "1")
    @JsonProperty("PageNum")
    private Integer pageNum;

    //项目编号
    @JsonProperty("PROJECT")
    private String project;


}
