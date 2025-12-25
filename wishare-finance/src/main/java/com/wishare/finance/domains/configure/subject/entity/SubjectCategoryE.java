package com.wishare.finance.domains.configure.subject.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 科目类型
 *
 * @author yancao
 */
@Getter
@Setter
@TableName("subject_category")
public class SubjectCategoryE {

    /**
     * 科目类型id
     */
    @TableId
    private Long id;

    /**
     * 科目类型编码
     */
    private String categoryCode;

    /**
     * 科目类型名称
     */
    private String categoryName;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 父科目id
     */
    private Long parentId;

    /**
     * 是否叶子节点：0否，1是
     */
    private Integer leaf;

    /**
     * 科目类型路径
     */
    private String path;

    /**
     * 科目体系id
     */
    private Long pertainId;

    /**
     * 是否删除：0否，1是
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 应用id（客户端id-应用id）
     */
    private String tenantId;

    /**
     * 创建人id
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新人id
     */
    private String operator;

    /**
     * 更新人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

    public SubjectCategoryE() {
    }

    public SubjectCategoryE(Long id) {
        this.id = id;
    }

}
