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
<body style="font-family: 'SimSun', serif;box-sizing: border-box; margin: 0 auto; width: 100%; height: 100vh; max-width: 876px;font-size: 14px;">
<div style="line-height:36px; font-size: 30px; padding: 68px 0 0 0; text-align: center"> 宜兴灵山大拈花湾物业服务有限公司 </div>
<div style="line-height:36px;font-size: 26px;text-align: center;padding-top: 8px;">收款收据</div>
<div style="padding:0 64px 0 32px">
    <div >
        <div style="line-height:36px;margin-bottom: 8px; overflow: hidden">
            <div style="float: left">开票日期：<span>${billingTime!}</span></div>
            <div style="float: right">编号：<span>${receiptNo!}</span></div>
        </div>
    </div>
</div>
<table  width="100%" style="table-layout: auto; word-wrap: break-word; text-align: center;
        border-collapse: collapse;">
    <tr style="line-height:36px;">
        <td colspan="5" style="text-align: left;">
            <span style="float: left;display: inline-block;width: 40%;padding: 4px 0px 4px 53px;box-sizing: border-box">业主/客户： ${customerName!}</span>
            <span style="float: right;display: inline-block;width: 50%;padding: 4px 16px;box-sizing: border-box"> 项目/房号： ${communityName!} ${roomName!}</span>
        </td>
    </tr>
    <tr>
        <td style="line-height:36px;text-align: left;padding: 4px 53px;" colspan="5">缴费方式：${settleWayChannelStr!}</td>
    </tr>
    <tr style="line-height:36px;">
        <td width="208" style="border-left: 0;box-sizing: border-box;  padding: 4px 0; text-align: center">付款内容</td>
        <td width="151" style="box-sizing: border-box; padding: 4px 0; text-align: center">数量</td>
        <td width="151" style="box-sizing: border-box; padding: 4px 0; text-align: center">单价（元）</td>
        <td width="151" style="box-sizing: border-box;padding: 4px 0; text-align: center">金额（元）</td>
        <td width="151" style="border-right: 0;padding: 4px 0">备注</td>
    </tr>
    <#list invoiceReceiptDetails as detail>
        <tr style="line-height:36px">
            <td style="border-left: 0;border-top: 0; padding: 8px 4px;text-align: center">${detail.chargeItemName!}</td>
            <td style="padding: 8px 4px; border-top: 0; text-align: center">${detail.numStr!}<br/>${detail.instrumentStr!""}</td>
            <td style="padding: 8px 4px;border-top: 0; text-align: center"><#if detail.price??>${(detail.price?number/100)?string("#0.00")!}</#if></td>
            <td style="padding: 8px 4px;border-top: 0; text-align: center">${(detail.invoiceAmount/100)?string("#0.00")!}</td>
            <td style="border-right: 0;border-top: 0; padding: 8px 4px"></td>
        </tr>
    </#list>
    <#if invoiceReceiptDetails?size lt 5>
        <#list 1..(5 - invoiceReceiptDetails?size) as _>
            <tr style="line-height:36px">
                <td style="border-left: 0;border-top: 0; padding: 8px 4px;text-align: center"><span style="font-weight: 400; color: white">熊仔王盖章点</span></td>
                <td style="padding: 8px 4px; border-top: 0; text-align: center"></td>
                <td style="padding: 8px 4px;border-top: 0; text-align: center"></td>
                <td style="padding: 8px 4px;border-top: 0; text-align: center"></td>
                <td style="border-right: 0;border-top: 0; padding: 8px 4px"></td>
            </tr>
        </#list>
    </#if>
    <tr style="line-height:36px">
        <td colspan="3" style="text-align:left;padding: 4px 0px 4px 53px;">合计（大写）:${invoiceAmountTotalUppercase!}</td>
        <td colspan="2" style="text-align:left;padding: 4px 0px 4px 32px;">金额（小写）：¥${(invoiceAmountTotal/100)?string("#0.00")!}</td>
    </tr>
</table>
<#--<table width="100%" style="table-layout: fixed; text-align: center; word-wrap: break-word;border-top: 0px solid #f0f0f0;">-->
<#--    <#if otherAmounts?? && (otherAmounts?size > 0)>-->
<#--        <#list otherAmounts as other>-->
<#--            <tr style="color: #14b968">-->
<#--                <td style="padding: 8px 4px">${other.otherAmountName!}</td>-->
<#--                <td style="padding: 8px 4px; text-align: right"><#if other.price??>${other.price?string("#0.00")!}</#if></td>-->
<#--                <td style="padding: 8px 4px; text-align: right">${other.num!}</td>-->
<#--                <td style="padding: 8px 4px; text-align: right">${other.otherAmount?string("#0.00")!}</td>-->
<#--                <td style="padding: 8px 4px; visibility: hidden">${other.remark!}</td>-->
<#--            </tr>-->
<#--        </#list>-->
<#--    </#if>-->
<#--</table>-->
<#--<hr />-->
<div style="line-height:36px;position: relative; padding: 12px 12px 12px 4px; padding-bottom: 16px">
    <div style="overflow: hidden; margin-bottom: 8px">
        <#if signStatus?? && signStatus == 0>
            <div style="float: left">收款单位（盖章）：<span style="font-weight: 400; color: white">-----</span></div>
        <#else>
            <div  style="float: left">收款单位（盖章）：<span style="font-weight: 400; color: black"></span></div>
        </#if>
        <div style="position: absolute;width: 100%;top: 12px;text-align: center">收款：<span style="font-weight: 400; color: black">${payeeName!}</span></div>
        <#--clerkStatus 存在值 不展示开票人-->
        <#if clerkStatus??>
            <div style="float: right;padding-right: 35px;width:160px;color: white;">开票人：<span style="font-weight: 400;color: white; ">${clerk!}</span></div>
        <#else>
            <div style="float: right;padding-right: 35px;width:160px;">开票人：<span style="font-weight: 400; color: black">${clerk!}</span></div>
        </#if>
    </div>
    <#if enableElectSign?? && (enableElectSign=1) && electSignType?? && (electSignType=2)>
        <div>
            <img style="width: 90px; height: 90px; opacity: 0.7;" src="${signPictureUrl!}"  alt=""/>
        </div>
    </#if>
</div>
</body>
</html>
