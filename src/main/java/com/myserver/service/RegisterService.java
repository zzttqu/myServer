package com.myserver.service;

import com.myserver.Dao.MyUser;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface RegisterService {
    Integer checkUserName(String username);

    Integer checkUserEmail(String email);

    String sendCode(String email);

    //Boolean validateCode(EmailCode emailCode);

    Boolean createUser(MyUser myUser) ;
}
