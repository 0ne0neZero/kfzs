package com.wishare.finance.apps.scheduler.mdm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author longhuadmin
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MDM73F {

    private String acctUnitID;

    private String starttime;

    private String endtime;

    @JsonProperty("PageNum")
    private Integer PageNum ;


}
