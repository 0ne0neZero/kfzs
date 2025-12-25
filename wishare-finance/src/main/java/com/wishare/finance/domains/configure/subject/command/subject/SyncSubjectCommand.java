package com.wishare.finance.domains.configure.subject.command.subject;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 同步科目命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/13
 */
@Getter
@Setter
@ApiModel("同步科目信息")
public class SyncSubjectCommand {

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 是否叶子节点：0否，1是
     */
    private Integer leaf;

    /**
     * 科目路径
     */
    private String path;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 科目体系id
     */
    private Long subjectSystemId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 父科目id
     */
    private Long parentId;

    /**
     * 辅助核算
     */
    private String auxiliaryCount;

    /**
     * 是否税费科目 0否 1是 默认0
     */
    private Integer existTax;

    /**
     * 是否启用：0未启用，1启用
     */
    private Integer disabled;
    /**
     * 数据来源id
     */
    private String idExt;

}
