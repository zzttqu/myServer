package com.myserver.service;

import com.myserver.Dao.Post;
import com.myserver.Dao.UserLike;

import java.util.List;

public interface PostService {
    List<Post> getGeneralPosts(Integer num);

    Boolean likePost(UserLike userLike);

    List<Post> getImgPosts(Integer num);

    Boolean createPost(Post post);

//    Boolean createImage(List<ImgInfoDto> imgInfos);
}
