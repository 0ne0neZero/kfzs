package com.wishare.finance.infrastructure.beans;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelImportResult<T> {
    private List<T> successList;

    private List<ExcelImportErrResult<T>> errList;

    public ExcelImportResult(List<T> successList, List<ExcelImportErrResult<T>> errList) {
        this.successList = successList;
        this.errList = errList;
    }

    public ExcelImportResult(List<ExcelImportErrResult<T>> errList) {
        this.successList = new ArrayList<>();
        this.errList = errList;
    }
}