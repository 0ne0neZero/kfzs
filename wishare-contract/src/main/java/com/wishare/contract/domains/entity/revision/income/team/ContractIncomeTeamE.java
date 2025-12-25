package com.wishare.contract.domains.entity.revision.income.team;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Arrays;
/**
 * <p>
 * 收入合同-团队表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_team")
public class ContractIncomeTeamE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 关联合同ID
     */
    private String contractId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 所属公司ID
     */
    private String orgId;

    /**
     * 所属部门ID
     */
    private String departId;

    /**
     * 所属部门名称
     */
    private String departName;

    /**
     * 所属公司名称
     */
    private String orgName;

    /**
     * 团队角色ID
     */
    private String roleId;

    /**
     * 团队角色
     */
    private String role;

    /**
     * 截止有效期
     */
    private LocalDate endDate;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;


    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 关联合同ID
    */
    public static final String CONTRACT_ID = "contractId";

    /**
    * 姓名
    */
    public static final String NAME = "name";

    /**
    * 所属公司ID
    */
    public static final String ORG_ID = "orgId";

    /**
    * 所属部门ID
    */
    public static final String DEPART_ID = "departId";

    /**
    * 所属部门名称
    */
    public static final String DEPART_NAME = "departName";

    /**
    * 所属公司名称
    */
    public static final String ORG_NAME = "orgName";

    /**
    * 团队角色
    */
    public static final String ROLE = "role";

    /**
    * 截止有效期
    */
    public static final String END_DATE = "endDate";

    /**
    * 租户id
    */
    public static final String TENANT_ID = "tenantId";

    /**
    * 创建人
    */
    public static final String CREATOR = "creator";

    /**
    * 创建人名称
    */
    public static final String CREATOR_NAME = "creatorName";

    /**
    * 创建时间
    */
    public static final String GMT_CREATE = "gmtCreate";

    /**
    * 操作人
    */
    public static final String OPERATOR = "operator";

    /**
    * 操作人名称
    */
    public static final String OPERATOR_NAME = "operatorName";

    /**
    * 操作时间
    */
    public static final String GMT_MODIFY = "gmtModify";

    /**
    * 是否删除  0 正常 1 删除
    */
    public static final String DELETED = "deleted";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CONTRACT_ID,
                                    NAME,
                                    ORG_ID,
                                    DEPART_ID,
                                    DEPART_NAME,
                                    ORG_NAME,
                                    ROLE,
                                    END_DATE,
                                    TENANT_ID,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    DELETED);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
