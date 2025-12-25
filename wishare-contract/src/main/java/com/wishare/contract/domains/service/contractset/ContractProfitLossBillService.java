package com.wishare.contract.domains.service.contractset;

import com.wishare.contract.domains.dto.contractset.ContractProfitLossBillD;
import com.wishare.contract.domains.entity.contractset.ContractProfitLossBillE;
import com.wishare.contract.domains.mapper.contractset.ContractProfitLossBillMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossBillV;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillPageF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillSaveF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillUpdateF;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wishare.contract.domains.consts.contractset.ContractProfitLossBillFieldConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 损益账单关联表
 * </p>
 *
 * @author ljx
 * @since 2022-10-17
 */
@Service
@Slf4j
public class ContractProfitLossBillService extends ServiceImpl<ContractProfitLossBillMapper, ContractProfitLossBillE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossBillMapper contractProfitLossBillMapper;

    /**
     * 定时获取待处理发票
     * @return
     */
    public List<ContractProfitLossBillD> invoiceGetTask() {
        return contractProfitLossBillMapper.invoiceGetTask();
    }
}
