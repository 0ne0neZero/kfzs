package com.wishare.contract.apps.remote.fo.org;


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

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
*  更新请求参数1
* </p>
*
* @author chenglong
* @since 2023-07-031
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "下拉列表请求参数", description = "")
public class CustomerListF {

    /**
    * code
    */
    @ApiModelProperty("客户编码")
    @Length(message = "客户编码不可超过 40 个字符",max = 40)
    private String code;
    /**
    * name
    */
    @ApiModelProperty("客户名称")
    @Length(message = "客户名称不可超过 100 个字符",max = 100)
    private String name;
    /**
    * nature
    */
    @ApiModelProperty("客户性质 1企业 2个人")
    private Integer nature;
    /**
    * busTypeCode
    */
    @ApiModelProperty("行业分类编码")
    private Long busTypeCode;
    /**
    * busType
    */
    @ApiModelProperty("行业分类")
    @Length(message = "行业分类不可超过 50 个字符",max = 50)
    private String busType;
    /**
    * brandCode
    */
    @ApiModelProperty("客户品牌编码")
    @Length(message = "客户品牌编码不可超过 40 个字符",max = 40)
    private String brandCode;
    /**
    * brand
    */
    @ApiModelProperty("客户品牌")
    @Length(message = "客户品牌不可超过 50 个字符",max = 50)
    private String brand;
    /**
    * sourceCode
    */
    @ApiModelProperty("客户来源编码")
    @Length(message = "客户来源编码不可超过 20 个字符",max = 20)
    private String sourceCode;
    /**
    * source
    */
    @ApiModelProperty("客户来源(1市场拓展 2自然来访 3内部推荐)")
    @Length(message = "客户来源(1市场拓展 2自然来访 3内部推荐)不可超过 50 个字符",max = 50)
    private String source;
    /**
    * levelCode
    */
    @ApiModelProperty("级别编码")
    @Length(message = "级别编码不可超过 20 个字符",max = 20)
    private String levelCode;
    /**
    * level
    */
    @ApiModelProperty("级别名称")
    @Length(message = "级别名称不可超过 50 个字符",max = 50)
    private String level;
    /**
    * chanceNum
    */
    @ApiModelProperty("商机数量")
    private Integer chanceNum;
    /**
    * dealStatus
    */
    @ApiModelProperty("是否成交:false未成交， true已成交")
    private Boolean dealStatus;
    /**
    * coopStatus
    */
    @ApiModelProperty("合作状态(1合作中 2未合作  3已结束)")
    private Integer coopStatus;
    /**
    * disabled
    */
    @ApiModelProperty("是否启用  0 开启 1 禁用")
    private Integer disabled;
    /**
    * taxpayerTypeCode
    */
    @ApiModelProperty("纳税人资质编码")
    private Integer taxpayerTypeCode;
    /**
    * taxpayerCode
    */
    @ApiModelProperty("纳税人识别号")
    @Length(message = "纳税人识别号不可超过 255 个字符",max = 255)
    private String taxpayerCode;
    /**
    * enterpriseTypeCode
    */
    @ApiModelProperty("企业类型编码")
    @Length(message = "企业类型编码不可超过 40 个字符",max = 40)
    private String enterpriseTypeCode;
    /**
    * enterpriseType
    */
    @ApiModelProperty("企业类型")
    @Length(message = "企业类型不可超过 50 个字符",max = 50)
    private String enterpriseType;
    /**
    * taxpayerType
    */
    @ApiModelProperty("纳税人资质：1一般纳税人，2小规模纳税人，3简易征收纳税人，4政府机关")
    @Length(message = "纳税人资质：1一般纳税人，2小规模纳税人，3简易征收纳税人，4政府机关不可超过 50 个字符",max = 50)
    private String taxpayerType;
    /**
    * legalManName
    */
    @ApiModelProperty("法人代表名称")
    @Length(message = "法人代表名称不可超过 50 个字符",max = 50)
    private String legalManName;
    /**
    * registerCapital
    */
    @ApiModelProperty("注册资本金")
    @Digits(integer = 18,fraction =2,message = "注册资本金不正确")
    private BigDecimal registerCapital;
    /**
    * registerDate
    */
    @ApiModelProperty("注册日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registerDate;
    /**
    * phone
    */
    @ApiModelProperty("公司电话")
    @Length(message = "公司电话不可超过 50 个字符",max = 50)
    private String phone;
    /**
    * serviceRange
    */
    @ApiModelProperty("经营范围")
    @Length(message = "经营范围不可超过 1,000 个字符",max = 1000)
    private String serviceRange;
    /**
    * registerAddressCode
    */
    @ApiModelProperty("注册地址省市区编码")
    @Length(message = "注册地址省市区编码不可超过 255 个字符",max = 255)
    private String registerAddressCode;
    /**
    * registerAddressDetail
    */
    @ApiModelProperty("注册地址详细信息")
    @Length(message = "注册地址详细信息不可超过 255 个字符",max = 255)
    private String registerAddressDetail;
    /**
    * registerAddress
    */
    @ApiModelProperty("注册地址完整信息")
    @Length(message = "注册地址完整信息不可超过 255 个字符",max = 255)
    private String registerAddress;
    /**
    * licenseTypeCode
    */
    @ApiModelProperty("个人-证件类型编码")
    @Length(message = "个人-证件类型编码不可超过 40 个字符",max = 40)
    private String licenseTypeCode;
    /**
    * licenseType
    */
    @ApiModelProperty("个人-证件类型")
    @Length(message = "个人-证件类型不可超过 50 个字符",max = 50)
    private String licenseType;
    /**
    * licenseNum
    */
    @ApiModelProperty("个人-证件号码")
    @Length(message = "个人-证件号码不可超过 50 个字符",max = 50)
    private String licenseNum;
    /**
    * sex
    */
    @ApiModelProperty("个人-性别 ( 男 or 女 )")
    @Length(message = "个人-性别 ( 男 or 女 )不可超过 10 个字符",max = 10)
    private String sex;
    /**
    * birthDate
    */
    @ApiModelProperty("个人-出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    /**
    * age
    */
    @ApiModelProperty("个人-年龄")
    private Integer age;
    /**
    * personPhone
    */
    @ApiModelProperty("个人-联系电话")
    @Length(message = "个人-联系电话不可超过 50 个字符",max = 50)
    private String personPhone;
    /**
    * contactAddressCode
    */
    @ApiModelProperty("个人-联系地址省市区编码")
    @Length(message = "个人-联系地址省市区编码不可超过 255 个字符",max = 255)
    private String contactAddressCode;
    /**
    * contactAddressDetail
    */
    @ApiModelProperty("个人-联系地址详细信息")
    @Length(message = "个人-联系地址详细信息不可超过 255 个字符",max = 255)
    private String contactAddressDetail;
    /**
    * contactAddress
    */
    @ApiModelProperty("个人-联系地址完整信息")
    @Length(message = "个人-联系地址完整信息不可超过 255 个字符",max = 255)
    private String contactAddress;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * creator
    */
    @ApiModelProperty("创建人ID")
    @Length(message = "创建人ID不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人姓名")
    @Length(message = "创建人姓名不可超过 40 个字符",max = 40)
    private String creatorName;
    /**
    * gmtModify
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * operator
    */
    @ApiModelProperty("操作人ID")
    @Length(message = "操作人ID不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * operatorName
    */
    @ApiModelProperty("操作人姓名")
    @Length(message = "操作人姓名不可超过 40 个字符",max = 40)
    private String operatorName;
    /**
    * busStartTime
    */
    @ApiModelProperty("营业期限（开始）")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate busStartTime;
    /**
    * creditCode
    */
    @ApiModelProperty("统一社会信用代码")
    @Length(message = "统一社会信用代码不可超过 40 个字符",max = 40)
    private String creditCode;
    /**
    * busEndTime
    */
    @ApiModelProperty("营业期限（结束）")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate busEndTime;
    @ApiModelProperty("列表返回长度，不传入时默认20")
    private Integer limit;
    @ApiModelProperty("最后一个数据的ID，用于下拉时触发加载更多动作")
    private Long indexId;

    /**
     * 主数据编码 外部编码
     */
    @ApiModelProperty("主数据编码 外部编码")
    private String mainDataCode;
}
