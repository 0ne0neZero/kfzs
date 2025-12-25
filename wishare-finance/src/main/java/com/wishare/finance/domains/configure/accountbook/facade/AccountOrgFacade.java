package com.wishare.finance.domains.configure.accountbook.facade;

import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.fo.OrgFinanceF;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.finance.infrastructure.remote.vo.org.MerchantRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.starter.exception.BizException;
import java.util.List;
import java.util.Objects;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/8/22
 * @Description:
 */
@Service
@Slf4j
public class AccountOrgFacade {

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    /**
     * 查询当前组织和父级组织列表
     *
     * @param statutoryBodyId
     * @return
     */
    public List<Long> orgByPid(Long statutoryBodyId) {
        List<Long> ids = orgClient.orgFinanceOrgByPid(statutoryBodyId);
        return ids;
    }

    /**
     * 获取当前租户下法定单位列表
     *
     * @param orgFinanceF orgFinanceF
     * @return List
     */
    public List<StatutoryBodyRv> getOrgFinanceList(OrgFinanceF orgFinanceF) {
        return orgClient.getOrgFinanceList(orgFinanceF);
    }

    /**
     * 根据id查询财务组织详情
     *
     * @param id
     * @return
     */
    public OrgFinanceRv orgFinance(String id) {
        return orgClient.orgFinance(id);
    }


    /**
     * 根据id获取租户详情
     *
     * @param id
     * @return
     */
    public OrgTenantRv tenantGetById(String id) {
        return orgClient.tenantGetById(id);
    }

    /**
     * 调用基座获取法定单位信息
     *
     * @param statutoryBodyId
     * @return
     */
    public OrgFinanceRv getOrgFinance(Long statutoryBodyId) {
        OrgFinanceRv orgFinanceRv = orgClient.orgFinance(statutoryBodyId.toString());
        if (Objects.isNull(orgFinanceRv)) {
            throw BizException.throw400("该法定单位不存在");
        }
        if (StringUtils.isBlank(orgFinanceRv.getTaxpayerNo())) {
            throw BizException.throw400("该法定单位纳税人识别号无效，请检查");
        }
        return orgFinanceRv;
    }

    /**
     * 根据客商名称获取客商
     * @param name
     * @return
     */
    public String getMerchantByName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        Long merchantId = orgClient.getMerchantIdByName(name);
        if (null == merchantId) {
            return "";
        }
        MerchantRv merchant = orgClient.getMerchantById(merchantId);
        if (merchant == null || StringUtils.isBlank(merchant.getCode())) {
            return "";
        }
        return merchant.getCode();
    }

    /**
     * 根据客商名称获取客商
     * @param name
     * @return
     */
    public List<MerchantRv> listMerchantByName(String name) {
        List<MerchantRv> merchantRvs = orgClient.listMerchantByName(name);
        if (StringUtils.isBlank(name) || "业主".contains(name)) {
            MerchantRv merchantRv = new MerchantRv();
            merchantRv.setName("业主");
            merchantRv.setId(4L);
            merchantRv.setCode("0000001C");
            merchantRvs.add(merchantRv);
        }
        return merchantRvs;
    }
}
