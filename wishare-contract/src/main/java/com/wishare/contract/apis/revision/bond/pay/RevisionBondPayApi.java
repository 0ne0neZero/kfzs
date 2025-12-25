package com.wishare.contract.apis.revision.bond.pay;

import com.wishare.contract.domains.vo.revision.bond.pay.BondPayDetailV;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayPageF;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.bond.pay.RevisionBondPayAppService;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayV;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPaySaveF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayUpdateF;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayListV;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayListF;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 保证金改版-缴纳类保证金
 * </p>
 *
 * @author chenglong
 * @since 2023-07-28
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"保证金改版-缴纳类保证金"})
@RequestMapping("/revisionBondPay")
public class RevisionBondPayApi {

    private final RevisionBondPayAppService revisionBondPayAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = RevisionBondPayV.class)
    @GetMapping
    public RevisionBondPayV get(@Validated RevisionBondPayF revisionBondPayF){
        return revisionBondPayAppService.get(revisionBondPayF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = RevisionBondPayV.class)
    @PostMapping("/list")
    public RevisionBondPayListV list(@Validated @RequestBody RevisionBondPayListF revisionBondPayListF){
        return revisionBondPayAppService.list(revisionBondPayListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody RevisionBondPaySaveF revisionBondPayF){
        return revisionBondPayAppService.save(revisionBondPayF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody RevisionBondPayUpdateF revisionBondPayF){
        revisionBondPayAppService.update(revisionBondPayF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return revisionBondPayAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = RevisionBondPayV.class)
    @PostMapping("/page")
    public PageV<RevisionBondPayV> page(@RequestBody PageF<RevisionBondPayPageF> request) {
        return revisionBondPayAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = RevisionBondPayV.class)
    @PostMapping("/pageFront")
    public PageV<BondPayDetailV> frontPage(@RequestBody PageF<SearchF<RevisionBondPayE>> request) {
        return revisionBondPayAppService.frontPage(request);
    }

}
