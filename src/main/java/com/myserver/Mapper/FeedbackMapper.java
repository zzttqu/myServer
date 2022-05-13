package com.myserver.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myserver.Dao.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}
