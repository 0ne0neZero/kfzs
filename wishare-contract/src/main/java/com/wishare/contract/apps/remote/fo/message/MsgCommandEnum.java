package com.wishare.contract.apps.remote.fo.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 指令集合
 * </p>
 *
 * @author light
 * @since 2022/10/19
 */

public enum MsgCommandEnum  {
    /**
     * hello 指令
     */
    HELLO(100001L,String.class),

    /**
     * 发送站内信指令
     */
    SEND_MESSAGE(100002L, com.wishare.contract.apps.remote.fo.message.MessageF.class),

    /**
     * 发送短信指令
     */
    SEND_SMS(100003L, com.wishare.contract.apps.remote.fo.message.SmsSendF.class);

    /**
     * 指令编码，上下文唯一
     */
    private final long commandCode;

    private final Class<?> specialType;
    MsgCommandEnum(long commandCode, Class<?> specialType) {
        this.commandCode = commandCode;
        this.specialType = specialType;
    }

    @JsonValue
    public long getCommandCode() {
        return commandCode;
    }



    @JsonCreator
    public static com.wishare.contract.apps.remote.fo.message.MsgCommandEnum parse(Integer commandCode) {
        if (commandCode == null) {
            return null;
        }
        for (com.wishare.contract.apps.remote.fo.message.MsgCommandEnum item : com.wishare.contract.apps.remote.fo.message.MsgCommandEnum.values()) {
            if (item.commandCode == commandCode) {
                return item;
            }
        }
        return null;
    }

}
