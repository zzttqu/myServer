package com.myserver.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myserver.Dao.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DialogMapper extends BaseMapper<Post> {
    //获取对话列表数据
    @Select("select dialog_id,uid,text,img,status,dateTime,likes from dialog where status!=-1 order by dateTime desc limit #{num} , 10")
    List<Post> selectByPage(Integer num);

    //修改对话获得的赞数量
    @Update("update dialog set status=#{status} where dialog_id=#{id}")
    Boolean updateDialogStatus(Integer id, Integer status);

}
