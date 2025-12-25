package com.wishare.contract.apps.fo.revision.income;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  10:39
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息表推送F", description = "收入合同订立信息表推送F")
public class ContractIncomePullF {
    @ApiModelProperty("原系统合同id")
    private String fromid;

    @ApiModelProperty("合同主数据编码")
    private String conmaincode;

    @ApiModelProperty("合同中文名称")
    private String connamecn;

    @ApiModelProperty("合同语言")
    private String conlanguage;

    @ApiModelProperty("合同名称")
    private String conname;

    @ApiModelProperty("合同编码")
    private String concode;

    @ApiModelProperty("是否补充合同")
    private String issupplycontract;

    @ApiModelProperty("补充合同所属原合同主数据编码")
    private String supconmaincode;

    @ApiModelProperty("内部合同关联校验码")
    private String oppositeconmaincode;

    @ApiModelProperty("项目编码")
    private String projnumber;

    @ApiModelProperty("项目名称")
    private String projname;

    @ApiModelProperty("收支类型")
    private String incomeexpendtype;

    @ApiModelProperty("合同业务类型")
    private String businesstype;

    @ApiModelProperty("合同履行地(国家)")
    private String conperformcountry;

    @ApiModelProperty("合同履行地(省)")
    private String conperformprovinces;

    @ApiModelProperty("合同状态 0 待提交， 1 审批中， 2 已拒绝， 3 未开始， 4 执行中， 5 已到期， 6 已终止")
    private Integer status;

    @ApiModelProperty("合同履行地(市)")
    private String conperformcity;

    @ApiModelProperty("合同履行地(县)")
    private String conperformxian;

    @ApiModelProperty("签约日期")
    private String signdate;

    @ApiModelProperty("争议解决方式")
    private String disputesolutionway;

    @ApiModelProperty("合同范本使用情况")
    private String conmodelusecondition;

    @ApiModelProperty("合同承办人id")
    private String conundertakerid;

    @ApiModelProperty("合同承办人姓名")
    private String conundertaker;

    @ApiModelProperty("合同承办人联系方式")
    private String conundertakercontact;

    @ApiModelProperty("合同概述")
    private String consummary;

    @ApiModelProperty("管理部门id")
    private String oid;

    @ApiModelProperty("我方签约单位id")
    private String partiesunitid;

    @ApiModelProperty("我方签约单位名称")
    private String partiesunitname;

    @ApiModelProperty("合同实际履约主体id")
    private String sjlydwid;

    @ApiModelProperty("合同实际履约主体名称")
    private String sjlydwname;

    @ApiModelProperty("合同状态")
    private String constatus;

    @ApiModelProperty("总金额币种/信托资金规模币种/贷款总额币种")
    private String currency;

    @ApiModelProperty("税率")
    private BigDecimal taxrate;

    @ApiModelProperty("税额")
    private BigDecimal taxamt;

    @ApiModelProperty("合同总金额(含税)")
    private BigDecimal hsamt;

    @ApiModelProperty("合同变更后总金额（含税）")
    private BigDecimal hsbgamt;

    @ApiModelProperty("管理部门名称")
    private String oname;

    @ApiModelProperty("(租赁信息)")
    private String zlxx;

    @ApiModelProperty("(特征信息)")
    private String tzxx;

    @ApiModelProperty("(合同对方信息)")
    private String htdfxx;

    @ApiModelProperty("(合同支付信息:支付方信息)")
    private String fkdwxx;

    @ApiModelProperty("(合同支付信息:收入方信息)")
    private String skdwxx;

    @ApiModelProperty("(保证金信息)")
    private String bzjxx;

    @ApiModelProperty("(审批信息)")
    private String spxx;

    @ApiModelProperty("(附件信息)")
    private String fjxx;

    @ApiModelProperty("(保险信息)")
    private String bxxx;

    @ApiModelProperty("(担保信息)")
    private String dbxx;

    @ApiModelProperty("履约进度")
    private String performschedule;

    @ApiModelProperty("附件信息替换")
    private List<AttachmentE> attachmentEList;

    @ApiModelProperty("来源系统编码")
    private String fromsyscode;

    @ApiModelProperty("启用状态")
    private String enablestatus;

    @ApiModelProperty("停用说明")
    private String deactivatedesc;
}
