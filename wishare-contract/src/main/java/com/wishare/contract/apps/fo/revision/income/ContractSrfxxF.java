package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 合同订立信息拓展表收入方信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractSrfxxF {
    @ApiModelProperty("收入方名称")
    private String payee;

    @ApiModelProperty("收入方编码")
    private String payeeid;

    @ApiModelProperty("收入方账户类型")
    private String payeeaccountype;

    @ApiModelProperty("收入方账户类型翻译")
    private String payeeaccountypename;

    @ApiModelProperty("收入方开户行国别")
    private String bankcountry;

    @ApiModelProperty("收入方账户-开户行")
    private String payeeaccounbank;

    @ApiModelProperty("收入方账户-账户名称")
    private String payeeaccounname;

    @ApiModelProperty("收入方账户-账号")
    private String payeeaccounnumber;

    @ApiModelProperty("收入方账户-id")
    private String payeeaccountid;

    @ApiModelProperty("收入方swift code")
    private String swiftcode;

    @ApiModelProperty("实际收款人名称")
    private String truepayee;

    @ApiModelProperty("实际收款人编码")
    private String truepayeeid;

    @ApiModelProperty("实际付款人")
    private String truedrawee;

    @ApiModelProperty("实际付款人编码")
    private String truedraweeid;

    @ApiModelProperty("实际收款人账户类型")
    private String truepayeeaccountype;

    @ApiModelProperty("实际收款人开户行国别")
    private String truebankcountry;

    @ApiModelProperty("实际收款人账户-开户行")
    private String truepayeeaccounbank;

    @ApiModelProperty("实际收款人账户-账户id")
    private String truepayeeaccountid;

    @ApiModelProperty("实际收款人账户-账户名称")
    private String truepayeeaccounname;

    @ApiModelProperty("实际收款人账户-账号")
    private String truepayeeaccounnumber;

    @ApiModelProperty("实际收款人swift code")
    private String trueswiftcode;

    @ApiModelProperty("应收合同金额")
    private String payeeamt;

    @ApiModelProperty("收入方信息前端展示")
    private List<ContractSrfxxPayeeExtendF> payeeName;

    @ApiModelProperty("实际收入方信息前端展示")
    private List<ContractSrfxxTruePayeeExtendF> truepayeeName;

    @ApiModelProperty("收入方账号信息前端展示")
    private List<ContractSrfxxPayAccountExtendF> payeeaccounnameInfo;

    @ApiModelProperty("实际收入方账号信息前端展示")
    private List<ContractSrfxxTruePayAccountExtendF> truepayeeaccounnameInfo;

    @ApiModelProperty("支出方名称")
    private String drawee;

    @ApiModelProperty("支出方编码")
    private String draweeid;
}
