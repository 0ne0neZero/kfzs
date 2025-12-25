<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>收据</title>
</head>
<body style="font-family: 'SimSun', serif;box-sizing: border-box; margin: 0px auto; width: 100%; height: 100vh; max-width: 700px">

<#--<img src="${zhongjiaoLogo!}" alt="zhongjiaoLogo" style="height: 25px; width: auto; max-width: 50%;padding-top: 0px; padding-left: 5px;" />-->
<div style="position: relative;font-weight: 600; font-size: 20px; color: black; padding: 0 0 0px 0; text-align: center">
    ${statutoryBodyName!}电子专用收据
    <div style="position: absolute;right: 30px;top: -10px;color:#fff; z-index: -1">Sign作废</div>
</div>

<div style="padding: 12px 16px 12px;border-bottom: 1px solid black;">
    <div
            style="
          font-weight: 600;
          color: black;
          margin-bottom: 16px;
        "
    >
        <div style="margin-bottom: 8px; overflow: hidden">
            <div style="float: left"> 项目名称: <span style="font-weight: 400; color: black">${communityName!}</span></div>
            <div style="float: right;width:240px">单据编号：<span style="font-weight: 400; color: black">${receiptNo!}</span></div>
        </div>
        <div style="margin-bottom: 8px; overflow: hidden">
            <div style="float: left">业主信息：<span style="font-weight: 400; color: black">${roomName!}_${customerName!'    '}</span></div>
            <div style="float: right;width:240px"> 缴费方式: <span style="font-weight: 400; color: black">${settleWayChannelStr!}</span></div>
        </div>
    </div>
    <table
            width="100%" border="1px solid #dcdfe6"
            style="table-layout: fixed; word-wrap: break-word; text-align: center; border-collapse: collapse;"
    >
        <thead style="background-color: #fafafa; box-sizing: border-box;border: 1px solid black;">
        <tr>
            <th style="box-sizing: border-box; width: 100px; padding: 4px 0px; text-align: center;">收费科目</th>
            <th style="box-sizing: border-box; width: 181px; padding: 4px 0px; text-align: center; ">费用期间</th>
            <th style="box-sizing: border-box; width: 73px; padding: 4px 0px; text-align: center; ">单价(元)</th>
            <th style="box-sizing: border-box; width: 80px; padding: 4px 0px; text-align: center; ">用量/面积</th>
            <th style="box-sizing: border-box; width: 73px; padding: 4px 0px; text-align: center; ">应收金额</th>
            <th style="box-sizing: border-box; width: 73px; padding: 4px 0px; text-align: center; ">实收金额</th>
        </tr>
        </thead>
        <tbody>
            <#list invoiceReceiptDetails as detail>
                <tr style="color: #000000d9; ">
                    <td style="padding: 8px 4px; ">${detail.chargeItemName!}</td>
<#--                    <td style="padding: 8px 4px; ">${detail.expensePeriod!}</td>-->
                    <td style="padding: 8px 4px; "><#if detail.expensePeriod??>
                            ${detail.expensePeriod}
                        <#else>
                            预存
                        </#if>
                    </td>
                    <td style="padding: 8px 4px; text-align: right; "><#if detail.price??>${(detail.price?number/100)?string("#0.00")!}</#if></td>
                    <td style="padding: 8px 4px; text-align: right; ">${detail.numStr!}</td>
                    <td style="padding: 8px 4px; text-align: right; ">${(detail.receivableAmount/100)?string("#0.00")!}</td>
                    <td style="padding: 8px 4px; text-align: right; ">${(detail.invoiceAmount/100)?string("#0.00")!}</td>
<#--                    <td style="padding: 8px 4px; text-align: right">${(detail.actualPayAmount/100)?string("#0.00")!}</td>-->
                </tr>
            </#list>
                <tr style="color: #000000d9; ">
                    <td style="padding: 8px 4px; ">合计：</td>
                    <td style="padding: 8px 4px; font-weight: 600; text-align: left;" colspan="3">${invoiceAmountTotalUppercase!}</td>
                    <td style="padding: 8px 4px; text-align: right; ">${(receivableAmountSum/100)?string("#0.00")!}</td>
                    <td style="padding: 8px 4px; text-align: right; ">${(invoiceAmountTotal/100)?string("#0.00")!}</td>
                </tr>
                <tr style="color: #000000d9; ">
                    <td style="padding: 8px 4px; text-align: center;">收款备注：</td>
                    <td style="padding: 8px 4px; text-align: left;" colspan="5">${remark!}</td>
                </tr>
        </tbody>
    </table>

    <table
            width="100%"
            style="table-layout: fixed; text-align: center; border-collapse: collapse; word-wrap: break-word"
    >
        <thead style="visibility: hidden">
        <tr style="visibility: hidden">
            <th style="box-sizing: border-box; width: 100px; height: 0px"></th>
            <th style="box-sizing: border-box; width: 96px"></th>
            <th style="box-sizing: border-box; width: 110px"></th>
            <th style="box-sizing: border-box; width: 92px"></th>
            <th style="box-sizing: border-box"></th>
        </tr>
        </thead>
        <tbody>
        <#if otherAmounts?? && (otherAmounts?size > 0)>
            <#list otherAmounts as other>
                <tr style="color: #14b968">
                    <td style="padding: 8px 4px">${other.otherAmountName!}</td>
                    <td style="padding: 8px 4px; text-align: right"><#if other.price??>${other.price?string("#0.00")!}</#if></td>
                    <td style="padding: 8px 4px; text-align: right">${other.num!}</td>
                    <td style="padding: 8px 4px; text-align: right">${other.otherAmount?string("#0.00")!}</td>
                    <td style="padding: 8px 4px; visibility: hidden">${other.remark!}</td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
    <div style="padding: 12px 8px 0px 0;margin-top: -16px;box-sizing: border-box;width: 100%;">
        <div style="display:flex">
            <p style="margin-bottom: 0;">注:本次交易可凭本收据换开发票，发票开具后本收据作废无效;押金类交易收据不可换开发票，凭本收据办理退款手续，请妥善保存。服务监督电话: 400-818-3399。</p>
        </div>
    </div>
</div>
<#--<hr />-->
<div style="position: relative; padding: 12px 16px; padding-bottom: 16px">
    <div style="overflow: hidden; margin-bottom: 8px">
        <div style="float: left">开具日期：<span style="font-weight: 400; color: black">${billingTime!}</span></div>
        <div style="float: right;width:160px">缴费日期：<span style="font-weight: 400; color: black">${payTime!'    '}</span></div>
    </div>
    <div style="overflow: hidden; margin-bottom: 8px">
    <#if signStatus?? && signStatus == 0>
        <div style="float: left">收款单位（盖章）：<span style="font-weight: 400; color: white">熊仔王盖章点</span></div>
    <#else>
        <div  style="float: left">收款单位（盖章）：<span style="font-weight: 400; color: black">${statutoryBodyName!}</span></div>
    </#if>
        <div style="position: absolute;width: 100%;top: 12px;text-align: center">缴费人：<span style="font-weight: 400; color: black">${customerName!}</span></div>
        <#--clerkStatus 存在值 不展示开票人-->
    <#if clerkStatus??>
        <div style="float: right;width:160px;color: white;">开票人：<span style="font-weight: 400;color: white; ">${clerk!}</span></div>
    <#else>
        <div style="float: right;width:160px">开票人：<span style="font-weight: 400; color: black">${clerk!}</span></div>
    </#if>

    </div>
    <#if enableElectSign?? && (enableElectSign=1) && electSignType?? && (electSignType=2)>
        <div>
            <img style="width: 90px; height: 90px; opacity: 0.7;" src="${signPictureUrl!}" />
        </div>
    </#if>
</div>

</body>
</html>
