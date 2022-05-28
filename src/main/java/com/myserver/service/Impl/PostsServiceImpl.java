package com.myserver.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myserver.Dao.ImgInfo;
import com.myserver.Dao.Login;
import com.myserver.Dao.Post;
import com.myserver.Mapper.PostsMapper;
import com.myserver.Mapper.ImgInfoMapper;
import com.myserver.Mapper.UserLikeMapper;
import com.myserver.Dao.UserLike;
import com.myserver.service.PostService;
import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * 类型：Service
 * 作用：dialog服务层
 *
 * @author 张天奕
 * @see PostService
 * @see PostsMapper
 * @see UserLikeMapper
 */
@Service
public class PostsServiceImpl implements PostService {
    @Resource
    private PostsMapper postsMapper;
    @Resource
    private UserLikeMapper userLikeMapper;
    @Resource
    private ImgInfoMapper imgInfoMapper;
    @Value("${file.uploadFolder}")
    private String uploadFolder;

    /**
     * 点赞操作调用了{@link UserLikeMapper}
     *
     * @param userLike {@link UserLike}
     * @return 返回点赞是否是第一次，第一次返回true
     */
    @Override
    public Boolean likePost(UserLike userLike) {
        UserLike sqlLike = userLikeMapper.selectOne(new QueryWrapper<UserLike>().eq("dialog_id", userLike.getDialog_id()).eq("uid", userLike.getUid()));
        if (sqlLike != null) {
            if (sqlLike.getStatus() == 0) {
                userLikeMapper.unlike(userLike.getUid(), userLike.getDialog_id());
                return false;
            } else if (sqlLike.getStatus() == 1) {
                userLikeMapper.like(userLike.getDialog_id(), userLike.getUid());
                return false;
            }
            //这种情况是不存在的，只是为了防止报错
            else {
                return true;
            }
        } else {
            userLikeMapper.insert(userLike);
            return true;
        }
//        System.out.println(userLikeDao.insert(userLike));

    }

    /**
     * 创建dialog操作调用了{@link UserLikeMapper}
     *
     * @param post {@link Post}
     * @return 返回是否创建成功dialog
     */
    @Override
    public Boolean createPost(Post post) {
        //设置默认状态为标准
        post.setStatus(0);
        post.setLikes(0);
        return postsMapper.insert(post) == 1;
    }

    /**
     * 创建dialog操作调用了{@link UserLikeMapper}
     *
     * @param num 分页
     * @return 返回部分dialog
     */
    @Override
    public List<Post> getGeneralPosts(Integer num) {
        List<Post> posts = postsMapper.selectByPage(num);
        return posts;
    }

    @Override
    public List<Post> getImgPosts(Integer num) {
        Page<Post> page = new Page<>(num, 10);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "status", "category").eq("category", 2);
        queryWrapper.orderByDesc("dateTime");
        List<Post> imgPosts = postsMapper.selectPage(page, queryWrapper).getRecords();
        for (Post p : imgPosts) {
            List<ImgInfo> imgInfos = imgInfoMapper.selectList(new QueryWrapper<ImgInfo>().select().eq("postID", p.getId()));
            p.setImg(imgInfos);
        }
        return imgPosts;
    }

    @Override
    public Integer createImage(MultipartFile img) throws IOException {
        byte[] raw = img.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(raw);
        String suffix = img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf(".") + 1);
        String fileName = UUID.randomUUID() + "." + suffix;
        String rawFileName = "raw_" + fileName;
        String thumbFileName = "thumb_" + fileName;
        File file = new File(uploadFolder + "img\\imgs\\raw\\" + rawFileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(raw);
        fos.close();
        Thumbnails.of(in)
                .size(400, 400)
                .outputQuality(0.8)
                .toFile(uploadFolder + "img\\imgs\\raw\\" + thumbFileName);
//                .asBufferedImage();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ImageIO.write(thumbBufferedImage, "jpg", out);
//        byte[] thumb = out.toByteArray();
        ImgInfo imgInfo = new ImgInfo();
        imgInfo.setPath(fileName);
        imgInfoMapper.insert(imgInfo);
        return imgInfo.getId();
    }
}
