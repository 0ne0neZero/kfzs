package com.wishare.contract.domains.vo.revision.remind;

import com.wishare.contract.domains.enums.revision.RemindMessageConfigEnum;
import com.wishare.contract.domains.enums.revision.RevTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractAndPlanInfoV {

    @ApiModelProperty(value = "消息配置")
    private RemindMessageConfigEnum messageConfig;

    @ApiModelProperty(value = "合同性质")
    private RevTypeEnum contractNature;

    @ApiModelProperty(value = "合同主键id")
    private String contractId;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "合同编码")
    private String contractNo;

    @ApiModelProperty(value = "经办人id")
    private Long handlerId;

    @ApiModelProperty(value = "经办人姓名")
    private String handlerName;

    @ApiModelProperty(value = "所属项目id")
    private String communityId;

    @ApiModelProperty(value = "合同开始日期")
    private LocalDate contractGmtExpireStart;

    @ApiModelProperty(value = "合同结束日期")
    private LocalDate contractGmtExpireEnd;

    @ApiModelProperty(value = "计划收款id")
    private String planId;

    @ApiModelProperty(value = "计划收款时间")
    private LocalDate plannedCollectionTime;

}
