package com.wishare.finance.infrastructure.remote.vo.user;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Copyright @ 2022 慧享科技 Co. Ltd.
 * All right reserved.
 * <p>
 * 企业信息
 * </p>
 *
 * @author:PengAn
 * @since:2022-09-27
 * @description:
 **/
@Accessors(chain = true)
@ApiModel(value = "enterprise_base_info请求对象", description = "企业信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseBaseInfoRV {
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
}
