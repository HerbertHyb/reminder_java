package cn.herbert.reminder_java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReminderJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReminderJavaApplication.class, args);
    }

}
