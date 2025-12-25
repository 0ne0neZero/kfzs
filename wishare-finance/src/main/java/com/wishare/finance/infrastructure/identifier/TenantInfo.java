package com.wishare.finance.infrastructure.identifier;

import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 租户信息
 *
 * @Author dxclay
 * @Date 2022/12/29
 * @Version 1.0
 */
@Getter
@Setter
public class TenantInfo {

    /**
     * 租户id
     */
    private String id;
    /**
     * 租户名称
     */
    private String name;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;
    /**
     * 启用禁用 0 启用 1 禁用
     */
    private Boolean disabled;
    /**
     * 业态 客户属性", notes = "酒店
     */
    private String nature;
    /**
     * 客户简称
     */
    private String shortName;
    /**
     * 客户英文简称
     */
    private String englishName;
    /**
     * 公司logoFileVo
     */
    private FileVo logoFileVo;
    /**
     * 公司logo
     */
    private String logo;

}
