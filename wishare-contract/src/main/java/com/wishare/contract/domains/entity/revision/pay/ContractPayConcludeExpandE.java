package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Arrays;
/**
 * <p>
 * 支出合同订立信息拓展表
 * </p>
 *
 * @author chenglong
 * @since 2023-09-24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_pay_conclude_expand")
public class ContractPayConcludeExpandE {

    /**
     * 支出合同订立信息拓展表id
     */
    @TableId(value = ID)
    private Long id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同语言
     */
    private String conlanguage;

    /**
     * 合同名称
     */
    private String conname;

    /**
     * 是否补充合同
     */
    private String issupplycontract;

    /**
     * 内部合同关联校验码
     */
    private String oppositeconmaincode;

    /**
     * 收支类型
     */
    private String incomeexpendtype;

    /**
     * 合同履行地(国家)
     */
    private String conperformcountry;

    /**
     * 合同履行地(省)
     */
    private String conperformprovinces;

    /**
     * 合同履行地(市)
     */
    private String conperformcity;

    /**
     * 合同履行地(县)
     */
    private String conperformxian;

    private String sjlydw;

    private String sjlydwid;

    private String sjlydwidname;

    private String conperformprovincesname;

    private String conperformcityname;

    private String conperformxianname;

    /**
     * 争议解决方式
     */
    private String disputesolutionway;

    /**
     * 合同范本使用情况
     */
    private String conmodelusecondition;

    /**
     * 合同承办人id
     */
    private String conundertakerid;

    /**
     * 合同承办人姓名
     */
    private String conundertaker;

    /**
     * 合同承办人联系方式
     */
    private String conundertakercontact;

    /**
     * 税率
     */
    private BigDecimal taxrate;

    /**
     * 税额
     */
    private BigDecimal taxamt;

    /**
     * 合同变更后总金额（含税）
     */
    private BigDecimal hsbgamt;

    /**
     * 履约进度
     */
    private String performschedule;

    /**
     * (担保信息)
     */
    private String dbxx;

    /**
     * (租赁信息)
     */
    private String zlxx;

    /**
     * (保险信息)
     */
    private String bxxx;

    /**
     * (合同对方信息)
     */
    private String htdfxx;

    /**
     * (签约单位信息)
     */
    private String qydws;

    /**
     * (特征信息)
     */
    private String tzxx;

    /**
     * (合同支付信息:支付方信息)
     */
    private String fkdwxx;

    /**
     * (合同支付信息:收入方信息)
     */
    private String skdwxx;

    /**
     * (保证金信息)
     */
    private String bzjxx;

    /**
     * (审批信息)
     */
    private String spxx;

    /**
     * (附件信息)
     */
    private String fjxx;

    /**
     * (PDF附件信息)
     */
    private String pdffjxx;

    /**
     * (合同管理类别)
     */
    private String conmanagetype;

    /**
     * (增值业务类型)
     */
    private String conincrementype;

    /**
     * (服务类型)
     */
    private String fwlb;

    /**
     * (是否满足集团招商刻度0是1否)
     */
    private Integer fmzjtzskd;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;



    @ApiModelProperty("是否有授权人")
    private String issqr;

    @ApiModelProperty("授权人（多个授权人姓名和身份证号信息）")
    private String authorizedname;



    public static final String ISSQR = "issqr";

    public static final String AUTHORIZEDNAME = "authorizedname";



    /**
    * 支出合同订立信息拓展表id
    */
    public static final String ID = "id";

    /**
     * 合同id
     */
    public static final String CONTRACTID = "contractId";

    /**
    * 合同语言
    */
    public static final String CONLANGUAGE = "conlanguage";

    /**
    * 合同名称
    */
    public static final String CONNAME = "conname";

    /**
    * 是否补充合同
    */
    public static final String ISSUPPLYCONTRACT = "issupplycontract";

    /**
    * 内部合同关联校验码
    */
    public static final String OPPOSITECONMAINCODE = "oppositeconmaincode";

    /**
    * 收支类型
    */
    public static final String INCOMEEXPENDTYPE = "incomeexpendtype";

    /**
    * 合同履行地(国家)
    */
    public static final String CONPERFORMCOUNTRY = "conperformcountry";

    /**
    * 合同履行地(省)
    */
    public static final String CONPERFORMPROVINCES = "conperformprovinces";

    /**
    * 合同履行地(市)
    */
    public static final String CONPERFORMCITY = "conperformcity";

    /**
    * 合同履行地(县)
    */
    public static final String CONPERFORMXIAN = "conperformxian";


    /**
     * 合同履行地(省)
     */
    public static final String CONPERFORMPROVINCESNAME = "conperformprovincesname";

    /**
     * 合同履行地(市)
     */
    public static final String CONPERFORMCITYNAME = "conperformcityname";

    /**
     * 合同履行地(县)
     */
    public static final String CONPERFORMXIANNAME = "conperformxianname";


    public static final String SJLYDWID = "sjlydwid";

    public static final String SJLYDWIDNAME = "sjlydwidname";

    /**
    * 争议解决方式
    */
    public static final String DISPUTESOLUTIONWAY = "disputesolutionway";

    /**
    * 合同范本使用情况
    */
    public static final String CONMODELUSECONDITION = "conmodelusecondition";

    /**
    * 合同承办人id
    */
    public static final String CONUNDERTAKERID = "conundertakerid";

    /**
    * 合同承办人姓名
    */
    public static final String CONUNDERTAKER = "conundertaker";

    /**
    * 合同承办人联系方式
    */
    public static final String CONUNDERTAKERCONTACT = "conundertakercontact";

    /**
    * 税率
    */
    public static final String TAXRATE = "taxrate";

    /**
    * 税额
    */
    public static final String TAXAMT = "taxamt";

    /**
    * 合同变更后总金额（含税）
    */
    public static final String HSBGAMT = "hsbgamt";

    /**
    * 履约进度
    */
    public static final String PERFORMSCHEDULE = "performschedule";

    /**
    * (担保信息)
    */
    public static final String DBXX = "dbxx";

    /**
    * (租赁信息)
    */
    public static final String ZLXX = "zlxx";

    /**
    * (保险信息)
    */
    public static final String BXXX = "bxxx";

    /**
    * (合同对方信息)
    */
    public static final String HTDFXX = "htdfxx";

    /**
    * (特征信息)
    */
    public static final String TZXX = "tzxx";

    /**
    * (合同支付信息:支付方信息)
    */
    public static final String FKDWXX = "fkdwxx";

    /**
    * (合同支付信息:收入方信息)
    */
    public static final String SKDWXX = "skdwxx";

    /**
    * (保证金信息)
    */
    public static final String BZJXX = "bzjxx";

    /**
    * (审批信息)
    */
    public static final String SPXX = "spxx";

    /**
    * (附件信息)
    */
    public static final String FJXX = "fjxx";

    /**
    * 创建人
    */
    public static final String CREATOR = "creator";

    /**
    * 创建人名称
    */
    public static final String CREATOR_NAME = "creatorName";

    /**
    * 创建时间
    */
    public static final String GMT_CREATE = "gmtCreate";

    /**
    * 操作人
    */
    public static final String OPERATOR = "operator";

    /**
    * 操作人名称
    */
    public static final String OPERATOR_NAME = "operatorName";

    /**
    * 操作时间
    */
    public static final String GMT_MODIFY = "gmtModify";

    /**
    * 是否删除  0 正常 1 删除
    */
    public static final String DELETED = "deleted";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CONTRACTID,
                                    CONLANGUAGE,
                                    CONNAME,
                                    ISSUPPLYCONTRACT,
                                    OPPOSITECONMAINCODE,
                                    INCOMEEXPENDTYPE,
                                    CONPERFORMCOUNTRY,
                                    CONPERFORMPROVINCES,
                                    CONPERFORMCITY,
                                    CONPERFORMXIAN,
                                    DISPUTESOLUTIONWAY,
                                    CONMODELUSECONDITION,
                                    CONUNDERTAKERID,
                                    CONUNDERTAKER,
                                    CONUNDERTAKERCONTACT,
                                    TAXRATE,
                                    TAXAMT,
                                    HSBGAMT,
                                    PERFORMSCHEDULE,
                                    CONPERFORMPROVINCESNAME,
                                    CONPERFORMCITYNAME,
                                    CONPERFORMXIANNAME,
                                    SJLYDWID,
                                    SJLYDWIDNAME,
                                    ISSQR,
                                    AUTHORIZEDNAME,
                                    DBXX,
                                    ZLXX,
                                    BXXX,
                                    HTDFXX,
                                    TZXX,
                                    FKDWXX,
                                    SKDWXX,
                                    BZJXX,
                                    SPXX,
                                    FJXX,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    DELETED);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
