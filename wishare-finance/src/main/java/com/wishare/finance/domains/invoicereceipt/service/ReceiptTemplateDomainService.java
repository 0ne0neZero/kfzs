package com.wishare.finance.domains.invoicereceipt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateV;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ElectSignTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.EnableElectSignEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptTemplateRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author: Linitly
 * @date: 2023/8/7 19:55
 * @descrption:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceiptTemplateDomainService {

    private final ReceiptTemplateRepository receiptTemplateRepository;
    private final ChargeClient chargeClient;

    /**
     * 添加票据模板
     * @param receiptTemplateE 票据模板
     * @return
     */
    public Long add(ReceiptTemplateE receiptTemplateE) {
        this.checkTemplateName(receiptTemplateE);
        receiptTemplateRepository.save(receiptTemplateE);
        return receiptTemplateE.getId();
    }

    /**
     * 修改票据模板
     * @param receiptTemplateE 票据模板
     */
    @Transactional
    public boolean update(ReceiptTemplateE receiptTemplateE) {
        ReceiptTemplateE templateE = receiptTemplateRepository.getById(receiptTemplateE.getId());
        if (Objects.isNull(templateE)) {
            throw BizException.throw400("未找到该票据模板");
        }
        this.checkTemplateName(receiptTemplateE);
        boolean res = receiptTemplateRepository.updateById(receiptTemplateE);
        // 模板名称修改时，更新开票规则绑定的票据模板信息
        if (!receiptTemplateE.getTemplateName().equals(templateE.getTemplateName())) {
            chargeClient.updateReceiptTemplate(receiptTemplateE.getId(), receiptTemplateE.getTemplateName());
        }
        return res;
    }

    /**
     * 分页获取票据模板列表
     * @param form
     * @return
     */
    public Page<ReceiptTemplateE> pageList(PageF<SearchF<?>> form) {
        Page<?> page = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("rt.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.orderByDesc("rt.gmt_create");
        return receiptTemplateRepository.pageList(page, queryModel);
    }

    /**
     * 根据ID获取票据模板信息
     * @param id
     * @return
     */
    public ReceiptTemplateE get(Long id) {
        ReceiptTemplateE receiptTemplateE = receiptTemplateRepository.getById(id);
        if (Objects.isNull(receiptTemplateE)) {
            throw BizException.throw400("未找到该票据模板");
        }
        return receiptTemplateE;
    }

    /**
     * 获取票据模板列表
     * @return
     */
    public List<ReceiptTemplateE> list() {
        LambdaQueryWrapper<ReceiptTemplateE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReceiptTemplateE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq(ReceiptTemplateE::getDisabled, DataDisabledEnum.启用.getCode());
        return receiptTemplateRepository.list(queryWrapper);
    }

    /**
     * 删除票据模板
     * @param id
     * @return
     */
    public boolean delete(Long id) {
        // 判断开票规则是否绑定，如果有绑定返回错误信息
        Long count = chargeClient.countByReceiptTemplateId(id);
        if (Objects.nonNull(count) && count > 0) {
            throw BizException.throw400("该模板已绑定开票规则，请先解绑再删除!");
        }
        return receiptTemplateRepository.removeById(id);
    }

    /**
     * 校验模板名称是否重复
     * @param template 票据模板
     */
    private void checkTemplateName(ReceiptTemplateE template) {
        LambdaQueryWrapper<ReceiptTemplateE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReceiptTemplateE::getTemplateName, template.getTemplateName());
        queryWrapper.ne(Objects.nonNull(template.getId()), ReceiptTemplateE::getId, template.getId());
        long countByTemplateName = receiptTemplateRepository.count(queryWrapper);
        if (countByTemplateName > 0) {
            throw BizException.throw400("已存在相同的模板名称");
        }
    }
}
