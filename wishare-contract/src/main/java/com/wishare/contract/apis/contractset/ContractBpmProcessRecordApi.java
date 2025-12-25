package com.wishare.contract.apis.contractset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordPageF;
import com.wishare.contract.domains.entity.contractset.ContractBpmProcessRecordE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractBpmProcessRecordAppService;
import com.wishare.contract.domains.vo.contractset.ContractBpmProcessRecordV;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordUpdateF;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 
 * </p>
 *
 * @author jinhui
 * @since 2023-02-24
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {""})
@RequestMapping("/contractBpmProcessRecord")
public class ContractBpmProcessRecordApi {

    private final ContractBpmProcessRecordAppService contractBpmProcessRecordAppService;
    @ApiOperation(value = "单个查询", notes = "", response = ContractBpmProcessRecordV.class)
    @GetMapping
    public ContractBpmProcessRecordV get(@ModelAttribute ContractBpmProcessRecordF contractBpmProcessRecordF){
        return contractBpmProcessRecordAppService.get(contractBpmProcessRecordF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractBpmProcessRecordV.class)
    @GetMapping("/list")
    public List<ContractBpmProcessRecordV> list(@RequestParam(value = "limit",defaultValue = "20") Integer limit, @ModelAttribute ContractBpmProcessRecordF contractBpmProcessRecordF){
        return contractBpmProcessRecordAppService.list(contractBpmProcessRecordF,limit);
    }

    @ApiOperation(value = "", notes = "")
    @PostMapping
    public Long save(@Validated @RequestBody ContractBpmProcessRecordSaveF contractBpmProcessRecordF){
        return contractBpmProcessRecordAppService.save(contractBpmProcessRecordF);
    }

    @ApiOperation(value = "更新", notes = "")
    @PutMapping
    public String update(@Validated @RequestBody ContractBpmProcessRecordUpdateF contractBpmProcessRecordF){
        contractBpmProcessRecordAppService.update(contractBpmProcessRecordF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "", notes = "")
    @DeleteMapping
    public boolean remove(@RequestBody ContractBpmProcessRecordF contractBpmProcessRecordF){
        return contractBpmProcessRecordAppService.remove(contractBpmProcessRecordF);
    }

    @ApiOperation(value = "分页列表", response = ContractBpmProcessRecordV.class)
    @PostMapping("/page")
    public PageV<ContractBpmProcessRecordV> page(@RequestBody PageF<ContractBpmProcessRecordPageF> request) {
        return contractBpmProcessRecordAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractBpmProcessRecordV.class)
    @PostMapping("/pageFront")
    public PageV<ContractBpmProcessRecordV> frontPage(@RequestBody PageF<SearchF<ContractBpmProcessRecordE>> request) {
        return contractBpmProcessRecordAppService.frontPage(request);
    }

}
