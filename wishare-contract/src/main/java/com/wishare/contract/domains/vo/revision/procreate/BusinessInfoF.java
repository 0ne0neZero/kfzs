package com.wishare.contract.domains.vo.revision.procreate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 功能解释
 *
 * @author 龙江锋
 * @date 2023/10/20 14:44
 */
@Data
public class BusinessInfoF {

    @ApiModelProperty(value = "表单唯一id")
    private String formDataId;

    @ApiModelProperty(value = "内嵌url")
    private String innerUrl;

    @ApiModelProperty(value = "编辑标记 0 不能编辑 1 可以编辑")
    private Integer editFlag = 0;

    @ApiModelProperty(value = "表单类型 1 支出合同 2 收入合同 3 付款结算单 4 付款单")
    private Integer formType;

    @ApiModelProperty(value = "流程类型")
    private String flowType;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "所属区域")
    private Integer ssdw;

    @ApiModelProperty(value = "合同业务类型")
    private Integer htywlx;

    @ApiModelProperty(value = "收支类型")
    private Integer szlx;

    @ApiModelProperty(value = "成本合同类别")
    private Integer cbhtlb;

    @ApiModelProperty(value = "收入合同类别")
    private Integer srhtlb;

    @ApiModelProperty(value = "增值合同类型")
    private Integer zzhtlx;

    @ApiModelProperty(value = "增值合同类型")
    private Integer zzhtlx1;

    @ApiModelProperty(value = "服务类别")
    private Integer fwlb;

    @ApiModelProperty(value = "是否满足利润刻度0是1否")
    private Integer sfmzjtlrkdz;

    @ApiModelProperty(value = "是否满足招商刻度0是1否")
    private Integer sfmzjtzskdz;

    @ApiModelProperty(value = "合同总金额（含税）")
    private BigDecimal htzjehs;

    @ApiModelProperty(value = "所属区域")
    private Integer ssqy;

    @ApiModelProperty(value = "收入类型")
    private Integer srlx;

    @ApiModelProperty(value = "结算类型")
    private Integer jslx;

    @ApiModelProperty(value = "所属层级")
    private Integer sscj;

    @ApiModelProperty(value = "结算分类")
    private Integer jsfl;

    @ApiModelProperty(value = "项目类型")
    private Integer xmlx;

    @ApiModelProperty(value = "项目名称")
    private String xmmc;

    @ApiModelProperty(value = "合同结算含税金额-本期")
    private BigDecimal htjshsje;

    @ApiModelProperty(value = "合同范本使用情况")
    private Integer htfbsyqk;

    @ApiModelProperty(value = "我方签约单位")
    private Integer wfqydw;

    @ApiModelProperty(value = "是否补充协议")
    private Integer sfbcxy;

    @ApiModelProperty(value = "流程id-更新流程时传")
    private String processId;

    /* 立项管理 */
    @ApiModelProperty(value = "立项类型")
    private Integer lxlx;

    @ApiModelProperty(value = "具体分类")
    private Integer jtfl;

    @ApiModelProperty(value = "含税金额（元）")
    private BigDecimal hsje;

    @ApiModelProperty(value = "立项是由")
    private String lxsy;

    @ApiModelProperty(value = "本月累计使用金额")
    private String byljsyje;
    /* 立项管理 */

    @ApiModelProperty(value = "管理组织")
    private Integer glzz;

}
