package com.wishare.finance.domains.configure.organization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryInvoiceConfListF;
import com.wishare.finance.domains.configure.organization.command.AddStatutoryInvoiceConfCommand;
import com.wishare.finance.domains.configure.organization.command.UpdateStatutoryInvoiceConfCommand;
import com.wishare.finance.domains.configure.organization.entity.StatutoryInvoiceConfE;
import com.wishare.finance.domains.configure.organization.repository.StatutoryInvoiceConfRepository;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatutoryInvoiceConfDomainService {

    private final StatutoryInvoiceConfRepository statutoryInvoiceConfRepository;

    /**
     * 新增电子开票设置
     *
     * @param command
     * @return
     */
    public Long addStatutoryInvoiceConf(AddStatutoryInvoiceConfCommand command) {
        StatutoryInvoiceConfE statutoryInvoiceConfE = Global.mapperFacade.map(command, StatutoryInvoiceConfE.class);
        checkMachineCode(command.getMachineCode());
        statutoryInvoiceConfRepository.save(statutoryInvoiceConfE);
        return statutoryInvoiceConfE.getId();
    }

    /**
     * 校检机器编码是否已经存在
     *
     * @param machineCode
     */
    private void checkMachineCode(String machineCode) {
        StatutoryInvoiceConfE statutoryInvoiceConfE = statutoryInvoiceConfRepository.getByMachineCode(machineCode);
        if (statutoryInvoiceConfE != null) {
            throw BizException.throw400("该机器编码已存在");
        }
    }

    /**
     * 修改电子开票设置
     *
     * @param command
     * @return
     */
    public Long updateStatutoryInvoiceConf(UpdateStatutoryInvoiceConfCommand command) {
        StatutoryInvoiceConfE statutoryInvoiceConfE = statutoryInvoiceConfRepository.getById(command.getId());
        if (null == statutoryInvoiceConfE) {
            throw BizException.throw400(ErrMsgEnum.STATUTORYINVOICECONF_NOT_FOUND.getErrMsg());
        }
        StatutoryInvoiceConfE update = Global.mapperFacade.map(command, StatutoryInvoiceConfE.class);
        statutoryInvoiceConfRepository.updateById(update);
        return statutoryInvoiceConfE.getId();
    }

    /**
     * 删除电子开票设置
     *
     * @param id
     * @return
     */
    public Boolean deleteStatutoryInvoiceConf(Long id) {
        StatutoryInvoiceConfE statutoryInvoiceConfE = statutoryInvoiceConfRepository.getById(id);
        if (null == statutoryInvoiceConfE) {
            throw BizException.throw400(ErrMsgEnum.STATUTORYINVOICECONF_NOT_FOUND.getErrMsg());
        }
        return  statutoryInvoiceConfRepository.removeById(statutoryInvoiceConfE);
    }

    /**
     * 分页查询电子开票设置
     *
     * @param form
     * @return
     */
    public Page<StatutoryInvoiceConfE> queryPage(PageF<SearchF<?>> form) {
        return statutoryInvoiceConfRepository.queryPage(form);
    }

    /**
     * 列表查询电子开票设置
     *
     * @param form
     * @return
     */
    public List<StatutoryInvoiceConfE> queryList(StatutoryInvoiceConfListF form) {
        return statutoryInvoiceConfRepository.queryList(form);
    }
}
