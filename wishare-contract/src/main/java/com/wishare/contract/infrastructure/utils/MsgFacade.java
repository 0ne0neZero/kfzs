package com.wishare.contract.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Properties;

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
     * 发送邮件
     *
     * @param destEmailAddress 收件方地址
     * @param subject          邮箱的主题
     * @param content
     * @param files
     */
    public void sendEmail(String destEmailAddress, String subject, String content, MultipartFile[] files) {
        //创建session
        Session session = createSSLSession();
        // 3. 创建一封邮件
        MimeMessage message;
        try {
            message = createMimeMessage(session, destEmailAddress, subject);
            MimeMultipart mm = new MimeMultipart();
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(content, "text/html;charset=UTF-8");
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
    private Session createSSLSession() {
        // 创建属性文件对象
        Properties pro = new Properties();
        // 需要指定邮件的服务器地址，复制一下。推荐去复制，自己写容易写错了。
        // 邮件服务器主机
        pro.setProperty("mail.smtp.host", emailHost);
        // 邮件传输协议
        pro.setProperty("mail.transport.protocol", "smtp");
        // 设置邮件发送需要认证
        pro.setProperty("mail.smtp.auth", "true");
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
