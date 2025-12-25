package com.wishare.finance.apps.scheduler.mdm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ZjDictionaryResponse<T>{

    private String code;
    private String msg;
    private List<T> data;
    @JsonProperty("TotalPage")
    private int totalPage;
    @JsonProperty("PageNum")
    private int pageNum;
    @JsonProperty("PageSize")
    private int pageSize;
    @JsonProperty("TotalNumber")
    private int totalNumber;
}
