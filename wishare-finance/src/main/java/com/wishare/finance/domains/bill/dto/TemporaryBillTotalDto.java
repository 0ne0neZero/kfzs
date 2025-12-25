package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 临时收费账单合计信息
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("应收账单合计信息")
public class TemporaryBillTotalDto extends BillTotalDto{

}
