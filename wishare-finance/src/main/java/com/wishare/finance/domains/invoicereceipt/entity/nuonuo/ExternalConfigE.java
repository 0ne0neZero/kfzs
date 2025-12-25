package com.wishare.finance.domains.invoicereceipt.entity.nuonuo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 第三方配置表(ExternalConfig)实体类
 *
 * @author makejava
 * @since 2022-08-09 10:14:15
 */
@Getter
@Setter
@TableName("external_config")
public class ExternalConfigE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 第三方服务前缀（如：yonyou，nuonuo）
     */
    private String externalPrefix;
    /**
     * 过滤字段
     */
    private String filter;
    /**
     * 参数配置信息
     */
    private String configJson;
    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 创建时间
     */
    private Date gmtCreate;
}

