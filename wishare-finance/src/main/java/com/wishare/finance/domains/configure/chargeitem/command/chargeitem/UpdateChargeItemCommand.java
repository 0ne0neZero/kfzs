package com.wishare.finance.domains.configure.chargeitem.command.chargeitem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 更新费项命令
 *
 * @author yancao
 */
@Getter
@Setter
public class UpdateChargeItemCommand {

    /**
     * 费项id
     */
    private Long id;

    /**
     * 费项名称
     */
    private String name;

    /**
     * 费项编码
     */
    private String code;

    /**
     * 费项类型
     */
    private Integer type;

    /**
     * 费项属性 1收入,2支出，3代收代付及其他
     */
    private Integer attribute;

    /**
     * 税率id
     */
    private Long taxRateId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 父费项id
     */
    private Long parentId;

    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;

    /**
     * 是否显示:0隐藏 1显示
     */
    private Integer showed;

    /**
     * 是否启用暂估收人:0未启用，1启用
     */
    private Integer estimated;

    /**
     * 是否末级：0否,1是
     */
    private Integer lastLevel;

    /**
     * 租户id
     */
    private String tenantId;

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

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 业务标识
     */
    private String businessFlag;

    /**
     * 分成比例费项编码
     */
    private String shareChargeId;

    /**
     * 是否校验唯一性,1:关闭唯一性校验,其他:开启唯一性校验
     */
    private Integer isUnique;

    /**
     * 是否为违约金 0 否 1 是
     */
    private Integer isOverdue;

    /**
     * 业务板块编码
     */
    private String businessSegmentCode;

    /**
     * 业务板块名称
     */
    private String businessSegmentName;

    private List<ChargeItemBusinessCommand> businessCommands;

    /**
     * 关联的税率信息
     */
    private String taxRateInfo;
}
