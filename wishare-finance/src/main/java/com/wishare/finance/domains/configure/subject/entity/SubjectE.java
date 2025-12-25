package com.wishare.finance.domains.configure.subject.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 科目
 *
 * @author yancao
 */
@Getter
@Setter
@TableName(TableNames.SUBJECT)
public class SubjectE {

    /**
     * 科目id
     */
    @TableId
    private Long id;

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
     * 是否删除：0否，1是
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 现金类别： 0 无，1现金，2银行，3现金等价物
     */
    private Integer cashType;

    /**
     * 是否启用：0未启用，1启用
     */
    private Integer disabled;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 父科目编码（导入费项）
     */
    @TableField(exist = false)
    private String parentCode;

    /**
     * 父科目编码路径
     */
    @TableField(exist = false)
    private List<String> codePath;

    @TableField(exist = false)
    private String fullName;


    /**
     * 数据来源id
     */
    private String idExt;

    public void init(){
        if (Objects.isNull(id)){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.SUBJECT);
        }
    }

    /**
     * 获取辅助核算类型
     * @return
     */
    public List<Integer> convertWithAuxiliaryCount(){
        ArrayList<Integer> types = new ArrayList<>();
        if (Objects.nonNull(auxiliaryCount)) {
            List<String> list = JSONObject.parseArray(auxiliaryCount, String.class);
            if (CollectionUtils.isNotEmpty(list)) {
                for (String s : list) {
                    try {
                        types.add(AssisteItemTypeEnum.valueOfByAscCode(s).getCode());
                    }catch (Exception e){
                        //nothing
                    }
                }
            }
        }
        return types;
    }

    /**
     * 判断是否是现金科目
     * @return
     */
    public boolean verifyCashSubject(){
        return Objects.nonNull(cashType) && cashType != 0;
    }

}
