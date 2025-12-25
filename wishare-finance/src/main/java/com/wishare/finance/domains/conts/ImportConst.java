package com.wishare.finance.domains.conts;

import com.wishare.finance.domains.enums.FinanceFileRecordBusinessTypeEnum;

/**
 * 收费系统导入的sheetName
 * 部分sheet的名字可选择ChargeFileRecordBusinessTypeEnum这个导入的name
 *
 * @author xiaolin
 */
public interface ImportConst {

    String BLUE_INVOICE_IMPORT = FinanceFileRecordBusinessTypeEnum.蓝票补录导入.getName();
}
