package com.wishare.finance.apps.model.report.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询账单生成报表返回数据
 * @author yancao
 */
@Setter
@Getter
@ApiModel("分页查询账单生成报表返回数据")
public class GenerateBillReportPageV {

    @ApiModelProperty("年度")
    @ExcelProperty(value = "年度", index = 0)
    private String year;

    @ApiModelProperty("月份")
    @ExcelProperty(value = "月份", index = 1)
    private String month;

    @ApiModelProperty("项目群")
    @ExcelProperty(value = "项目群", index = 2)
    private String projectGroup;

    @ApiModelProperty("项目名称")
    @ExcelProperty(value = "项目名称", index = 3)
    private String communityName;

    @ApiModelProperty("楼栋名称")
    @ExcelProperty(value = "楼栋名称", index = 4)
    private String buildingName;

    @ApiModelProperty("房屋名称")
    @ExcelProperty(value = "房屋名称", index = 5)
    private String roomName;

    @ApiModelProperty("业主名称")
    @ExcelProperty(value = "业主名称", index = 6)
    private String ownerName;

    @ApiModelProperty("费项")
    @ExcelProperty(value = "费项", index = 7)
    private String chargeItemName;

    @ApiModelProperty("账单生成情况")
    @ExcelProperty(value = "账单生成情况", index = 8)
    private String generation;

}
