package com.wishare.finance.infrastructure.remote.fo.msg;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wishare.owl.converter.IBaseEnum;
import com.wishare.owl.enhance.swagger.annotations.SwaggerDisplayEnum;
import com.wishare.starter.exception.BizException;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author light
 * @since 2023/2/9
 */
@SwaggerDisplayEnum(name = "C端通知卡片内部内容展示风格有穷枚举")
@Getter
public enum NoticeConsumerMobileCardItemDisplayTypeEnum implements IBaseEnum<Integer> {
    LF_BOLD(1,"值换行+加粗"),
    HEAD_TAIL(2,"头+尾"),
    ;

    NoticeConsumerMobileCardItemDisplayTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    @Getter(AccessLevel.NONE)
    private final Integer code;
    private final String desc;


    @JsonValue
    @Override
    public Integer getCode() {
        return code;
    }

    @JsonCreator
    public static NoticeConsumerMobileCardItemDisplayTypeEnum parse(int code) {
        switch (code) {
            case 1:
                return LF_BOLD;
            case 2:
                return HEAD_TAIL;
        }
        throw BizException.throw400("解析" + code + "失败，请输入 1 或者 2，代表数据卡片，或者文章消息卡片");
    }
}
