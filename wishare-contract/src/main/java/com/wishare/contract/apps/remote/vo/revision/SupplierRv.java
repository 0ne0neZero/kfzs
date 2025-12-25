package com.wishare.contract.apps.remote.vo.revision;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  15:04
 */
@Data
@ApiModel(value = "供应商基础信息返回数据")
public class SupplierRv {

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private String id;
    /**
     * 供应商编码
     */
    @ApiModelProperty("供应商编码")
    private String code;
    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String name;
    /**
     * 供应商性质 1企业 2个人
     */
    @ApiModelProperty("供应商性质 1企业 2个人")
    private Integer nature;
    /**
     * 服务类型编码
     */
    @ApiModelProperty("服务类型编码")
    private String serviceTypeCode;
    /**
     * 服务类型：1工程类，2保安服务，3保洁服务，4绿化改造，5劳务外包
     */
    @ApiModelProperty("服务类型：1工程类，2保安服务，3保洁服务，4绿化改造，5劳务外包")
    private String serviceType;
    /**
     * 供应商来源编码
     */
    @ApiModelProperty("供应商来源编码")
    private String sourceCode;
    /**
     * 供应商来源(1市场拓展 2自然来访 3内部推荐)
     */
    @ApiModelProperty("供应商来源(1市场拓展 2自然来访 3内部推荐)")
    private String source;
    /**
     * 级别编码
     */
    @ApiModelProperty("级别编码")
    private String levelCode;
    /**
     * 级别名称
     */
    @ApiModelProperty("级别名称")
    private String level;
    /**
     * 合同数量
     */
    @ApiModelProperty("合同数量")
    private Integer contractNum;
    /**
     * 合作状态(1合作中 2未合作 3已结束)
     */
    @ApiModelProperty("合作状态(1合作中 2未合作 3已结束)")
    private Integer coopStatus;
    /**
     * 是否启用  0 开启 1 禁用
     */
    @ApiModelProperty("是否启用  0 开启 1 禁用")
    private Integer disabled;
    /**
     * 纳税人资质编码
     */
    @ApiModelProperty("纳税人资质编码")
    private Integer taxpayerTypeCode;
    /**
     * 纳税人识别号
     */
    @ApiModelProperty("纳税人识别号")
    private String taxpayerCode;
    /**
     * 企业类型编码
     */
    @ApiModelProperty("企业类型编码")
    private String enterpriseTypeCode;
    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private String enterpriseType;
    /**
     * 纳税人资质：1一般纳税人，2小规模纳税人，3简易征收纳税人，4政府机关
     */
    @ApiModelProperty("纳税人资质：1一般纳税人，2小规模纳税人，3简易征收纳税人，4政府机关")
    private String taxpayerType;
    /**
     * 法人代表名称
     */
    @ApiModelProperty("法人代表名称")
    private String legalManName;
    /**
     * 注册资本金
     */
    @ApiModelProperty("注册资本金")
    private BigDecimal registerCapital;
    /**
     * 注册资本金Show
     */
    @ApiModelProperty("注册资本金Show")
    private String registerCapitalShow;
    /**
     * 公司电话
     */
    @ApiModelProperty("公司电话")
    private String phone;
    /**
     * 经营范围
     */
    @ApiModelProperty("经营范围")
    private String serviceRange;
    /**
     * 省市区code集合
     */
    @ApiModelProperty("省市区code集合")
    private List<String> addressCodeList;
    /**
     * 注册地址省市区编码
     */
    @ApiModelProperty("注册地址省市区编码")
    private String registerAddressCode;
    /**
     * 注册地址详细信息
     */
    @ApiModelProperty("注册地址详细信息")
    private String registerAddressDetail;
    /**
     * 注册地址完整信息
     */
    @ApiModelProperty("注册地址完整信息")
    private String registerAddress;
    /**
     * 个人-证件类型编码
     */
    @ApiModelProperty("个人-证件类型编码")
    private String licenseTypeCode;
    /**
     * 个人-证件类型
     */
    @ApiModelProperty("个人-证件类型")
    private String licenseType;
    /**
     * 个人-证件号码
     */
    @ApiModelProperty("个人-证件号码")
    private String licenseNum;
    /**
     * 个人-性别 ( 男 or 女 )
     */
    @ApiModelProperty("个人-性别 ( 男 or 女 )")
    private String sex;
    /**
     * 个人-出生日期
     */
    @ApiModelProperty("个人-出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    /**
     * 个人-年龄
     */
    @ApiModelProperty("个人-年龄")
    private Integer age;
    /**
     * 个人-联系电话
     */
    @ApiModelProperty("个人-联系电话")
    private String personPhone;
    /**
     * 个人-联系地址省市区编码
     */
    @ApiModelProperty("个人-联系地址省市区编码")
    private String contactAddressCode;
    /**
     * 个人-联系地址详细信息
     */
    @ApiModelProperty("个人-联系地址详细信息")
    private String contactAddressDetail;
    /**
     * 个人-联系地址完整信息
     */
    @ApiModelProperty("个人-联系地址完整信息")
    private String contactAddress;
    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
     * 营业期限（开始）
     */
    @ApiModelProperty("营业期限（开始）")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate busStartTime;
    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private String creditCode;
    /**
     * 营业期限（结束）
     */
    @ApiModelProperty("营业期限（结束）")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate busEndTime;


    @ApiModelProperty("主数据编码")
    private String mainDataCode;

}
