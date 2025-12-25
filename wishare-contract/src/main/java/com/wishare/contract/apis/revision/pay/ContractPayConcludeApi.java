package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeListF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.remote.clients.AuthClient;
import com.wishare.contract.apps.remote.vo.TreeMenuV;
import com.wishare.contract.domains.enums.revision.ContractBusinessLineEnum;
import com.wishare.contract.domains.vo.ContractEnumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeListV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeTreeV;
import com.wishare.contract.domains.vo.revision.pay.ContractSignDateAttachV;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.pay.ContractPayConcludeAppService;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeListV;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 支出合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同订立信息表"})
@RequestMapping("/contractPayConclude")
public class ContractPayConcludeApi {

    private final ContractPayConcludeAppService contractPayConcludeAppService;
    @Autowired
    private AuthClient authClient;

    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayConcludeV.class)
    @GetMapping
    public ContractPayConcludeV get(@Validated @RequestBody ContractPayConcludeF contractPayConcludeF){
        return contractPayConcludeAppService.get(contractPayConcludeF);
    }

    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayConcludeV.class)
    @PostMapping("/get")
    public ContractPayConcludeV gets(@Validated @RequestBody ContractPayConcludeF contractPayConcludeF){
        return contractPayConcludeAppService.get(contractPayConcludeF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractPayConcludeV.class)
    @PostMapping("/list")
    public ContractPayConcludeListV list(@Validated @RequestBody ContractPayConcludeListF contractPayConcludeListF){
        return contractPayConcludeAppService.list(contractPayConcludeListF);
    }

    @ApiOperation(value = "支出收入合同详情信息", notes = "支出收入合同详情信息", response = ContractPayConcludeV.class)
    @PostMapping("/queryContractInfo")
    public ContractPayConcludeV queryContractInfo(@RequestParam("id") String id){
        return contractPayConcludeAppService.queryContractInfo(id).get(0);
    }

    @ApiOperation(value = "合同信息查询(包括计划)", notes = "下拉列表，默认数量20", response = ContractPayConcludeListV.class)
    @PostMapping("/queryInfo")
    public ContractPayConcludeListV queryInfo(@Validated @RequestBody ContractPayConcludeListF contractPayConcludeListF){
        return contractPayConcludeAppService.queryInfo(contractPayConcludeListF);
    }

    @ApiOperation(value = "合同信息查询(包括计划)-新")
    @PostMapping("/queryInfoNew")
    public ContractPayConcludeListV queryInfoNew(@Validated @RequestBody ContractPayConcludeListF contractPayConcludeListF){
        return contractPayConcludeAppService.queryInfoNew(contractPayConcludeListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractPayConcludeSaveF contractPayConcludeF){
        return contractPayConcludeAppService.save(contractPayConcludeF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractPayConcludeUpdateF contractPayConcludeF){
        contractPayConcludeAppService.update(contractPayConcludeF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPayConcludeAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractPayConcludeV.class)
    @PostMapping("/page")
    public PageV<ContractPayConcludeV> page(@RequestBody PageF<ContractPayConcludePageF> request) {
        return contractPayConcludeAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractPayConcludeV.class)
    @PostMapping("/pageFront")
    public PageV<ContractPayConcludeTreeV> frontPage(@RequestBody PageF<SearchF<ContractPayConcludeQuery>> request) {
        return contractPayConcludeAppService.frontPageV2(request);
    }

    @ApiOperation("获取合同签约日期和扫描件")
    @GetMapping("/contractSignDateAttach")
    public ContractSignDateAttachV contractSignDateAttach(@RequestParam("id") String id){
        return contractPayConcludeAppService.contractSignDateAttach(id);
    }

    @ApiOperation(value = "合并指定父合同的合同清单")
    @GetMapping("/fixFunds")
    public void frontPage(@RequestParam("pid") String pid) {
        contractPayConcludeAppService.handleMergePayFund(pid);
    }

    @ApiOperation(value = "批量合并指定父合同的合同清单")
    @PostMapping("/batchFixFunds")
    public void frontPage(@RequestBody MergePayFundPidF mergePayFundPidF) {
        contractPayConcludeAppService.handleMergePayFund(mergePayFundPidF);
    }

    @ApiOperation(value = "获取支出合同业务线下拉数据")
    @GetMapping("/getPayContractBusinessLineEnum")
    public List<ContractEnumV> getPayContractBusinessLineEnum() {

        List<TreeMenuV> menuList = authClient.getTree(Boolean.FALSE);
        List<ContractEnumV> resultList = new ArrayList<>();
        //支出合同ID：13803683281341L
        List<TreeMenuV> result = findNodesByPid(menuList, 13803683281341L);
        if(CollectionUtils.isEmpty(result)){
            return resultList;
        }
        List<Long> idList = result.stream().map(TreeMenuV:: getId).collect(Collectors.toList()) ;
        if(idList.size() == 3){
            ContractEnumV v = new ContractEnumV();
            v.setId(ContractBusinessLineEnum.物管.getCode());
            v.setName(ContractBusinessLineEnum.物管.getName());
            resultList.add(v);
            ContractEnumV v1 = new ContractEnumV();
            v1.setId(ContractBusinessLineEnum.建管.getCode());
            v1.setName(ContractBusinessLineEnum.建管.getName());
            resultList.add(v1);
            ContractEnumV v3 = new ContractEnumV();
            v3.setId(ContractBusinessLineEnum.商管.getCode());
            v3.setName(ContractBusinessLineEnum.商管.getName());
            resultList.add(v3);
            ContractEnumV v2 = new ContractEnumV();
            v2.setId(ContractBusinessLineEnum.全部.getCode());
            v2.setName(ContractBusinessLineEnum.全部.getName());
            resultList.add(v2);
            return resultList;
        }
        if(idList.contains(13803731508344L)){
            ContractEnumV v = new ContractEnumV();
            v.setId(ContractBusinessLineEnum.物管.getCode());
            v.setName(ContractBusinessLineEnum.物管.getName());
            resultList.add(v);
        }
        if(idList.contains(13803720190667L)){
            ContractEnumV v1 = new ContractEnumV();
            v1.setId(ContractBusinessLineEnum.建管.getCode());
            v1.setName(ContractBusinessLineEnum.建管.getName());
            resultList.add(v1);
        }
        if(idList.contains(13803720190668L)){
            ContractEnumV v3 = new ContractEnumV();
            v3.setId(ContractBusinessLineEnum.商管.getCode());
            v3.setName(ContractBusinessLineEnum.商管.getName());
            resultList.add(v3);
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
