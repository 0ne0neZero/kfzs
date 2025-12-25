package com.wishare.finance.domains.configure.organization.facade;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.configure.organization.fo.AddPayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.fo.QueryPaymentChannelF;
import com.wishare.finance.apps.model.configure.organization.fo.UpdatePayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.vo.PayChannelConfV;
import com.wishare.finance.infrastructure.remote.clients.payment.PaymentReconcileClient;
import com.wishare.finance.infrastructure.remote.enums.Channel;
import com.wishare.finance.infrastructure.remote.enums.ChannelTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.payment.AddPaymentChannelByOutMchF;
import com.wishare.finance.infrastructure.remote.fo.payment.GetFileRF;
import com.wishare.finance.infrastructure.remote.fo.payment.UpdatePaymentChannelRf;
import com.wishare.finance.infrastructure.remote.fo.sign.AccountCheckingFileDownloadF;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentChannelRv;
import com.wishare.finance.infrastructure.remote.vo.payment.ReconciliationYinlianV;
import com.wishare.finance.infrastructure.remote.vo.payment.TLMchInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xujian
 * @date 2022/11/23
 * @Description:
 */
@Service
@Slf4j
public class PaymentFacade {

    @Setter(onMethod_ = {@Autowired})
    private PaymentReconcileClient paymentReconcileClient;

    /**
     * 方圆对接通联签约代扣商户号
     */
    @Value("${fang-yuan.sign.merchantId:66059506513199D}")
    private String merchantId;

    /**
     * 方圆对接通联签约代扣用户名
     */
    @Value("${fang-yuan.sign.userName:66059506513199D04}")
    private String userName;

    /**
     * 新增支付渠道
     *
     * @param form
     * @return
     */
    public Long addPayChannelConf(AddPayChannelConfF form) {
        ChannelTypeEnum channelTypeEnum = ChannelTypeEnum.valueOfByCode(form.getChannelType());

        AddPaymentChannelByOutMchF addPaymentChannelByOutMchF = new AddPaymentChannelByOutMchF();
        addPaymentChannelByOutMchF.setOutMchNo(form.getStatutoryBodyId().toString());
        addPaymentChannelByOutMchF.setMchName(form.getStatutoryBodyName());
        addPaymentChannelByOutMchF.setName(channelTypeEnum.getDes());
        addPaymentChannelByOutMchF.setChannelType(form.getChannelType());
        addPaymentChannelByOutMchF.setChannelParams(JSON.toJSONString(form.getChannelParams()));
        addPaymentChannelByOutMchF.setDisabled(form.getDisabled());
        return paymentReconcileClient.addByOutMch(addPaymentChannelByOutMchF).getId();
    }

    /**
     * 修改支付渠道
     *
     * @param form
     * @return
     */
    public Boolean updatePayChannelConf(UpdatePayChannelConfF form) {
        UpdatePaymentChannelRf updatePaymentChannelRf = new UpdatePaymentChannelRf();
        updatePaymentChannelRf.setId(form.getPayChannelId());
        updatePaymentChannelRf.setName(form.getPayChannelName());
        updatePaymentChannelRf.setChannelParams(form.getChannelParams() == null ? null : JSON.toJSONString(form.getChannelParams()));
        updatePaymentChannelRf.setChannelType(form.getChannelType());
        updatePaymentChannelRf.setDisabled(form.getDisabled());
        return paymentReconcileClient.update(updatePaymentChannelRf);
    }

    /**
     * 删除支付渠道
     *
     * @param id
     * @return
     */
    public Boolean deletePayChannelConf(Long id) {
        return paymentReconcileClient.delete(id);
    }

    /**
     * 根据id启用或禁用支付渠道
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        UpdatePaymentChannelRf updatePaymentChannelRf = new UpdatePaymentChannelRf();
        updatePaymentChannelRf.setId(id);
        updatePaymentChannelRf.setDisabled(disableState);
        return paymentReconcileClient.update(updatePaymentChannelRf);
    }

    /**
     * 查询支付渠道信息
     *
     * @param id
     */
    public PaymentChannelRv detailPayChannelConf(Long id) {
        return paymentReconcileClient.get(id);
    }

    /**
     * 分页查询支付渠道
     *
     * @param form
     * @return
     */
    public PageV<PaymentChannelRv> payChannelConfPage(PageF<SearchF<?>> form) {
        return paymentReconcileClient.getPage(form);
    }

    public List<PayChannelConfV> payChannelConfVList(QueryPaymentChannelF queryPaymentChannelF) {
        return paymentReconcileClient.getList(queryPaymentChannelF);
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public FileVo fileUpload(MultipartFile file) {
        return paymentReconcileClient.uploadIcon(file);
    }


    /**
     * 通过sftp下载对账文件
     */
    public Map<String, File> reconciliationDownloadSftp(String filePath, String queryName, String channel) {
        //return getLocalFile(filePath,queryName);

        GetFileRF getFileRF = new GetFileRF();
        getFileRF.setFilePath(filePath);
        getFileRF.setQueryName(queryName);
        getFileRF.setChannel(channel);
        return paymentReconcileClient.reconciliationDownloadSftpBatch(getFileRF);
    }

    public List<ReconciliationYinlianV> reconciliationDownloadHttp(String filePath, String queryName, String channel) {

        GetFileRF getFileRF = new GetFileRF();
        getFileRF.setFilePath(filePath);
        getFileRF.setQueryName(queryName);
        getFileRF.setChannel(channel);
        if (Channel.招商银行.getChannelCode().equals(getFileRF.getChannel())){
            return paymentReconcileClient.cmbReconciliationDownload(getFileRF);
        }
        return paymentReconcileClient.reconciliationDownloadHttp(getFileRF);
    }
    public byte[] reconciliationDownload(String filePath, String queryName, String channel) {
        GetFileRF getFileRF = new GetFileRF();
        getFileRF.setFilePath(filePath);
        getFileRF.setQueryName(queryName);
        getFileRF.setChannel(channel);
        return paymentReconcileClient.reconciliationDownloadSftp(getFileRF);
    }

    public byte[] reconciliationDownloadFromTL(TLMchInfo info,String communityId) {
        AccountCheckingFileDownloadF accountCheckingFile = new AccountCheckingFileDownloadF();
        accountCheckingFile.setMerchantId(info.getMerchantId());
        accountCheckingFile.setUserName(info.getUserName());
        accountCheckingFile.setReqSn(info.getMerchantId() + String.format("-%016d", System.currentTimeMillis()));
        LocalDate currentDay = LocalDate.now();
        LocalDateTime startDay = currentDay.minusDays(1).atTime(0, 0, 0);
        LocalDateTime endDay = currentDay.minusDays(1).atTime(23, 59, 59);
        accountCheckingFile.setStartDay(startDay);
        accountCheckingFile.setEndDay(endDay);
        accountCheckingFile.setStatus(0);
        accountCheckingFile.setContFee("1");
        accountCheckingFile.setType(0);
        accountCheckingFile.setCommunityId(communityId);
//        LocalDate recDate = LocalDate.now().minusDays(1);
//        GetFileForTLF getFileRF = new GetFileForTLF();
//        getFileRF.setFilePath(merchantId);
//        getFileRF.setQueryName(recDate.toString());

        return paymentReconcileClient.accountCheckingFileDownload(accountCheckingFile);
    }

    /**
     * 用于测试获取本地文件
     *
     * @return
     */
    private Map<String, File> getLocalFile(String filePath, String queryName) {
        File dirFile = new File("E:\\workDoc\\tmp\\");

        // 如果文件夹不存在或着不是文件夹，则返回 null
        if (Objects.isNull(dirFile) || !dirFile.exists() || dirFile.isFile()) {
            return null;
        }
        File[] childrenFiles = dirFile.listFiles();
        if (Objects.isNull(childrenFiles) || childrenFiles.length == 0) {
            return null;
        }
        Map<String, File> fileMap = new HashMap<>();
        for (File childFile : childrenFiles) {
            // 如果是文件，直接添加到结果集合
            if (childFile.isFile() && childFile.getName().contains(queryName)) {
                fileMap.put(childFile.getName(), childFile);
            }
        }
        return fileMap;
    }

}
