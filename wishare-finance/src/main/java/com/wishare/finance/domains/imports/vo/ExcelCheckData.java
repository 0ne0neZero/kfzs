package com.wishare.finance.domains.imports.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelCheckData<T> {

    private List<T> successList;

    private List<T> errList;
}
