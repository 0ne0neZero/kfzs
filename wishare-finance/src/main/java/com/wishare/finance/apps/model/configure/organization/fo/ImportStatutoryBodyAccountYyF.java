package com.wishare.finance.apps.model.configure.organization.fo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/26
 * @Description:
 */
@Getter
@Setter
@ApiModel("导入远洋银行账户")
public class ImportStatutoryBodyAccountYyF {

    @ExcelProperty(value = "所属组织", index = 0)
    private String belongOrg;

    @ExcelProperty(value = "账户编码", index = 1)
    private String name;

    @ExcelProperty(value = "账号", index = 2)
    private String bankAccount;

    @ExcelProperty(value = "户名", index = 3)
    private String accountName;

    @ExcelProperty(value = "开户银行", index = 4)
    private String bankName;

    @ExcelProperty(value = "银行类别", index = 5)
    private String bankType;

    @ExcelProperty(value = "人行联行行号", index = 6)
    private String pbcInterBankNo;

    @ExcelProperty(value = "法定单位Id",index = 7)
    private String statutoryBodyId;

    @ExcelProperty(value = "法定单位名称",index = 8)
    private String statutoryBodyName;

    @ExcelProperty(value = "开户日期", index = 9)
    private String openAccountDate;

    //收款付款类型：1.收款付款，2.收款，3.付款
    @ExcelProperty(value = "收付属性", index = 10)
    private String recPayTypeStr;

    //账户类型：1.基本账户，2一般账户，3专用账户
    @ExcelProperty(value = "账户属性", index = 11)
    private String typeStr;

    @ExcelProperty(value = "集团账户", index = 12)
    private String groupAccounts;

    @ExcelProperty(value = "网银开通状态", index = 13)
    private String onlineBankingStatus;
}
