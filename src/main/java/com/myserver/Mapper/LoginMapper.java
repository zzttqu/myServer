package com.myserver.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myserver.Dao.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginMapper extends BaseMapper<Login> {
    @Select("select uid,dateTime from login where uid=#{uid} order by dateTime DESC")
    List<Login> collectLogin(Integer uid);
}
