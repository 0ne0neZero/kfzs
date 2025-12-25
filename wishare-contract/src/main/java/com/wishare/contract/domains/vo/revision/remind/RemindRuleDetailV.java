package com.wishare.contract.domains.vo.revision.remind;

import com.wishare.contract.domains.enums.revision.RemindTargetTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RemindRuleDetailV implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "提醒天数")
    private Integer remindDays;

    @ApiModelProperty(value = "消息目标类型")
    private RemindTargetTypeEnum targetType;

    @ApiModelProperty(value = "目标信息列表")
    private List<TargetInfoV> targetInfos;

}
