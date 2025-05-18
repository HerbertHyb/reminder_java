package cn.herbert.reminder_java.config;

import cn.herbert.reminder_java.utils.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

public class InterConfig implements WebMvcConfigurer {
    //通用来处理各种跨域的问题
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置不拦截的路径
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/user_register"); //注册
        excludePath.add("/auth/loginByPassword"); //登录
        excludePath.add("/logout"); //登出
        excludePath.add("/static/**");  //静态资源
        excludePath.add("/assets/**");  //静态资源
        registry.addInterceptor(jwtAuthenIntercetpor())
                .addPathPatterns("/**")
                // 排除拦截路径
                .excludePathPatterns(excludePath);
    }

    @Bean
    public JwtInterceptor jwtAuthenIntercetpor() {
        return new JwtInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 允许跨域的路径
                .addMapping("/**")
                // 允许跨域的path
                .allowedOrigins("*")
                // 允许凭证
                .allowCredentials(true)
                // 允许通过方法
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
                // 跨域的生存期
                .maxAge(3600 * 24);
    }
}
