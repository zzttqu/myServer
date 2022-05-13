package com.myserver.config;

import com.myserver.Intercepter.AdminInterceptor;
import com.myserver.Intercepter.LoginInterceptor;
import com.myserver.Intercepter.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @Resource
    private LoginInterceptor loginInterceptor;
    @Resource
    private AccessLimitInterceptor accessLimitInterceptor;
    @Resource
    private AdminInterceptor adminInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //这个是静态资源的映射路径
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + uploadFolder);
    }


    //这个是全局前置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/register");
//                .addPathPatterns("/**")
//                .excludePathPatterns("/landr/**")
//                .excludePathPatterns("/dialog")
//                .excludePathPatterns("/feedback")
//                .excludePathPatterns("/file/**");
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin");
        registry.addInterceptor(accessLimitInterceptor)
                .addPathPatterns("/**");
    }
}
