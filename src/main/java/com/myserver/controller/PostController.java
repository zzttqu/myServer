package com.myserver.controller;

import com.myserver.Dao.Post;
import com.myserver.Dao.ExpInfo;
import com.myserver.config.myannotation.AccessLimit;
import com.myserver.service.ExpInfoService;
import com.myserver.service.Impl.PostsServiceImpl;
import com.myserver.utils.IpGetter;
import com.myserver.utils.R;
import com.myserver.Dao.UserLike;
import com.myserver.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 类型：Controller
 * 作用：对话相关操作的api
 *
 * @author 张天奕
 * @see PostsServiceImpl 服务层实现类
 */
@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private ExpInfoService expInfoService;


    /**
     * 获取dialog信息
     *
     * @param number  需要取几个dialog的信息
     * @param request session需要用
     * @return list of Dialog对话列表
     */
    @AccessLimit(maxCount = 10, seconds = 60)
    @GetMapping
    public R getDialogs(Integer number, HttpServletRequest request) {
        String ip = IpGetter.getRemoteHost(request);
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        if (uid == null) {
            log.info("A guest " + ip + " has get dialog");
            if (number != 0) {
                return new R(1, 0);
            }
        }
        return new R(1, postService.getImgPosts(number));
    }

    /**
     * 对指定id点赞
     *
     * @param id      被点赞的dialog_id
     * @param request 需要从session中获取uid
     * @return 首次点赞返回此次点赞获得的exp，其他情况点赞则返回成功
     */
    @GetMapping("/{id}")
    public R likeDialogs(@PathVariable Integer id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer uid = (Integer) session.getAttribute("uid");
        UserLike userLike = new UserLike(uid, id);
        if (postService.likePost(userLike)) {
            Integer count = expInfoService.newExpInfo(new ExpInfo(uid, 1));
            return new R(1, count);
        }
        else {
            return new R(1, 0);
        }
    }

    /**
     * 创建dialog
     *
     * @param post  Dialog对象{@link Post}
     * @param request 需要session中的uid和username
     * @return 发帖返回此次点赞获得的exp，未登录返回失败
     * @see Post
     */
    @PostMapping("/create")
    public R createNewDialogs(@RequestBody Post post, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return new R(0, 0);
        }
        Integer uid = (Integer) session.getAttribute("uid");
        post.setUid(uid);
        post.setTitle(username);
        postService.createPost(post);
        Integer count = expInfoService.newExpInfo(new ExpInfo(uid, 0));
        return new R(1, count);
    }

    /**
     * 上传的图片进行处理，压缩并生成uuid的地址
     *
     * @param files 文件
     * @return 成功就返回上传图片的字符串
     */
    @PostMapping("/img")
    public R uploadImg(@RequestParam("file") MultipartFile[] files) {
        List<Integer> imgList = new ArrayList<>();
        if (files.length == 0) {
            return new R(0, "empty");
        }
        for (MultipartFile multipartFile : files) {

            if (!multipartFile.isEmpty()) {
                try {
                    imgList.add(postService.createImage(multipartFile));
                } catch (Exception e) {
                    return new R(0, "error");
                }
            }
            else {
                return new R(0, "empty");
            }
        }
        //这里传回去对应的图片id
        return new R(1, imgList);
    }
}
