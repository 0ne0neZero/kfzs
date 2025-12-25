package com.wishare.finance.domains.fangyuan.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.fangyuan.vo.ParkRenewInfoPageV;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.fangyuan.dto.ParkRenewInfoPageDto;
import com.wishare.finance.domains.fangyuan.entity.ParkRenewInfo;
import com.wishare.finance.domains.fangyuan.repository.mapper.ParkRenewInfoMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ParkRenewInfoRepository extends ServiceImpl<ParkRenewInfoMapper, ParkRenewInfo> {

    public PageV<ParkRenewInfoPageDto> getParkRenewInfoList(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        // 增加条件
        queryWrapper.ne("b.state", BillStateEnum.作废.getCode())
                .ne("b.reversed", BillReverseStateEnum.已冲销.getCode());
        queryWrapper.groupBy("b.room_id");
        queryWrapper.groupBy("b.pay_time");
        queryWrapper.orderByDesc("b.pay_time");
        queryWrapper.orderByDesc("b.id");
        IPage<ParkRenewInfoPageDto> parkRenewInfoVIPage = baseMapper.queryPage(RepositoryUtil.convertMPPage(queryF),
                RepositoryUtil.putLogicDeleted(queryWrapper, "b"));

        return PageV.of(parkRenewInfoVIPage.getCurrent(), parkRenewInfoVIPage.getSize(), parkRenewInfoVIPage.getTotal(), parkRenewInfoVIPage.getRecords());
    }

    public ParkRenewInfo getByBillId(Long billId) {
        return getOne(new LambdaQueryWrapper<ParkRenewInfo>().eq(ParkRenewInfo::getBillId, billId));
    }


}
