package com.wishare.finance.apps.model.bill.fo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class RelieveFreezeF {

    @ApiModelProperty(value = "账单编号")
    @NotEmpty(message = "账单编号不能为空")
    private List<String> billNo;

    @ApiModelProperty(value = "项目id")
    @JsonIgnore
    private String communityId;

}
