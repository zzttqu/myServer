package com.myserver.service.Impl;

import com.myserver.Dao.ExpInfo;
import com.myserver.Dao.SignIn;
import com.myserver.Dto.SignInDto;
import com.myserver.Mapper.ExpInfoMapper;
import com.myserver.Mapper.SignInMapper;
import com.myserver.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 类型：Service
 * 作用：登录服务层
 *
 * @author 张天奕
 * @see SignInService
 * @see SignInMapper
 * @see ExpInfoMapper
 */
@Service
public class SignInServiceImpl implements SignInService {
    @Autowired
    private SignInMapper signInMapper;
    @Autowired
    private ExpInfoMapper expInfoMapper;

    /**
     * 签到核心实现函数
     *
     * @param uid 用户的uid
     * @return 此次签到获得的经验值
     */
    //签到
    @Override
    public Integer signIn(Integer uid) {
        SignIn signIn = signInMapper.selectByUid(uid);
        //设置exp原因为签到 2
        ExpInfo expInfo = new ExpInfo(uid, 2);
        //未签过到
        if (null == signIn) {
            signInMapper.insert(new SignIn(uid, LocalDateTime.now()));
            expInfo.setExp(10);
        }
        //签过到
        else {
            LocalDate signInTime = signIn.getDateTime().toLocalDate();
            LocalDate currTime = LocalDate.now();
            long daysDiff = ChronoUnit.DAYS.between(signInTime, currTime);
            //重复签到
            if (daysDiff <= 0) {
                return 0;
            }
            if (daysDiff > 1) {
                // 1, 超过一天, 把连续签到的天数重置为 1
                signIn.setContinueDays(1);
                expInfo.setExp(10);
            }
            else {
                // 2, 没有超过一天, 把连续签到的天数+1
                signIn.setContinueDays(signIn.getContinueDays() + 1);
                Integer conDays = signIn.getContinueDays();
                //根据不同的登录天数积分
                if (conDays == 2) {
                    expInfo.setExp(10);
                }
                else if (conDays == 3) {
                    expInfo.setExp(20);
                }
                else if (conDays == 4) {
                    expInfo.setExp(20);
                }
                else if (conDays == 5) {
                    expInfo.setExp(30);
                }
                else if (conDays == 6) {
                    expInfo.setExp(30);
                }
                else if (conDays == 7) {
                    expInfo.setExp(60);
                }
            }
            signIn.setDateTime(LocalDateTime.now());
            signInMapper.updateById(signIn);
        }
        expInfoMapper.insert(expInfo);
        return expInfo.getExp();
    }

    /**
     * 获得该uid的签到信息
     * @param uid 用户uid
     * @return {@link SignInDto}
     */
    //签到列表
    @Override
    public List<SignInDto> signInList(Integer uid) {
        SignIn signIn = signInMapper.selectByUid(uid);
        List<SignInDto> list = new ArrayList<>(7);
        if (null == signIn) {
            //没有签过到
            for (int i = 1; i < 8; i++) {
                list.add(new SignInDto(i, 0));
            }
        }
        else {
            LocalDate signInTime = signIn.getDateTime().toLocalDate();
            LocalDate currTime = LocalDate.now();
            long daysDiff = ChronoUnit.DAYS.between(signInTime, currTime);
            Integer continueDays;
            if (daysDiff > 1) {
                // 1, 超过一天, 把返回到前端的签到的天数重置为0，后端等签到时候看就行了。
                // 设置定时任务吧，一天一次
                // signIn.setContinueDays(0);
                // sigInDao.updateById(signIn);
                continueDays = 0;
            }
            else {
                continueDays = signIn.getContinueDays();
            }
            //只要签过到了，但是不到7天都是这样
            if (continueDays <= 6) {
                //这个是奖励部分

                //
                for (int i = 1; i < 8; i++) {
                    if (i <= continueDays) {
                        list.add(new SignInDto(i, 1));
                    }
                    else {
                        list.add(new SignInDto(i, 0));
                    }
                }
            }
            else {
                //6天后签到天数要随着日期增加
                for (int i = 5; i > -2; i--) {
                    if (i > -1) {
                        list.add(new SignInDto(continueDays - i, 1));
                    }
                    else {
                        list.add(new SignInDto(continueDays + 1, 0));
                    }
                }
            }
        }
        return list;
    }

}
