package com.myserver.service;

import com.myserver.Dao.Dialog;
import com.myserver.Dao.Login;
import com.myserver.Dao.MyUser;
import com.myserver.Dto.ChangeStatusDto;

import java.util.List;

public interface AdminService {
    List<Login> getAllLogin(Integer pageNum);

    List<MyUser> getAllUser(Integer pageNum);

    Boolean changeUserInfo(ChangeStatusDto changeStatusDto);

    List<Dialog> getAllDialog(Integer pageNum);

    Boolean changeDialogInfo(ChangeStatusDto changeStatusDto);

    Boolean checkUserStatus(String username);
}
