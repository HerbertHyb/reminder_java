package cn.herbert.reminder_java.utils;

import cn.herbert.reminder_java.auth.Msg;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class JwtInterceptor implements HandlerInterceptor {
    // 执行控制器方法前激发
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer requestURL = request.getRequestURL();
        // 获取令牌
        String token = request.getHeader("token");
        System.out.println("url:" + requestURL);
        System.out.println("token:" + token);

        // 如果执行的不是处理器方法，则不必拦截
        if (!(handler instanceof HandlerMethod)) {
            System.out.println("没加注解不拦截...");
            return true;
        }
        HandlerMethod handler1 = (HandlerMethod) handler;
        // 获取拦截的方法对象
        Method method = handler1.getMethod();
        // 从方法对象中查看是否含有指定的注解
        JwtToken jwtToken = method.getAnnotation(JwtToken.class);
        if (jwtToken != null) {// 方法前含有指定注解
            // 校验令牌有效性
            boolean b = JwtUtil.checkToken(token);
            if (b) {
                return true;
            } else {
                // 返回给前端
                Msg msg = Msg.fail("token invalid");
                response.getWriter().write(JSONUtil.toJsonStr(msg));
                return false;
            }
        }
        return true;
    }

    // 执行完控制器方法的业务逻辑后触发
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    // 执行完控制器逻辑视图后，在进入视图解析器前出发
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
