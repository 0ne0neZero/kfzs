package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

/**
 * 分页获取未关联外部数据的费项
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("分页获取未关联外部数据的费项")
public class QueryNotRelationChargeItemF {

    @ApiModelProperty(value = "外部id")
    private Long externalId;

    @ApiModelProperty(value = "外部类型： 1税目")
    private Integer externalType;

    @ApiModelProperty(value = "是否查询末级费项：0否 1是")
    private Integer lastStage;

    @Valid
    @ApiModelProperty(value = "检索条件")
    private PageF<SearchF<?>> query;
}
