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

import java.time.LocalDateTime;

/**
* <p>
* 客户-账户记录表 更新请求参数
* </p>
*
* @author chenglong
* @since 2023-07-0311
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "客户-账户记录表下拉列表请求参数", description = "客户-账户记录表")
public class CustomerAccountListF {

    /**
    * supplierId
    */
    @ApiModelProperty("关联供应商ID")
    @Length(message = "关联供应商ID不可超过 40 个字符",max = 40)
    private String supplierId;
    /**
    * natureCode
    */
    @ApiModelProperty("性质编码")
    private Integer natureCode;
    /**
    * nature
    */
    @ApiModelProperty("性质 1企业 2个人")
    @Length(message = "性质 1企业 2个人不可超过 40 个字符",max = 40)
    private String nature;
    /**
    * name
    */
    @ApiModelProperty("账户名称")
    @Length(message = "账户名称不可超过 50 个字符",max = 50)
    private String name;
    /**
    * creditCode
    */
    @ApiModelProperty("统一社会信用代码")
    @Length(message = "统一社会信用代码不可超过 50 个字符",max = 50)
    private String creditCode;
    /**
    * openBank
    */
    @ApiModelProperty("开户行")
    @Length(message = "开户行不可超过 100 个字符",max = 100)
    private String openBank;
    /**
    * accountNum
    */
    @ApiModelProperty("账号")
    @Length(message = "账号不可超过 50 个字符",max = 50)
    private String accountNum;
    /**
    * addressCode
    */
    @ApiModelProperty("地址省市区编码")
    @Length(message = "地址省市区编码不可超过 255 个字符",max = 255)
    private String addressCode;
    /**
    * addressDetail
    */
    @ApiModelProperty("地址详细信息")
    @Length(message = "地址详细信息不可超过 255 个字符",max = 255)
    private String addressDetail;
    /**
    * address
    */
    @ApiModelProperty("地址完整信息")
    @Length(message = "地址完整信息不可超过 255 个字符",max = 255)
    private String address;
    /**
    * phone
    */
    @ApiModelProperty("电话")
    @Length(message = "电话不可超过 30 个字符",max = 30)
    private String phone;
    /**
    * disabled
    */
    @ApiModelProperty("是否启用  0 开启 1 禁用")
    private Integer disabled;
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
    @ApiModelProperty("列表返回长度，不传入时默认20")
    private Integer limit;
    @ApiModelProperty("最后一个数据的ID，用于下拉时触发加载更多动作")
    private String indexId;

    /**
     * 主数据编码 外部编码
     */
    @ApiModelProperty("主数据编码 外部编码")
    private String mainDataCode;
}
