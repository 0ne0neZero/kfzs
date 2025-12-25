package com.wishare.finance.infrastructure.notify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 远洋通知
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/2
 */
@Getter
@Setter
public class YYNotification {

    private static final String YY_SIGN_KEY = "a6624ff617b7c8cc2ae49bd8b51aa430";

    /**
     * 通知信息
     */
    private String notifyId;

    /**
     * 随机串, 32位长度
     */
    private String nonce;
    /**
     * 通知类型 1收款，2付款，3退款，4账单支付，5收票，1001BPM报销
     */
    private String notifyType;
    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 签名字串
     */
    private String sign;

    /**
     * 数据信息
     */
    private Object data;

    public static YYNotification create(String notifyType, Object data, String ...ignore){
        YYNotification yyNotification = new YYNotification();
        yyNotification.setNotifyId(IdentifierFactory.generateNSUUID());
        yyNotification.setNotifyType(notifyType);
        yyNotification.setData(data);
        yyNotification.setTimestamp(DateTimeUtil.nowDateTime());
        yyNotification.setNonce(SignatureUtil.randomStr(32));
        Object signData;
        if (data instanceof List) {
            List dataList = (List) data;
            signData = dataList.get(0);
        } else {
            signData = data;
        }
        String sortSign = SignatureUtil.sortSign(SignatureUtil.beanToMap(signData), ignore);
        sortSign = sortSign + "&nonce=" + yyNotification.getNonce() + "&timestamp=" + yyNotification.getTimestamp();
        String key = yyNotification.getNotifyId() + YY_SIGN_KEY;
        yyNotification.setSign(SignatureUtil.hmacSha256ToStr(key, sortSign).toUpperCase());
        return yyNotification;
    }


}
