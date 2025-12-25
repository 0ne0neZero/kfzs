package com.wishare.finance.apps.model.yuanyang.servicedata;

import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import lombok.Data;

import java.util.List;

/**
 * bpm通用凭证流程中间数据载体
 */
@Data
public class BpmProcessData {

    List<AccountBookE> accountBooks;

    List<BusinessUnitE> businessUnits;

}
