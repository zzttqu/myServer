package com.myserver.utils;

import java.util.Arrays;
import java.util.Base64;

public class Base64Img {
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
        Base64.Encoder encoder=Base64.getEncoder();
        String base64="data:image/jpeg;base64,"+ encoder.encodeToString(bytes);
        return base64;
    }
}
