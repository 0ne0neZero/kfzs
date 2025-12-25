package com.wishare.finance.domains.reconciliation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationYinlianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2023/2/19
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconciliationYinlianDomainService {

    private final ReconciliationYinlianRepository reconciliationYinlianRepository;

    /**
     * 批量添加银联数据
     *
     * @param docList
     */
    public Boolean addBatch(List<ReconciliationYinlianE> docList) {
       return reconciliationYinlianRepository.saveBatch(docList, IService.DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量获取未对账银联数据
     */
    public List<ReconciliationYinlianE> listNotReconcile(List<String> seqIds) {
        LambdaQueryWrapper<ReconciliationYinlianE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ReconciliationYinlianE::getSeqId, seqIds);
        wrapper.eq(ReconciliationYinlianE::getState, 0);
        return reconciliationYinlianRepository.list(wrapper);
    }

    /**
     * 批量获取未对账银联数据 远洋专用
     */
    public List<ReconciliationYinlianE> listYYNotReconcile(List<String> seqIds) {
        LambdaQueryWrapper<ReconciliationYinlianE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ReconciliationYinlianE::getSearchReferenceNo, seqIds);
        wrapper.eq(ReconciliationYinlianE::getState, 0);
        return reconciliationYinlianRepository.list(wrapper);
    }

    /**
     * 根据id列表更新状态
     * @param rids
     * @param state
     * @return
     */
    public boolean updateStateByIds(List<Long> rids, int state){
        return reconciliationYinlianRepository.update(new LambdaUpdateWrapper<ReconciliationYinlianE>()
                .set(ReconciliationYinlianE::getState, state)
                .in(ReconciliationYinlianE::getId, rids));
    }

    public ReconciliationYinlianE getReconciliationYinlianEByFileName(String fileName){
        return reconciliationYinlianRepository.getReconciliationYinlianEByFileName(fileName);
    }
}
