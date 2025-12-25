<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>收据</title>
    <style>
        table {
            margin: 0 auto;
            border: 1px solid #000000;
            border-collapse: collapse;
        }

        th,
        td {
            border: 1px solid #000000;
            text-align: center;
        }
    </style>
</head>

<body style="font-family: 'SimSun', serif;box-sizing: border-box; margin: 0px auto; width: 100%; height: 100vh; max-width: 700px">
<div style="font-weight: 400; font-size: 20px;  padding: 32px 0; text-align: center"> ${statutoryBodyName!}收据 </div>
<div style="padding: 0px 16px">
    <div style="font-weight: 400;margin-bottom: 16px;">
        <div style="margin-bottom: 8px">编号：<span style="font-weight: 400;">${receiptNo!}</span></div>
        <div style="margin-bottom: 8px"> 缴费方式: <span style="font-weight: 400; ">${settleWayChannelStr!}</span></div>
        <div style="margin-bottom: 8px; overflow: hidden">
            <div style="float: left">房号：<span style="font-weight: 400; ">${roomName!}</span></div>
            <div style="float: right">缴纳人：<span style="font-weight: 400; ">${customerName!}</span></div>
        </div>
    </div>


    <table width="100%" style="table-layout: fixed; text-align: center; word-wrap: break-word;">
        <tr>
            <td style="box-sizing: border-box;  padding: 4px 0px; text-align: center;">序号</td>
            <td style="box-sizing: border-box; width: 100px; padding: 4px 0px; text-align: center;">费项名称</td>
            <td style="box-sizing: border-box; width: 96px; padding: 4px 0px; text-align: center;">单价（元）</td>
            <td style="box-sizing: border-box; width: 110px; padding: 4px 0px; text-align: center;">计量</td>
            <td style="box-sizing: border-box; width: 92px; padding: 4px 0px; text-align: center;">小计</td>
            <td style="padding: 4px 0px">备注</td>
        </tr>
        <#assign counter = 1>
        <#list invoiceReceiptDetails as detail>
            <tr style="color: #000000d9">
                <td style="padding: 8px 4px">${counter}</td>
                <td style="padding: 8px 4px">${detail.chargeItemName!}</td>
                <td style="padding: 8px 4px; text-align: center;"><#if detail.price??>${(detail.price?number/100)?string("#0.00")!}</#if></td>
                <td style="padding: 8px 4px; text-align: center;">${detail.numStr!}<br/>${detail.instrumentStr!""}</td>
                <td style="padding: 8px 4px; text-align: center;">${(detail.invoiceAmount/100)?string("#0.00")!}</td>
                <td style="padding: 8px 4px">${detail.remark!}</td>
            </tr>
        </#list>
        <#if invoiceReceiptDetails?size lt 5>
            <#list 1..(5 - invoiceReceiptDetails?size) as _>
                <tr style="line-height:36px">
                    <td style="padding: 8px 4px; border-top: 0; text-align: center"></td>
                    <td style="border-left: 0;border-top: 0; padding: 8px 4px;text-align: center"><span style="font-weight: 400; color: white">-----</span></td>
                    <td style="padding: 8px 4px; border-top: 0; text-align: center"></td>
                    <td style="padding: 8px 4px;border-top: 0; text-align: center"></td>
                    <td style="padding: 8px 4px;border-top: 0; text-align: center"></td>
                    <td style="border-right: 0;border-top: 0; padding: 8px 4px"></td>
                </tr>
            </#list>
        </#if>
        <tr style="line-height:36px">
            <td colspan="3" style="text-align:left;padding:4px 16px">合计（大写）:${invoiceAmountTotalUppercase!}</td>
            <td colspan="3" style="text-align:left;padding:4px 16px">金额（小写）：¥${(invoiceAmountTotal/100)?string("#0.00")!}</td>
        </tr>
    </table>
    <#if otherAmounts?? && (otherAmounts?size > 0)>
    <table width="100%" style="table-layout: fixed; text-align: center; word-wrap: break-word;">

        <thead style="visibility: hidden">
        <tr style="visibility: hidden">
            <td style="padding: 8px 4px"></td>
            <td style="box-sizing: border-box; width: 100px; height: 0px"></td>
            <td style="box-sizing: border-box; width: 96px"></td>
            <td style="box-sizing: border-box; width: 110px"></td>
            <td style="box-sizing: border-box; width: 92px"></td>
            <td style="box-sizing: border-box"></td>
        </tr>
        </thead>
        <tbody>
        <#if otherAmounts?? && (otherAmounts?size > 0)>
            <#list otherAmounts as other>
                <tr style="color: #14b968">
                    <td style="padding: 8px 4px"></td>
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
    </#if>
</div>
<div style="position: relative; padding: 0px 16px; padding-bottom: 16px;margin-top: 8px">
    <div style="overflow: hidden; margin-bottom: 8px">
        <div style="float: left">开具日期：<span style="font-weight: 400; ">${billingTime!}</span></div>
        <div style="float: right">缴费日期：<span style="font-weight: 400; ">${payTime!'    '}</span></div>
    </div>
    <div style="overflow: hidden; margin-bottom: 8px">
        <#if signStatus?? && signStatus == 0>
            <div style="float: left">收款单位（盖章）：<span style="font-weight: 400; color: white;">熊仔王盖章点</span></div>
        <#else>
            <div style="float: left">收款单位（盖章）：<span style="font-weight: 400; ">${statutoryBodyName!}</span></div>
        </#if>
        <div style="float: right">开票人：<span style="font-weight: 400; ">${clerk!}</span></div>
    </div>
    <#if enableElectSign?? && (enableElectSign=1) && electSignType?? && (electSignType=2)>
        <div>
            <img style="width: 90px; height: 90px; opacity: 0.7;" src="${signPictureUrl!}" />
        </div>
    </#if>
</div>
</body>
</html>
