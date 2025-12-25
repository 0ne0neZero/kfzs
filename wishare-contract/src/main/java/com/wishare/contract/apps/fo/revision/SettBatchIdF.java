package com.wishare.contract.apps.fo.revision;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("结算/确收单id集合")
public class SettBatchIdF implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> settIds;

}
