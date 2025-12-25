package com.wishare.contract.apps.remote.vo;


import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 客商基础信息返回数据
 *
 * @author yancao
 */
@Data
@ApiModel(value = "客商基础信息返回数据")
public class MerchantRv {

    @ApiModelProperty(value = "客商id")
    private Long id;

    @ApiModelProperty(value = "客商编码")
    private String code;

    @ApiModelProperty(value = "客商名称")
    private String name;

    @ApiModelProperty(value = "客商简称")
    private String simpleName;

    @ApiModelProperty(value = "客商类型：1客户，2供应商，3客商")
    private String type;

    @ApiModelProperty(value = "客商标签：1增值类客户，2基础类客户，3增值类供应商，4基础类供应商")
    private List<String> labelList;

    @ApiModelProperty(value = "客商行业：1互联网，2房地产，3生成，4政府，5其他")
    private String industry;

    @ApiModelProperty(value = "服务类型：1工程类，2保安服务，3保洁服务，4绿化改造，5劳务外包")
    private List<String> serviceTypeList;

    @ApiModelProperty(value = "统一信用代码")
    private String creditCode;

    @ApiModelProperty(value = "纳税人资质：1一般纳税人，2小规模纳税人，3简易征收纳税人，4政府机关")
    private String taxpayerType;

    @ApiModelProperty(value = "法人代表信息")
    private String legalInfo;

    @ApiModelProperty(value = "成立时间")
    private LocalDate establishTime;

    @ApiModelProperty(value = "注册资本金")
    private BigDecimal registerCapital;

    @ApiModelProperty(value = "公司网址")
    private String companyWebsite;

    @ApiModelProperty(value = "区划地址")
    private List<Integer> districtAddressList;

    @ApiModelProperty(value = "详细联系地址")
    private List<String> detailedAddressList;

    @ApiModelProperty(value = "详细联系地址")
    private String detailedAddress;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "客商来源：1他人介绍，2自主开发")
    private String source;

    @ApiModelProperty(value = "合作状态：1初步沟通，2商务洽谈，3正式签约，4结束合作")
    private String cooperationStatus;

    @ApiModelProperty(value = "营业执照")
    private FileVo businessLicense;

    @ApiModelProperty(value = "客商资料")
    private List<FileVo> materialList;

    @ApiModelProperty(value = "客商介绍")
    private String introduction;

    @ApiModelProperty(value = "是否主数据同步：0否，1是")
    private String mainData;

    @ApiModelProperty(value = "租户id")
    private String tenantId;
}

