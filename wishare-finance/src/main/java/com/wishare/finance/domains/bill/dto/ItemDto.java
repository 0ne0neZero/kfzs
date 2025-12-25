package com.wishare.finance.domains.bill.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemDto {

    /**
     * 项目id
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

}
