package com.wishare.finance.infrastructure.remote.fo.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author fxl
 * @describe
 * @date 2024/1/11
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "财务云返回体", description = "财务云返回体")
public class FinanceCloudD<T> implements Serializable {

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
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
