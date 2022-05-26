package com.myserver.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myserver.Dao.ExpCount;
import com.myserver.Dao.ExpInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExpInfoMapper extends BaseMapper<ExpInfo> {
    @Select("SELECT COUNT(1) FROM expinfo WHERE uid=#{uid} and TO_DAYS(dateTime)=TO_DAYS(now()) AND cause=#{cause}")
    Integer singleExp(Integer uid, Integer cause);

    @Select("SELECT cause,count FROM(SELECT uid,dateTime,cause,COUNT(1) count FROM expinfo WHERE uid=#{uid} and TO_DAYS(dateTime)=TO_DAYS(now()) GROUP BY cause  ORDER BY NULL) c")
    List<ExpCount> expList(Integer uid);

    //获取单个用户的exp
    @Select("select SUM(exp) AS total,uid,status from expinfo where uid=1 and status=0")
    Integer getTotal(Integer uid);

    //更新单个用户的登录信息
//    @Update("UPDATE `user` SET exp=(select SUM(exp) AS total from expinfo GROUP BY uid HAVING (uid=#{uid} and status=0)) where uid=#{uid}")
//    Integer updateOne(Integer uid);
}
