package com.myserver.controller;

import com.myserver.Dao.Dialog;
import com.myserver.Dao.Feedback;
import com.myserver.service.DialogService;
import com.myserver.service.FeedbackService;
import com.myserver.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 类型：Controller
 * 作用：反馈相关操作的api
 * @see com.myserver.service.Impl.FeedbackServiceImpl 服务层实现类
 *
 * @author 张天奕
 */
@Slf4j
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    /**
     * 创建反馈
     *
     * @param feedback {@link Feedback}
     * @param request 获取session
     * @return 返回创建是否成功 Boolean
     */
    @PostMapping
    public R newFeedback(@RequestBody Feedback feedback, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username==null) {
            feedback.setUid(0);
            feedbackService.newFeedback(feedback);
            log.info("A guest has feedback");
        } else {
            feedback.setUid((Integer) session.getAttribute("uid"));
//            System.out.println(feedback);
            feedbackService.newFeedback(feedback);
            log.info("Username: " + username + " has feedback");
        }
        return new R(1, 1);
    }
}
