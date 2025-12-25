package com.wishare.finance.apis.configure.organization;

import com.wishare.finance.apps.model.configure.organization.fo.AddPayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.fo.QueryPaymentChannelF;
import com.wishare.finance.apps.model.configure.organization.fo.UpdatePayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.vo.PayChannelConfV;
import com.wishare.finance.apps.service.configure.organization.PayChannelConfAppService;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/15
 * @Description:
 */
@Api(tags = {"支付渠道管理"})
@RestController
@RequestMapping("/payChannelConf")
@RequiredArgsConstructor
public class PayChannelConfApi {

    private final PayChannelConfAppService payChannelConfAppService;

    @ApiOperation(value = "新增支付渠道", notes = "新增支付渠道")
    @PostMapping("/add")
    public Long addPayChannelConf(@RequestBody @Validated AddPayChannelConfF form) {
        //form.check();
        return payChannelConfAppService.addPayChannelConf(form);
    }

    @ApiOperation(value = "修改支付渠道", notes = "修改支付渠道")
    @PostMapping("/update")
    public Boolean updatePayChannelConf(@RequestBody @Validated UpdatePayChannelConfF form) {
        //form.check();
        return payChannelConfAppService.updatePayChannelConf(form);
    }

    @ApiOperation(value = "删除支付渠道", notes = "删除支付渠道")
    @DeleteMapping("/delete/{id}")
    public Boolean deletePayChannelConf(@PathVariable("id") Long id) {
        return payChannelConfAppService.deletePayChannelConf(id);
    }

    @ApiOperation(value = "根据id获取支付渠道详情", notes = "根据id获取支付渠道详情")
    @GetMapping("/detail/{id}")
    public PayChannelConfV detailPayChannelConf(@PathVariable("id") Long id) {
        return payChannelConfAppService.detailPayChannelConf(id);
    }

    @ApiOperation(value = "根据id启用或禁用支付渠道", notes = "根据id启用或禁用银行账户")
    @PostMapping("/enable/{id}/{disableState}")
    public Boolean enable(@PathVariable("id") Long id, @PathVariable("disableState") Integer disableState) {
        if (null == disableState || null == DataDisabledEnum.valueOfByCode(disableState)) {
            throw BizException.throw400(ErrMsgEnum.DISABLE_STATE_EXCEPTION.getErrMsg());
        }
        return payChannelConfAppService.enable(id, disableState);
    }

    @ApiOperation(value = "分页获取支付渠道", notes = "分页获取支付渠道")
    @PostMapping("/page")
    public PageV<PayChannelConfV> payChannelConfPage(@RequestBody PageF<SearchF<?>> form) {
        PageV<PayChannelConfV> result = payChannelConfAppService.payChannelConfPage(form);
        return result;
    }
    @ApiOperation(value = "获取支付渠道列表", notes = "获取支付渠道列表")
    @PostMapping("/list")
    public List<PayChannelConfV> payChannelConfVList(@RequestBody @Valid QueryPaymentChannelF queryPaymentChannelF){
        return  payChannelConfAppService.payChannelConfVList(queryPaymentChannelF);

    }

    @ApiOperation(value = "上传接口", notes = "上传接口", response = FileVo.class)
    @PostMapping("/file/upload")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "__File", allowMultiple = true, paramType = "query",
            dataTypeClass = MultipartFile.class, required = true)
    public FileVo fileUpload(@RequestParam("file") MultipartFile file) {
        return payChannelConfAppService.fileUpload(file);
    }

}
