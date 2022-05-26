package com.myserver.service;

import com.myserver.Dao.Post;
import com.myserver.Dao.UserLike;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<Post> getPosts(Integer num);

    Boolean likePost(UserLike userLike);

    Boolean createPost(Post post);

    Integer createImage(MultipartFile img) throws IOException;
}
