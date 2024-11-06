package com.nms.platsvc.tunnel.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA工具类，包含加密和数字签名的功能
 *
 * @author jerryt92.github.io
 * @date 2022/6/20
 */
public class RSAUtil {

    /**
     * RSA密钥长度，须在512-16384之间
     */
    private static final int RSA_KEY_SIZE = 1024;
    private static final String ALGORITHM = "RSA";
    /**
     * 数字签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 生成RSA公钥/私钥对
     *
     * @return keyPair
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(RSA_KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 把公钥转换为byte[]
     *
     * @param publicKey
     * @return
     */
    public static byte[] getPublicKeyBytes(PublicKey publicKey) {
        return publicKey.getEncoded();
    }

    /**
     * 把byte[]转换为公钥
     *
     * @param publicKeyBytes
     * @return
     */
    public static PublicKey getPublicKeyFromBytes(byte[] publicKeyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        return publicKey;
    }
    /**
     * 从PEM格式的公钥字符串中获取公钥
     * @param pem
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromPEM(String pem) throws Exception {
        String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 把私钥转换为byte[]
     *
     * @param privateKey
     * @return
     */
    public static byte[] getPrivateKeyBytes(PrivateKey privateKey) {
        return privateKey.getEncoded();
    }

    /**
     * 把byte[]转换为私钥
     *
     * @param privateKeyBytes
     * @return
     */
    public static PrivateKey getPrivateKeyFromBytes(byte[] privateKeyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        return privateKey;
    }

    /**
     * 用公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);

    }

    /**
     * 用私钥解密
     *
     * @param encrypted
     * @param privateKey
     * @return
     */
    public static byte[] decrypt(byte[] encrypted, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);
    }

    /**
     * 签名
     *
     * @param privateKeyStr 私钥
     * @param data          签名内容
     * @return signature 签名值
     */
    public static String sign(String privateKeyStr, String data)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        PrivateKey privateKey = RSAUtil.getPrivateKeyFromBytes(Base64.getDecoder().decode(privateKeyStr));
        Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
        Sign.initSign(privateKey);
        Sign.update(data.getBytes());
        String signature = Base64.getEncoder().encodeToString(Sign.sign());
        return signature;
    }

    /**
     * 验签
     *
     * @param publicKeyStr 公钥
     * @param data         验签内容
     * @param signature    签名值
     * @return 验签结果
     */
    public static boolean verifySign(String publicKeyStr, String data, String signature)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        PublicKey publicKey = RSAUtil.getPublicKeyFromBytes(Base64.getDecoder().decode(publicKeyStr));
        Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
        verifySign.initVerify(publicKey);
        verifySign.update(data.getBytes());
        return verifySign.verify(Base64.getDecoder().decode(signature.substring(0, 172)));
    }

    public static void main(String[] args) throws Exception {
        // 加密解密演示
        System.out.println("\n===========加密解密演示===========\n");
        String msg = "Hello RSA.";
        System.out.println("原文：" + msg);
        // 获取公钥/私钥对
        KeyPair keyPair = generateRSAKeyPair();
        // 将公钥/私钥转为可明文显示的字符串
        String publicKeyStr = Base64.getEncoder().encodeToString(getPublicKeyBytes(keyPair.getPublic()));
        String privateKeyStr = Base64.getEncoder().encodeToString(getPrivateKeyBytes(keyPair.getPrivate()));
        // 明文显示公钥/私钥
        System.out.println("PublicKey : " + publicKeyStr);
        System.out.println("PrivateKey : " + privateKeyStr);
        // 使用公钥进行加密（先将字符串类型的公钥转换为PublicKey类型），并将加密的字节码转为可明文显示的字符串
        String encrypted = Base64.getEncoder().encodeToString(encrypt(msg.getBytes(StandardCharsets.UTF_8),
                getPublicKeyFromBytes(Base64.getDecoder().decode(publicKeyStr))));
        // 明文显示加密后的数据
        System.out.println("Encrypted : " + encrypted);
        // 使用私钥进行解密
        System.out.println("Decrypted : " + new String(decrypt(Base64.getDecoder().decode(encrypted),
                getPrivateKeyFromBytes(Base64.getDecoder().decode(privateKeyStr))), "UTF-8"));
        // 数字签名演示
        System.out.println("\n===========数字签名演示===========\n");
        System.out.println("公钥： " + publicKeyStr);
        System.out.println("私钥： " + privateKeyStr);
        System.out.println("原文：" + msg);
        String signature = sign(privateKeyStr, msg);
        System.out.println("签名值：" + signature);
        System.out.println("验签结果：" + verifySign(publicKeyStr, msg, signature));
        PublicKey publicKey = getPublicKeyFromPEM("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDIPaXTqWrwO/bsE6D7pnT8Q5Yzw3QU3bOucOSfuIWcpwZc6mh7gHvrRZoHTkLwjooeKIxc6o4oKvXaQUjXRWqZyuH5Dy+xGQ5mi/3CwlE9/kCa/0wMN9+gA3RyNCK5skb03KZCNTnpOpXIEZlg60AVjjTpSu579GGcVg8TrT9xlD9TxQGo9FfFXkXvM1OXawM0PSoVNvsojdCHwl+IPwtzuN14cmTweUNHupGVU+MT+PraU76cFJ86X8XTTXEFnZkw4BaPCivZCZfKsW+Zf7AN/9l3C2Yzk/jYSNnUa/3ejJQ3Pq4/ZOD/l2wrhIYGEIZBp4rKPGPktjnuXQLGdOf4oML0QX3ze8HQbSb1ED+IUhUkE4Bls1tJ/iMT3oklpKvoT6T7I0XEcOs0f7QpzOZlOJNuS4uhC+4wD5vAvrj7qPMwXEqOc/LfTow+m9fDQjkxrHZDYN1pm66QvnVyekLfMYmDwxj3SudAPmkpIaRyZ4kOiDdSwV14rD5M1P35WhE= tianjingli92@outlook.com\n");
        System.out.println(publicKey);
    }
}
