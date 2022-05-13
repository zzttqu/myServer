package com.myserver.utils;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CodeUtils {
    public String MD5(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            byte[] byteArray = md5.digest();
            BigInteger bigInt = new BigInteger(1, byteArray);
            // 参数16表示16进制
            StringBuilder result = new StringBuilder(bigInt.toString(16));
            // 不足32位高位补零
            while (result.length() < 32) {
                result.insert(0, "0");
            }
            String code = result.toString();
            code = code.substring(code.length() - 5);
            return code;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "emailCode", key = "#email")
    public String getCode(String email) {
        return null;
    }

//    public static void main(String[] args) {
//        String c = new CodeUtils().MD5("1161085395@qq.com" + "2");
//        c = c.substring(c.length() - 5);
//        System.out.println(c);
//    }
}
