package com.wishare.finance.infrastructure.remote.vo.space;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fengxiaolin
 * @date 2023/3/21
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "archives_enterprise_base请求对象", description = "企业档案-基础信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivesEnterpriseBaseV {
    private Long id;
    @ApiModelProperty("企业名称")
    private String name;
    @ApiModelProperty("统一社会信用代码")
    private String creditCode;
    @ApiModelProperty("企业简称")
    private String simpleName;
    @ApiModelProperty("企业logo")
    private FileVo logo;
    @ApiModelProperty("营业执照")
    private FileVo businessLicense;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("区")
    private String district;
    @ApiModelProperty("地点名称")
    private String addressName;
    @ApiModelProperty("企业详细地址")
    private String addressDetail;
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    @ApiModelProperty("纬度")
    private BigDecimal latitude;
    @ApiModelProperty("企业电话")
    private String mobileNum;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("联系人姓名")
    private String contactor;
    @ApiModelProperty("联系人手机号码")
    private String contactorMobileNum;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("是否删除 false 未删除 null 删除")
    private Boolean deleted;
    @ApiModelProperty("创建人ID")
    private String creator;
    @ApiModelProperty("操作人ID")
    private String operator;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("修改人姓名")
    private String operatorName;

    @ApiModelProperty("资产列表")
    private List<String> spaceNameList;
}
