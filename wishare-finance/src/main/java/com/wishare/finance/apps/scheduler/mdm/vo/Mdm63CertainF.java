package com.wishare.finance.apps.scheduler.mdm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
public class Mdm63CertainF {

    private String ctCode;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date start;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date end;

}
