<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>收据</title>
</head>
<body style="font-family: 'SimSun', serif;box-sizing: border-box; margin: 0px auto; width: 100%; height: 100vh; max-width: 700px">
<table>
    <tr>
        <td style="width: 240px;text-align: left"><img src="${fangyuanLogo1!}" alt="fangyuanLogo1" style="height: 45px; width: auto; max-width: 70%;padding-top: 10px; padding-left: 50px;text-align: left" /></td>
        <td style="font-size: 24px;font-weight: bold; color: #606266; text-align: center">方圆现代生活收款收据</td>
    </tr>
</table>
<table>
    <tr>
        <td width="50%" style= "text-align: left">
            业务单元: <span style="font-weight: 400; color: #606266">${roomName!}</span>
        </td>
        <td width="30%" style= "text-align: left">
            收款序号：<span style="font-weight: 400; color: #606266">${receiptNo!}</span>
        </td>
    </tr>
    <tr>
        <td width="50%" style= "text-align: left">
            业户名称: <span style="font-weight: 400; color: #606266">${customerName!}</span>
        </td>
        <td width="30%" style= "text-align: left">
            填表日期：<span style="font-weight: 400; color: #606266">${billingTime!}</span>
        </td>

    </tr>
</table>
    <table
            width="100%"
            style="table-layout: fixed; word-wrap: break-word;border: 1px solid rgb(228, 224, 236); text-align: center; border-collapse: collapse"

    >
        <thead style="border: 1px solid rgb(228, 224, 236); background-color: #fafafa; box-sizing: border-box">
            <tr>
                <th style="border: 1px solid rgb(228, 224, 236);box-sizing: border-box; width: 180px; height: 40px; padding: 4px 0px; text-align: center">收费项目</th>
                <th style="border: 1px solid rgb(228, 224, 236);box-sizing: border-box; width: 220px; height: 40px; padding: 4px 0px; text-align: center">收费起止日期</th>
                <th style="border: 1px solid rgb(228, 224, 236);box-sizing: border-box; width: 100px; height: 40px; padding: 4px 0px; text-align: right">金额（元）</th>
                <th style="border: 1px solid rgb(228, 224, 236);padding: 4px 0px">备注</th>
            </tr>
        </thead>
        <tbody>
        <#list invoiceReceiptDetails as detail>
            <tr style="color: #000000d9">
                <td style="padding: 8px 4px">${detail.chargeItemName!}</td>
                <td style="padding: 8px 4px">${detail.expensePeriod!}</td>
                <td style="padding: 8px 4px; text-align: right"><#if detail.price??>${(detail.price?number/100)?string("#0.00")!}</#if></td>
                <td style="padding: 8px 4px">${detail.remarkNew!}</td>
            </tr>
        </#list>
        </tbody>
        <tr>
            <td colspan="4" style="border: 1px solid rgb(228, 224, 236);">
                <div style="float: left;padding: 10px;">合计（人民币大写）: ${invoiceAmountTotalUppercase!}</div>
                <div style="float: right;padding: 10px;">(小写) ¥${(invoiceAmountTotal/100)?string("#0.00")!}</div>
            </td>
        </tr>
    </table>

<p></p>
<div >
    <table>
        <tr>
            <td style="width: 180px;vertical-align: top;text-align: left">收款人：<span style="font-weight: 20; color: #606266">${payeeName!}</span></td>
            <td style="width: 240px;vertical-align: top;text-align: center">收款方式：<span style="font-weight: 20; color: #606266">${payChannel!}</span></td>
            <td style="width: 180px;vertical-align: top;text-align: right">收款单位（盖章）：</td>
        </tr>
    </table>
</div>
</body>
</html>
