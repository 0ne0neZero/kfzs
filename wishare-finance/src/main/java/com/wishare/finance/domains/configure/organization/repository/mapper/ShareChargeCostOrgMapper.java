package com.wishare.finance.domains.configure.organization.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.organization.dto.ShareChargeCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.ShareChargeCostOrgE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dongpeng
 * @date 2023/7/22
 * @Description:
 */
@Mapper
public interface ShareChargeCostOrgMapper extends BaseMapper<ShareChargeCostOrgE> {

    List<ShareChargeCostOrgDto> getRelation(@Param("costOrdId") Long costOrdId);

    ShareChargeCostOrgDto getShareChargeInfo(@Param("costOrdId") Long costOrdId, @Param("shareChargeId") Long shareChargeId);
}
