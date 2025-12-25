package com.wishare.finance.apis.configure.chargeitem;

import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.configure.chargeitem.fo.*;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemTreeV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeNameV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ReduceApprovalProcessDTO;
import com.wishare.finance.apps.service.bill.ReductionApplicationFormService;
import com.wishare.finance.apps.service.configure.chargeitem.ChargeItemAppService;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费项控制器
 *
 * @author yancao
 */
@Api(tags = {"费项接口"})
@RestController
@RequestMapping("/charge")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChargeItemApi {

    private final ChargeItemAppService chargeItemAppService;

    private final ChargeItemRepository chargeItemRepository;
    private final ReductionApplicationFormService reductionApplicationFormService;



    @ApiOperation("减免发起OA")
    @PostMapping("/startReductionByOA")
    public ReduceApprovalProcessDTO startReductionByOA(@RequestBody ReduceApprovalProcessF f){
        return reductionApplicationFormService.startReductionByOA(f);
    }

    @ApiOperation(value = "新增费项", notes = "新增费项")
    @PostMapping("/create")
    public Long create(@RequestBody @Validated CreateChargeItemF createChargeItemF) {
        return chargeItemAppService.create(createChargeItemF);
    }

    @ApiOperation(value = "更新费项", notes = "更新费项")
    @PostMapping("/update")
    public Boolean update(@RequestBody @Validated UpdateChargeItemF updateChargeItemF) {
        return chargeItemAppService.update(updateChargeItemF);
    }

    @ApiOperation(value = "启用费项", notes = "启用费项")
    @PostMapping("/enable/id/{id}")
    public Boolean enable(@PathVariable("id") Long id) {
        return chargeItemAppService.enable(id);
    }

    @ApiOperation(value = "禁用费项", notes = "禁用费项")
    @PostMapping("/disable/id/{id}")
    public Boolean disable(@PathVariable("id") Long id) {
        return chargeItemAppService.disable(id);
    }

    @ApiOperation(value = "根据id显示或隐藏费项", notes = "根据id显示或隐藏费项")
    @PostMapping("/showed")
    public Boolean showed(@RequestBody @Validated ShowOrHideChargeItemF showOrHideChargeItemF) {
        return chargeItemAppService.showed(showOrHideChargeItemF);
    }

    @ApiOperation(value = "删除费项", notes = "删除费项")
    @DeleteMapping("/delete/id/{id}")
    public Boolean delete(@PathVariable("id") Long id) {
        return chargeItemAppService.delete(id);
    }

    @ApiOperation(value = "根据ID查费项数据", notes = "根据ID查费项数据")
    @GetMapping("/query/id/{id}")
    public ChargeItemV getById(@PathVariable("id") Long id) {
        return chargeItemAppService.getById(id);
    }

    @ApiOperation(value = "根据ID集合查费项数据", notes = "根据ID集合查费项数据")
    @PostMapping("/query/idList")
    public List<ChargeItemV> getByIdList(@RequestBody List<Long> idList) {
        return chargeItemAppService.getByIdList(idList);
    }

    @ApiOperation(value = "根据编码集合查费项数据", notes = "根据编码集合查费项数据")
    @PostMapping("/query/codeList")
    public List<ChargeItemV> getByCodeList(@RequestBody List<String> codeList) {
        return chargeItemAppService.getByCodeList(codeList);
    }

    @ApiOperation("根据id查询子费项列表")
    @GetMapping("/query/child")
    List<ChargeItemV> queryChildById(@RequestParam(value = "id", required = false) Long id) {
        return chargeItemAppService.queryChildById(id);
    }

    @ApiOperation("分页查询费项树")
    @PostMapping("/query/page")
    PageV<ChargeItemV> queryByPage(@RequestBody PageF<SearchF<ChargeItemE>> queryChargeItemPageF) {
        return chargeItemAppService.queryByPage(queryChargeItemPageF);
    }

    @ApiOperation("分页查询所有费项")
    @PostMapping("/query/all/page")
    PageV<ChargeItemV> queryAllByPage(@RequestBody PageF<SearchF<ChargeItemE>> queryChargeItemPageF) {
        return chargeItemAppService.queryAllByPage(queryChargeItemPageF);
    }

    @ApiOperation("分页查询未关联外部数据的费项")
    @PostMapping("/query/notrelation/page")
    PageV<ChargeItemV> queryAllByPage(@RequestBody QueryNotRelationChargeItemF queryF) {
        return chargeItemAppService.queryNotRelationByPage(queryF);
    }

    @ApiOperation(value = "根据ID查费项全路径名称", notes = "根据ID查费项全路径名称")
    @GetMapping("/query/name/{id}")
    public String getNameById(@PathVariable("id") Long id) {
        return chargeItemAppService.getNameById(id);
    }

    @ApiOperation(value = "根据ID查目标费项名称", notes = "根据ID查目标费项名称")
    @GetMapping("/query/simpleName/{id}")
    public String getSimpleNameById(@PathVariable("id") Long id) {
        return chargeItemAppService.getSimpleNameById(id);
    }

    @ApiOperation(value = "获取费项树", notes = "获取费项树")
    @GetMapping("/query/tree")
    public List<ChargeItemTreeV> queryTree(@RequestParam(value = "filterId", required = false) Long filterId,
                                           @RequestParam(value = "attribute", required = false) String attribute,
                                           @RequestParam(value = "type", required = false) String type,
                                           @RequestParam(value = "parentIds", required = false) String parentIds,
                                           @RequestParam(value = "specialParentIds", required = false) String specialParentIds) {
        return chargeItemAppService.queryTree(filterId, attribute, type, parentIds, specialParentIds);
    }

    @ApiOperation(value = "获取费项树（不包含最后一级）", notes = "获取费项树（不包含最后一级）")
    @GetMapping("/query/treeLevel")
    public List<ChargeItemTreeV> queryTreeLevel(@RequestParam(value = "filterId", required = false) Long filterId,
                                           @RequestParam(value = "attribute", required = false) String attribute,
                                           @RequestParam(value = "type", required = false) String type) {
        return chargeItemAppService.queryTreeLevel(filterId, attribute, type);
    }

    @ApiOperation(value = "根据ID集合查费项名称", notes = "根据ID集合查费项名称")
    @PostMapping("/query/name/idList")
    public List<ChargeNameV> getNameByIdList(@RequestBody List<Long> idList) {
        return chargeItemAppService.getNameByIdList(idList);
    }

    @ApiOperation(value = "根据名称集合查费项是否末级", notes = "根据名称集合查费项是否末级")
    @PostMapping("/query/leaf/nameList")
    public List<ChargeNameV> getIsLeafByIdNameList(@RequestBody List<String> nameList) {
        return chargeItemAppService.getIsLeafByIdNameList(nameList);
    }

    @ApiOperation(value = "获取末级费项(最后一级费项)", notes = "获取末级费项(最后一级费项)")
    @GetMapping("/query/lastStage")
    public List<ChargeNameV> getLastStageChargeItem(@RequestParam(value = "type", required = false) @ApiParam("费项类型：1常规收费类型、2临时收费类型、3押金收费类型、4常规付费类型、5押金付费类型") String type,
                                                    @RequestParam(value = "attribute", required = false) @ApiParam("费项属性： 1收入,2支出,3代收代付及其他")  String attribute) {
        return chargeItemAppService.getLastStageChargeItem(type, attribute);
    }

    @ApiOperation(value = "获取末级费项(全路径)", notes = "获取末级费项(全路径)")
    @GetMapping("/query/lastStageAllPath")
    public List<ChargeNameV> getLastStageChargeItemAllPath(@RequestParam(value = "type", required = false) @ApiParam("费项类型：1常规收费类型、2临时收费类型、3押金收费类型、4常规付费类型、5押金付费类型") String type,
                                                    @RequestParam(value = "attribute", required = false) @ApiParam("费项属性： 1收入,2支出,3代收代付及其他")  String attribute) {
        return chargeItemAppService.getLastStageChargeItemAllPath(type, attribute);
    }

    @ApiOperation(value = "根据是否末级字段获取末级费项", notes = "根据是否末级字段获取末级费项")
    @GetMapping("/query/LastLevel")
    public List<ChargeNameV> getLastStageChargeItemByLastLevel() {
        return chargeItemAppService.getLastStageChargeItemByLastLevel();
    }

    @ApiOperation(value = "根据费项id获取费项路径", notes = "根据费项id获取费项路径")
    @GetMapping("/query/path/{id}")
    public List<ChargeNameV> getPathById(@PathVariable("id") Long id) {
        return chargeItemAppService.getPathById(id);
    }

    @ApiOperation(value = "导入费项", notes = "导入费项")
    @PostMapping("/import")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public Boolean importCharge(@RequestParam("file") MultipartFile file) {
        return chargeItemAppService.importCharge(file);
    }

    @ApiOperation(value = "根据父费项id生成费项编码", notes = "根据父费项id生成费项编码")
    @PostMapping("/generate/code/{parentId}")
    public Map<String, String> generateCode(@PathVariable("parentId") Long parentId) {
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("code",chargeItemAppService.generateCode(parentId));
        return stringStringHashMap;
    }

    @ApiOperation(value = "查询费项树", notes = "查询费项树")
    @PostMapping("/query/trees")
    public List<ChargeItemTreeV> getTree(@RequestBody ChargeItemTreeF chargeItemTreeF) {
        return chargeItemRepository.getTree(chargeItemTreeF);
    }

    @ApiOperation(value = "根据费项条件查询", notes = "根据费项条件查询")
    @PostMapping("/query/infos")
    public List<ChargeNameV> getChargeItemInfoList(@RequestParam(value = "nameList", required = false) @ApiParam("费项名称集合") List<String> nameList,
                                                   @RequestParam(value = "attributeList", required = false) @ApiParam("费项属性集合： 1收入,2支出,3代收代付及其他")  List<Byte> attributeList,
                                                   @RequestParam(value = "codeList", required = false) @ApiParam("费项编码") List<String> codeList,
                                                   @RequestParam(value = "typeList", required = false) @ApiParam("费项类型集合： 1收入,2支出,3代收代付及其他")  List<Byte> typeList) {
        return chargeItemRepository.getChargeItemInfoList(nameList,attributeList,codeList);
    }

    @ApiOperation(value = "根据费项code查询费项信息", notes = "根据费项code查询费项信息")
    @GetMapping("/query/code/{code}")
    public ChargeItemV getChargeItemByCode(@PathVariable("code") String code) {
        return chargeItemAppService.getByFeeCode(code);
    }


    @ApiOperation(value = "查询分成费项信息", notes = "查询分成费项信息")
    @GetMapping("/query/ChargeItemInfoList")
    public List<ChargeNameV> getShareChargeItem() {
        return chargeItemAppService.getChargeItemInfoList();
    }
    @ApiOperation(value = "根据费项code查询费项信息", notes = "根据费项code查询费项信息")
    @GetMapping("/getByItemCode")
    public ChargeItemV getByItemCode(@RequestHeader("tenantId") String tenantId , @RequestParam("itemCode") String itemCode) {
        return chargeItemAppService.getByFeeCode(itemCode);
    }
    @ApiOperation(value = "根据费项类型获取费项树", notes = "根据费项类型获取费项树")
    @GetMapping("/query/getByItemType")
    public List<ChargeItemTreeV> queryTreeByType(@RequestParam(value = "filterId", required = false) Long filterId,
                                           @RequestParam(value = "attribute", required = false) String attribute,
                                           @RequestParam(value = "type", required = false) String type) {
        return chargeItemAppService.queryTreeByType(filterId, attribute, type);
    }

    @ApiOperation(value = "获取费项树", notes = "获取费项树")
    @GetMapping("/query/tree/new")
    public List<ChargeItemTreeV> queryTreeNew(@RequestParam(value = "filterId", required = false) Long filterId,
                                           @RequestParam(value = "attribute", required = false) String attribute,
                                           @RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "incomeOrPay") Integer incomeOrPay) {
        return chargeItemAppService.queryTreeNew(filterId, attribute, type,incomeOrPay);
    }
}
