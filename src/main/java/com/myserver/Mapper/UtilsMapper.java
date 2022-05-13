package com.myserver.Mapper;

import com.myserver.Dao.UtilsDao.UserKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UtilsMapper {
    @Select("select uid, rawpassword password from user")
    List<UserKey> selectAll();

    boolean updateAllUserKey(@Param("uids") List<UserKey> uids);

    @Update("update user set userkey=#{password} where uid=#{uid}")
    boolean updateOneUserKey(String password, Integer uid);
}
