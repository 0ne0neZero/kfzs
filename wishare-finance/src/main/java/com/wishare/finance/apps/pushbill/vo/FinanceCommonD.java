package com.wishare.finance.apps.pushbill.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-15
 */
@Data
public class FinanceCommonD<T> implements Serializable {

    private String code;

    private String msg;

    private List<T> data;
}
