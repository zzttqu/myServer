package com.myserver;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myserver.Dao.MyUser;
import com.myserver.Mapper.ExpInfoMapper;
import com.myserver.Mapper.UtilsMapper;
import com.myserver.Dao.UtilsDao.UserKey;
import com.myserver.service.ExpInfoService;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.*;
import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.myserver.utils.UidPasswordUtils.decrypt;


@SpringBootTest
class MyServerApplicationTests {

    @Autowired
    private ExpInfoService expInfoService;
    @Resource
    private UtilsMapper utilsMapper;
    @Resource
    private ExpInfoMapper expInfoMapper;

    @Test
    void TestTest() {
        System.out.println(expInfoMapper.getTotal(1));
    }

    @Test
    void contextLoads() {
        String s = DigestUtils.sha1Hex("123");
        String s3 = DigestUtils.sha256Hex("123");
        String s4 = DigestUtils.sha256Hex("123");
        System.out.println(s);
        System.out.println(s3);
    }

    @Test
    void passwordTest() {
        List<UserKey> userKeys = utilsMapper.selectAll();
        List<UserKey> newUserKeys = new ArrayList<>();
        System.out.println(userKeys);
        for (UserKey item : userKeys) {
            newUserKeys.add(new UserKey(item.getUid(), DigestUtils.sha256Hex(item.getUid() + item.getPassword())));
        }
        System.out.println(newUserKeys);
        utilsMapper.updateAllUserKey(newUserKeys);
    }

    @Test
    void passwordDecode() {
        ObjectMapper mapper = new ObjectMapper();
        long startTime = System.currentTimeMillis();
        try {
            String jsonStr = decrypt("U2FsdGVkX1//Vdc0zngVbBmXuwXxqkONq9XryBdYAp0LvR1brcAYcaq/eNqvIXMLjaBJTmdxArlntyRonrWmng==");
            MyUser myUser = mapper.readValue(jsonStr, MyUser.class);
            System.out.println(myUser);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException | JsonProcessingException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运⾏时间：" + (endTime - startTime) + "ms");
    }

    @Test
    void upload() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization","fiMHwj7hVUGnjyAYQ2R7AFrluBLopmhZ");
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        File file=new File("E:\\picture\\1634294271735 (2).jpg");
        param.add("smfile", new FileSystemResource(file));
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(param, headers);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(headers.toString());
        String body = restTemplate.postForEntity("https://sm.ms/api/v2/upload", r, String.class).getBody();
        System.out.println(body);
    }
    @Test
    void cosGen(){
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {
            //这里的 SecretId 和 SecretKey 代表了用于申请临时密钥的永久身份（主账号、子账号等），子账号需要具有操作存储桶的权限。
            // 替换为您的云 api 密钥 SecretId
//            config.put("secretId", "AKIDqgngOktqmV5Uoxsc9RUCtTq9KRYaTNB3");
//            // 替换为您的云 api 密钥 SecretKey
//            config.put("secretKey", "fBnNfxIZYoP8ojdSE9974pSt8NSOAnR9");

            // 设置域名:
            // 如果您使用了腾讯云 cvm，可以设置内部域名
            //config.put("host", "sts.internal.tencentcloudapi.com");

            // 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", "imagehost-1306578662");
            // 换成 bucket 所在地区
            config.put("region", "ap-beijing");

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
            // 列举几种典型的前缀授权场景：
            // 1、允许访问所有对象："*"
            // 2、允许访问指定的对象："a/a1.txt", "b/b1.txt"
            // 3、允许访问指定前缀的对象："a*", "a/*", "b/*"
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefixes", new String[] {"postImages"});

            // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
            // 简单上传、表单上传和分块上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分块上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            Response response = CosStsClient.getCredential(config);
            System.out.println(response.credentials.tmpSecretId);
            System.out.println(response.credentials.tmpSecretKey);
            System.out.println(response.credentials.sessionToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }
    }
}
