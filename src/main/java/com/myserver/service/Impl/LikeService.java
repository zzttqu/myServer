package com.myserver.service.Impl;

import com.myserver.Mapper.UserLikeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @deprecated
 */
@Service
public class LikeService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserLikeMapper userLikeMapper;



//    public void likes(Integer uid, Integer dialog_id) {
//        String likedKey = RedisKeyUtils.getLikedId(uid, dialog_id);
//        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey, "1");
//    }
//
//    public void unLikes(Integer uid, Integer dialog_id) {
//        String likedKey = RedisKeyUtils.getLikedId(uid, dialog_id);
//        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey);
//        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey);
//    }
//
//    public List<Integer> sqlToRedis(Integer uid) {
//        List<Integer> userLikes = userLikeDao.getUserLikeByUid(uid);
////        String str = userLikes.toString().replaceAll("(?:\\[|null|\\]| +)", "");
////        redisTemplate.opsForList().leftPushAll(uid.toString(),userLikes);
//        //string是key的类型，Integer是数组中的类型
//        ListOperations<String, Integer> listOps = redisTemplate.opsForList();
//        listOps.leftPushAll(uid.toString(), userLikes);
//        return listOps.range(uid.toString(), 0, -1);
//    }
//
//    public void redisToSql() {
//
//    }

}
