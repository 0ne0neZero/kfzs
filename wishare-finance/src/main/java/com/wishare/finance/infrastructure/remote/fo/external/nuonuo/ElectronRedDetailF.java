package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import lombok.Getter;
import lombok.Setter;

/**
 * 红字确认单明细信息
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/13
 */
@Getter
@Setter
public class ElectronRedDetailF {

    /**
     * 对应蓝票明细行序号
     */
    private String blueDetailIndex;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 商品编码
     */
    private String goodsCode;

}
