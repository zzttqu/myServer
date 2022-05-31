package com.myserver.utils.tencent;

import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

@Slf4j
@Component
public class TencentCosUtil {
    //API密钥secretId
    private static final String secretId = "AKIDzqfMtHUXLsCHgpnY4OWR9crtgS3TfbIk";

    //API密钥secretKey
    private static final String secretKey = "IL8gxzFI8j6GAoDQSJxPbYJtEMIzSgfR";

//    //存储桶所属地域
//    private final String region = "存储桶所属地域";
//
//    //存储桶空间名称
//    private final String bucketName = "存储桶空间名称";

    //存储桶访问域名
    private String url;
    @Cacheable(cacheNames = "cosKey")
    public Response cosGen(){
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {
            //这里的 SecretId 和 SecretKey 代表了用于申请临时密钥的永久身份（主账号、子账号等），子账号需要具有操作存储桶的权限。
            // 替换为您的云 api 密钥 SecretId
            config.put("secretId", secretId);
            // 替换为您的云 api 密钥 SecretKey
            config.put("secretKey", secretKey);

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
            config.put("allowPrefixes", new String[] {"postImages/*"});

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
//            System.out.println(response.credentials.tmpSecretId);
//            System.out.println(response.credentials.tmpSecretKey);
//            System.out.println(response.credentials.sessionToken);
            return CosStsClient.getCredential(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }

    }

//    //上传文件前缀路径(eg:/images/) 设置自己的主目录
//    private String prefix;
//
//    /**
//     * 上传File类型的文件
//     *
//     * @param file 文件
//     * @return 上传文件在存储桶的链接
//     */
//    public String upload(File file) {
//        //生成唯一文件名
//        String newFileName = generateUniqueName(file.getName());
//        //文件在存储桶中的key
//        String key = prefix + newFileName;
//        //声明客户端
//        COSClient cosClient = null;
//        try {
//            //初始化用户身份信息(secretId,secretKey)
//            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
//            //设置bucket的区域
//            ClientConfig clientConfig = new ClientConfig(new Region(region));
//            //生成cos客户端
//            cosClient = new COSClient(cosCredentials, clientConfig);
//            //创建存储对象的请求
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
//            //执行上传并返回结果信息
////            new PutObjectRequest(bucketName)
//            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//            return url + key;
//        } catch (CosClientException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭客户端(关闭后台线程)
//            cosClient.shutdown();
//        }
//        return null;
//    }
//
//    /**
//     * upload()重载方法
//     *
//     * @param multipartFile 文件对象
//     * @return 上传文件在存储桶的链接
//     */
//    public String upload(MultipartFile multipartFile) {
//        //生成唯一文件名
//        String newFileName = generateUniqueName(multipartFile.getOriginalFilename());
//        //文件在存储桶中的key
//        String key = prefix  + newFileName;
//        //声明客户端
//        COSClient cosClient = null;
//        //准备将MultipartFile类型转为File类型
//        File file = null;
//        try {
//            //生成临时文件
//            file = File.createTempFile("temp", null);
//            //将MultipartFile类型转为File类型
//            multipartFile.transferTo(file);
//            //初始化用户身份信息(secretId,secretKey)
//            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
//            //设置bucket的区域
//            ClientConfig clientConfig = new ClientConfig(new Region(region));
//            //生成cos客户端
//            cosClient = new COSClient(cosCredentials, clientConfig);
//            //创建存储对象的请求
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
//            //执行上传并返回结果信息
//            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//            return url + key;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭客户端(关闭后台线程)
//            cosClient.shutdown();
//        }
//        return null;
//    }
//
//    /**
//     * upload()重载方法
//     * 流方式上传
//     *
//     * @param multipartFile
//     * @return 上传文件在存储桶的链接
//     */
//    public String uploadStream(MultipartFile multipartFile) {
//        //生成唯一文件名
//        String newFileName = generateUniqueName(multipartFile.getOriginalFilename());
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH) + 1;
//        int day = cal.get(Calendar.DATE);
//        //文件在存储桶中的key
//        String key = prefix + year + "/" + month + "/" + day + "/" + newFileName;
//        //声明客户端
//        COSClient cosClient = null;
//        try {
//            //初始化用户身份信息(secretId,secretKey)
//            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
//            //设置bucket的区域
//            ClientConfig clientConfig = new ClientConfig(new Region(region));
//            //生成cos客户端
//            cosClient = new COSClient(cosCredentials, clientConfig);
//            // 获取文件流
//            InputStream inputStream = multipartFile.getInputStream();
//            // 获取文件名
//            String fileName = multipartFile.getOriginalFilename();
//            // 创建上传Object的Metadata
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(inputStream.available());
//            objectMetadata.setCacheControl("no-cache");
//            objectMetadata.setHeader("Pragma", "no-cache");
//            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
//            objectMetadata.setContentDisposition("inline;filename=" + fileName);
//            //创建存储对象的请求
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
//            //执行上传并返回结果信息
//            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//            return url + key;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭客户端(关闭后台线程)
//            cosClient.shutdown();
//        }
//        return null;
//    }
//
//    /**
//     * Description: 判断Cos服务文件上传时文件的contentType
//     *
//     * @param filenameExtension 文件后缀
//     * @return String
//     */
//    public static String getcontentType(String filenameExtension) {
//        if (filenameExtension.equalsIgnoreCase("bmp")) {
//            return "image/bmp";
//        }
//        if (filenameExtension.equalsIgnoreCase("gif")) {
//            return "image/gif";
//        }
//        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
//                || filenameExtension.equalsIgnoreCase("png")) {
//            return "image/jpeg";
//        }
//        if (filenameExtension.equalsIgnoreCase("html")) {
//            return "text/html";
//        }
//        if (filenameExtension.equalsIgnoreCase("txt")) {
//            return "text/plain";
//        }
//        if (filenameExtension.equalsIgnoreCase("vsd")) {
//            return "application/vnd.visio";
//        }
//        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
//            return "application/vnd.ms-powerpoint";
//        }
//        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
//            return "application/msword";
//        }
//        if (filenameExtension.equalsIgnoreCase("xml")) {
//            return "text/xml";
//        }
//        return "image/jpeg";
//    }
//
//    /**
//     * 根据UUID生成唯一文件名
//     *
//     * @param originalName
//     * @return
//     */
//    public String generateUniqueName(String originalName) {
//        return UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));
//    }
}
