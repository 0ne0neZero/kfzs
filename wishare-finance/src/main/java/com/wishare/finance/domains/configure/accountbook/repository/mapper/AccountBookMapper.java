package com.wishare.finance.domains.configure.accountbook.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountbookDTO;
import com.wishare.finance.apps.model.configure.accountbook.fo.AccountBookListF;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description: 账簿mapper
 */
@Mapper
public interface AccountBookMapper extends BaseMapper<AccountBookE> {

    /**
     * 分页查询账簿列表
     *
     * @param page
     * @param queryModel
     * @return
     */
    List<AccountBookE> queryPage(Page<?> page, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 获取账簿列表详情
     *
     * @param form
     * @param tenantId
     * @return
     */
    List<AccountbookDTO> listInfo(@Param("form") AccountBookListF form,@Param("tenantId") String tenantId);

    /**
     * 根据费项id和成本中心映射费项
     * @param chargeItemId 费项id
     * @param costCenterId 成本中心id
     * @return
     */
    AccountBookE selectByChargeItemAndCostCenter(@Param("chargeItemId") Long chargeItemId, @Param("costCenterId") Long costCenterId);

    List<AccountBookE> selectListByCostAndChargeItem(@Param("costCenterId") Long costCenterId,@Param("chargeItemId") Long chargeItemId,@Param("statutoryBodyId") Long statutoryBodyId);

    /**
     * 根据费项+成本中心+法定单位查询账簿
     * @param costCenterId
     * @param chargeItemId
     * @param statutoryBodyId
     * @return
     */
    List<AccountBookE> selectListByCostChargeStatutory(@Param("costCenterId") Long costCenterId,@Param("chargeItemId") Long chargeItemId,@Param("statutoryBodyId") Long statutoryBodyId);

    List<AccountBookE> queryList(@Param("ew") QueryWrapper<?> queryModel);

    List<Long> getIdByBusinessUnits(@Param("list") List<Long> businessUnits);
    Long   getStatutoryBodyIdByBookId(@Param("bookId") Long bookId);

}
