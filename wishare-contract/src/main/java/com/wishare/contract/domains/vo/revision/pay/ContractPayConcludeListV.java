package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
* <p>
* 支出合同订立信息表
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同订立信息表下拉列表视图对象", description = "支出合同订立信息表")
public class ContractPayConcludeListV {
    @ApiModelProperty("支出合同订立信息表下拉列表视图列表")
    private List<ContractPayConcludeV> infoList;
    @ApiModelProperty("偏移坐标，当下滑加载更多时，传入接口返回的该值作为请求参数之一")
    private String indexId;


}
