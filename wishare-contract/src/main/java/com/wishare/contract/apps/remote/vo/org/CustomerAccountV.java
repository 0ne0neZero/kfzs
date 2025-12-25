package com.wishare.contract.apps.remote.vo.org;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
* <p>
* 客户-账户记录表视图对象
* </p>
*
* @author chenglong
* @since 2023-07-031
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "客户-账户记录表视图对象", description = "客户-账户记录表视图对象")
public class CustomerAccountV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 关联客户ID
    */
    @ApiModelProperty("关联客户ID")
    private String supplierId;
    /**
    * 性质编码
    */
    @ApiModelProperty("性质编码")
    private Integer natureCode;
    /**
    * 性质 1企业 2个人
    */
    @ApiModelProperty("性质 1企业 2个人")
    private String nature;
    /**
    * 抬头
    */
    @ApiModelProperty("抬头")
    private String name;
    /**
     * 纳税人识别号 不可为空
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxCode;

    /**
     * AddressV
     */
    @ApiModelProperty(value = "地址信息AddressV")
    private String addressV;
    /**
    * 统一社会信用代码
    */
    @ApiModelProperty("统一社会信用代码")
    private String creditCode;
    /**
    * 开户行
    */
    @ApiModelProperty("开户行")
    private String openBank;
    /**
    * 账号
    */
    @ApiModelProperty("账号")
    private String accountNum;
    /**
    * 地址省市区编码
    */
    @ApiModelProperty("地址省市区编码")
    private String addressCode;
    /**
    * 地址详细信息
    */
    @ApiModelProperty("地址详细信息")
    private String addressDetail;
    /**
    * 电话
    */
    @ApiModelProperty("电话")
    private String phone;
    /**
    * 是否启用  0 开启 1 禁用
    */
    @ApiModelProperty("是否启用  0 开启 1 禁用")
    private Integer disabled;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 创建人ID
    */
    @ApiModelProperty("创建人ID")
    private String creator;
    /**
    * 创建人姓名
    */
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * 操作人ID
    */
    @ApiModelProperty("操作人ID")
    private String operator;
    /**
    * 操作人姓名
    */
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    /**
     * 是否集团内单位0否1是
     */
    @ApiModelProperty("是否集团内单位0否1是")
    private String isInner;
    /**
     * 账户状态正常冻结其它
     */
    @ApiModelProperty("账户状态正常冻结其它")
    private String accountStatus;
    /**
     * 金融机构名称
     */
    @ApiModelProperty("金融机构名称")
    private String openBankName;
    /**
     * 往来单位名称
     */
    @ApiModelProperty("往来单位名称")
    private String supplierName;

    private String mainDataCode;

}
