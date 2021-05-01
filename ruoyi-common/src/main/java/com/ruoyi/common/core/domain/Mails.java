package com.ruoyi.common.core.domain;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.InputStream;
import java.util.Properties;

/**
 * ren
 */
@SpringBootTest
public class Mails {
    @Test
    public void loadDate(){
        System.out.println("MAIL_LOCAL_HOST="+MAIL_LOCAL_HOST);
    }

    /**
     * Configurations.
     */
    private static final Properties CFG = new Properties();

    static {
            try{
                Resource resource = new ClassPathResource("commonConfig.properties");
                InputStream is = resource.getInputStream();
                CFG.load(is);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    /**
     * Gets a configuration string property with the specified key.
     *
     * @param key the specified key
     * @return string property value corresponding to the specified key, returns {@code null} if not found
     */
    private static String get(final String key) {
        return CFG.getProperty(key);
    }

    /**
     * Gets a configuration boolean property with the specified key.
     *
     * @param key the specified key
     * @return boolean property value corresponding to the specified key, returns {@code null} if not found
     */
    private static Boolean getBoolean(final String key) {
        final String stringValue = get(key);
        if (null == stringValue) {
            return null;
        }

        return Boolean.valueOf(stringValue);
    }

    /**
     * Gets a configuration float property with the specified key.
     *
     * @param key the specified key
     * @return float property value corresponding to the specified key, returns {@code null} if not found
     */
    private static Float getFloat(final String key) {
        final String stringValue = get(key);
        if (null == stringValue) {
            return null;
        }

        return Float.valueOf(stringValue);
    }

    /**
     * Gets a configuration integer property with the specified key.
     *
     * @param key the specified key
     * @return integer property value corresponding to the specified key, returns {@code null} if not found
     */
    private static Integer getInt(final String key) {
        final String stringValue = get(key);
        if (null == stringValue) {
            return null;
        }

        return Integer.valueOf(stringValue);
    }

    /**
     * Gets a configuration long property with the specified key.
     *
     * @param key the specified key
     * @return long property value corresponding to the specified key, returns {@code null} if not found
     */
    private static Long getLong(final String key) {
        final String stringValue = get(key);
        if (null == stringValue) {
            return null;
        }

        return Long.valueOf(stringValue);
    }



    /**
     * 邮件渠道 {@code local} 是否开启 debug..
     */
    public static final boolean MAIL_LOCAL_ISDEBUG = getBoolean("mail.local.isdebug");

    /**
     * 邮件渠道 {@code local} 发信协议.
     */
    public static final String MAIL_LOCAL_TRANSPORT_PROTOCOL = get("mail.local.transport.protocol");

    /**
     * 邮件渠道 {@code local} 发信服务地址.
     */
    public static final String MAIL_LOCAL_HOST = get("mail.local.host");

    /**
     * 邮件渠道 {@code local} 发信服务端口.
     */
    public static final String MAIL_LOCAL_PORT = get("mail.local.port");

    /**
     * 邮件渠道 {@code local} 是否开启 SMTP 验证.
     */
    public static final String MAIL_LOCAL_SMTP_AUTH = get("mail.local.smtp.auth");

    /**
     * 邮件渠道 {@code local} SMTP 是否启用 SSL.
     */
    public static final String MAIL_LOCAL_SMTP_SSL = get("mail.local.smtp.ssl.enable");

    /**
     * 邮件渠道 {@code local} SMTP 是否启用 TLS.
     */
    public static final String MAIL_LOCAL_SMTP_STARTTLS = get("mail.local.smtp.starttls.enable");

    /**
     * 邮件渠道 {@code local} 发信地址.
     */
    public static final String MAIL_LOCAL_SMTP_SENDER = get("mail.local.smtp.sender");

    /**
     * 邮件渠道 {@code local} SMTP 用户名.
     */
    public static final String MAIL_LOCAL_SMTP_USERNAME = get("mail.local.smtp.username");

    /**
     * 邮件渠道 {@code local} SMPT 密码.
     */
    public static final String MAIL_LOCAL_SMTP_PASSWORD = get("mail.local.smtp.passsword");

}
