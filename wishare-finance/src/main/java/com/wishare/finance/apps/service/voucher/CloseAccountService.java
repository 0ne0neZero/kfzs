package com.wishare.finance.apps.service.voucher;

import java.util.ArrayList;
import java.util.List;

import com.wishare.finance.apps.model.voucher.fo.CloseAccountF;
import com.wishare.finance.apps.model.voucher.fo.CloseAccountsF;
import com.wishare.finance.domains.voucher.entity.CloseAccount;
import com.wishare.finance.domains.voucher.entity.VoucherTemplate;
import com.wishare.finance.domains.voucher.repository.CloseAccountRepository;
import com.wishare.finance.domains.voucher.service.CloseAccountDomainService;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 关账管理应用服务
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CloseAccountService {

    private final CloseAccountDomainService closeAccountDomainService;


    /**
     * 新增关账记录
     * @param closeAccountF
     */
    @Transactional
    public Long addCloseAccount(CloseAccountF closeAccountF) {
        return closeAccountDomainService.addCloseAccount(Global.mapperFacade.map(closeAccountF, CloseAccount.class));
    }

    /**
     * 关账/反关账
     * @param closeAccountF
     * @return
     */
    public boolean operateCloseAccount(CloseAccountF closeAccountF) {
        return closeAccountDomainService.operateCloseAccount(closeAccountF);
    }

    /**
     * 批量关账
     * @param closeAccountsF
     * @return
     */
    public boolean closeAccounts(CloseAccountsF closeAccountsF) {
        return closeAccountDomainService.closeAccounts(closeAccountsF);
    }
}
