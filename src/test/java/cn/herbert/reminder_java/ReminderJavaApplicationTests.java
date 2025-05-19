package cn.herbert.reminder_java;

import cn.herbert.reminder_java.pojo.Food;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

@SpringBootTest
class ReminderJavaApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private JavaMailSender javaMailSender;

    private String subject = "Test Email Title";
    private String content = "Test Email Content";
    private String to = "reminder2025@126.com";
    private String from = "reminder2025@126.com";

    @Test
    public void testSendSimpleMail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailMessage.setTo(to);
        mailMessage.setFrom(from);
        javaMailSender.send(mailMessage);
        System.out.println("Email sent successfully");
    }

}
