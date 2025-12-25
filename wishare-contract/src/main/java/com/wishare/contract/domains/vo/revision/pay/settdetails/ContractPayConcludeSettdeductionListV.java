package com.wishare.contract.domains.vo.revision.pay.settdetails;

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
* 结算单扣款明细表信息
* </p>
*
* @author zhangfy
* @since 2024-05-20
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单扣款明细表信息下拉列表视图对象", description = "结算单扣款明细表信息")
public class ContractPayConcludeSettdeductionListV {
    @ApiModelProperty("结算单扣款明细表信息下拉列表视图列表")
    private List<ContractPayConcludeSettdeductionV> infoList;
    @ApiModelProperty("偏移坐标，当下滑加载更多时，传入接口返回的该值作为请求参数之一")
    private String indexId;
}
