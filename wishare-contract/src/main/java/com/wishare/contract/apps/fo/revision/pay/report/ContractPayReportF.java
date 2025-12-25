package com.wishare.contract.apps.fo.revision.pay.report;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/5/28 14:23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同台账请求参数", description = "支出合同台账请求参数")
public class ContractPayReportF {

    @ApiModelProperty("所属项目ID")
    private List<String> communityIdList;

    @ApiModelProperty("应结日期")
    private List<String> plannedCollectionTime;

    @ApiModelProperty("应结日期")
    private List<String> paymentDate;

    @ApiModelProperty("合同履约状态：0.尚未履行;1.正在履行;2.合同停用;3.合同终止;99.未生效")
    private List<String> statusList;

    @ApiModelProperty("跳转对应合同ID结合")
    private List<String> contractList;

    @ApiModelProperty("属性")
    private String attribute;

    @ApiModelProperty("是否NK")
    private Integer isNK = 0;

    //-------后端逻辑使用--------

    @ApiModelProperty("应结日期-开始")
    private String plannedCollectionTimeStart;

    @ApiModelProperty("应结日期-结束")
    private String plannedCollectionTimeEnd;

    @ApiModelProperty("应结日期-开始")
    private String paymentDateSatrt;

    @ApiModelProperty("应结日期-结束")
    private String paymentDateEnd;

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("组织ID")
    private List<String> orgList;
    @ApiModelProperty("超级管理")
    private Boolean superAccount = Boolean.FALSE;

    @ApiModelProperty("区域")
    private List<String> isBackDate;
    @ApiModelProperty("部门名称")
    private String departName;
    @ApiModelProperty("合同类别")
    private List<String> conmanagetype;
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("合同编码")
    private String contractNo;
    @ApiModelProperty("供应商名称")
    private String merchantName;
    @ApiModelProperty("结算周期")
    private List<String> splitMode;
    @ApiModelProperty("合同起始日期")
    private String gmtExpireStart;
    @ApiModelProperty("合同终止日期")
    private String gmtExpireStartEnd;
    @ApiModelProperty("合同起始日期")
    private String gmtExpireEndStart;
    @ApiModelProperty("合同终止日期")
    private String gmtExpireEnd;

}
