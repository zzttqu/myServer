package com.myserver.service;

import com.myserver.Dao.Dialog;
import com.myserver.Dao.UserLike;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DialogService {
    List<Dialog> getDialogs(Integer num);

    Boolean likeDialogs(UserLike userLike);

    Boolean createDialog(Dialog dialog);

    Integer createImage(MultipartFile img) throws IOException;
}
