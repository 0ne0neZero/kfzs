package com.wishare.contract.domains.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* <p>
* 工程类合同计提信息表
* </p>
*
* @author wangrui
* @since 2022-11-29
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractEngineeringPlanV {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("计提编号")
    private String accrualCode;
    @ApiModelProperty("上次工程完成百分比")
    private BigDecimal lastPercent;
    @ApiModelProperty("上次工程总金额")
    private BigDecimal lastAmount;
    @ApiModelProperty("本次工程百分比")
    private BigDecimal thisTimePercent;
    @ApiModelProperty("本次工程总金额")
    private BigDecimal thisTimeAmount;
    @ApiModelProperty("本次计提金额")
    private BigDecimal accrualAmount;
    @ApiModelProperty("计提资料")
    private String accrualData;
    @ApiModelProperty("计提资料")
    private String accrualDataName;
    @ApiModelProperty("计提资料FileVo")
    private List<FileVo> accrualDataFileVo;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("审批状态")
    private Integer approvalStatus;
    @ApiModelProperty("审批编号")
    private String approvalNo;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

}
