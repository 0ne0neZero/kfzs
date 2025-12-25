package com.wishare.finance.domains.configure.arrears.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.SearchV;
import com.wishare.finance.domains.configure.arrears.entity.ArrearsCategoryE;
import com.wishare.finance.domains.configure.arrears.repository.mapper.ArrearsCategoryMapper;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArrearsCategoryRepository extends ServiceImpl<ArrearsCategoryMapper, ArrearsCategoryE> {

    public List<SearchV> searchComponent(String searchKey) {
        return baseMapper.searchComponent(searchKey);
    }
}
