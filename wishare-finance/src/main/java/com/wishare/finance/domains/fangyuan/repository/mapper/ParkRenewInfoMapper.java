package com.wishare.finance.domains.fangyuan.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.fangyuan.dto.ParkRenewInfoPageDto;
import com.wishare.finance.domains.fangyuan.entity.ParkRenewInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ParkRenewInfoMapper extends BaseMapper<ParkRenewInfo> {

    IPage<ParkRenewInfoPageDto> queryPage(@Param("page") Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

}
