package com.wishare.finance.domains.voucher.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 凭证账簿信息
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/26
 */
@Getter
@Setter
public class VoucherAccountBook {

    /**
     * 账簿id
     */
    private Long accountBookId;
    /**
     * 账簿编码
     */
    private String accountBookCode;
    /**
     * 账簿名称
     */
    private String accountBookName;

    public VoucherAccountBook() {
    }

    public VoucherAccountBook(Long accountBookId, String accountBookCode, String accountBookName) {
        this.accountBookId = accountBookId;
        this.accountBookCode = accountBookCode;
        this.accountBookName = accountBookName;
    }
}
