package com.myserver.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myserver.Dao.UserLike;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserLikeMapper extends BaseMapper<UserLike> {
    //获得指定uid喜欢的数据列表
    @Select("select id,uid,status from userlike where uid=#{uid} and status=0")//会用索引不用担心
    List<Integer> getUserLikeByUid(Integer uid);

    @Select("select uid id from userlike where uid=#{uid} and id=#{id}")
    List<UserLike> selectUidDid(Integer uid,Integer id);
    //这个是每隔一段时间更新一次posts的likes数量
    //如果status为0计数，否则不计数
    @Update("UPDATE posts INNER JOIN " +
            "(SELECT id,count(`status`=0 or null) as count FROM userlike GROUP BY id ) " +
            "c on posts.id=c.id SET posts.likes=c.count")
    Boolean updatePostLike();
    @Update("update userlike set status=0 where uid=#{uid} and id=#{id}")
    Integer like(Integer id,Integer uid);
    @Update("update userlike set status=1 where uid=#{uid} and id=#{id}")
    Integer unlike(Integer uid,Integer id);

    //
}
