package com.wishare.finance.infrastructure.conts;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/31
 */
public enum TextContentEnum {

    /**
     * 电子收据
     */
    电子收据(0),
    /**
     * 发票补发
     */
    发票补发(2),
    /**
     * 收据补发
     */
    收据补发(3);

    private int code;

    TextContentEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
