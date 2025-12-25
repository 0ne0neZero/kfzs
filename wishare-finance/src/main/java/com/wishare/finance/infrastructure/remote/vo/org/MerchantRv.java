package com.wishare.finance.infrastructure.remote.vo.org;

import lombok.Data;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/2 13:16
 * @version: 1.0.0
 */
@Data
public class MerchantRv {

    /**
     * 客商id
     */
    private Long id;

    /**
     * 客商编码
     */
    private String code;

    /**
     * 客商名称
     */
    private String name;
}
