package com.wishare.finance.domains.mdm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.apps.pushbill.vo.BankAccountV;
import com.wishare.finance.domains.mdm.entity.Mdm73E;
import com.wishare.finance.domains.mdm.entity.Mdm97E;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface Mdm97Mapper extends BaseMapper<Mdm97E> {

    int batchInsert(@Param("list") List<Mdm97E> list);

    List<BankAccountV> selectValidBankByOid(@Param("oid") String oid);
}
