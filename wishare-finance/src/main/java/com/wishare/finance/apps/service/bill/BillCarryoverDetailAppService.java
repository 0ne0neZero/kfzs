package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillCarryoverDetailE;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import com.wishare.finance.domains.bill.repository.BillApproveRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverDetailRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverRepository;
import com.wishare.finance.domains.bill.service.BillCarryoverDetailDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.ThreadPoolManager;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yyx
 * @project wishare-finance
 * @title BillCarryoverDetailAppService
 * @date 2023.09.21  11:02
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillCarryoverDetailAppService {

    private final BillCarryoverDetailDomainService billCarryoverDetailDomainService;

    private final BillApproveRepository approveRepository;


    /**
     * 将通过的结转信息转存到结转详情数据表
     * @return {@link Boolean}
     */
    public Boolean reverseCarryoverInfo(Boolean isTodayOpr) {
        ThreadPoolManager.execute(ThreadLocalUtil.curIdentityInfo(), isTodayOpr,
                (e) -> {
                    billCarryoverDetailDomainService.reverseCarryoverInfo(isTodayOpr);
                });
        return true;
    }

    public List<Long> getTargetIdByBillId(Long billId,String supCpUnitId) {
        BillApproveE billApprove = approveRepository.getCarryoverApproveByBillId(billId, supCpUnitId);
        if (billApprove == null) {
            return new ArrayList<>();
        }
        BillCarryoverRepository carryoverRepository = Global.ac.getBean(BillCarryoverRepository.class);
        BillCarryoverE carryoverE = carryoverRepository.getByApproveId(billApprove.getId());
        if (carryoverE == null) {
            return new ArrayList<>();
        }
        return carryoverE.getCarryoverDetail().stream().map(CarryoverDetail::getTargetBillId).collect(Collectors.toList());
    }

    /**
     * 校验是否当前帐单被结转过
     * @param targetId
     * @return
     */
    public boolean checkTargetIdHadCarryoverd(Long targetId) {

        List<BillCarryoverDetailE> billCarryoverDetailES = Global.ac.getBean(
                BillCarryoverDetailRepository.class).getBaseMapper().selectList(
                new LambdaQueryWrapper<BillCarryoverDetailE>().eq(
                        BillCarryoverDetailE::getTargetBillId, targetId));
        return CollectionUtils.isEmpty(billCarryoverDetailES)?false:true;
    }
}
