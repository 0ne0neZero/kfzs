package com.wishare.finance.apps.model.reconciliation.fo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.bill.dto.FlowBillPageDto;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ApiModel("中交流水认领参数")
public class ClaimFlowZJF {

    @ApiModelProperty("领用金额")
    private BigDecimal claimAmount;

    @Deprecated
    @ApiModelProperty("系统来源：1收费 2合同")
    private Integer sysSource;

    @ApiModelProperty("认领类型(1:发票认领;2:账单认领;)")
    private Integer claimType;

    @ApiModelProperty("认领记录ID(修改时传入)")
    private Long flowClaimRecordId;

    @ApiModelProperty("收款单集合集合")
    @NotEmpty(message = "收款单集合不能为空")
    private List<FlowBillPageDto> gatherBillList;

    @ApiModelProperty("流水id集合")
    @NotEmpty(message = "流水id集合不能为空")
    private List<Long> flowIdList;

    @ApiModelProperty("上级收费单元ID")
    private String supCpUnitId;

    @ApiModelProperty("日报表附件集合")
    private List<FileVo> reportFileVos;

    @ApiModelProperty("银行回单附件集合")
    private List<FileVo> flowFileVos;

    @ApiModelProperty("是否差额认领 0 差额认领 1或者其他 全额认领 若是差额认领必须传0")
    private Integer differenceFlag;

    @ApiModelProperty("差额认领原因 差额认领必传")
    private String differenceReason;

    @ApiModelProperty("差额认领备注")
    private String differenceRemark;


    @ApiModelProperty("暂存记录Id")
    private Long flowTempRecordId;

    @ApiModelProperty("单据备注(凭据摘要)")
    private String receiptRemark;

    @ApiModelProperty("到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate gmtExpire;

    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;
}
