package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author hhb
 * @describe
 * @date 2025/11/7 16:59
 */
@Data
@Accessors(chain = true)
@TableName("contract_pay_cost_community")
public class ContractPayCostCommunityE {

    @TableId(value = ID)
    private String id;
    //项目ID
    private String communityId;
    //项目名称
    private String communityName;

    public static final String ID = "id";
}
