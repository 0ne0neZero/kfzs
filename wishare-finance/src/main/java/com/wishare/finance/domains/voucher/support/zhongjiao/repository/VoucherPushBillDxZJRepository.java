package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.apps.pushbill.vo.UploadLinkZJ;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJDo2;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJMoneyV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJV;
import com.wishare.finance.apps.pushbill.vo.dx.vo.VoucherBillAutoFileZJVo;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherPushBillDxZJMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherPushBillDxZJRepository extends ServiceImpl<VoucherPushBillDxZJMapper, VoucherBillDxZJ> {
    private final   VoucherPushBillDxZJMapper voucherPushBillZJMapper;


    public PageV<VoucherBillZJV> pageBySearch(PageF<SearchF<?>> searchPageF) {
        QueryWrapper<?> queryModel = searchPageF.getConditions().getQueryModel();
        Page<VoucherBillDxZJ> voucherBillList = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(queryModel).orderByDesc("id").orderByDesc("gmt_create"));
        return RepositoryUtil.convertMoneyPage(voucherBillList, VoucherBillZJV.class);
    }

    public Page<VoucherBillZJDo2> pageBySearch2(PageF<SearchF<?>> searchPageF) {
        QueryWrapper<?> queryModel = searchPageF.getConditions().getQueryModel();
//        queryModel.eq("approve_state", 0);
        return baseMapper.selectBySearch2(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(queryModel,"vb").groupBy("vb.id").orderByDesc("id").orderByDesc("gmt_create"));

    }

    public VoucherBillZJMoneyV getMoney(PageF<SearchF<?>> form) {
        VoucherBillZJMoneyV moneyV =
                voucherPushBillZJMapper.getMoney(RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
        if (Objects.isNull(moneyV)){
            moneyV = new VoucherBillZJMoneyV();
            moneyV.setMoney(BigDecimal.ZERO);
        }
        return moneyV;
    }


    public boolean addLinkZJ(UploadLinkZJF uploadLinkZJF) {
        UploadLinkZJ uploadLinkZJ = Global.mapperFacade.map(uploadLinkZJF, UploadLinkZJ.class);
        VoucherBillDxZJ voucherBillZJ = baseMapper.selectOne(new LambdaQueryWrapper<VoucherBillDxZJ>()
                .eq(VoucherBillDxZJ::getVoucherBillNo,uploadLinkZJF.getBillNo()));
        if (Objects.nonNull(voucherBillZJ)) {
            List<UploadLinkZJ> uploadLinkZJList = voucherBillZJ.getUploadLink();
            if(CollectionUtils.isNotEmpty(uploadLinkZJList)){
                List<UploadLinkZJ> uploadLinkZJS = uploadLinkZJList.stream().filter(file -> file.getUploadLink().equals(uploadLinkZJ.getUploadLink())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(uploadLinkZJS)){
                    UploadLinkZJ uploadLinkZJLast = uploadLinkZJS.get(0);
                    if(Objects.isNull(uploadLinkZJLast.getImageIdZJ()) && Objects.nonNull(uploadLinkZJ.getImageIdZJ())){
                        uploadLinkZJLast.setImageIdZJ(uploadLinkZJ.getImageIdZJ());
                        return baseMapper.updateById(voucherBillZJ) > 0;
                    }else {
                        return true;
                    }
                }
            }else{
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


    public VoucherBillDxZJ queryByVoucherBillNo(String voucherBillNo){
       return baseMapper.queryByVoucherBillNo(voucherBillNo);
    }

    public List<VoucherBillAutoFileZJVo> selectAutoFileVo(String voucherBillNo, Integer billEventType) {
        return baseMapper.selectAutoFileVo(voucherBillNo, billEventType);
    }

    //根据条件查询报账单数据
    public VoucherBillDxZJ getVoucherBillDxZJByQuery(String voucherBillNo) {
        return baseMapper.getVoucherBillDxZJByQuery(voucherBillNo);
    }

    //根据结算单id查询报账单数据
    public List<VoucherBillDxZJ> getBillDxZjBySettlementId(String settlementId) {
        return baseMapper.getBillDxZjBySettlementId(settlementId);
    }
    //根据ID更改来自合同数据
    public void updateBilDxZjFromContract(Long id, String otherBusinessReasons, String externalDepartmentCode, Integer calculationMethod) {
        baseMapper.updateBilDxZjFromContract(id, otherBusinessReasons, externalDepartmentCode, calculationMethod);
    }

}
