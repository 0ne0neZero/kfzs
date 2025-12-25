//package com.wishare.finance.apps.service.configure.subject;
//
//import com.wishare.finance.domains.configure.accountbook.facade.AmpFinanceFacade;
//import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
//import com.wishare.finance.infrastructure.remote.fo.ampfinance.AssisteAccountRF;
//import com.wishare.finance.infrastructure.remote.fo.ampfinance.AssisteAccountSyncRF;
//import com.wishare.finance.infrastructure.remote.vo.ampfinance.AssisteAccountRv;
//import com.wishare.starter.beans.PageF;
//import com.wishare.starter.beans.PageV;
//import com.wishare.starter.exception.BizException;
//import com.wishare.starter.helpers.UidHelper;
//import com.wishare.tools.starter.fo.search.SearchF;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
///**
// * @author xujian
// * @date 2022/12/2
// * @Description:
// */
//@Service
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class AssisteAccountAppService {
//
//    private final AmpFinanceFacade ampFinanceFacade;
//
//    /**
//     * 分页获取辅助核算
//     *
//     * @param form
//     * @return
//     */
//    public PageV<AssisteAccountRv> assisteAccountPage(PageF<SearchF<?>> form) {
//        return ampFinanceFacade.assisteAccountPage(form);
//    }
//
//    /**
//     * 获取辅助核算列表
//     *
//     * @return
//     */
//    public List<AssisteAccountRv> assisteAccountList(AssisteAccountRF form) {
//        return ampFinanceFacade.assisteAccountList(form);
//    }
//
//    /**
//     * 辅助核算同步
//     *
//     * @param form
//     * @return
//     */
//    public Boolean assisteAccountSync(AssisteAccountSyncRF form) {
//        return ampFinanceFacade.assisteAccountSync(form);
//    }
//
//    /**
//     * 导出
//     *
//     * @param form
//     * @param response
//     * @return
//     */
//    public Boolean export(PageF<SearchF<?>> form, HttpServletResponse response) {
//        List<AssisteAccountRv> assisteAccountRvList = ampFinanceFacade.exportList(form);
//        if (CollectionUtils.isNotEmpty(assisteAccountRvList)) {
//            String fileName = "辅助核算导出" + UidHelper.nextIdByDateStr("assiste_account_export");
//            try {
//                ExcelUtil.export(fileName, assisteAccountRvList, AssisteAccountRv.class, response);
//                return true;
//            } catch (Exception e) {
//                throw BizException.throw400("数据导出失败：" + e.getMessage());
//            }
//        }
//        throw BizException.throw400("导出数据为空");
//    }
//
//
//}
