package com.myserver.service;

import com.myserver.Dto.SignInDto;

import java.util.List;

public interface SignInService {
    Integer signIn(Integer uid);

    List<SignInDto> signInList(Integer uid);
}
