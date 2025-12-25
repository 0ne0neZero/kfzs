package com.wishare.finance.domains.configure.arrears.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.SearchV;
import com.wishare.finance.domains.configure.arrears.entity.ArrearsCategoryE;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ArrearsCategoryMapper extends BaseMapper<ArrearsCategoryE> {

    List<SearchV> searchComponent(@Param("searchKey")String searchKey);

}
