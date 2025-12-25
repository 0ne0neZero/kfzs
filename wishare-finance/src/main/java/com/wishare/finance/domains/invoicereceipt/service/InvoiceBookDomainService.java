package com.wishare.finance.domains.invoicereceipt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceReceiveDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.GetInvoiceReceiptNoF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBookListF;
import com.wishare.finance.apps.model.invoice.invoicebook.dto.ReceiveDetailDto;
import com.wishare.finance.domains.invoicereceipt.command.invociebook.AddInvoiceBookCommand;
import com.wishare.finance.domains.invoicereceipt.command.invociebook.AddInvoiceReceiveCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceBookStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoicePoolE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveDetailedE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceBookRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoicePoolRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiveDetailedRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiveRepository;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.utils.InvoiceUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceBookDomainService {

    private final InvoiceBookRepository invoiceBookRepository;

    private final InvoicePoolRepository invoicePoolRepository;

    private final InvoiceReceiveRepository invoiceReceiveRepository;

    private final InvoiceReceiveDetailedRepository invoiceReceiveDetailedRepository;

    /**
     * 新增票本
     *
     * @param command
     * @return
     */
    @Transactional
    public Long addInvoiceBook(AddInvoiceBookCommand command) {
        InvoiceBookE invoiceBookE = Global.mapperFacade.map(command, InvoiceBookE.class);
        //根据票本保存票池
        List<InvoicePoolE> invoicePoolEList = invoiceBookE.generalInvoicePool();
        //校检发票编号是否重复
        checkInvoiceNum(invoicePoolEList,command.getType());
        invoiceBookRepository.save(invoiceBookE);
        invoicePoolRepository.saveBatch(invoicePoolEList);
        return invoiceBookE.getId();
    }

    /**
     * 校检发票编号是否重复
     *
     * @param invoicePoolEList
     * @param invoiceType
     */
    private void checkInvoiceNum(List<InvoicePoolE> invoicePoolEList, Integer invoiceType) {
        List<Long> invoiceNums = invoicePoolEList.stream().map(invoice -> Long.parseLong(invoice.getInvoiceNum())).collect(Collectors.toList());
        Long c1 = invoicePoolRepository.getByInvoiceNums(invoiceNums,invoiceType);
        if (c1 != null && c1 > 0) {
            throw BizException.throw400("该批次发票号存在重复");
        }
    }

    /**
     * 票本领用
     *
     * @param command
     * @return
     */
    @Transactional
    public Boolean receive(AddInvoiceReceiveCommand command) {
        InvoiceBookE invoiceBookE = invoiceBookRepository.getById(command.getReceiveInvoiceBookId());
        if (null == invoiceBookE) {
            throw BizException.throw400(ErrMsgEnum.INVOICE_BOOK_NO_EXISTS.getErrMsg());
        }
        //查询该票本全部已经领取数量
        Long receiveNumberTotal = invoiceReceiveRepository.getReceiveNumberTotal(command.getReceiveInvoiceBookId());
        if (receiveNumberTotal + command.getReceiveNumber() > invoiceBookE.getBuyQuantity()) {
            throw BizException.throw400(ErrMsgEnum.INVOICE_QUANTIT_MORE.getErrMsg());
        }
        invoiceBookE.setState(handleInvoiceBookState(invoiceBookE.getBuyQuantity(), receiveNumberTotal, command.getReceiveNumber()));
        InvoiceReceiveE invoiceReceiveE = command.getInvoiceReceiveE();
        List<InvoiceReceiveDetailedE> invoiceReceiveDetailedEList = command.getInvoiceReceiveDetailedE(invoiceReceiveE, command,invoiceBookE);
        invoiceReceiveRepository.save(invoiceReceiveE);
        invoiceReceiveDetailedRepository.saveBatch(invoiceReceiveDetailedEList);
        invoiceBookRepository.saveOrUpdate(invoiceBookE);
        //删除票池
        List<Long> invoiceNum = invoiceReceiveDetailedEList.stream().map(InvoiceReceiveDetailedE::getInvoiceNum).collect(Collectors.toList());
        invoicePoolRepository.deletByInvoiceNum(invoiceNum);
        return true;
    }

    /**
     * 分页查询票本
     *
     * @param form
     * @return
     */
    public Page<InvoiceBookE> queryPage(PageF<SearchF<?>> form) {
        return invoiceBookRepository.queryPage(form);
    }

    /**
     * 查询票本列表
     *
     * @param form
     * @return
     */
    public List<InvoiceBookE> querylist(InvoiceBookListF form) {
        return invoiceBookRepository.querylist(form);
    }

    /**
     * 删除票本
     *
     * @param id
     * @return
     */
    public Boolean deleteInvoiceBook(Long id) {
        InvoiceBookE invoiceBookE = invoiceBookRepository.getById(id);
        if (null == invoiceBookE) {
            throw BizException.throw400(ErrMsgEnum.INVOICE_BOOK_NO_EXISTS.getErrMsg());
        }
        if (invoiceBookE.getState() != InvoiceBookStateEnum.未领用.getCode()) {
            throw BizException.throw400(ErrMsgEnum.INVOICE_BOOK_HAS_RECEIVE.getErrMsg());
        }
        invoiceBookRepository.removeById(id);
        invoicePoolRepository.deleteByInvoiceBookId(id);
        return true;
    }

    /**
     * 根据id获取票本详情
     *
     * @param id
     * @return
     */
    public InvoiceBookE detailInvoiceBook(Long id) {
        return invoiceBookRepository.getById(id);
    }

    /**
     * 分页查询发票领用列表
     *
     * @param form
     * @return
     */
    public Page<InvoiceReceiveDto> receiveQueryPage(PageF<SearchF<?>> form) {
        return invoiceReceiveRepository.receiveQueryPage(form);
    }

    /**
     * 根据id获取票本领用详情
     *
     * @param id
     * @return
     */
    public ReceiveDetailDto receiveDetail(Long id) {
        InvoiceBookE invoiceBookE = invoiceBookRepository.getById(id);
        if (null == invoiceBookE) {
            throw BizException.throw400(ErrMsgEnum.INVOICE_BOOK_NO_EXISTS.getErrMsg());
        }
        Long receiveNumberTotal = invoiceReceiveRepository.getReceiveNumberTotal(id);

        ReceiveDetailDto receiveDetailDto = new ReceiveDetailDto();
        receiveDetailDto.setInvoiceBookId(invoiceBookE.getId());
        receiveDetailDto.setBuyOrgId(invoiceBookE.getBuyOrgId());
        receiveDetailDto.setBuyOrgName(invoiceBookE.getBuyOrgName());
        receiveDetailDto.setInvoiceBookNumber(invoiceBookE.getInvoiceBookNumber());
        receiveDetailDto.setCanReceiveQuantity(invoiceBookE.getBuyQuantity() - receiveNumberTotal);
        receiveDetailDto.setCanReceiveInvoiceStartNumber(InvoiceUtil.addOrSubtract(invoiceBookE.getInvoiceStartNumber(), receiveNumberTotal));
        receiveDetailDto.setBuyDate(invoiceBookE.getBuyDate());
        receiveDetailDto.setInvoiceCode(invoiceBookE.getInvoiceCode());
        return receiveDetailDto;
    }

    /**
     * 根据购买总数量和已领取数量，获得票本领用状态
     *
     * @param buyQuantity        购买总数
     * @param receiveNumberTotal 已经领取数量
     * @param receiveNumber      当前领取数量
     * @return
     */
    private Integer handleInvoiceBookState(Long buyQuantity, Long receiveNumberTotal, Long receiveNumber) {
        if (buyQuantity == receiveNumberTotal + receiveNumber) {
            return InvoiceBookStateEnum.全部领用.getCode();
        }
        if (buyQuantity > receiveNumberTotal + receiveNumber) {
            return InvoiceBookStateEnum.部分领用.getCode();
        }
        throw BizException.throw400("票本数量异常：购买总数:" + buyQuantity + " 已经领取数量:" + receiveNumberTotal + " 当前领取数量" + receiveNumber);
    }


    /**
     * 获取可以领用发票号
     *
     * @param form
     * @return
     */
    public List<String> getInvoiceReceiptNo(GetInvoiceReceiptNoF form) {
        List<InvoiceReceiveE> invoiceReceiveEList = invoiceReceiveRepository.getIdByCommunity(form.getCommunityId(),form.getType());
        List<String> invoiceReceiptNos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(invoiceReceiveEList)) {
            List<Long> invoiceReceiptIds = invoiceReceiveEList.stream().map(InvoiceReceiveE::getId).collect(Collectors.toList());
            invoiceReceiptNos = invoiceReceiveDetailedRepository.getInvoiceReceiptNo(invoiceReceiptIds,form.getPageSize(),form.getType());
        }
        return invoiceReceiptNos;
    }
}
