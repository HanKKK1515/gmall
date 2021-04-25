package com.hll.gmall.conf;

import com.hll.gmall.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor);
        interceptorRegistration.addPathPatterns("/**");
        interceptorRegistration.excludePathPatterns("/**/*.css", "/**/*.js");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
