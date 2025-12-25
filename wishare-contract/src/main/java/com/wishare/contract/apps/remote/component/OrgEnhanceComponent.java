package com.wishare.contract.apps.remote.component;

import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.apps.remote.vo.org.OrgDetailsRV;
import com.wishare.owl.util.OptionalCollection;
import com.wishare.starter.interfaces.ApiBase;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrgEnhanceComponent implements ApiBase {
    private final OrgFeignClient orgFeignClient;

    /**
     * 获取当前用户租户名称
     * @return
     */
    public String getTenantName() {
        ResponseEntity<TenantInfoRv> result = orgFeignClient.getTenantInfoById(getTenantId().get());
        if (result == null || HttpStatus.OK != result.getStatusCode()) {
            log.error("根据租户ID{}获取租户信息失败", result);
            return null;
        }
        return result.getBody().getName();
    }

    /**
     * 获取当前用户租户英文简称名称
     * @return
     */
    public String getEnglishTenantName() {
        ResponseEntity<TenantInfoRv> result = orgFeignClient.getTenantInfoById(getTenantId().get());
        if (result == null || HttpStatus.OK != result.getStatusCode()) {
            log.error("根据租户ID{}获取租户信息失败", result);
            return null;
        }
        return result.getBody().getEnglishName().toUpperCase();
    }

    /**
     * 获取当前用户租户英文简称名称-位数
     * @return
     */
    public String getEnglishTenantNameByDigit(Integer digit) {
        ResponseEntity<TenantInfoRv> result = orgFeignClient.getTenantInfoById(getTenantId().get());
        if (result == null || HttpStatus.OK != result.getStatusCode()) {
            log.error("根据租户ID{}获取租户信息失败", result);
            return null;
        }
        String originEnglishName = result.getBody().getEnglishName().toUpperCase();
        if(originEnglishName.length() > digit) {
            return originEnglishName.substring(0, digit);
        }
        return originEnglishName;
    }

    /**
     * 获取当前用户租户英文简称名称-位数
     * @return
     */
    public String getEnglishTenantNameByDigitAndTenantId(String tenantId, Integer digit) {
        ResponseEntity<TenantInfoRv> result = orgFeignClient.getTenantInfoById(tenantId);
        if (result == null || HttpStatus.OK != result.getStatusCode()) {
            log.error("根据租户ID{}获取租户信息失败", result);
            return null;
        }
        String originEnglishName = result.getBody().getEnglishName().toUpperCase();
        if(originEnglishName.length() > digit) {
            return originEnglishName.substring(0, digit);
        }
        return originEnglishName;
    }

    /**
     * 获取某些组织下所有的子组织
     * @param orgIds
     * @return
     */
    public Set<String> getChildrenOrgListByOrgId(List<String> orgIds) {
        Set<String> orgIdList = new HashSet<>();
        orgIdList.addAll(orgIds);
        OptionalCollection.ofNullable(orgIds).ifNotEmpty(v -> {
            v.forEach(v1 -> {
                OptionalCollection.ofNullable(orgFeignClient.getOrgListByOrgId(Long.valueOf(v1)))
                    .ifNotEmpty(orgDetailsRVS -> {
                        List<String> childrens = orgDetailsRVS.stream().map(OrgDetailsRV::getId).map(id -> id.toString()).collect(
                            Collectors.toList());
                        orgIdList.addAll(childrens);
                    });
            });
        });

        return orgIdList;
    }


    /**
     * 根据组织ID获取组织名称
     * @param orgId
     * @return
     */
    public String getOrgNameByOrgId(String orgId) {
        ResponseEntity<OrgDetailsRV> result = orgFeignClient.getOrgById(Long.valueOf(orgId));
        if (result == null || HttpStatus.OK != result.getStatusCode()) {
            log.error("根据组织ID{}获取组织信息失败", result);
            return null;
        }
        return result.getBody().getOrgName();
    }

}
