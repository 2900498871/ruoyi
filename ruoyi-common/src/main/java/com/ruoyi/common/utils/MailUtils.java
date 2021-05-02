package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.Mails;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通过 JavaMail SMTP 发送邮件.
 * <p>
 * 支持三种类型的邮件：
 * <ul>
 * <li>SIMPLE 简单类型，可以发送 HTML 格式文本，遵从 FreeMarker 模板的设置</li>
 * <li>IMAGE HTML 格式类型，同时会保存 HTML 里面的 image 到磁盘，并生成 eml 保存到服务器的配置目录</li>
 * </ul>
 * </p>
 *
 * @author <a href="https://github.com/snowflake3721">snowflake</a>
 * @author <a href="http://88250.b3log.org">ren</a>
 * @version 1.0.2.2, Jun 17, 2019
 * @since 2.1.0
 */
@SpringBootTest
public class MailUtils implements java.io.Serializable {

    public static final String sender = Mails.MAIL_LOCAL_SMTP_SENDER;
    public static final String username = Mails.MAIL_LOCAL_SMTP_USERNAME;
    public static final String password = Mails.MAIL_LOCAL_SMTP_PASSWORD;

    private static final long serialVersionUID = -1000794424345267933L;
    private static final String CHARSET = "text/html;charset=UTF-8";
    private static final boolean is_debug = Boolean.valueOf(Mails.MAIL_LOCAL_ISDEBUG);
    private static final String mail_transport_protocol = Mails.MAIL_LOCAL_TRANSPORT_PROTOCOL;
    private static final String mail_host = Mails.MAIL_LOCAL_HOST;
    private static final String mail_port = Mails.MAIL_LOCAL_PORT;
    private static final String mail_smtp_auth = Mails.MAIL_LOCAL_SMTP_AUTH;
    private static final String mail_smtp_ssl_enable = Mails.MAIL_LOCAL_SMTP_SSL;
    private static final String mail_smtp_starttls_enable = Mails.MAIL_LOCAL_SMTP_STARTTLS;
    private static MailUtils MailUtils;
    private static final Properties prop = new Properties();

    public MailUtils() {
        prop.setProperty("mail.transport.protocol", mail_transport_protocol);
        prop.setProperty("mail.host", mail_host);
        prop.setProperty("mail.port", mail_port);
        prop.setProperty("mail.smtp.auth", mail_smtp_auth);
        prop.setProperty("mail.smtp.ssl.enable", mail_smtp_ssl_enable);
        prop.setProperty("mail.smtp.starttls.enable", mail_smtp_starttls_enable);
        prop.setProperty("mail.smtp.sender", sender);
        prop.setProperty("mail.smtp.username", username);
        prop.setProperty("mail.smtp.passsword", password);
    }

    public static final MailUtils getInstance() {
        if (null == MailUtils) {
            MailUtils = MailUtilsHolder.INSTANCE;
        }
        return MailUtils;
    }

    private static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    private static void setTo(String[] to, MimeMessage message) throws MessagingException, AddressException {
        // 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        if (null != to && to.length > 0) {
            if (to.length == 1) {
                try {
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(to[0]));
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            } else {

                List<InternetAddress> iaList = new ArrayList<InternetAddress>();
                for (String t : to) {
                    InternetAddress ia = new InternetAddress(t);
                    iaList.add(ia);
                }
                InternetAddress[] iaArray = new InternetAddress[to.length];
                try {
                    message.setRecipients(Message.RecipientType.TO, iaList.toArray(iaArray));
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    public  void main() {

        System.out.println(CHARSET.toLowerCase());

        MailUtils mailSender = getInstance();

         String subject = "eml with Image"; String content =
         "这是一封邮件正文带图片<img width=\"60px\" src=\"http://localhost:8080/images/logo-M301-161X105.png\" />的邮件";
          String[] tos = { "2900498871@qq.com" };
        // mailSender.sendMessage(tos, subject, "123123");
        StringBuilder sb = new StringBuilder();
        sb.append("");
        mailSender.sendHTML("测试邮件","测试邮件2",tos,sb.toString());

    }

    private Object readResolve() {
        return MailUtilsHolder.INSTANCE;
    }

    public void sendMessage(String[] tos, String subject, String content) {
        try{
            // 1、创建session
            Session session = Session.getInstance(prop);
            // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(is_debug);
            // 2、通过session得到transport对象
            Transport ts = session.getTransport();
            // 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
            ts.connect(mail_host, Integer.valueOf(mail_port), username, password);
            // 4、创建邮件
            MimeMessage message = new MimeMessage(session);
            createTextMail(message, sender, tos, subject, content);
            // 5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建一封只包含文本的邮件.
     *
     * @param message
     * @param from
     * @param to
     * @param subject
     * @param content
     * @return
     * @throws Exception
     */
    public MimeMessage createTextMail(MimeMessage message, String from, String[] to, String subject, String content) throws Exception {
        // 指明邮件的发件人
        message.setFrom(new InternetAddress(from));
        setTo(to, message);

        // 邮件的标题,只包含文本的简单邮件

        if (StringUtils.isEmpty(subject)) {
            subject = "email from the system";
        }
        message.setSubject(StringUtils.trimToEmpty(subject));
        // 邮件的文本内容
        message.setContent(content, CHARSET);
        // 返回创建好的邮件对象

        return message;
    }

    /**
     * Sends a HTML mail for toMailList.
     *
     * @param fromName
     * @param subject
     * @param toMailList
     * @param html
     */
    public void sendHTML(final String fromName, final String subject, final List<String> toMailList, String html) {
        if (null != toMailList && toMailList.size() > 0) {
            sendHTML(fromName, subject, toMailList.toArray(new String[toMailList.size()]), html);
        }
    }

    /**
     * Sends a HTML mail.
     *
     * @param fromName
     * @param subject
     * @param toMailSingle
     * @param html
     */
    public void sendHTML(final String fromName, final String subject, final String toMailSingle, String html) {
        sendHTML(fromName, subject, new String[]{toMailSingle}, html);
    }

    private void sendHTML(final String fromName, final String subject, final String[] toMail, final String html) {
        try {
            getInstance().sendMessage(toMail, subject, html);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private static class MailUtilsHolder {
        private static final MailUtils INSTANCE = new MailUtils();
    }

}
