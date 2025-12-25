package com.wishare.finance.domains.voucher.support.fangyuan.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FangYuanResult<T> {

    private Integer code;

    private String msg;

    private T data;
}