package com.wishare.finance.apps.scheduler.mdm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author longhuadmin
 */
@Data
@Builder
public class Mdm97F implements Serializable {

    private String starttime;

    private String endtime;

    @JsonProperty("AUTHORIZEDUNIT")
    private String AUTHORIZEDUNIT;

    @JsonProperty("PageNum")
    private Integer PageNum ;
}
