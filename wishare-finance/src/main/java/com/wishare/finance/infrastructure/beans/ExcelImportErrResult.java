package com.wishare.finance.infrastructure.beans;

import lombok.Data;

@Data
public class ExcelImportErrResult<T> {

    private T t;

    private String errMsg;

    public ExcelImportErrResult(){}

    public ExcelImportErrResult(T t, String errMsg){
        this.t = t;
        this.errMsg = errMsg;
    }
}
