package com.wishare.contract.infrastructure.utils;

import com.wishare.contract.domains.consts.ContractAddValueEnum;
import com.wishare.contract.domains.consts.ContractAreaEnum;
import com.wishare.contract.domains.consts.ContractAreaSonEnum;
import com.wishare.contract.domains.consts.ContractBusTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 对接bpm,找寻流id
 *
 * @author: PengAn
 * @create: 2022-07-13
 * @description:
 **/
@Component
@Slf4j
public class BpmProDefUtils {

    /**
     * 逗号,用于分隔图片
     */
    private static final String COMMA = ",";

    /**
     * 根据标准组织名称，查询区域归属
     * @param standardOrgName
     * @return
     */
    public String getAreaName(String standardOrgName) {
        if(StringUtils.isNotBlank(standardOrgName)){
            for (ContractAreaEnum ee : ContractAreaEnum.values()) {
                if (standardOrgName.contains(ee.getCode())) {
                    return ee.getCode();
                }
            }
        }
        return null;
    }

    /**
     * 根据业务类型，查询增值业务
     * @param busName
     * @return
     */
    public String getAddValueName(String busName) {
        if(StringUtils.isNotBlank(busName)){
            for (ContractAddValueEnum ee : ContractAddValueEnum.values()) {
                if (busName.contains(ee.getCode())) {
                    return ee.getCode();
                }
            }
        }
        return null;
    }

    /**
     * 根据获取业务类型，包括增值外业务类型
     * @param busName
     * @return
     */
    public String getBusName(String busName) {
        if(StringUtils.isNotBlank(busName)){
            for (ContractBusTypeEnum ee : ContractBusTypeEnum.values()) {
                if (busName.contains(ee.getCode())) {
                    return ee.getCode();
                }
            }
        }
        return null;
    }

    /**
     * 根据标准组织名称，查询具体属于哪个区域
     * @param standardOrgName
     * @return
     */
    public String getAreaSonName(String standardOrgName) {
        if(StringUtils.isNotBlank(standardOrgName)){
            for (ContractAreaSonEnum ee : ContractAreaSonEnum.values()) {
                if (standardOrgName.contains(ee.getCode())) {
                    return ee.getCode();
                }
            }
        }
        return null;
    }

}
