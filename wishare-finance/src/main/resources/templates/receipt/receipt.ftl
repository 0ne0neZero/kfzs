<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>收据</title>
</head>
<body style="font-family: 'SimSun', serif;box-sizing: border-box; margin: 0px auto; width: 100%; height: 100vh; max-width: 700px">
<div style="font-weight: 600; font-size: 20px; color: #323233; padding: 32px 0; text-align: center"> ${statutoryBodyName!}收据 </div>
<div style="padding: 0px 16px">
    <div
            style="
          font-weight: 600;
          color: #323233;

          margin-bottom: 16px;
        "
    >
        <div style="margin-bottom: 8px">编号：<span style="font-weight: 400; color: #606266">${receiptNo!}</span></div>
        <div style="margin-bottom: 8px"> 缴费方式: <span style="font-weight: 400; color: #606266">${settleWayChannelStr!}</span></div>
        <div style="margin-bottom: 8px; overflow: hidden">
            <div style="float: left">房号：<span style="font-weight: 400; color: #606266">${communityName!} ${roomName!}</span></div>
            <div style="float: right">缴纳人：<span style="font-weight: 400; color: #606266">${customerName!}</span></div>
        </div>
    </div>
    <table
            width="100%"
            style="table-layout: fixed; word-wrap: break-word; text-align: center; border-collapse: collapse"
    >
        <thead style="background-color: #fafafa; box-sizing: border-box">
        <tr>
            <th style="box-sizing: border-box; width: 100px; padding: 4px 0px; text-align: center">收费项</th>
            <th style="box-sizing: border-box; width: 96px; padding: 4px 0px; text-align: right">单价（元）</th>
            <th style="box-sizing: border-box; width: 110px; padding: 4px 0px; text-align: right">计量</th>
            <th style="box-sizing: border-box; width: 92px; padding: 4px 0px; text-align: right">小计（元）</th>
            <th style="padding: 4px 0px">备注</th>
        </tr>
        </thead>
        <tbody>
        <#list invoiceReceiptDetails as detail>
        <tr style="color: #000000d9">
            <td style="padding: 8px 4px">${detail.chargeItemName!}</td>
            <td style="padding: 8px 4px; text-align: right"><#if detail.price??>${(detail.price?number/100)?string("#0.00")!}</#if></td>
            <td style="padding: 8px 4px; text-align: right">${detail.numStr!}<br/>${detail.instrumentStr!""}</td>
            <td style="padding: 8px 4px; text-align: right">${(detail.invoiceAmount/100)?string("#0.00")!}</td>
            <td style="padding: 8px 4px">${detail.remark!}</td>
        </tr>
        </#list>
        </tbody>
    </table>
    <div
            style="
          overflow: hidden;
          padding: 16px;
          font-weight: 600;
          border-bottom: 1px solid #f0f0f0;
          border-top: 1px solid #f0f0f0;
        "
    >
        <div style="float: left">合计（大写）:<span>${invoiceAmountTotalUppercase!}</span></div>
        <div style="float: right">(小写)：<span>¥${(invoiceAmountTotal/100)?string("#0.00")!}</span></div>
    </div>
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
</div>
<hr />
<div style="position: relative; padding: 0px 16px; padding-bottom: 16px">
    <div style="overflow: hidden; margin-bottom: 8px">
        <div style="float: left">开具日期：<span style="font-weight: 400; color: #606266">${billingTime!}</span></div>
        <div style="float: right">缴费日期：<span style="font-weight: 400; color: #606266">${payTime!'    '}</span></div>
    </div>
    <div style="overflow: hidden; margin-bottom: 8px">
        <#if signStatus?? && signStatus == 0>
            <div style="float: left">收款单位（盖章）：</div>
        <#else>
            <div style="float: left">收款单位（盖章）：<span style="font-weight: 400; color: #606266">${statutoryBodyName!}</span></div>
        </#if>
        <div style="float: right">开票人：<span style="font-weight: 400; color: #606266">${clerk!}</span></div>
    </div>
    <!--远洋挪章-->
    <div style="overflow: hidden; margin-bottom: 8px">
        <#if signStatus?? && signStatus == 0>
            <div style="float: left;font-weight: 400; color: white;">收款单位（盖章熊仔王盖章点</div>
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
