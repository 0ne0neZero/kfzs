
package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.dto.ImportBillDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "导入应收账单结果信息", parent = ImportBillDto.class)
public class ImportReceivableBillV extends ImportBillDto {


}
