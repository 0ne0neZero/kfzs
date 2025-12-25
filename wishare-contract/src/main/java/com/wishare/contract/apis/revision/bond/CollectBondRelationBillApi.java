package com.wishare.contract.apis.revision.bond;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillPageF;
import com.wishare.contract.domains.entity.revision.bond.CollectBondRelationBillE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.bond.CollectBondRelationBillAppService;
import com.wishare.contract.domains.vo.revision.bond.CollectBondRelationBillV;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillSaveF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillUpdateF;
import com.wishare.contract.domains.vo.revision.bond.CollectBondRelationBillListV;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillListF;
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
 * 收取保证金改版关联单据明细表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-27
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收取保证金改版关联单据明细表"})
@RequestMapping("/collectBondRelationBill")
public class CollectBondRelationBillApi {

    private final CollectBondRelationBillAppService collectBondRelationBillAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = CollectBondRelationBillV.class)
    @GetMapping
    public CollectBondRelationBillV get(@Validated CollectBondRelationBillF collectBondRelationBillF){
        return collectBondRelationBillAppService.get(collectBondRelationBillF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = CollectBondRelationBillV.class)
    @PostMapping("/list")
    public CollectBondRelationBillListV list(@Validated @RequestBody CollectBondRelationBillListF collectBondRelationBillListF){
        return collectBondRelationBillAppService.list(collectBondRelationBillListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody CollectBondRelationBillSaveF collectBondRelationBillF){
        return collectBondRelationBillAppService.save(collectBondRelationBillF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody CollectBondRelationBillUpdateF collectBondRelationBillF){
        collectBondRelationBillAppService.update(collectBondRelationBillF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return collectBondRelationBillAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = CollectBondRelationBillV.class)
    @PostMapping("/page")
    public PageV<CollectBondRelationBillV> page(@RequestBody PageF<CollectBondRelationBillPageF> request) {
        return collectBondRelationBillAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = CollectBondRelationBillV.class)
    @PostMapping("/pageFront")
    public PageV<CollectBondRelationBillV> frontPage(@RequestBody PageF<SearchF<CollectBondRelationBillE>> request) {
        return collectBondRelationBillAppService.frontPage(request);
    }

}
