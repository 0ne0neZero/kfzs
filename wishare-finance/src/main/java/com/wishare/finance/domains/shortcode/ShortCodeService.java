package com.wishare.finance.domains.shortcode;

/**
 * 短链服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2024/4/23
 */
public interface ShortCodeService {

    /**
     * 创建短链
     * @param longUrl 长链接地址
     * @return 短链地址
     */
    String createShortCode(String longUrl);

    /**
     * 获取长链信息
     * @param shortCode 短链码
     * @return 长链
     */
    String getLongUrl(String shortCode);


}
