package com.wishare.contract.domains.vo.revision.income;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 收入合同订立信息拓展表视图对象
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息拓展表视图对象", description = "收入合同订立信息拓展表视图对象")
public class ContractIncomeConcludeExpandV {

    /**
    * 收入合同订立信息拓展表id
    */
    @ApiModelProperty("收入合同订立信息拓展表id")
    private Long id;
    /**
    * 合同id
    */
    @ApiModelProperty("合同id")
    private String contractId;
    /**
    * 合同语言
    */
    @ApiModelProperty("合同语言")
    private String conlanguage;
    /**
     * 合同管理类别
     */
    @ApiModelProperty("合同管理类别")
    private String conmanagetype;
    /**
     * 增值业务类型
     */
    @ApiModelProperty("增值业务类型")
    private String conincrementype;
    /**
     * 是否满足集团招商刻度0是1否
     */
    @ApiModelProperty("是否满足集团招商刻度0是1否")
    private Integer fmzjtzskd;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    private String conname;
    /**
    * 是否补充合同
    */
    @ApiModelProperty("是否补充合同")
    private String issupplycontract;
    /**
    * 内部合同关联校验码
    */
    @ApiModelProperty("内部合同关联校验码")
    private String oppositeconmaincode;
    /**
    * 收支类型
    */
    @ApiModelProperty("收支类型")
    private String incomeexpendtype;
    /**
    * 合同履行地(国家)
    */
    @ApiModelProperty("合同履行地(国家)")
    private String conperformcountry;
    /**
    * 合同履行地(省)
    */
    @ApiModelProperty("合同履行地(省)")
    private String conperformprovinces;
    /**
    * 合同履行地(市)
    */
    @ApiModelProperty("合同履行地(市)")
    private String conperformcity;
    /**
    * 合同履行地(县)
    */
    @ApiModelProperty("合同履行地(县)")
    private String conperformxian;
    /**
    * 争议解决方式
    */
    @ApiModelProperty("争议解决方式")
    private String disputesolutionway;
    /**
    * 合同范本使用情况
    */
    @ApiModelProperty("合同范本使用情况")
    private String conmodelusecondition;
    /**
    * 合同承办人id
    */
    @ApiModelProperty("合同承办人id")
    private String conundertakerid;
    /**
    * 合同承办人姓名
    */
    @ApiModelProperty("合同承办人姓名")
    private String conundertaker;
    /**
    * 合同承办人联系方式
    */
    @ApiModelProperty("合同承办人联系方式")
    private String conundertakercontact;
    /**
    * 税率
    */
    @ApiModelProperty("税率")
    private BigDecimal taxrate;
    /**
    * 税额
    */
    @ApiModelProperty("税额")
    private BigDecimal taxamt;
    /**
    * 合同变更后总金额（含税）
    */
    @ApiModelProperty("合同变更后总金额（含税）")
    private BigDecimal hsbgamt;

    @ApiModelProperty("履约进度")
    private String performschedule;
    /**
     * (租赁信息)
     */
    @ApiModelProperty("(租赁信息)")
    private String zlxx;
    /**
     * (合同对方信息)
     */
    @ApiModelProperty("(合同对方信息)")
    private String htdfxx;
    /**
     * (签约单位)
     */
    @ApiModelProperty("(签约单位)")
    private String qydws;
    /**
    * (特征信息)
    */
    @ApiModelProperty("(特征信息)")
    private String tzxx;
    /**
    * (合同支付信息:支付方信息)
    */
    @ApiModelProperty("(合同支付信息:支付方信息)")
    private String fkdwxx;
    /**
    * (合同支付信息:收入方信息)
    */
    @ApiModelProperty("(合同支付信息:收入方信息)")
    private String skdwxx;
    /**
     * (合同市拓投模数据)
     */
    @ApiModelProperty("(合同市拓投模数据)")
    private String cttmsj;
    /**
    * (保证金信息)
    */
    @ApiModelProperty("(保证金信息)")
    private String bzjxx;
    /**
    * (审批信息)
    */
    @ApiModelProperty("(审批信息)")
    private String spxx;
    /**
    * (附件信息)
    */
    @ApiModelProperty("(附件信息)")
    private String fjxx;

    /**
     * (PDF附件信息)
     */
    @ApiModelProperty("(PDF附件信息)")
    private String pdffjxx;

    @ApiModelProperty("(保险信息)")
    private String bxxx;

    @ApiModelProperty("(担保信息)")
    private String dbxx;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人
    */
    @ApiModelProperty("操作人")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

    @ApiModelProperty("履约主体")
    private String sjlydw;

    @ApiModelProperty("履约主体id")
    private String sjlydwid;

    @ApiModelProperty("履约主体翻译")
    private String sjlydwidname;

    @ApiModelProperty("合同履行地(省)中文")
    private String conperformprovincesname;


    @ApiModelProperty("合同履行地(市)中文")
    private String conperformcityname;

    @ApiModelProperty("合同履行地(县)中文")
    private String conperformxianname;


    @ApiModelProperty("是否有授权人")
    private String issqr;

    @ApiModelProperty("授权人（多个授权人姓名和身份证号信息）")
    private String authorizedname;



}
