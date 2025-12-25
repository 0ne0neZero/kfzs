package com.wishare.finance.domains.configure.subject.strategy;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;

import java.util.List;

/**
 * 辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
public interface AssisteItemStrategy {

    /**
     * 辅助核算类型
     * @return
     */
    AssisteItemTypeEnum type();

    /**
     * 获取辅助核算信息列表
     * @param name 名称
     * @param code 编码
     * @param sbId 法定单位编码（暂定）
     * @return 辅助核算信息列表
     */
    List<AssisteItemOBV> list(String name, String code, String sbId);

    /**
     * 根据辅助核算id获取辅助核算信息
     * @param aiId 辅助核算信息
     * @return
     */
    AssisteItemOBV getById(String aiId);


    default AssisteItemOBV toAssisteItem(AssisteItemTypeEnum assisteItemTypeEnum, String code, String name){
        AssisteItemOBV assisteItemOBV = new AssisteItemOBV();
        assisteItemOBV.setType(assisteItemTypeEnum.getCode());
        assisteItemOBV.setCode(code);
        assisteItemOBV.setName(name);
        assisteItemOBV.setAscCode(assisteItemTypeEnum.getAscCode());
        assisteItemOBV.setAscName(assisteItemTypeEnum.getValue());
        return assisteItemOBV;
    }


}
