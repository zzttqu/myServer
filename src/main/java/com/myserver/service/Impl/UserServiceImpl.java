package com.myserver.service.Impl;


import com.myserver.Dao.Avatar;
import com.myserver.Dao.Login;
import com.myserver.Dto.UserInfoDto;
import com.myserver.Mapper.AvatarMapper;
import com.myserver.Mapper.LoginMapper;
import com.myserver.Mapper.MyUserMapper;
import com.myserver.Mapper.UserLikeMapper;
import com.myserver.Dao.MyUser;
import com.myserver.service.SendMailService;
import com.myserver.service.UserService;
import com.myserver.utils.Base64Img;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.myserver.utils.UidPasswordUtils.decrypt;

/**
 *
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private MyUserMapper myUserMapper;
    @Resource
    private SendMailService sendMailService;
    @Resource
    private UserLikeMapper userLikeMapper;
    @Resource
    private LoginMapper loginMapper;
    @Resource
    private AvatarMapper avatarMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public UserInfoDto checkPassword(String userKey) {
        //简单粗暴的使用key进行寻找，有就是有，没有就是没有
        MyUser myUser = myUserMapper.getByKey(userKey);
        if (myUser == null) {
            return new UserInfoDto(
                    null, null,
                    null, 0, null);
        }
        Avatar avatar = avatarMapper.selectById(myUser.getAvatar());
        if (avatar == null) {
            return new UserInfoDto(
                    myUser.getUsername(),
                    myUser.getUid(),
                    userLikeMapper.getUserLikeByUid(myUser.getUid()), myUser.getStatus(), null);
        }
        String base64str = Base64Img.encode(avatar.getAvatar());
        return new UserInfoDto(
                myUser.getUsername(),
                myUser.getUid(),
                userLikeMapper.getUserLikeByUid(myUser.getUid()), myUser.getStatus(), base64str);
    }

    //更新用户登录信息
    //别再更新了！直接插入到login表中，到时候统计都好统计
    @Override
    public Boolean updateUserLoginInfo(Integer uid, String ip) {
        return loginMapper.insert(new Login(uid, ip)) == 1;
    }

    //忘记密码验证
    @Override
    public String sendCode(String email) {
        if (sendMailService.sendCodeToEmail(email, "changePassword").equals("failed")) {
            return "failed";
        }
        return "success";
    }

    @Override
    public Boolean changeUsername(Integer uid, String username) {
        String original = myUserMapper.selectUsernameByUid(uid);
        if (myUserMapper.updateUsername(uid, username)) {
            stringRedisTemplate.delete("usernameNum::" + original);
            stringRedisTemplate.opsForValue().set("usernameNum::" + username, uid.toString(), 5, TimeUnit.MINUTES);
            return true;
        }

        return false;
    }
    //查看是否登录+获取用户喜爱的
//    @Override
//    @Cacheable(value = "like", key = "#uid")
//    public List<Integer> checkLogin(Integer uid) {
//        return userLikeMapper.getUserLikeByUid(uid);
//    }

    /**
     * 修改密码
     *
     * @param uid     指定是哪个UID，修改
     * @param userKey 修改后的密码
     * @return 修改成功与否
     */
    //修改密码
    @Override
    public Boolean changePassword(Integer uid, String userKey) {
        String rawPassword;
        try {
            rawPassword = decrypt(userKey);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return false;
        }
        myUserMapper.updateRawPassword(uid, rawPassword);
        myUserMapper.updateUserKey(uid, DigestUtils.sha256Hex(uid + rawPassword));
        return true;
    }

    @Override
    public Boolean changeAvatar(Integer uid, String base64Str) {
        Avatar avatar = new Avatar();
        avatar.setUid(uid);
        avatar.setAvatar(Base64Img.decode(base64Str));
        avatarMapper.insert(avatar);
        return myUserMapper.updateUserAvatar(avatar.getId(), uid) == 1;
    }

    /**
     * 看看有没有新功能判断用户上次登录和更新的时间
     *
     * @param uid 用户的uid
     * @return 返回是不是真
     */
    @Override
    public Boolean getUserLogin(Integer uid) {
        List<Login> list = loginMapper.collectLogin(uid);
        LocalDateTime d1 = LocalDateTime.of(2022, 4, 23, 10, 23, 0);
        //获取上一次的记录,如果只有一次也就是新建用户就直接返回FALSE
        if (list.size() >= 2) {
            LocalDateTime d2 = list.get(1).getDateTime();
//            return d2.isBefore(d1);
            return false;
        } else {
            return false;
        }

    }
}
