package com.wishare.finance.apps.model.configure.subject.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 15:14
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("关联科目现金流量")
public class SaveSubjectCashFlowCodesF {

    /**
     * 科目code
     */
    @ApiModelProperty("科目code")
    private String subjectCode;

    /**
     * 现金流量code
     */
    @ApiModelProperty("现金流量code")
    private String cashFlowCode;

    /**
     * 类型 0 现金 1 银行 2 现金等价物
     */
    @ApiModelProperty("类型 0 现金 1 银行 2 现金等价物")
    private Integer type;

    /**
     * 主附表 0 主表  1 附表
     */
    @ApiModelProperty("主附表 0 主表  1 附表")
    private Integer isMain;

    /**
     * 方向 1 流入  2 流出
     */
    @ApiModelProperty("方向 1 流入  2 流出")
    private Integer inOut;
}
