package com.myserver.service.Impl;

import com.myserver.Dao.Feedback;
import com.myserver.Mapper.FeedbackMapper;
import com.myserver.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类型：Service
 * 作用：反馈服务层
 *
 * @author 张天奕
 * @see FeedbackService
 * @see FeedbackMapper
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 新建一条反馈信息
     *
     * @param feedback {@link Feedback}
     * @return 成功就返回真
     */
    @Override
    public Boolean newFeedback(Feedback feedback) {
        int insert = feedbackMapper.insert(feedback);
        return insert == 1;
    }
}
