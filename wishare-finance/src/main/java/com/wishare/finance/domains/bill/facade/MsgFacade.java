package com.wishare.finance.domains.bill.facade;

import com.alibaba.fastjson.JSON;
import com.sun.mail.util.MailSSLSocketFactory;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.shortcode.ShortCodeService;
import com.wishare.finance.infrastructure.configs.SmsConfigProperties;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.remote.clients.base.MsgClient;
import com.wishare.finance.infrastructure.remote.fo.msg.MsgSmsRf;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.UUID;

/**
 * @author xujian
 * @date 2022/10/20
 * @Description:
 */
@Slf4j
@Component
@RefreshScope
public class MsgFacade {

    @Value("${email.host}")
    private String emailHost;
    @Value("${email.address}")
    private String emailAddress;
    @Value("${email.password}")
    private String emailPassword;

    /**
     * 远洋短信模板
     */
    @Value("${sms.templateId}")
    private Long templateId;
    @Value("${sms.receiptTemplateId}")
    private Long receiptTemplateId;


    @Setter(onMethod_ = @Autowired)
    protected SmsConfigProperties smsConfigProperties;

    /**
     * 远洋发票补发短信模板
     */
    @Value("${sms.invoiceResendTemplateId}")
    private Long invoiceResendTemplateId;
    /**
     * 远洋收据补发短信模板
     */
    @Value("${sms.receiptResendTemplateId}")
    private Long receiptResendTemplateId;

    /**
     * 短链前缀
     */
    @Value("${short.url.prefix:https://jxfw.ccccsg.com/sUrl/}")
    private String shortUrlPrefix;

    /**
     * 远洋短信发送标识
     */
    @Value("${sms.tag:#{null}}")
    private Integer tag;

    @Autowired
    private MsgClient msgClient;

    /**
     * 默认租户简称
     */
    private static final String DEFAULT_TENANT_NAME = "慧享云";

    /**
     * 项目电话文本
     */
    private static final String PHONE_TEXT = "，如有疑问请致电";

    @Autowired
    private ShortCodeService shortCodeService;



    /**
     * 电票推送短信
     *
     * @param mobileNum
     * @param tenantShortName 租户简称
     * @param roomName 房号
     * @param pdfUrl 下载链接
     * @param phone 项目电话
     */
    public void smsInvoice(String mobileNum, String tenantShortName, String roomName, String pdfUrl, String phone) {
        String[] params;
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            params = new String[]{roomName, shortCodeService.createShortCode(pdfUrl), phone};
        }else {
            if (StringUtils.isBlank(tenantShortName)) {
                tenantShortName = DEFAULT_TENANT_NAME;
            }
            if (StringUtils.isNotBlank(phone)) {
                phone = PHONE_TEXT + phone;
            } else {
                phone = "";
            }
            if (StringUtils.isBlank(roomName)) {
                roomName = "";
            }
            String shortCode = shortCodeService.createShortCode(pdfUrl);
            params = new String[]{tenantShortName, roomName, shortUrlPrefix + shortCode, phone};
        }
        sendSms(mobileNum, params, templateId);
    }

    /**
     * 电票补发短信
     * @param mobileNum 电话号码
     * @param customerName 客户名称
     * @param tenantShortName 租户简称
     * @param invoiceNum 发票数量
     * @param invoiceStr 发票具体信息
     * @param communityName 项目名称
     */
    @Deprecated
    public void smsResendInvoice(String mobileNum, String customerName, String tenantShortName, String invoiceNum,
                                 String invoiceStr, String communityName) {
        String[] params;
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            params = new String[]{customerName, tenantShortName, invoiceNum, shortCodeService.createShortCode(invoiceStr), communityName};
        }else {
            String shortCode = shortCodeService.createShortCode(invoiceStr);
            params = new String[]{customerName, tenantShortName, invoiceNum, shortUrlPrefix + shortCode, communityName};
        }
        sendSms(mobileNum, params, invoiceResendTemplateId);
    }

    /**
     * 收据补发短信
     * @param mobileNum
     * @param roomName 房号
     * @param tenantShortName
     * @param receiptNum 收据数量
     * @param receiptStr 收据url信息
     * @param communityName
     */
    @Deprecated
    public void smsResendReceipt(String mobileNum, String roomName, String tenantShortName, String receiptNum, String receiptStr, String communityName) {
        String[] params;
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            params = new String[]{roomName, tenantShortName, receiptNum, shortCodeService.createShortCode(receiptStr), communityName};
        }else {
            params = new String[]{roomName, tenantShortName, receiptNum, receiptStr, communityName};
        }
        sendSms(mobileNum, params, receiptResendTemplateId);
    }

    /**
     * 收据推送短信
     * @param mobileNum 电话号码
     * @param tenantShortName 租户简称
     * @param roomName 房号
     * @param pdfUrl 下载链接
     * @param phone 项目电话
     */
    public void smsReceipt(String mobileNum, String tenantShortName, String roomName, String pdfUrl, String phone) {
        String[] params;
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            params = new String[]{roomName, shortCodeService.createShortCode(pdfUrl), phone};
        }else {
            if (StringUtils.isBlank(tenantShortName)) {
                tenantShortName = DEFAULT_TENANT_NAME;
            }
            if (StringUtils.isNotBlank(phone)) {
                phone = PHONE_TEXT + phone;
            } else {
                phone = "";
            }
            if (StringUtils.isBlank(roomName)) {
                roomName = "";
            }
            String shortCode = shortCodeService.createShortCode(pdfUrl);
            params = new String[]{tenantShortName, roomName, shortUrlPrefix + shortCode, phone};
        }

        sendSms(mobileNum, params, receiptTemplateId);
    }

    /**
     * 收据作废推送短信
     * @param mobileNum 电话号码
     * @param tenantShortName 租户简称
     * @param roomName 房号
     * @param invoiceReceiptNO 收据编号
     * @param phone 项目电话
     */
    public void smsReceiptVoid(String mobileNum, String tenantShortName, String roomName, String invoiceReceiptNO, String phone) {

        String[] params;
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            params = new String[]{roomName, invoiceReceiptNO, phone};
        }else {
            if (StringUtils.isBlank(tenantShortName)) {
                tenantShortName = DEFAULT_TENANT_NAME;
            }
            if (StringUtils.isNotBlank(phone)) {
                phone = PHONE_TEXT + phone;
            } else {
                phone = "";
            }
            if (StringUtils.isBlank(roomName)) {
                roomName = "";
            }
            params = new String[]{tenantShortName, roomName, invoiceReceiptNO, phone};
        }

        sendSms(mobileNum, params, smsConfigProperties.getReceiptTemplateVoidId());
    }







    /**
     * 发送短信
     * @param mobileNum 手机号
     * @param params 参数数组
     * @param templateId 模板id
     */
    public void sendSms(String mobileNum, String[] params, Long templateId) {
        MsgSmsRf msgSmsRf = new MsgSmsRf();
        msgSmsRf.setMobileNum(mobileNum);
        msgSmsRf.setTemplateId(templateId);
        msgSmsRf.setParams(params);
        String uuid = UUID.randomUUID().toString();
        log.info("短信发送开始：{}, suffix:{}", JSON.toJSONString(msgSmsRf), uuid);
        String success = msgClient.sms(tag, uuid, msgSmsRf);
        log.info("短信发送结束：{}", success);
    }


    /**
     * 发送邮件
     *
     * @param destEmailAddress
     * @param subject 主题
     * @param content 内容
     * @param files
     */
    public void sendEmail(String destEmailAddress, String subject, String content, MultipartFile[] files) {
        //创建session
        Session session = null;
        try {
            session = createSSLSession();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        // 3. 创建一封邮件
        MimeMessage message;
        try {
            message = createMimeMessage(session, destEmailAddress, subject);
            MimeMultipart mm = new MimeMultipart();
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(content, "text/plain;charset=UTF-8");
            mm.addBodyPart(text);
            if (files != null && files.length > 0) {
                if (files.length > 0) {
                    for (MultipartFile f : files) {
                        // MultipartFile 转化成 InputStream
                        byte[] byteArr = f.getBytes();
                        InputStream inputStream = new ByteArrayInputStream(byteArr);
                        MimeBodyPart attachment = new MimeBodyPart();
                        DataSource source = new ByteArrayDataSource(inputStream, "application/msexcel");
                        attachment.setDataHandler(new DataHandler(source));
                        // 设置附件的文件名（需要编码）
                        attachment.setFileName(MimeUtility.encodeText(f.getOriginalFilename()));
                        // 10. 设置文本和 附件 的关系（合成一个大的混合"节点" / Multipart ）
                        // 如果有多个附件，可以创建多个多次添加
                        mm.addBodyPart(attachment);
                    }
                }
            }
            message.setContent(mm);
            message.saveChanges();
            // 获取到传输对象
            Transport transport = session.getTransport();
            transport.connect(emailHost, emailAddress, emailPassword);
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            log.error("发送邮件异常信息：{}", e.getMessage());
        }
    }

    /**
     * 创建一封邮件
     *
     * @param session
     * @param destEmailAddress
     * @param subject
     * @return
     */
    private MimeMessage createMimeMessage(Session session, String destEmailAddress, String subject) throws MessagingException {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress(emailAddress));
        // 设置收件人
        // Message.RecipientType.TO 表示收件人
        // Message.RecipientType.CC 表示抄送给XXX
        // Message.RecipientType.BCC 表示暗送
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(destEmailAddress));
        // 设置主题
        message.setSubject(subject);
        return message;
    }

    /**
     * 创建session
     *
     * @return
     */
    private Session createSSLSession() throws GeneralSecurityException {
        // 创建属性文件对象
        Properties pro = new Properties();
        // 需要指定邮件的服务器地址，复制一下。推荐去复制，自己写容易写错了。
        // 邮件服务器主机
        pro.setProperty("mail.smtp.host", emailHost);
        // 邮件传输协议
        pro.setProperty("mail.transport.protocol", "smtp");
        // 设置邮件发送需要认证
        pro.setProperty("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        pro.put("mail.smtp.ssl.enable", "true");
        pro.put("mail.smtp.ssl.socketFactory", sf);
        pro.put("mail.smtp.ssl.protocols", "TLSv1.2");
        //端口
        //pro.setProperty("mail.smtp.ssl.enable", "true");
        // 连接邮件的服务器，会话
        Session session = Session.getInstance(pro, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
        session.setDebug(false);
        return session;
    }


}
