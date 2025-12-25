package com.wishare.finance.domains.configure.subject.repository;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteBizTypeMapper;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteInoutclassMapper;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteOrgDeptMapper;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteOrgMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 辅助核算资源库
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssisteItemRepository {

    //private final AssisteOrgMapper assisteOrgMapper;
    //private final AssisteOrgDeptMapper assisteOrgDeptMapper;
    //private final AssisteBizTypeMapper assisteBizTypeMapper;
    //private final AssisteInoutclassMapper assisteInoutclassMapper;

    ///**
    // * 辅助核算
    // * @param name 名称
    // * @param code 编码
    // * @param type 类型
    // * @return
    // */
    //public List<AssisteItemOBV> getBaseAssisteItem(String name, String code, String type){
    //    switch (AssisteItemTypeEnum.valueOf(type)){
    //        case 部门:
    //            return assisteOrgDeptMapper.selectAssisteItems(name, code);
    //        case 业务单元:
    //            return assisteOrgMapper.selectAssisteItems(name, code);
    //        case 业务类型:
    //            return assisteBizTypeMapper.selectAssisteItems(name, code);
    //        case 收支项目:
    //            return assisteInoutclassMapper.selectAssisteItems(name, code);
    //        case 客商:
    //            break;
    //        case 项目:
    //
    //            break;
    //        case 人员档案:
    //            break;
    //        case 银行账户:
    //            break;
    //        case 增值税税率:
    //            break;
    //        case 存款账户性质:
    //            break;
    //    }
    //    return new ArrayList<>();
    //}


}
