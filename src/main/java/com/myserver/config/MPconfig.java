package com.myserver.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class MPconfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "status", 0);
        this.fillStrategy(metaObject, "createTime", LocalDateTime.now());
        this.fillStrategy(metaObject, "lastLoginTime", LocalDateTime.now());
        this.fillStrategy(metaObject, "loginTimes", 0);
        this.fillStrategy(metaObject, "continueDays", 1);
        this.fillStrategy(metaObject, "dateTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
    //分页拦截器
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
