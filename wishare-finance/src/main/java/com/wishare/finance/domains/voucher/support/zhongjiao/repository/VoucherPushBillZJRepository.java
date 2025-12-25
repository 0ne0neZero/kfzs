package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.apps.pushbill.vo.UploadLinkZJ;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJMoneyV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJDo2;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherPushBillZJMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherPushBillZJRepository extends ServiceImpl<VoucherPushBillZJMapper, VoucherBillZJ> {
    private final   VoucherPushBillZJMapper voucherPushBillZJMapper;

    public Page<VoucherBillZJ> pageBySearch(PageF<SearchF<?>> searchPageF) {
        QueryWrapper<?> queryModel = searchPageF.getConditions().getQueryModel();
//        queryModel.eq("approve_state", 0);
        return baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(queryModel).orderByDesc("id").orderByDesc("gmt_create"));
    }

    public Page<VoucherBillZJDo2> pageBySearch2(PageF<SearchF<?>> searchPageF) {
        QueryWrapper<?> queryModel = searchPageF.getConditions().getQueryModel();
//        queryModel.eq("approve_state", 0);
        return baseMapper.selectBySearch2(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(queryModel,"vb").groupBy("vb.id").orderByDesc("id").orderByDesc("gmt_create"));

    }

    public VoucherBillZJMoneyV getMoney(PageF<SearchF<?>> form) {
        return voucherPushBillZJMapper.getMoney(RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel(),"vb"));
    }


    public boolean addLinkZJ(UploadLinkZJF uploadLinkZJF) {
        UploadLinkZJ uploadLinkZJ = Global.mapperFacade.map(uploadLinkZJF, UploadLinkZJ.class);
        VoucherBillZJ voucherBillZJ = baseMapper.selectOne(new LambdaQueryWrapper<VoucherBillZJ>()
                .eq(VoucherBillZJ::getVoucherBillNo,uploadLinkZJF.getBillNo()));
        if (Objects.nonNull(voucherBillZJ)) {
            List<UploadLinkZJ> uploadLinkZJList = voucherBillZJ.getUploadLink();
            if(Objects.isNull(uploadLinkZJList)){
                uploadLinkZJList= Lists.newArrayList();
            }
            uploadLinkZJList.add(uploadLinkZJ);
            voucherBillZJ.setUploadLink(uploadLinkZJList);
            voucherBillZJ.setUploadNum(voucherBillZJ.getUploadNum() + 1);
            voucherBillZJ.setUpload(0);
            return baseMapper.updateById(voucherBillZJ) > 0;
        }
        return false;
    }

    public void delete(Long voucherBillId) {
        baseMapper.delete(voucherBillId);
    }


    public VoucherBillZJ queryByVoucherBillNo(String voucherBillNo){
       return baseMapper.queryByVoucherBillNo(voucherBillNo);
    }

}
