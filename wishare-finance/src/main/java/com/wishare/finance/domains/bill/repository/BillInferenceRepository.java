package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.BillInferenceE;
import com.wishare.finance.domains.bill.repository.mapper.BillInferenceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/24 22:05
 * @version: 1.0.0
 */
@Service
public class BillInferenceRepository extends ServiceImpl<BillInferenceMapper, BillInferenceE> {

    /**
     * 根据单个账单获取推凭记录
     * @param billId
     * @param type
     * @param event
     * @return
     */
    public BillInferenceE getByBillIdAndEventType(Long billId, Integer type, int event) {
        return baseMapper.getByBillIdAndEventType(billId, type, event);
    }

    /**
     * 根据事件类型以及账单类型获取推凭状态数据
     * @param eventType
     * @param billType
     * @return
     */
    public List<BillInferenceE> listByBillTypeAndEventType(Integer eventType, BillTypeEnum billType) {
        LambdaQueryWrapper<BillInferenceE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillInferenceE::getBillType, billType.getCode());
        queryWrapper.eq(BillInferenceE::getEventType, eventType);

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据单个账单获取推凭记录
     * @param id
     * @param type
     * @return
     */
    public BillInferenceE getByBillIdAndType(Long id, Integer type) {
        LambdaQueryWrapper<BillInferenceE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillInferenceE::getBillType, type);
        queryWrapper.eq(BillInferenceE::getBillId, id);
        List<BillInferenceE> list = baseMapper.selectList(queryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 批量获取账单推凭记录
     * @param billIds
     * @param type
     * @param event
     * @return
     */
    public List<BillInferenceE> listByBillIdAndEventType(List<Long> billIds, Integer type, int event) {
        return baseMapper.listByBillIdAndEventType(billIds, type, event);
    }

    /**
     * 批量获取账单推凭记录
     * @param billIds
     * @param type
     * @return
     */
    public List<BillInferenceE> listByBillIdAndType(List<Long> billIds, Integer type) {

        LambdaQueryWrapper<BillInferenceE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillInferenceE::getBillType, type);
        queryWrapper.in(BillInferenceE::getBillId, billIds);
        queryWrapper.groupBy(BillInferenceE::getBillId);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 批量删除失败的
     * @param inferIds
     * @return
     */
    public boolean batchDeleteInference(List<Long> inferIds) {
        return baseMapper.batchDeleteInference(inferIds);
    }
}
