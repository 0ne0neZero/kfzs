package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.apps.model.reconciliation.fo.ListUserInfoF;
import com.wishare.finance.apps.model.reconciliation.fo.UserOrgRoleF;
import com.wishare.finance.apps.model.reconciliation.vo.UserMobileV;
import com.wishare.finance.apps.model.reconciliation.vo.UserOrgRoleV;
import com.wishare.finance.infrastructure.remote.vo.space.UserInfoRawV;
import com.wishare.finance.infrastructure.remote.vo.user.ArchivesUserV;
import com.wishare.finance.infrastructure.remote.vo.user.EnterpriseBaseInfoRV;
import com.wishare.finance.infrastructure.remote.vo.user.UserInfoRv;
import com.wishare.finance.infrastructure.remote.vo.user.UserTenantPermissionRv;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 认证中心
 *
 * @author wyt
 */
@OpenFeignClient(name = "wishare-auth", serverName = "认证中心", path = "/auth")
public interface AuthClient {

    @PostMapping(value = "/role/getByRoleAndOrgs", name = "根据组织和角色查询是否存在对应数据")
    Boolean getByRoleAndOrgs(@RequestParam List<String> orgIds, @RequestParam String roleId);

}
