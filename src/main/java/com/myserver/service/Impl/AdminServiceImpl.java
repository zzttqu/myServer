package com.myserver.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myserver.Dao.Dialog;
import com.myserver.Dao.Login;
import com.myserver.Dao.MyUser;
import com.myserver.Mapper.DialogMapper;
import com.myserver.Mapper.LoginMapper;
import com.myserver.Mapper.MyUserMapper;
import com.myserver.Dto.ChangeStatusDto;
import com.myserver.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    MyUserMapper myUserMapper;
    @Resource
    LoginMapper loginMapper;
    @Resource
    DialogMapper dialogMapper;

    @Override
    public Boolean checkUserStatus(String username) {
        MyUser myUser = myUserMapper.getByUsername(username);
        return myUser.getStatus() == 5;
    }

    @Override
    public List<Login> getAllLogin(Integer pageNum) {
        Page<Login> page = new Page<>(pageNum, 10);
        QueryWrapper<Login> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("uid", "lastip", "dateTime");
        queryWrapper.orderByDesc("dateTime");
        return loginMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public List<MyUser> getAllUser(Integer pageNum) {
        Page<MyUser> page = new Page<>(pageNum, 5);
        QueryWrapper<MyUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("uid", "email", "username", "lastLoginTime", "createTime", "status", "exp")
                .orderByDesc("createTime");
        return myUserMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public Boolean changeUserInfo(ChangeStatusDto changeStatusDto) {
        return myUserMapper.updateUserStatus(changeStatusDto.getStatus(), changeStatusDto.getId());
    }

    @Override
    public List<Dialog> getAllDialog(Integer pageNum) {
        Page<Dialog> page = new Page<>(pageNum, 5);
        QueryWrapper<Dialog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().orderByDesc("dateTime");
        return dialogMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public Boolean changeDialogInfo(ChangeStatusDto changeStatusDto) {
        return dialogMapper.updateDialogStatus(changeStatusDto.getId(), changeStatusDto.getStatus());
    }
}
