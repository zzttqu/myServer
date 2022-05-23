package com.myserver.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myserver.Dao.MyUser;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface MyUserMapper extends BaseMapper<MyUser> {
    //登录部分
    //核对用户名和密码
    @Select("select uid,username,email,userkey,status from user where username=#{username}")
    MyUser getByUsername(String username);

    //验证用户密码
    @Select("select uid,username,userkey,status,avatar from user where userkey=#{userKey}")
    MyUser getByKey(String userKey);

    //更新用户登录数据
    @Update("update user set lastLoginTime =#{dateTime},loginTimes=loginTimes+1,lastLoginIp=#{ip} where uid=#{uid}")
    boolean updateLoginInfo(Date dateTime, Integer uid, String ip);

    //修改用户密码
    @Update("update user set rawpassword=#{password} where uid=#{uid}")
    boolean updateRawPassword(Integer uid, String password);

    @Update("update user set userkey=#{userKey} where uid=#{uid}")
    boolean updateUserKey(Integer uid, String userKey);

    @Update("update user set avatar=#{avatar} where uid=#{uid}")
    Integer updateUserAvatar(Integer avatar, Integer uid);

    @Update("update user set username=#{username} where uid=#{uid}")
    boolean updateUsername(Integer uid, String username);

    //在update之前得先删除缓存
    @Select("select uid,username from user where uid=#{uid}")
    String selectUsernameByUid(Integer uid);

    //迅速查询username是否存在
    @Select("select uid,username from user where username=#{username}")
    MyUser searchUsername(String username);

    //迅速查询email是否存在
    @Select("select uid,email from user where email=#{email}")
    MyUser searchEmail(String email);

    //查询所有uid
    @Select("select uid from user")
    List<Integer> getAll();

    //查询所有
    @Select("select uid, email, username, lastLoginTime, exp from user")
    List<MyUser> selectAll();

    //修改用户权限
    @Update("update user status set status=#{status} where uid=#{uid}")
    Boolean updateUserStatus(Integer status, Integer uid);


}
