package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.ContractConcludeF;
import com.wishare.contract.apps.fo.contractset.ContractConcludeSaveF;
import com.wishare.contract.apps.fo.contractset.ContractConcludeUpdateF;
import com.wishare.contract.apps.remote.vo.TemporaryChargeBillPageV;
import com.wishare.contract.apps.service.contractset.ContractBpmAppService;
import com.wishare.contract.apps.service.contractset.ContractConcludeAppService;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.List;

/**
 * 同订立bpm相关接口合
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"合同订立bpm相关接口合"})
@RequestMapping("/bpm")
public class ContractBpmApi implements IOwlApiBase {

    private final ContractBpmAppService contractBpmAppService;

    @PostMapping("/dealBpmExpendApply")
    @ApiOperation(value = "合同订立支出bpm处理", notes = "合同订立支出bpm回调处理")
    public Boolean dealBpmExpendApply(@RequestParam("bpmResId") @ApiParam("bpm审批流id") @NotNull(message = "bpm审批流id不能为空") String bpmResId,
                                      @RequestParam("checkFlag") @ApiParam("是否审批成功，true：通过，false：不通过") @NotNull(message = "审批结果不能为空") Boolean checkFlag,
                                      @RequestParam(value = "errorMsg",required = false) @ApiParam("审核不通过原因") String errorMsg) throws ParseException {
        log.info("external合同订立支出bpm回调参数------>bpmResId:{},checkFlag:{},errorMsg:{}", bpmResId,checkFlag,errorMsg);
        return contractBpmAppService.dealBpmExpendApply(bpmResId,checkFlag,errorMsg);
    }

    @PostMapping("/dealBpmNoExpendApply")
    @ApiOperation(value = "合同订立非支出bpm回调处理", notes = "合同订立非支出bpm回调处理")
    public Boolean dealBpmNoExpendApply(@RequestParam("bpmResId") @ApiParam("bpm审批流id") @NotNull(message = "bpm审批流id不能为空") String bpmResId,
                                      @RequestParam("checkFlag") @ApiParam("是否审批成功，true：通过，false：不通过") @NotNull(message = "审批结果不能为空") Boolean checkFlag,
                                      @RequestParam(value = "errorMsg",required = false) @ApiParam("审核不通过原因") String errorMsg) throws ParseException {
        log.info("external合同订立非支出bpm回调参数------>bpmResId:{},checkFlag:{},errorMsg:{}", bpmResId,checkFlag,errorMsg);
        return contractBpmAppService.dealBpmExpendApply(bpmResId,checkFlag,errorMsg);
    }

    @PostMapping("/dealBpmExpendPayApply")
    @ApiOperation(value = "合同订立支出bpm处理", notes = "合同订立支出bpm回调处理")
    public Boolean dealBpmExpendPayApply(@RequestParam("bpmResId") @ApiParam("bpm审批流id") @NotNull(message = "bpm审批流id不能为空") String bpmResId,
                                      @RequestParam("checkFlag") @ApiParam("是否审批成功，true：通过，false：不通过") @NotNull(message = "审批结果不能为空") Boolean checkFlag,
                                      @RequestParam(value = "errorMsg",required = false) @ApiParam("审核不通过原因") String errorMsg) {
        log.info("external合同订立支出付款bpm回调参数------>bpmResId:{},checkFlag:{},errorMsg:{}", bpmResId,checkFlag,errorMsg);
        return contractBpmAppService.dealBpmExpendPayApply(bpmResId,checkFlag,errorMsg);
    }

    @PostMapping("/dealBpmExpendInvoiceApply")
    @ApiOperation(value = "合同订立支出bpm处理", notes = "合同订立支出bpm回调处理")
    public Boolean dealBpmExpendInvoiceApply(@RequestParam("bpmResId") @ApiParam("bpm审批流id") @NotNull(message = "bpm审批流id不能为空") String bpmResId,
                                         @RequestParam("checkFlag") @ApiParam("是否审批成功，true：通过，false：不通过") @NotNull(message = "审批结果不能为空") Boolean checkFlag,
                                         @RequestParam(value = "errorMsg",required = false) @ApiParam("审核不通过原因") String errorMsg) {
        log.info("external合同订立支出付款bpm回调参数------>bpmResId:{},checkFlag:{},errorMsg:{}", bpmResId,checkFlag,errorMsg);
        return contractBpmAppService.dealBpmExpendInvoiceApply(bpmResId,checkFlag,errorMsg);
    }

}
