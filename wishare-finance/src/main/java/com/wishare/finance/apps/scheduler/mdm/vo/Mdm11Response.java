package com.wishare.finance.apps.scheduler.mdm.vo;

import com.wishare.finance.domains.mdm.entity.Mdm11E;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


/**
 * @author longhuadmin
 */
@Data
@SuppressWarnings("all")
public class Mdm11Response {

    /**
     * 内码
     **/
    private String id;

    /**
     * 中文项目全称
     **/
    private String cusitem_fullname_chs;

    /**
     * 停用标记
     **/
    private String is_disabled;

    /**
     * 中文简称
     **/
    private String cusitem_name_chs;

    /**
     * 是否明细
     **/
    private Boolean treeinfo_is_detail;

    /**
     * 分级码
     **/
    private String treeinfo_path;

    /**
     * 项目编号
     **/
    private String cusitem_code;

    /**
     * 完工否
     **/
    private Boolean is_completed;

    /**
     * 项目类型
     **/
    private String cusitem_property;

    /**
     * 核算类别，即2.3.6中的id
     **/
    private String cusitem_category;

    /**
     * 级数
     **/
    private Integer treeinfo_layer;

    /**
     * 密级
     **/
    private String security_level;

    /**
     * 自定义数值01-09
     **/
    private Double def_number_col_01;
    private Double def_number_col_02;
    private Double def_number_col_03;
    private Double def_number_col_04;
    private Double def_number_col_05;
    private Double def_number_col_06;
    private Double def_number_col_07;
    private Double def_number_col_08;
    private Double def_number_col_09;

    /**
     * 最后修改时间
     **/
    private String last_modified_time;

    public Mdm11E transfer2Mdm11E() {
        Mdm11E mdm11E = new Mdm11E();
        mdm11E.setId(this.id);
        mdm11E.setCusitemFullnameChs(this.cusitem_fullname_chs);
        mdm11E.setIsDisabled(this.is_disabled);
        mdm11E.setCusitemNameChs(this.cusitem_name_chs);
        mdm11E.setTreeinfoIsDetail(this.treeinfo_is_detail);
        mdm11E.setTreeinfoPath(this.treeinfo_path);
        mdm11E.setCusitemCode(this.cusitem_code);
        mdm11E.setIsCompleted(this.is_completed);
        mdm11E.setCusitemProperty(this.cusitem_property);
        mdm11E.setCusitemCategory(this.cusitem_category);
        mdm11E.setTreeinfoLayer(this.treeinfo_layer);
        mdm11E.setSecurityLevel(this.security_level);
        mdm11E.setDefNumberCol01(this.def_number_col_01);
        mdm11E.setDefNumberCol02(this.def_number_col_02);
        mdm11E.setDefNumberCol03(this.def_number_col_03);
        mdm11E.setDefNumberCol04(this.def_number_col_04);
        mdm11E.setDefNumberCol05(this.def_number_col_05);
        mdm11E.setDefNumberCol06(this.def_number_col_06);
        mdm11E.setDefNumberCol07(this.def_number_col_07);
        mdm11E.setDefNumberCol08(this.def_number_col_08);
        mdm11E.setDefNumberCol09(this.def_number_col_09);
        mdm11E.setLastModifiedTime(this.last_modified_time);
        return mdm11E;
    }
}
