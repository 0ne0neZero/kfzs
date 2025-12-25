package com.wishare.contract.domains.entity.merchant;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 客商基础信息表
 * </p>
 *
 * @author yancao
 * @since 2022-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("merchant")
public class MerchantE implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客商id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 客商编码
     */
    private String code;

    /**
     * 客商名称
     */
    private String name;

    /**
     * 客商简称
     */
    private String simpleName;

    /**
     * 客商类型：1客户，2供应商，3客商
     */
    private Integer type;

    /**
     * 客商标签：1增值类客户，2基础类客户，3增值类供应商，4基础类供应商
     */
    private String label;

    /**
     * 客商行业：1互联网，2房地产，3生成，4政府，5其他
     */
    private Integer industry;

    /**
     * 服务类型：1工程类，2保安服务，3保洁服务，4绿化改造，5劳务外包
     */
    private String serviceType;

    /**
     * 统一信用代码
     */
    private String creditCode;

    /**
     * 纳税人资质：1一般纳税人，2小规模纳税人，3简易征收纳税人，4政府机关
     */
    private Integer taxpayerType;

    /**
     * 法人代表信息
     */
    private String legalInfo;

    /**
     * 成立时间
     */
    private LocalDate establishTime;

    /**
     * 注册资本金
     */
    private BigDecimal registerCapital;

    /**
     * 公司网址
     */
    private String companyWebsite;

    /**
     * 区划地址
     */
    private String districtAddress;

    /**
     * 详细联系地址
     */
    private String detailedAddress;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 客商来源：1他人介绍，2自主开发
     */
    private Integer source;

    /**
     * 合作状态：1初步沟通，2商务洽谈，3正式签约，4结束合作
     */
    private Integer cooperationStatus;

    /**
     * 客商资料
     */
    private String material;

    /**
     * 客商介绍
     */
    private String introduction;

    /**
     * 是否主数据同步：0否，1是
     */
    private Integer mainData;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;


}
