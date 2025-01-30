package com.bafang.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.bafang.common.BaseContext;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Slf4j
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 校验是否登录
                    StpUtil.checkLogin();
                    // 设置当前用户 ID 到 ThreadLocal
                    Long userId = StpUtil.getLoginIdAsLong();
                    BaseContext.setCurrentId(userId);

                }))
                .addPathPatterns("/**")
                .excludePathPatterns("/bafang/user/register", "/bafang/user/login");
    }
}