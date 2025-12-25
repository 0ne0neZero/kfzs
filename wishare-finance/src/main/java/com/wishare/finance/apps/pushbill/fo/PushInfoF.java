package com.wishare.finance.apps.pushbill.fo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PushInfoF {

    @JsonProperty("CWY_NM")
    private String CWYNM;

    @JsonProperty("BZD_NM")
    private String BZDNM;

    @JsonProperty("BZD_STAGE_CODE")
    private String BZDSTAGECODE;


    @JsonProperty("BZD_STATUS_CODE")
    private String BZDSTATUSCODE;

    @JsonProperty("BZD_PREVSTAGE_CODE")
    private String BZDPREVSTAGECODE;

    @JsonProperty("BZD_PREVSTATUS_CODE")
    private String BZDPREVSTATUSCODE;

    @JsonProperty("BZD_STATUS_DATE")
    private String BZDSTATUSDATE;

    @JsonProperty("REMARK")
    private String REMARK;

    @JsonProperty("BIZ_CODE")
    private String BIZCODE;

    @JsonProperty("BZD_ZDR")
    private String BZDZDR;

    @JsonProperty("BZD_OPER")
    private String BZDOPER;

    @JsonProperty("IPS_CODE")
    private String IPSCODE;
}
