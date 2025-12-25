package com.wishare.finance.apps.service.acl;

import java.util.List;

import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostForBlockF;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostV;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * ACL 防腐层
 */
@Service
@Slf4j
public class AclOrgClientService {

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    /**
     * 当前分接口远洋专用
     * @param orgFinanceCostForBlockF
     * 当前接口返回的参数 orgFinanceCostV 【id（主键（成本id））、nameCn（组织中文名称）、block（期区string）、costCode（成本中心编码）、communityId、tenantId】
     * @return
     */
    @ApiOperation(value = "(远洋)根据项目下的期区ids查询成本信息集", notes = "(远洋)根据项目下的期区ids查询成本信息集")
    public List<OrgFinanceCostV> getByBlockIds(OrgFinanceCostForBlockF orgFinanceCostForBlockF){
        return orgClient.getByBlockIds(orgFinanceCostForBlockF);
    }

    @ApiOperation(value = "根据项目ids查询财务组织",notes = "根据项目id查询财务组织id")
    public List<OrgFinanceCostV> queryFinanceIdByCommunityId(List<String> communityIds){
        return orgClient.queryFinanceIdByCommunityId(communityIds);
    }

    /**
     * 根据id查询成本信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询成本中心",notes = "根据id查询成本中心")
    public OrgFinanceCostRv getOrgFinanceCostById(Long id){
        return orgClient.getOrgFinanceCostById(id);

    }


    /**
     * 根据项目id获取成本信息
     * @param communityId
     * @return
     */
    public OrgFinanceCostV getOrgFinanceCostByCommunityId(@RequestParam("communityId") String communityId){
        return orgClient.getOrgFinanceCostByCommunityId(communityId);
    }
}
