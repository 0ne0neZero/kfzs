package com.wishare.contract.apis.contractset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillPageF;
import com.wishare.contract.domains.entity.contractset.ContractProfitLossBillE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractProfitLossBillAppService;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossBillV;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillSaveF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillUpdateF;
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
 * 损益账单关联表
 * </p>
 *
 * @author ljx
 * @since 2022-10-17
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"损益账单关联表"})
@RequestMapping("/contractProfitLossBill")
public class ContractProfitLossBillApi {

    private final ContractProfitLossBillAppService contractProfitLossBillAppService;

}
