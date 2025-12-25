package com.wishare.contract.apis.revision.bond.pay;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillPageF;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.bond.pay.PayBondRelationBillAppService;
import com.wishare.contract.domains.vo.revision.bond.pay.PayBondRelationBillV;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillSaveF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillUpdateF;
import com.wishare.contract.domains.vo.revision.bond.pay.PayBondRelationBillListV;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillListF;
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
 * 缴纳保证金改版关联单据明细表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-28
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"缴纳保证金改版关联单据明细表"})
@RequestMapping("/payBondRelationBill")
public class PayBondRelationBillApi {

    private final PayBondRelationBillAppService payBondRelationBillAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = PayBondRelationBillV.class)
    @GetMapping
    public PayBondRelationBillV get(@Validated PayBondRelationBillF payBondRelationBillF){
        return payBondRelationBillAppService.get(payBondRelationBillF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = PayBondRelationBillV.class)
    @PostMapping("/list")
    public PayBondRelationBillListV list(@Validated @RequestBody PayBondRelationBillListF payBondRelationBillListF){
        return payBondRelationBillAppService.list(payBondRelationBillListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody PayBondRelationBillSaveF payBondRelationBillF){
        return payBondRelationBillAppService.save(payBondRelationBillF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody PayBondRelationBillUpdateF payBondRelationBillF){
        payBondRelationBillAppService.update(payBondRelationBillF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return payBondRelationBillAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = PayBondRelationBillV.class)
    @PostMapping("/page")
    public PageV<PayBondRelationBillV> page(@RequestBody PageF<PayBondRelationBillPageF> request) {
        return payBondRelationBillAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = PayBondRelationBillV.class)
    @PostMapping("/pageFront")
    public PageV<PayBondRelationBillV> frontPage(@RequestBody PageF<SearchF<PayBondRelationBillE>> request) {
        return payBondRelationBillAppService.frontPage(request);
    }

}
