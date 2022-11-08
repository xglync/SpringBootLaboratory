package org.example.config;

import org.example.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private static final String[] whiteNames = {"/login","/registry","swagger_ui.html"};
    private static final String[] blackNames = {"/user/**","/doctor/**","/department/**","/hospital/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义的拦截器对象
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns(blackNames)
                .excludePathPatterns(whiteNames);

    }

    //向容器注入拦截器对象
    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();

    }
}
