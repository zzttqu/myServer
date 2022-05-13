package com.myserver.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.myserver.Dao.SignIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SignInMapper extends BaseMapper<SignIn> {
    @Select("select id,uid,dateTime,continueDays from signin where uid=#{uid}")
    SignIn selectByUid(Integer uid);

    //这个是每天凌晨四点更新连续签到用的
    @Select("select id,uid,dateTime,continueDays from signin")
    List<SignIn> selectAll();

    //这个用xml写了
    boolean updateAll(List<Integer> ids);

}
