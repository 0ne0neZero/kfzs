package com.wishare.finance.apps.model.bill.fo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("条件查询参数")
@Getter
@Setter
public class TemporaryBillF {

    @ApiModelProperty("账单ID列表")
    private List<Long> billIds;

    @NotBlank(message = "项目id不能为空")
    private String communityId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutTime;

    @ApiModelProperty("是否忽视查询条件")
    private Boolean condition;

    @ApiModelProperty("结转状态")
    private List<Integer> carriedStateList;

    @ApiModelProperty("结算状态")
    private List<Integer> settleStateList;

    @ApiModelProperty("退款状态")
    private List<Integer> refundStateList;

    @ApiModelProperty("审核状态")
    private List<Integer> approveStateList;

    @ApiModelProperty("冲销状态")
    private List<Integer> reversedStateList;

    @ApiModelProperty("状态")
    private List<Integer> stateList;

    @ApiModelProperty("费项ID")
    private List<Long> chargeItemIdList;

    @ApiModelProperty("房号ID")
    private List<Long> roomIdList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("是否初始化审核")
    private Integer isInit;

    @ApiModelProperty("是否正序 true正序 false倒序 null不排序")
    private Boolean asc;

    @ApiModelProperty("排序字段")
    private String orderValue;

    @ApiModelProperty("查询数量")
    private Long size;

}
