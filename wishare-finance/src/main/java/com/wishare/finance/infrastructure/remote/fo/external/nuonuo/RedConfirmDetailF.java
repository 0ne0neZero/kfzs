package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import lombok.Getter;
import lombok.Setter;

/**
 * 红字确认单明细信息（全电部分冲红
 * 时才需要传）
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/14
 */
@Getter
@Setter
public class RedConfirmDetailF {

    private String blueDetailIndex;

    private String goodsName;

    private String goodsCode;

}
