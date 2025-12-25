package com.wishare.finance.domains.configure.organization.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/7/26
 * @Description:
 */
@Getter
@Setter
public class UpdateStatutoryBodyCommand {

    /**
     * 法定单位id
     */
    private Long id;

    /**
     * 法定单位编码
     */
    private String code;

    /**
     * 法人代表名称
     */
    private String corporatorName;
    /**
     * 法定单位名称中文
     */
    private String nameCn;
    /**
     * 法定单位名称英文
     */
    private String nameEn;
    /**
     * 纳税人类别：1小规模纳税人；2一般纳税人；3简易征收纳税人；4政府机关
     */
    private Integer taxpayerType;
    /**
     * 纳税人识别号
     */
    private String taxpayerNo;
    /**
     * 营业地址
     */
    private String address;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 开户行账号
     */
    private String bankAccount;
    /**
     * 主管税务机关编码
     */
    private String taxAuthorityCode;
    /**
     * 主管税务机关名称
     */
    private String taxAuthority;
    /**
     * 关联组织
     */
    private Long orgId;
    /**
     * 关联组织名称
     */
    private String orgName;
    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;
    /**
     * 操作人ID
     */
    private String operator;
    /**
     * 修改人姓名
     */
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;
    /**
     * 是否与主数据库同步：0否 1是
     */
    private Integer dataSyn;
}
