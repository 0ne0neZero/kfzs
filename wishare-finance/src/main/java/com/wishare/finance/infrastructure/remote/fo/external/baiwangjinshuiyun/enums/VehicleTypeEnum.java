package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交通工具类型
 * @author dongpeng
 * @date 2023/10/26 11:33
 */
@Getter
@AllArgsConstructor
public enum VehicleTypeEnum {
    飞机("1","飞机"),
    火车("2","火车"),
    长途汽车("3","长途汽车"),
    公共交通("4","公共交通"),
    出租车("5","出租车"),
    汽车("6","汽车"),
    船舶("7","船舶"),
    其他("9","其他"),
    ;

    private String code;

    private String name;


}
