package com.myserver.service.Impl;

import com.myserver.Dao.UtilsDao.UserKey;
import com.myserver.Mapper.MyUserMapper;
import com.myserver.Dao.MyUser;
import com.myserver.Mapper.UtilsMapper;
import com.myserver.service.RegisterService;
import com.myserver.service.SendMailService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.myserver.utils.UidPasswordUtils.decrypt;

/**
 * 类型：Service
 * 作用：注册服务层
 *
 * @author 张天奕
 * @see RegisterService
 * @see SendMailService
 * @see MyUserMapper
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private MyUserMapper myUserMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SendMailService sendMailService;
    @Resource
    private UtilsMapper utilsMapper;

    /**
     * 创建一个新用户，含所有信息
     *
     * @param myUser {@link MyUser}
     * @return 创建是否成功
     */
    @Override
    public Boolean createUser(MyUser myUser) {
        try {
            myUser.setRawPassword(decrypt(myUser.getUserKey()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return false;
        }
        if (myUserMapper.insert(myUser) == 1) {
            if (utilsMapper.updateOneUserKey(DigestUtils.sha256Hex(myUser.getUid() + myUser.getRawPassword()), myUser.getUid())) {
                stringRedisTemplate.delete("usernameNum::" + myUser.getUsername());
                stringRedisTemplate.delete("emailNum::" + myUser.getEmail());
                stringRedisTemplate.delete("emailCode::" + myUser.getEmail());
                return true;
            }
            //注册成功则删除对应用户名和邮箱数量的查询
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * 判断该用户名是否注册
     *
     * @param username 用户名
     * @return 返回该用户名是否被注册
     */
    @Override
    @Cacheable(cacheNames = "usernameNum", key = "#username")
    public Integer checkUserName(String username) {
        MyUser user = myUserMapper.searchUsername(username);
        if (user == null) {
            return 0;
        }
        else {
            return user.getUid();
        }
    }


    /**
     * 判断邮箱是否注册
     *
     * @param email 邮箱地址
     * @return 返回该邮箱是否被注册
     */
    @Override
    @Cacheable(cacheNames = "emailNum", key = "#email")
    public Integer checkUserEmail(String email) {
        MyUser user = myUserMapper.searchEmail(email);
        if (user == null) {
            return 0;
        }
        else {
            return user.getUid();
        }

    }

    //发送邮箱验证码
    //注册验证

    /**
     * @param email 验证未被注册的邮箱地址
     * @return 是否发送成功
     */
    @Override
    public String sendCode(String email) {
        if (checkUserEmail(email) == 0) {
            String msg = sendMailService.sendCodeToEmail(email, "register");
            if (msg.equals("failed")) {
                return msg;
            }
            else {
                return "success";
            }
        }
        else {
            return "repeated";
        }
    }


}
