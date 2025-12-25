package com.wishare.finance.domains.mdm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 现金流量
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mdm11")
public class Mdm11E implements Serializable {

    /**
     * 内码
     **/
    private String id;

    /**
     * 中文项目全称
     **/
    private String cusitemFullnameChs;

    /**
     * 停用标记
     **/
    private String isDisabled;

    /**
     * 中文简称
     **/
    private String cusitemNameChs;

    /**
     * 是否明细
     **/
    private Boolean treeinfoIsDetail;

    /**
     * 分级码
     **/
    private String treeinfoPath;

    /**
     * 项目编号
     **/
    private String cusitemCode;

    /**
     * 完工否
     **/
    private Boolean isCompleted;

    /**
     * 项目类型
     **/
    private String cusitemProperty;

    /**
     * 核算类别，即2.3.6中的id
     **/
    private String cusitemCategory;

    /**
     * 级数
     **/
    private Integer treeinfoLayer;

    /**
     * 密级
     **/
    private String securityLevel;

    /**
     * 自定义数值01-09
     **/
    private Double defNumberCol01;
    private Double defNumberCol02;
    private Double defNumberCol03;
    private Double defNumberCol04;
    private Double defNumberCol05;
    private Double defNumberCol06;
    private Double defNumberCol07;
    private Double defNumberCol08;
    private Double defNumberCol09;

    /**
     * 最后修改时间
     **/
    private String lastModifiedTime;
}
