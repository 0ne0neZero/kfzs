package com.wishare.contract.domains.entity.revision.remind;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.enums.DeletedEnum;
import com.wishare.contract.domains.enums.revision.RemindMessageTypeEnum;
import com.wishare.contract.domains.enums.revision.RemindTargetTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_remind_message_config")
public class ContractRemindMessageConfigE {

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 提醒天数
     */
    private Integer remindDays;

    /**
     * 消息提醒类型
     */
    private RemindMessageTypeEnum messageType;

    /**
     * 消息目标类型
     */
    private RemindTargetTypeEnum targetType;

    /**
     * 目标信息
     */
    private String targetInfo;

    /**
     * 是否删除
     */
    private DeletedEnum deleted;

    /**
     * 创建人id
     */
    private String creator;

    /**
     * 创建人姓名
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
     * 更新人姓名
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

}
