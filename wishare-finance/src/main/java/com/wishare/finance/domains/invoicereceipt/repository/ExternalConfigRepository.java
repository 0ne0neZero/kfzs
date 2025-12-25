package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wishare.finance.domains.invoicereceipt.entity.nuonuo.ExternalConfigE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.ExternalConfigMapper;
import com.wishare.starter.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Service
public class ExternalConfigRepository extends ServiceImpl<ExternalConfigMapper, ExternalConfigE> {

    @Autowired
    private ExternalConfigMapper externalConfigMapper;

    /**
     * 根据租户id和第三方前缀获取配置信息
     *
     * @param tenantId
     * @param externalPrefix
     * @param filter
     * @return
     */
    public String getConfig(String tenantId, String externalPrefix, String filter) {
        LambdaQueryWrapper<ExternalConfigE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExternalConfigE::getTenantId, tenantId);
        wrapper.eq(ExternalConfigE::getExternalPrefix, externalPrefix);
        List<ExternalConfigE> externalConfigs = externalConfigMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(externalConfigs)) {
            throw BizException.throw400("数据库没有查询到相应配置");
        }
        ExternalConfigE externalConfigE;
        if (externalConfigs.size() > 1) {
            externalConfigE = externalConfigs.stream()
                    .filter(config -> StringUtils.equals(config.getFilter(), filter))
                    .findFirst().orElseThrow(() -> BizException.throw400("数据库没有查询到相应配置"));
        } else {
            externalConfigE = externalConfigs.get(0);
        }
        String configJson = externalConfigE.getConfigJson();
        if (StringUtils.isBlank(configJson)) {
            throw BizException.throw400("第三方应用配置为空,请填充配置");
        }
        return configJson;
    }
}
