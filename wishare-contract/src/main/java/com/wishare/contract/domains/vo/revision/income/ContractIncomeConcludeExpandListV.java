package com.wishare.contract.domains.vo.revision.income;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;

/**
* <p>
* 收入合同订立信息拓展表
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息拓展表下拉列表视图对象", description = "收入合同订立信息拓展表")
public class ContractIncomeConcludeExpandListV {
    @ApiModelProperty("收入合同订立信息拓展表下拉列表视图列表")
    private List<ContractIncomeConcludeExpandV> infoList;
    @ApiModelProperty("偏移坐标，当下滑加载更多时，传入接口返回的该值作为请求参数之一")
    private Long indexId;
}
