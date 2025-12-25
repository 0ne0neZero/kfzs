package com.wishare.finance.infrastructure.remote.vo.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@Builder
public class WhereConditionV implements Serializable {

    private String acctUnitID;

    private String starttime;

    private String endtime;

    @JsonProperty("PageNum")
    private Integer PageNum ;

    @JsonProperty("WLDWBH")
    private String WLDWBH ;


}
