package org.railway.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {
    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender javaMailSender(MailProperties mailProperties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        // 基础配置
        sender.setHost(mailProperties.getHost());
        sender.setPort(mailProperties.getPort());
        sender.setUsername(mailProperties.getUsername());
        sender.setPassword(mailProperties.getPassword());
        sender.setProtocol(mailProperties.getProtocol());

        // 编码设置
        if (mailProperties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(mailProperties.getDefaultEncoding().name());
        }
        sender.setJavaMailProperties(javaMailProperties());
        sender.createMimeMessage();
        return sender;
    }

    private Properties javaMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.ssl.enable", true); // 启用 SSL
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", false);
        props.put("mail.smtp.timeout", 5000);
        props.put("mail.smtp.connectiontimeout", 5000);
        props.put("mail.smtp.starttls.enable", false); // 不使用 STARTTLS
        props.put("mail.mime.address.usecanonicalhostname", false);
        props.put("mail.smtp.localhost", "myComputer");
        props.put("mail.debug", true); // 可选：启用调试日志

        return props;
    }
}

