package com.wishare.finance.apps.pushbill.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PushInfoRspV {
    @JsonProperty("RETURN_CODE")
    private String returnCode = "S0000001";


    @JsonProperty("RETURN_DESC")
    private String returnDesc = "处理成功";
}
