package com.myserver;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myserver.Dao.MyUser;
import com.myserver.Mapper.UtilsMapper;
import com.myserver.Dao.UtilsDao.UserKey;
import com.myserver.service.AdminService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.myserver.utils.UidPasswordUtils.decrypt;


@SpringBootTest
class MyServerApplicationTests {

    @Autowired
    private AdminService adminService;
    @Resource
    private UtilsMapper utilsMapper;

    @Test
    void contextLoads() throws NoSuchAlgorithmException {
        String s = DigestUtils.sha1Hex("123");
        String s3 = DigestUtils.sha256Hex("123");
        String s4 = DigestUtils.sha256Hex("123");
        System.out.println(s);
        System.out.println(s3);
    }

    @Test
    void passwordTest() {
        List<UserKey> userKeys = utilsMapper.selectAll();
        List<UserKey> newUserKeys = new ArrayList<>();
        System.out.println(userKeys);
        for (UserKey item : userKeys) {
            newUserKeys.add(new UserKey(item.getUid(), DigestUtils.sha256Hex(item.getUid() + item.getPassword())));
        }
        System.out.println(newUserKeys);
        utilsMapper.updateAllUserKey(newUserKeys);
    }

    @Test
    void passwordDecode() {
        ObjectMapper mapper=new ObjectMapper();
        long startTime = System.currentTimeMillis();
            try {
                String jsonStr=decrypt("U2FsdGVkX1//Vdc0zngVbBmXuwXxqkONq9XryBdYAp0LvR1brcAYcaq/eNqvIXMLjaBJTmdxArlntyRonrWmng==");
                MyUser myUser = mapper.readValue(jsonStr, MyUser.class);
                System.out.println(myUser);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | JsonProcessingException e) {
                e.printStackTrace();
            }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运⾏时间：" + (endTime - startTime) + "ms");
    }

}
