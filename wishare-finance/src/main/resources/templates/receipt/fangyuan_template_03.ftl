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
    <td style="text-align: left">
      <img src="${fangyuanLogo2!}" alt="fangyuanLogo2" style="height: 45px; width: auto; max-width: 70%;padding-top: 10px; padding-left: 50px;text-align: left" />
    </td>
  </tr>

</table>
        <div style="font-size: 24px;font-weight: 700; color: #ED495C; text-align: center">
            广东益康物业服务有限公司收款收据
        </div>
<table>
  <tr>
      <td >
          <div style="color: #ED495C;line-height: 1px">
              <p>版</p>
              <p>权</p>
              <p>所</p>
              <p>有</p>
              <br/>
              <br/>
              <p>严</p>
              <p>禁</p>
              <p>复</p>
              <p>制</p>
          </div>
      </td>
      <td>
          <div style="color: #ED495C;line-height: 1px">
              <br/>
              <p>桂</p>
              <p>平</p>
              <p>市</p>
              <p>粤</p>
              <p>桂</p>
              <p>花</p>
              <p>城</p>
              <p>物</p>
              <p>业</p>
              <p>有</p>
              <p>限</p>
              <p>公</p>
              <p>司</p>
          </div>
      </td>
    <!--  -->
    <td>
      <table>
        <tr>
            <td width="50%" style= "text-align: left;color: #ED495C;">
                业务单元: <span style="font-weight: 400; color: #323233">${roomName!}</span>
            </td>
            <td width="35%" style= "text-align: left;color: #ED495C;">
                收款序号：<span style="font-weight: 400; color: #323233">${receiptNo!}</span>
            </td>
        </tr>
        <tr>
            <td width="50%" style= "text-align: left;color: #ED495C;">
                业户名称: <span style="font-weight: 400; color: #323233">${customerName!}</span>
            </td>
            <td width="35%" style= "text-align: left;color: #ED495C;">
                填表日期：<span style="font-weight: 400; color: #323233">${billingTime!}</span>
            </td>
    
        </tr>
    </table>
    <table
                width="100%"
                style="table-layout: fixed; word-wrap: break-word;border: 1px solid rgb(220, 71, 83); text-align: center; border-collapse: collapse"
    
        >
            <thead style="border: 1px solid rgb(220, 71, 83); background-color: #fafafa; box-sizing: border-box">
                <tr>
                    <th style="box-sizing: border-box; width: 180px; height: 40px; padding: 4px 0px; text-align: center;color: #ED495C;">收费项目</th>
                    <th style="box-sizing: border-box; width: 220px; height: 40px; padding: 4px 0px; text-align: center;color: #ED495C;">收费起止日期</th>
                    <th style="box-sizing: border-box; width: 100px; height: 40px; padding: 4px 0px; text-align: right;color: #ED495C;">金额（元）</th>
                    <th style="padding: 4px 0px;color: #ED495C;">备注</th>
                </tr>
            </thead>
            <tbody>
            <#list invoiceReceiptDetails as detail>
                <tr style="color: #606266">
                    <td style="padding: 8px 4px">${detail.chargeItemName!}</td>
                    <td style="padding: 8px 4px">${detail.expensePeriod!}</td>
                    <td style="padding: 8px 4px; text-align: right"><#if detail.price??>${(detail.price?number/100)?string("#0.00")!}</#if></td>
                    <td style="padding: 8px 4px">${detail.remark!}</td>
                </tr>
            </#list>
            </tbody>
            <tr>
                <td colspan="4">
                    <div style="float: left;padding: 10px;color: #ED495C;">合计（人民币大写）: <span style="font-size: 16px;color: #323233;">${invoiceAmountTotalUppercase!}</span></div>
                    <div style="float: right;padding: 10px;color: #ED495C;">(小写) <span style="font-size: 16px;color: #323233;">¥${(invoiceAmountTotal/100)?string("#0.00")!}</span> </div>
                </td>
            </tr>
    </table>
<div >
    <table>
        <tr>
            <td style="width: 180px;vertical-align: top;text-align: left; color: #ED495C;">收款人：<span style="font-weight: 20; color: #323233">${payeeName!}</span></td>
            <td style="width: 240px;vertical-align: top;text-align: center;color: #ED495C;">收款方式：<span style="font-weight: 20; color: #323233">${payChannel!}</span></td>
            <td style="width: 180px;vertical-align: top;text-align: right;color: #ED495C;">收款单位（盖章）：<span style="font-weight: 20; color: #323233"></span></td>
        </tr>
    </table>
    <table>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td style="width:400px; vertical-align: top;text-align: left; color: #ED495C;">
                说明：本数据经收款单位盖章方为有效，手写无效
            </td>
        </tr>
    </table>
</div>

    </td>
      <td>
          <div style="color: #ED495C;line-height: 1px">
              <p>①</p>
              <p>客</p>
              <p>户</p>
              <p>联</p>
              <p>︵</p>
              <p>白</p>
              <p>︶</p>
              <br/>
              <p>②</p>
              <p>存</p>
              <p>根</p>
              <p>联</p>
              <p>︵</p>
              <p>绿</p>
              <p>︶</p>
          </div>
      </td>

  </tr>
</table>




</body>
</html>

