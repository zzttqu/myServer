package com.myserver.service;

import com.myserver.utils.R;

public interface SignInService {
    Integer signIn(Integer uid);

    R signInList(Integer uid);
}
