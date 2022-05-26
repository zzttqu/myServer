package com.myserver.service;

import com.myserver.Dao.ExpCount;
import com.myserver.Dao.ExpInfo;
import java.util.List;

public interface ExpInfoService {
    //插入一条经验获取信息
    Integer newExpInfo(ExpInfo expInfo);

    //查询该用户当日所有项目条目
    List<ExpCount> getAllExpNum(Integer uid);

    //查询该用户该项当日条数
    Integer getExpNum(Integer uid, Integer cause);

    //查询该用户当日总exp
//    Integer getUserTodayExp(Integer uid);

    //查询该用户总exp
    Integer getUserTotalExp(Integer uid);

    //更新用户exp
    Integer updateUserTotalExp(Integer uid);

}
