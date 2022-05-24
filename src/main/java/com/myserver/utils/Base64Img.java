package com.myserver.utils;

import com.myserver.Dao.Avatar;
import com.myserver.Mapper.AvatarMapper;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;


public class Base64Img{
    @Resource
    private AvatarMapper avatarMapper;

    public static byte[] decode(String base64Str) {
        Base64.Decoder decoder = Base64.getDecoder();
        String data;
        String[] d = base64Str.split("base64,");
        if (d.length == 2) {
            data = d[1];
            return decoder.decode(data);
        }
        else {
            return null;
        }
    }

    public static String encode(byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        String base64 = "data:image/jpeg;base64," + encoder.encodeToString(bytes);
        return base64;
    }

    private void codePic() throws IOException {
        String uploadFolder = "C:\\uploadFiles\\img\\avatar";
        File[] files = new File(uploadFolder).listFiles();
        for (File file : files) {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String fileName = file.getName().replaceAll("[.][^.]+$", "");
            avatarMapper.insert(new Avatar(Integer.parseInt(fileName),bytes));
        }
    }

//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        codePic();
//    }
}
