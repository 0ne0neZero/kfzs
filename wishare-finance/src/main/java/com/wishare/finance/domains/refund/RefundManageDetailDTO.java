package com.wishare.finance.domains.refund;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RefundManageDetailDTO {
    private String communityId;
    private String payApplyId;
    private String payApplyCode;
    private String payApplyIdOld;
    private String payApplyCodeOld;
}
