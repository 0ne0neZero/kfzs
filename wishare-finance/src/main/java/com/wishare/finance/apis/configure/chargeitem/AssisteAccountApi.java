package com.wishare.finance.apis.configure.chargeitem;

import com.wishare.finance.apps.model.configure.chargeitem.fo.AssisteAccountF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AssisteAccountSyncF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.BusinessTypeListF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.AssisteAccountV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.BusinessTypeV;
import com.wishare.finance.apps.service.configure.chargeitem.AssisteAccountAppService;
import com.wishare.finance.domains.configure.subject.entity.AssisteBizType;
import com.wishare.finance.domains.configure.subject.repository.AssisteBizTypeRepository;
import com.wishare.finance.domains.configure.subject.repository.AssisteItemRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */

@Api(tags = {"辅助核算管理"})
@RestController
@RequestMapping("/assisteAccount")
@RequiredArgsConstructor
public class AssisteAccountApi {

    private final AssisteAccountAppService assisteAccountAppService;
    private final AssisteBizTypeRepository assisteBizTypeRepository;

    @ApiOperation(value = "分页获取辅助核算", notes = "分页获取辅助核算")
    @PostMapping("/page")
    public PageV<AssisteAccountV> assisteAccountPage(@RequestBody PageF<SearchF<?>> form) {
        return assisteAccountAppService.assisteAccountPage(form);
    }

    @ApiOperation(value = "获取辅助核算列表", notes = "获取辅助核算列表")
    @PostMapping("/list")
    public List<AssisteAccountV> assisteAccountList(@RequestBody AssisteAccountF form) {
        return assisteAccountAppService.assisteAccountList(form);
    }

    @ApiOperation(value = "根据科目id获取辅助核算列表", notes = "根据科目id获取辅助核算列表")
    @GetMapping("/list/subject/{subjectId}")
    public List<AssisteAccountV> assisteAccountListBySubject(@PathVariable("subjectId") Long subjectId) {
        if (subjectId == null || subjectId < 0) {
            throw BizException.throw400("科目id不存在");
        }
        return assisteAccountAppService.assisteAccountListBySubject(subjectId);
    }

    @ApiOperation(value = "导出数据列表", notes = "导出数据列表")
    @PostMapping("/exportList")
    public List<AssisteAccountV> exportList(@RequestBody PageF<SearchF<?>> form) {
        return assisteAccountAppService.exportList(form);
    }

    @ApiOperation(value = "根据条件获取业务类型", notes = "根据条件获取业务类型")
    @PostMapping("/businessType/list")
    public List<BusinessTypeV> businessTypeList(@RequestBody BusinessTypeListF form) {
        //return assisteAccountAppService.businessTypeList(form);
        return Global.mapperFacade.mapAsList(assisteBizTypeRepository.listByEntity(new AssisteBizType()
                .setCode(form.getCode()).setName(form.getName()).setDisabled(form.getState())), BusinessTypeV.class);

    }

    @ApiOperation(value = "辅助核算同步", notes = "辅助核算同步")
    @PostMapping("/sync")
    public Boolean assisteAccountSync(@RequestBody AssisteAccountSyncF form) {
        form.setSysSource(SysSourceEnum.收费系统.getCode());
        return assisteAccountAppService.assisteAccountSync(form);
    }

    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public Boolean export(@RequestBody PageF<SearchF<?>> form, HttpServletResponse response) {
        return assisteAccountAppService.export(form, response);
    }


}
