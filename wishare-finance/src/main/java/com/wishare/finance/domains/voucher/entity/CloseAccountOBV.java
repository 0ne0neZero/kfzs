package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 关账值对象
 */
@Getter
@Setter
@ApiModel(value="关账账簿信息")
public class CloseAccountOBV {

    private Long accountBookId;

    private String accountBookCode;

    private String accountBookName;

}
