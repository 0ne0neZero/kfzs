package com.wishare.contract.apps.remote.fo.bpm;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@ApiModel("bpm总部非支出类申请参数")
@AllArgsConstructor
@NoArgsConstructor
public class BpmContractNoExpendF {
    @ApiModelProperty(value = "合同id")
    private Long id;

    @ApiModelProperty(value = "bpm流程id")
    private String bpmProcessInstResponseId;

    @ApiModelProperty(value = "BO对象id")
    private String bpmBoUuid;

    @ApiModelProperty(value = "收款计划")
    private List<BpmContractCollectionPlanF> collectionPlanList;

    @ApiModelProperty(value = "损益计划")
    private List<BpmContractProfitLossPlanF> profitLossPlanList;

    @ApiModelProperty(value = "合同主体")
    private List<BpmContractBodyF> bpmContractBodyList;

    @ApiModelProperty("付款明细列表")
    private List<BpmContractPaymentDetailF> paymentDetailList;

    @ApiModelProperty(value = "bpm账号",required = true)
    @NotBlank(message = "bpm账号不能为空")
    private String bpmAccount;

    @ApiModelProperty("公司名称")
    @NotBlank(message = "公司名称不能为空")
    private String tenantName;

    @ApiModelProperty(value = "合同名称",required = true)
    @NotBlank(message = "合同名称不能为空")
    private String name;

    @ApiModelProperty(value = "合同起始日期",required = true)
    @NotBlank(message = "合同起始日期不能为空")
    private String gmtExpireStart;

    @ApiModelProperty(value = "合同结束日期",required = true)
    @NotBlank(message = "合同结束日期不能为空")
    private String gmtExpireEnd;

    @ApiModelProperty(value = "合同份数",required = true)
//    @NotBlank(message = "合同份数不能为空")
    private Integer count;

    @ApiModelProperty("合同摘要")
    @NotBlank(message = "合同摘要不能为空")
    private String contractAbstract;

    @ApiModelProperty("合同印章类型 1合同专用章 2公司公章")
    private Integer sealType;

    @ApiModelProperty("是否保证金 false 否 true 是")
    private Boolean bond;

    @ApiModelProperty("保证金金额")
    private BigDecimal bondAmount;

    @ApiModelProperty("业务类型")
    @NotNull(message = "合同业务类型不能为空")
    private BpmContractBusinessTypeF businessType;

    @ApiModelProperty("甲方名称")
    private String partyAName;
    @ApiModelProperty("乙方名称")
    private String PartyBName;
    @ApiModelProperty("丙方名称")
    private String PartyCName;
    @ApiModelProperty("甲方Id")
    private Long partyAId;
    @ApiModelProperty("乙方Id")
    private Long PartyBId;
    @ApiModelProperty("丙方Id")
    private Long PartyCId;
    @ApiModelProperty("合同文本")
    private String contractText;
    @ApiModelProperty("合同附件")
    private String contractEnclosure;
    @ApiModelProperty("其他说明文件")
    private String otherDocuments;
    @ApiModelProperty("金额（含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("金额（不含税）")
    private BigDecimal amountTaxExcluded;
    @ApiModelProperty("原币金额（含税）")
    private BigDecimal originalCurrency;
    @ApiModelProperty("合同预估金额")
    private BigDecimal estimatedAmount;
    @ApiModelProperty("汇率")
    private String exchangeRate;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("BPM流程id")
    private String processDefId;
    @ApiModelProperty("流程业务类型 0订立 1变更")
    private String busType;
    @ApiModelProperty("合同文本FileVo")
    private FileVo contractTextFileVo;
    @ApiModelProperty("合同附件FileVo")
    private List<FileVo> contractEnclosureFileVo;
    @ApiModelProperty("其他说明文件FileVo")
    private List<FileVo> otherDocumentsFileVo;

//    @ApiModelProperty("合同印章名称")
//    @NotBlank(message = "合同印章名称不能为空")
//    private String sealName;
}
