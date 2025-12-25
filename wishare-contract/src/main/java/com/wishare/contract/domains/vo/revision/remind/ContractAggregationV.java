package com.wishare.contract.domains.vo.revision.remind;

import com.wishare.contract.domains.entity.revision.remind.ContractRemindMessageConfigE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@ApiModel(value = "合同消息预警-数据聚合类")
public class ContractAggregationV {

    @ApiModelProperty(value = "合同到期预警-配置列表")
    private List<ContractRemindMessageConfigE> expireConfigs;

    @ApiModelProperty(value = "合同到期预警-合同与计划信息列表")
    private List<ContractAndPlanInfoV> expireList;

    @ApiModelProperty(value = "收款到期提醒-配置列表")
    private List<ContractRemindMessageConfigE> remindConfigs;

    @ApiModelProperty(value = "收款到期提醒-合同与计划信息列表")
    private List<ContractAndPlanInfoV> remindList;

    @ApiModelProperty(value = "收款逾期预警-配置列表")
    private List<ContractRemindMessageConfigE> overdueConfigs;

    @ApiModelProperty(value = "收款逾期预警-合同与计划信息列表")
    private List<ContractAndPlanInfoV> overdueList;

    @ApiModelProperty(value = "消息配置涉及到的角色id集合")
    private Set<String> roleIds;

    @ApiModelProperty(value = "合同所属项目id集合")
    private Set<String> communityIds;

    @ApiModelProperty(value = "合同所属项目涉及到的组织id集合")
    private Set<String> orgIds;

    @ApiModelProperty(value = "项目-组织映射map")
    private Map<String, CommunityOrgV> communityOrgMap;

    @ApiModelProperty(value = "员工id以及角色/组织列表")
    private List<UserOrgRoleV> userOrgRoles;

    public void initData() {
        this.expireConfigs = new ArrayList<>();
        this.expireList = new ArrayList<>();
        this.remindConfigs = new ArrayList<>();
        this.remindList = new ArrayList<>();
        this.overdueConfigs = new ArrayList<>();
        this.overdueList = new ArrayList<>();
        this.roleIds = new HashSet<>();
        this.communityIds = new HashSet<>();
        this.orgIds = new HashSet<>();
        this.communityOrgMap = new HashMap<>();
        this.userOrgRoles = new ArrayList<>();
    }

}
