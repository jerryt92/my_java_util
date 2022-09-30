package org.tjl.util;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: sai
 * @Date: 2022/6/13 0013 23:04
 * @ClassName: SHA256withRSAUtils
 * @Version: 1.0
 * @Description:
 */
public class SHA256withRSAUtils {

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 1024;//设置长度
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String ENCODE_ALGORITHM = "SHA-256";

    /**
     * 生成公、私钥
     * 根据需要返回String或byte[]类型
     *
     * @return
     */
    private static Map<String, String> createRSAKeys() {
        Map<String, String> keyPairMap = new HashMap<String, String>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            /*Map<String, byte[]> byteMap = new HashMap<String, byte[]>();
            byteMap.put(PUBLIC_KEY_NAME, publicKey.getEncoded());
            byteMap.put(PRIVATE_KEY_NAME, privateKey.getEncoded());*/

            //获取公、私钥值
            String publicKeyValue = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyValue = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            //存入
            keyPairMap.put(PUBLIC_KEY, publicKeyValue);
            keyPairMap.put(PRIVATE_KEY, privateKeyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPairMap;
    }

    /**
     * 解码PublicKey
     *
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码PrivateKey
     *
     * @param key
     * @return
     */
    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 签名
     *
     * @param key         私钥
     * @param requestData 请求参数
     * @return
     */
    public static String sign(String key, String requestData) {
        String signature = null;
        byte[] signed = null;
        try {
            PrivateKey privateKey = getPrivateKey(key);

            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();

            signature = Base64.getEncoder().encodeToString(signed);
            System.out.println("===签名结果：" + signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature;
    }

    /**
     * 验签
     *
     * @param key         公钥
     * @param requestData 请求参数
     * @param signature   签名
     * @return
     */
    public static boolean verifySign(String key, String requestData, String signature) {
        boolean verifySignSuccess = false;
        try {
            PublicKey publicKey = getPublicKey(key);

            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(requestData.getBytes());

            verifySignSuccess = verifySign.verify(Base64.getDecoder().decode(signature));
            System.out.println("===验签结果：" + verifySignSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verifySignSuccess;
    }

    public static void main(String[] args) {
        Map<String, String> keyPairMap = createRSAKeys();

        String publicKey = keyPairMap.get(PUBLIC_KEY);
        System.out.println("生成公钥： " + publicKey);

        String privateKey = keyPairMap.get(PRIVATE_KEY);
        System.out.println("生成私钥： " + privateKey);

        System.out.println("===开始RSA公、私钥测试===");
        String str = "alpha=001&beta=002&gamma=003";
        String sign = sign(privateKey, str);

        verifySign(publicKey, str, sign);
    }
}
