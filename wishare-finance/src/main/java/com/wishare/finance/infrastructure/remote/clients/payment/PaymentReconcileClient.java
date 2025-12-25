package com.wishare.finance.infrastructure.remote.clients.payment;

import com.wishare.finance.apps.model.configure.organization.fo.QueryPaymentChannelF;
import com.wishare.finance.apps.model.configure.organization.vo.PayChannelConfV;
import com.wishare.finance.infrastructure.remote.fo.payment.AddPaymentChannelByOutMchF;
import com.wishare.finance.infrastructure.remote.fo.payment.GetFileForTLF;
import com.wishare.finance.infrastructure.remote.fo.payment.GetFileRF;
import com.wishare.finance.infrastructure.remote.fo.payment.UpdatePaymentChannelRf;
import com.wishare.finance.infrastructure.remote.fo.sign.AccountCheckingFileDownloadF;
import com.wishare.finance.infrastructure.remote.vo.payment.ChannelConfigV;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentChannelRv;
import com.wishare.finance.infrastructure.remote.vo.payment.ReconciliationYinlianV;
import com.wishare.finance.infrastructure.remote.vo.payment.TLMchInfo;
import com.wishare.starter.annotations.OpenFeignClient;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author xujian
 * @date 2022/11/23
 * @Description:
 */
@OpenFeignClient(name = "${wishare.feignClients.payment.name:wishare-payment}", serverName = "支付中心", path = "${wishare.feignClients.payment.context-path:/payment}", contextId = "PaymentReconcileClient")
public interface PaymentReconcileClient {

    @PostMapping(value = "/file/upload", name = "上传接口", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileVo uploadIcon(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/channel/addByOutMch",name = "新增外部普通商户渠道商")
    PaymentChannelRv addByOutMch(@Validated @RequestBody AddPaymentChannelByOutMchF addPaymentChannelByOutMchF);

    @PostMapping(value = "/channel/page", name = "分页查询应用列表")
    PageV<PaymentChannelRv> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF);

    @PostMapping(value = "/channel/queryList", name = "查询支付渠道商列表")
    List<PayChannelConfV> getList(@Validated @RequestBody QueryPaymentChannelF queryPaymentChannelF);

    @PostMapping(value = "/channel/update",name = "更新支付渠道商")
    Boolean update(@Validated @RequestBody UpdatePaymentChannelRf updatePaymentChannelF);

    @DeleteMapping(value = "/channel/{paymentChannelId}", name = "删除支付渠道商")
    Boolean delete(@PathVariable("paymentChannelId") @ApiParam("支付渠道商id") Long paymentChannelId);

    @GetMapping(value = "/channel/{paymentChannelId}",name = "查询支付渠道商信息")
    PaymentChannelRv get(@PathVariable("paymentChannelId") @ApiParam("支付渠道商id") Long paymentChannelId);

    @PostMapping("/reconciliation/download/sftp")
    @ApiOperation(value = "通过sftp下载对账文件", notes = "通过sftp下载对账文件")
    byte[] reconciliationDownloadSftp(@RequestBody GetFileRF form);

    @PostMapping("/reconciliation/download/sftp/batch")
    @ApiOperation(value = "通过sftp批量下载对账文件", notes = "通过sftp批量下载对账文件")
    Map<String, File> reconciliationDownloadSftpBatch(@RequestBody GetFileRF form);

    @ApiOperation(value = "对账文件下载(200002)", notes = "对账文件下载(200002)")
    @PostMapping("/quickSign/accountCheckingFileDownload")
    public byte[] accountCheckingFileDownload(@RequestBody AccountCheckingFileDownloadF addF);

    @PostMapping("/reconciliation/download/sftp/tl")
    @ApiOperation(value = "对接通联通过sftp下载对账文件", notes = "对接通联通过sftp下载对账文件")
    public byte[] reconciliationDownloadSftpForTL(@RequestBody GetFileForTLF form);

    @PostMapping("reconciliation/download/htp")
    @ApiOperation(value = "对接通联http下载对账文件", notes = "对接通联http下载对账文件")
    List<ReconciliationYinlianV> reconciliationDownloadHttp(@RequestBody GetFileRF form);

    @PostMapping("reconciliation/download/http/cmb")
    @ApiOperation(value = "对接招商银行http下载对账文件", notes = "对接招商银行http下载对账文件")
    List<ReconciliationYinlianV> cmbReconciliationDownload(@RequestBody GetFileRF form);

    @GetMapping("/config/getTLCommunityList")
    @ApiOperation(value = "获取对应租户所有支付商户管理签约代扣配置对应项目集合", notes = "获取通联对应支付商户管理配置")
    List<String> getTLCommunityList();

    @GetMapping("/config/getTLMchInfoByBusinessId")
    @ApiOperation(value = "获取通联对应支付商户管理配置", notes = "获取通联对应支付商户管理配置")
    TLMchInfo getTLMchInfoByBusinessId(@RequestParam("businessId") String businessId,
                                              @RequestParam("channelCode") String channelCode);

    @GetMapping("/config/getChannelByBusinessId")
    @ApiOperation(value = "通过业务id获取支付渠道配置", notes = "通过业务id获取支付渠道配置")
    List<ChannelConfigV> getChannelByBusinessId(@RequestParam("businessId") String businessId);
}
