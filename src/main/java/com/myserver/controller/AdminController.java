package com.myserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myserver.Dao.Login;
import com.myserver.Dao.MyUser;
import com.myserver.Dto.AdminUserInfoDto;
import com.myserver.Dto.ChangeStatusDto;
import com.myserver.config.myannotation.AccessLimit;
import com.myserver.service.AdminService;
import com.myserver.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    @GetMapping("/check")
    public R checkStatus(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if (!("").equals(username)){
            if (adminService.checkUserStatus(username)) {
                return new R(1, 1);
            }
            else {
                return new R(1, 0);
            }
        }
        else {
            return new R(1, 0);
        }


    }

    @GetMapping("/user")
    public R getAllUser(Integer pageNum) throws JsonProcessingException {
        List<MyUser> allUser = adminService.getAllUser(pageNum);
        List<AdminUserInfoDto> dto = new ArrayList<>();
        for (MyUser user : allUser) {
            user.setEmail(user.getEmail().replaceAll(".{3,6}@", "****@"));
            dto.add(
                    new AdminUserInfoDto(
                            user.getUid(),
                            user.getEmail(),
                            user.getUsername(),
                            user.getLastLoginTime(),
                            user.getCreateTime(),
                            user.getStatus(),
                            user.getExp()));
        }
        return new R(1, dto);
    }

    @AccessLimit(seconds = 10, maxCount = 100)
    @GetMapping("/login")
    public R getAllLogin(Integer pageNum) {
        List<Login> allLogin = adminService.getAllLogin(pageNum);

        return new R(1, allLogin);
    }

    @PostMapping("/changeuser")
    public R changeUserStatus(@RequestBody ChangeStatusDto changeStatusDto) {
        if (changeStatusDto.getStatus() == 5) {
            return new R(0, 0);
        }
        if (adminService.changeUserInfo(changeStatusDto)) {
            return new R(1, 1);
        }
        else {
            return new R(1, 0);
        }
    }

    @GetMapping("/dialog")
    public R getAllDialog(Integer pageNum) {
        return new R(1, adminService.getAllDialog(pageNum));
    }

    @PostMapping("/changedialog")
    public R changeDialogStatus(@RequestBody ChangeStatusDto changeStatusDto) {
        if (adminService.changeDialogInfo(changeStatusDto)) {
            return new R(1, 1);
        }
        else {
            return new R(1, 0);
        }
    }
}
