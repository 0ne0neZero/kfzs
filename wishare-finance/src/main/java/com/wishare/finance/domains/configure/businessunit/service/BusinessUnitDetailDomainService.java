package com.wishare.finance.domains.configure.businessunit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.DeletedBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.UpdateBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitDetailRepository;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务单元service
 *
 * @author
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BusinessUnitDetailDomainService {

    private final BusinessUnitDetailRepository businessUnitDetailRepository;

}
