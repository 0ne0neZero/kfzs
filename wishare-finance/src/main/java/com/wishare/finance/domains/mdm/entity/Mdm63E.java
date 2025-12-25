package com.wishare.finance.domains.mdm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mdm63")
public class Mdm63E {

    /**
     * 应收应付明细ID
     **/
    @TableId
    private String ftId;

    /**
     * id
     **/
    private String id;

    /**
     * 单据编号-财务云单据编码
     **/
    private String billNum;

    /**
     * 业务日期
     **/
    private LocalDate bizDate;

    /**
     * 到期日期
     **/
    private LocalDate dqRq;

    /**
     * 应收/应付 AR:应收；AP：应付
     **/
    private String arapModule;

    /**
     * 往来单位ID-财务云往来单位id
     **/
    private String partnerId;

    /**
     * 往来单位编码-主数据往来单位编码
     **/
    private String partnerCode;

    /**
     * 合同ID-财务云合同ID
     **/
    private String contractId;

    /**
     * 合同编码-主数据合同编码
     **/
    private String contractCode;

    /**
     * 项目ID-财务云项目ID
     **/
    private String projectInfoId;

    /**
     * 项目编码-主数据项目编码
     **/
    private String projectInfoCode;

    /**
     * 业务科目ID-MDM17
     **/
    private String fundsPropId;

    /**
     * 已核销金额
     **/
    private BigDecimal clearAmount;

    /**
     * 未核销金额
     **/
    private BigDecimal dhxJe;

    /**
     * 金额/核销总额
     **/
    private BigDecimal amount;

    /**
     * 最后修改时间
     **/
    private Date lastModifiedTime;


    /**
     * 4A组织ID
     **/
    private String organizationId;

    /**
     * 款项性质名称
     **/
    private String fundsPropName;

    /**
     * 原币编号
     **/
    private String ybBh;

    /**
     * 本位币编号
     **/
    private String bwBbh;

    /**
     * 金额/核销总额（原币）
     **/
    private BigDecimal foreignCurrency;

    /**
     * 待核销金额（原币）
     **/
    private BigDecimal dhxJeYb;

    /**
     * 核销金额（原币）
     **/
    private BigDecimal hxJeYb;

    /**
     * 来源单据编号
     **/
    private String sourceBillCode;

    /**
     * 来源单据内码
     **/
    private String sourceBillId;

    /**
     * 摘要
     **/
    private String summary;
}
