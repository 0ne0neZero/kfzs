package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.chargeitem.vo.BPMDeptV;
import com.wishare.finance.domains.configure.chargeitem.entity.BPMDeptE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.BPMDeptMapper;
import com.wishare.starter.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BPMDeptRepository extends ServiceImpl<BPMDeptMapper, BPMDeptE> {

    @Autowired
    private BPMDeptMapper bpmDeptMapper;


    public List<BPMDeptV> BPMFilterDeptList() {
        List<BPMDeptE> list = bpmDeptMapper.selectList(Wrappers.<BPMDeptE>lambdaQuery().select(BPMDeptE::getDeptCode, BPMDeptE::getDeptName));

        return Global.mapperFacade.mapAsList(list, BPMDeptV.class);

    }
}
