package org.railway.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Email {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    /**
     * 发送 HTML 格式邮件
     */
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 启用 HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败", e);
        }
    }
}
