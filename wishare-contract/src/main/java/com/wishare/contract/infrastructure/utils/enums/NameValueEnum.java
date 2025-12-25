package com.wishare.contract.infrastructure.utils.enums;

/**
 * Copyright @ 2022 慧享科技 Co. Ltd.
 * All right reserved.
 *
 * @author: PengAn
 * @create: 2022-04-14
 * @description: 业务枚举类接口，包含name，value
 **/
public interface NameValueEnum<T> extends ValueEnum<T> {
    String getName();
}
