package com.wishare.finance.domains.shortcode;

import com.wishare.finance.infrastructure.support.shortcode.ShortCodeUtils;
import com.wishare.finance.infrastructure.support.shortcode.SnowflakeUl;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 短链实现
 *
 * @author dxclay
 * @version 1.0
 * @since 2024/4/23
 */
@Service
public class ShortCodeServiceImpl implements ShortCodeService {

    private static final String SHORT_CODE_KEY = "SHORT_CODE_KEY:";
    private static final String SHORT_CODE_PREFIX = "22";
    private static final int RETRY_TIMES = 5;
    private static final long EXPIRE_SECOND = 30*24*60*60;

    @Override
    public String createShortCode(String url) {
        String shortCode = generateShortCode();
        int count = 1;
        while (RedisHelper.exists(shortCode)) {
            if (count > RETRY_TIMES) throw BizException.throw300("短链编码重复次数超过" + RETRY_TIMES + "次。");
            shortCode = generateShortCode();
            count ++;
        }
        //添加到Redis
        RedisHelper.setAtExpire(SHORT_CODE_KEY + shortCode, EXPIRE_SECOND, url);
        return shortCode;
    }

    @Override
    public String getLongUrl(String shortCode) {
        if (Objects.isNull(shortCode)) throw new IllegalArgumentException("短链码不允许为空。");
        return RedisHelper.get(SHORT_CODE_KEY + shortCode);
    }

    private String generateShortCode() {
        return SHORT_CODE_PREFIX + ShortCodeUtils.generateShortCode(SnowflakeUl.getInstance().nextId(), ShortCodeUtils.SPEED_62);
    }

}
