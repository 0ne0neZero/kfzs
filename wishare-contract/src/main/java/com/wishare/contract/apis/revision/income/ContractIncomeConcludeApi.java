package com.wishare.contract.apis.revision.income;

import com.alibaba.fastjson.JSONArray;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.MergePayFundPidF;
import com.wishare.contract.apps.remote.clients.AuthClient;
import com.wishare.contract.apps.remote.vo.TreeMenuV;
import com.wishare.contract.apps.service.revision.income.ContractIncomeConcludeAppService;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.enums.revision.ContractBusinessLineEnum;
import com.wishare.contract.domains.vo.ContractEnumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeListV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeTreeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractSignDateAttachV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 收入合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同订立信息表"})
@RequestMapping("/contractIncomeConclude")
public class ContractIncomeConcludeApi {

    private final ContractIncomeConcludeAppService contractIncomeConcludeAppService;
    @Autowired
    private AuthClient authClient;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractIncomeConcludeV.class)
    @GetMapping
    public ContractIncomeConcludeV get(@Validated ContractIncomeConcludeF contractIncomeConcludeF){
        return contractIncomeConcludeAppService.get(contractIncomeConcludeF);
    }

    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractIncomeConcludeV.class)
    @PostMapping("/get")
    public ContractIncomeConcludeV gets(@Validated @RequestBody ContractIncomeConcludeF contractIncomeConcludeF){
        return contractIncomeConcludeAppService.get(contractIncomeConcludeF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractIncomeConcludeV.class)
    @PostMapping("/list")
    public ContractIncomeConcludeListV list(@Validated @RequestBody ContractIncomeConcludeListF contractIncomeConcludeListF){
        return contractIncomeConcludeAppService.list(contractIncomeConcludeListF);
    }

    @ApiOperation(value = "合同信息查询(包括计划)", notes = "下拉列表，默认数量20", response = ContractIncomeConcludeV.class)
    @PostMapping("/queryInfo")
    public ContractIncomeConcludeListV queryInfo(@Validated @RequestBody ContractIncomeConcludeListF contractIncomeConcludeListF){
        return contractIncomeConcludeAppService.queryInfo(contractIncomeConcludeListF);
    }

    @ApiOperation(value = "合同信息查询(包括计划)-新")
    @PostMapping("/queryInfoNew")
    public ContractIncomeConcludeListV queryInfoNew(@Validated @RequestBody ContractIncomeConcludeListF contractIncomeConcludeListF){
        return contractIncomeConcludeAppService.queryInfoNew(contractIncomeConcludeListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractIncomeConcludeSaveF contractIncomeConcludeF){
        return contractIncomeConcludeAppService.save(contractIncomeConcludeF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractIncomeConcludeUpdateF contractIncomeConcludeF){
        contractIncomeConcludeAppService.update(contractIncomeConcludeF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractIncomeConcludeAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractIncomeConcludeV.class)
    @PostMapping("/page")
    public PageV<ContractIncomeConcludeV> page(@RequestBody PageF<ContractIncomeConcludePageF> request) {
        return contractIncomeConcludeAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractIncomeConcludeV.class)
    @PostMapping("/pageFront")
    public PageV<ContractIncomeConcludeTreeV> frontPage(@RequestBody PageF<SearchF<ContractIncomeConcludeQuery>> request) {
        return contractIncomeConcludeAppService.frontPageV2(request);
    }

    @ApiOperation("获取合同签约日期和扫描件")
    @GetMapping("/contractSignDateAttach")
    public ContractSignDateAttachV contractSignDateAttach(@RequestParam("id") String id){
        return contractIncomeConcludeAppService.contractSignDateAttach(id);
    }

    @ApiOperation(value = "手动推送合同数据到枫行梦", response = ContractIncomeConcludeV.class)
    @GetMapping("/contractInfoToFxm")
    public void contractInfoToFxm(@RequestParam String secret, @RequestParam List<String> contractIds) {
        if ("QFpeS6C2pwoP".equals(secret)) {
            contractIncomeConcludeAppService.contractInfoToFxm(contractIds);
        }
    }

    @ApiOperation(value = "合并指定父合同的合同清单")
    @GetMapping("/fixFunds")
    public void frontPage(@RequestParam("pid") String pid) {
        contractIncomeConcludeAppService.handleMergePayFund(pid);
    }

    @ApiOperation(value = "批量合并指定父合同的合同清单")
    @PostMapping("/batchFixFunds")
    public void frontPage(@RequestBody MergePayFundPidF mergePayFundPidF) {
        contractIncomeConcludeAppService.handleMergePayFund(mergePayFundPidF);
    }

    @ApiOperation(value = "获取收入合同业务线下拉数据")
    @GetMapping("/getIncomeContractBusinessLineEnum")
    public List<ContractEnumV> getIncomeContractBusinessLineEnum() {
        List<TreeMenuV> menuList = authClient.getTree(Boolean.FALSE);
        List<ContractEnumV> resultList = new ArrayList<>();
        //收入合同ID：13803676612131L
        List<TreeMenuV> result = findNodesByPid(menuList, 13803676612131L);
        if(CollectionUtils.isEmpty( result)){
            return resultList;
        }
        List<Long> idList = result.stream().map(TreeMenuV:: getId).collect(Collectors.toList()) ;
        if(idList.size() == 2){
            ContractEnumV v = new ContractEnumV();
            v.setId(ContractBusinessLineEnum.物管.getCode());
            v.setName(ContractBusinessLineEnum.物管.getName());
            resultList.add(v);
            ContractEnumV v1 = new ContractEnumV();
            v1.setId(ContractBusinessLineEnum.建管.getCode());
            v1.setName(ContractBusinessLineEnum.建管.getName());
            resultList.add(v1);
            ContractEnumV v2 = new ContractEnumV();
            v2.setId(ContractBusinessLineEnum.全部.getCode());
            v2.setName(ContractBusinessLineEnum.全部.getName());
            resultList.add(v2);
        }else if(idList.contains(13803720190643L)){
            ContractEnumV v = new ContractEnumV();
            v.setId(ContractBusinessLineEnum.物管.getCode());
            v.setName(ContractBusinessLineEnum.物管.getName());
            resultList.add(v);
        }else{
            ContractEnumV v1 = new ContractEnumV();
            v1.setId(ContractBusinessLineEnum.建管.getCode());
            v1.setName(ContractBusinessLineEnum.建管.getName());
            resultList.add(v1);
        }
        return resultList;
    }

    public List<TreeMenuV> findNodesByPid(List<TreeMenuV> menuList, Long targetPid) {
        List<TreeMenuV> result = new ArrayList<>();
        for (TreeMenuV menu : menuList) {
            // 检查当前节点是否符合条件
            if (targetPid.equals(menu.getPid())) {
                result.add(menu);
            }
            // 递归检查子节点
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                result.addAll(findNodesByPid(menu.getChildren(), targetPid));
            }
        }
        return result;
    }

}
