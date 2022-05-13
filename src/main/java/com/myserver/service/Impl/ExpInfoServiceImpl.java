package com.myserver.service.Impl;

import com.myserver.Dao.ExpInfo;
import com.myserver.Dto.ExpCountDto;
import com.myserver.Mapper.ExpInfoMapper;
import com.myserver.service.ExpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 类型：Service
 * 作用：经验服务层
 *
 * @author 张天奕
 * @see ExpInfoService
 * @see ExpInfoMapper
 */
@Service
public class ExpInfoServiceImpl implements ExpInfoService {
    @Autowired
    private ExpInfoMapper expInfoMapper;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获得该用户的总经验值
     *
     * @param uid 用户uid
     * @return 返回总经验值
     */

    @Cacheable(value = "exp")
    @Override
    public Integer getUserTotalExp(Integer uid) {
        return expInfoMapper.getOneTotal(uid);
    }

    /**
     * 更新用户的总经验值
     *
     * @param uid 用户uid
     * @return 更新是否成功
     */
    @Override
    public Integer updateUserTotalExp(Integer uid) {
//        return expInfoMapper.updateOne(uid);
        return 1;
    }

    /**
     * 创建经验信息{@link ExpInfoMapper}，而且使用了redis缓存相关信息
     *
     * @param expInfo {@link ExpInfo}
     * @return 返回点赞是否是第一次，第一次返回true
     */
    @Override
    public Integer newExpInfo(ExpInfo expInfo) {
        //得先查今天该项获得exp是否达到上限
        Integer count = getExpNum(expInfo.getUid(), expInfo.getCause());
        //第一项是发帖，第二项是点赞，之后的再想吧
        int[] limit = new int[]{1, 4, 0, 0};
        for (int i = 0; i < limit.length; i++) {
            if (expInfo.getCause() == i) {
                if (count > limit[i]) {
                    return count;
                }
                else {
                    //这步才是写入数据
                    if (expInfoMapper.insert(expInfo) == 1) {
                        stringRedisTemplate.delete("count::SimpleKey [" + expInfo.getUid() + "," + expInfo.getCause() + "]");
                        stringRedisTemplate.delete("exp::" + expInfo.getUid());
                        return count + 1;
                    }
                    else {
                        return count;
                    }
                }
            }
        }
        return 0;
    }

    //在进入user界面的时候获取列表

    /**
     * 获取该用户所有项目的经验值
     *
     * @param uid 该用户uid
     * @return {@link ExpCountDto} 的列表
     */
    @Override
    public List<ExpCountDto> getAllExpNum(Integer uid) {
        List<ExpCountDto> expCountList = expInfoMapper.expList(uid);
        List<ExpCountDto> list = new ArrayList<>();
        //这里是有几种cause
        for (int i = 0; i < 2; i++) {
            list.add(new ExpCountDto(i, 0));
        }
        if (expCountList.size() == 0) {
            return list;
        }
        else {
            for (ExpCountDto count : expCountList) {
                for (ExpCountDto countDto : list) {
                    if (count.getCause().equals(countDto.getCause())) {
                        countDto.setCount(count.getCount());
                    }
                }
            }
        }
        list.sort(Comparator.comparing(ExpCountDto::getCause));

        return list;
    }

    //得先查今天该项获得exp是否达到上限
    //我觉得可以用缓存，毕竟老得查
    @Cacheable(value = "count")
    @Override
    public Integer getExpNum(Integer uid, Integer cause) {
        Integer count = expInfoMapper.singleExp(uid, cause);
        return count;
    }

}
