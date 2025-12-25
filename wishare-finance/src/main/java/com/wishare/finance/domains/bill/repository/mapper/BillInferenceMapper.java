package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.bill.entity.BillInferenceE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/24 22:06
 * @version: 1.0.0
 */
@Mapper
public interface BillInferenceMapper extends BaseMapper<BillInferenceE> {

    /**
     * 根据单个账单获取推凭记录
     * @param billId
     * @param type
     * @param eventType
     * @return
     */
    BillInferenceE getByBillIdAndEventType(@Param("billId") Long billId, @Param("type") Integer type, @Param("eventType") int eventType);

    /**
     * 批量获取账单推凭记录
     * @param billIds
     * @param type
     * @param event
     * @return
     */
    List<BillInferenceE> listByBillIdAndEventType(@Param("billIds") List<Long> billIds, @Param("type") Integer type, @Param("eventType") int event);

    /**
     * 批量删除
     * @param inferIds
     * @return
     */
    boolean batchDeleteInference(@Param("billIds") List<Long> inferIds);
}
