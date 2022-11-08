package org.example.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    //请求交给控制器之前，做拦截处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截请求，检查token");
        return true;
//        String token = request.getHeader("Authorization");
//        //验证
//        if(token != null){
//            if(token.equals("0") || token.equals("1")){
//                //校验token合法
//                return true;
//            }else {
//                throw new RuntimeException("非法token");
//            }
//        }else {
//            throw new RuntimeException("token为空");
//        }
////        return false;//拦截（false）\放行（true）
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    //在控制器响应返回后，做拦截处理
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("拦截请求，后置处理");
    }
}
