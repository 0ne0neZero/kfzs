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
* 合同收入损益表
* </p>
*
* @author chenglong
* @since 2023-10-24
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同收入损益表下拉列表视图对象", description = "合同收入损益表")
public class ContractIncomeConcludeProfitLossListV {
    @ApiModelProperty("合同收入损益表下拉列表视图列表")
    private List<ContractIncomeConcludeProfitLossV> infoList;
    @ApiModelProperty("偏移坐标，当下滑加载更多时，传入接口返回的该值作为请求参数之一")
    private String indexId;
}
