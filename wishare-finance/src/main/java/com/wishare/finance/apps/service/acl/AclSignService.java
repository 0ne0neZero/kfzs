package com.wishare.finance.apps.service.acl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wishare.finance.apps.model.signature.ESignF;
import com.wishare.finance.apps.model.signature.ESignResultZjV;
import com.wishare.finance.apps.model.signature.ElectronStampZjF;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.model.signature.EsignResultV;
import java.util.Objects;
import com.wishare.finance.apps.model.signature.ESignF;
import com.wishare.finance.apps.model.signature.ESignResultYyV;
import com.wishare.finance.apps.model.signature.ESignResultZjV;
import com.wishare.finance.apps.model.signature.ElectronStampYyF;
import com.wishare.finance.apps.model.signature.ElectronStampZjF;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.model.signature.EsignResultV;
import com.wishare.finance.infrastructure.conts.RedisConst;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * ACL 防腐层
 */
@Service
@Slf4j
public class AclSignService {

    @Autowired
    private ExternalClient externalClient;

    @Autowired
    private OrgClient orgClient;


    /**
     * 调用外部服务盖章 方圆
     *
     * @param signF
     * @param
     * @return
     */
    public String stamp(ESignF signF, Long invoiceReceiptId) {
        //传入的是法定单位id 需要替换成法定单位code
        //todo 临时处理 (针对处理 用于对方圆测试环境对应的针对处理)
        if(StringUtils.isNotBlank(signF.getOrgIDCardNum())&&signF.getOrgIDCardNum().equals("91440101231251085G")){
            signF.setOrgIDCardNum("91440101231251085G");
            signF.setAuthOrgName(null);
            signF.setAuthOrgIDCardNum(null);
        }else{
            OrgFinanceRv orgFinanceRv = orgClient.getOrgFinanceById(Long.valueOf(signF.getOrgIDCardNum()));
            signF.setOrgIDCardNum(orgFinanceRv.getTaxpayerNo());
            if(StringUtils.isNotBlank(signF.getAuthOrgIDCardNum())) {
                OrgFinanceRv orgFinanceRv2 = orgClient.getOrgFinanceById(Long.valueOf(signF.getAuthOrgIDCardNum()));
                signF.setAuthOrgIDCardNum(orgFinanceRv2.getTaxpayerNo());
            }
        }
        String stamp = "";
        try {
            stamp = externalClient.stamp(signF);
        } catch (Exception e) {
            //提取错误信息
            log.error("stamp:", e);
            String regex = "\"msg\":\"([^\"]*)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                String msg = matcher.group(1);
                RedisHelper.set(RedisConst.SIGN + invoiceReceiptId, msg);
            }
        }
        return stamp;
    }


    /**
     * 调用外部服务盖章 中交
     *
     * @param signF
     * @param
     * @return
     */
    public String stamp(ElectronStampZjF signF, Long invoiceReceiptId, String action) {
        //传入的是法定单位id 需要替换成法定单位code
        OrgFinanceRv orgFinanceRv = orgClient.getOrgFinanceById(Long.valueOf(signF.getOrgIDCardNum()));
        signF.setOrgIDCardNum(orgFinanceRv.getTaxpayerNo());
        signF.setCompanyName(orgFinanceRv.getNameCn());
        String stamp = "";
        try {
            stamp = externalClient.stampZj(signF);
        } catch (Exception e) {
            //提取错误信息
            log.error("stamp:", e);
            String regex = "\"msg\":\"([^\"]*)\"";
            Pattern pattern = Pattern.compile(regex);
            try{
                Matcher matcher = pattern.matcher(e.getMessage());
                if (matcher.find()) {
                    String msg = matcher.group(1);
                    RedisHelper.set(action + invoiceReceiptId, msg);
                }
            }catch (Exception e1){
                RedisHelper.set(action + invoiceReceiptId, "网络未知异常");
            }
        }
        return stamp;
    }



    /**
     *  方圆 获取外部签署结果
     * @param signFlowId
     * @return
     */
    public EsignResult querySignResultFY(String signFlowId) {
        try {
            EsignResultV esignResultV = externalClient.queryEsignResult(signFlowId);
            return Global.mapperFacade.map(esignResultV, EsignResult.class);
        } catch (Exception e) {
            log.error("querySignResultFY:", e);
        }
        return null;
    }


    /**
     *  中交 获取外部签署结果
     * @param signFlowId
     * @return
     */
    public EsignResult querySignResultZj(String signFlowId) {
        try {
            ESignResultZjV eSignResultZjV = externalClient.queryEsignZjResult(signFlowId);
            return Global.mapperFacade.map(eSignResultZjV, EsignResult.class);
        } catch (Exception e) {
            log.error("querySignResultZj:", e);
        }
        return null;
    }


    /**
     * 调用外部服务盖章 远洋
     *
     * @param signF
     * @param
     * @return
     */
    public String stamp(ElectronStampYyF signF, Long invoiceReceiptId, String action) {
        String stamp = "";
        //传入的是法定单位id 需要替换成法定单位code
        OrgFinanceRv orgFinanceRv = orgClient.getOrgFinanceById(Long.valueOf(signF.getOrgIDCardNum()));
        if(Objects.isNull(orgFinanceRv)){
            log.error("stamp error 未查询到对应的法定单位信息");
            return stamp;
        }
        signF.setOrgIDCardNum(orgFinanceRv.getTaxpayerNo());
        signF.setCompanyName(orgFinanceRv.getNameCn());
        try {
            stamp = externalClient.stampYy(signF);
        } catch (Exception e) {
            //提取错误信息
            log.error("stamp:", e);
            String regex = "\"msg\":\"([^\"]*)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                String msg = matcher.group(1);
                RedisHelper.set(action + invoiceReceiptId, msg);
            }
        }
        return stamp;
    }

    /**
     *  远洋 获取外部签署结果
     * @param signFlowId
     * @return
     */
    public EsignResult querySignResultYy(String signFlowId) {
        try {
            ESignResultYyV eSignResultYyV = externalClient.queryEsignYyResult(signFlowId);
            return Global.mapperFacade.map(eSignResultYyV, EsignResult.class);
        } catch (Exception e) {
            log.error("querySignResultYy:", e);
        }
        return null;
    }



}
