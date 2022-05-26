package com.myserver.service;

import com.myserver.Dao.Post;
import com.myserver.Dao.UserLike;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<Post> getDialogs(Integer num);

    Boolean likeDialogs(UserLike userLike);

    Boolean createDialog(Post post);

    Integer createImage(MultipartFile img) throws IOException;
}
