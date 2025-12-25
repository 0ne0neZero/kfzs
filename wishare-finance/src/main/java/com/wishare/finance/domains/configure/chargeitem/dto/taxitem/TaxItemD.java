package com.wishare.finance.domains.configure.chargeitem.dto.taxitem;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wishare.finance.infrastructure.configs.LongToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 税目dto
 *
 * @author yancao
 */
@Getter
@Setter
public class TaxItemD {

    @ApiModelProperty("税目id")
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("更新人")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("关联的费项")
    private List<TaxChargeItemRelationD> chargeItemList;

}
