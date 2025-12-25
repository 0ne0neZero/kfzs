package com.wishare.finance.apps.process.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BusinessInfoF {

    @ApiModelProperty(value = "是否默认提交到第二节点 0 ：不流转 1：流转 (默认)")
    private String isNextFlow;

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

    @ApiModelProperty(value = "审批事项说明")
    private String spsxsm;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal zfje;

    @ApiModelProperty(value = "减免原因")
    private String jmyy;

    @ApiModelProperty(value = "申请减免金额")
    private String sqjmje;

    @ApiModelProperty(value = "减免比例")
    private Double jmzkbl;

    @ApiModelProperty(value = "项目名称")
    private String xmmc;
    @ApiModelProperty(value = "是否结算单流程")
    private Integer isJsdProcess;

    @ApiModelProperty(value = "邀约人")
    private String sqr;
    @ApiModelProperty(value = "所属部门")
    private String ssbm;
    @ApiModelProperty(value = "申请日期")
    private String sqrq;
    @ApiModelProperty(value = "到访日期")
    private String rzrq;
    @ApiModelProperty(value = "附件")
    private String fj;
    @ApiModelProperty(value = "备注")
    private String bz;
    @ApiModelProperty(value = "所属分部")
    private String sqrfb;
    @ApiModelProperty(value = "到访单位")
    private String dfdw;
    @ApiModelProperty(value = "离开日期")
    private String lkrq;
    @ApiModelProperty(value = "被邀约人")
    private String byyr;
    @ApiModelProperty(value = "是否驾车，是-0  否-1")
    private Integer sfjc;
    @ApiModelProperty(value = "是否就餐，是-0  否-1")
    private Integer sfjc1;

}
