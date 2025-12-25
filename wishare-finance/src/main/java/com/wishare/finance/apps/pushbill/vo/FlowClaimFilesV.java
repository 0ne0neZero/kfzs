package com.wishare.finance.apps.pushbill.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FlowClaimFilesV {
    private String flowFiles;

    private String reportFiles;

    private String claimName;

    private LocalDateTime gmtCreate;
}
