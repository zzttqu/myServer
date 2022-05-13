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
    @Select("select dialog_id,uid,status from userlike where uid=#{uid} and status=0")//会用索引不用担心
    List<Integer> getUserLikeByUid(Integer uid);

    @Select("select uid dialog_id from userlike where uid=#{uid} and dialog_id=#{id}")
    List<UserLike> selectUidDid(Integer uid,Integer id);
    //这个是每隔一段时间更新一次dialog的likes数量
    //如果status为0计数，否则不计数
    @Update("UPDATE dialog INNER JOIN " +
            "(SELECT dialog_id,count(`status`=0 or null) as count FROM userlike GROUP BY dialog_id ) " +
            "c on dialog.dialog_id=c.dialog_id SET dialog.likes=c.count")
    Boolean updateDialogLike();
    @Update("update userlike set status=0 where uid=#{uid} and dialog_id=#{dialog_id}")
    Boolean like(Integer dialog_id,Integer uid);
    @Update("update userlike set status=1 where uid=#{uid} and dialog_id=#{dialog_id}")
    Integer unlike(Integer uid,Integer dialog_id);

    //
}
