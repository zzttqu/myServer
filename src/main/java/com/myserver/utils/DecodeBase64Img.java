package com.myserver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

public class DecodeBase64Img {
    public static boolean decode(String base64Str, String imgFilePath, String imgName) {
        Base64.Decoder decoder = Base64.getDecoder();
        String data;
        String[] d = base64Str.split("base64,");
        if (d.length == 2) {
            data = d[1];
            byte[] bytes = decoder.decode(data);
            try {
                File dirs = new File(imgFilePath);
                dirs.mkdirs();
                OutputStream out = new FileOutputStream(imgFilePath + imgName);
                out.write(bytes);
                out.flush();
                out.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }
}
