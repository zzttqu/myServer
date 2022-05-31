package com.myserver.config;

import com.myserver.Dao.SignIn;
import com.myserver.Mapper.SignInMapper;
import com.myserver.Mapper.UserLikeMapper;
import com.myserver.utils.UidPasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ScheduleTask {
    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private SignInMapper signInMapper;

    @Resource
    private UidPasswordUtils uidPasswordUtils;

    //每30s执行一次
//    @Scheduled(cron = "0/30 * * * * ? ")
    private void updateDialogLikes() {
        if (userLikeMapper.updatePostLike()) {
        }
        else {
            log.info("Dialog likes fail to update");
        }
    }



    //每天四点更新全体连续签到和exp情况
//    @Scheduled(cron = "0 0 4 * * ? ")
    //@Scheduled(cron = "0/30 * * * * ? ")
    private void updateSignInfo() {
        List<SignIn> signInList = signInMapper.selectAll();
        List<Integer> updateId = new ArrayList<>();
        LocalDate currTime = LocalDate.now();
        long daysDiff;
        LocalDate dateTime;
        for (SignIn signIn : signInList) {
            dateTime = signIn.getDateTime().toLocalDate();
            daysDiff = ChronoUnit.DAYS.between(dateTime, currTime);
            if (daysDiff > 1 && signIn.getContinueDays() != 0) {
                updateId.add(signIn.getId());
            }
        }
        if (updateId.size() != 0) {
            if (signInMapper.updateAll(updateId)) {
                log.info(updateId.size() + " User sign in info has been updated");
            }
            else {
                log.info(updateId.size() + " User sign in info fail to update");
            }
        }
        else {
            log.info("No user sign in info need to been updated");
        }
    }

//    //每天统计一次全体积分情况汇总到个人表中
//    @Scheduled
//    private void updateExpInfo() {
//        List<Integer> uids=myUserMapper.getAll();
//    }
}
