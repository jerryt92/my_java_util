package com.tjl.util;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/**
 * RSA工具类，包含加密和数字签名的功能
 *
 * @author TianJingli
 */
public class RSAUtil {

    // RSA密钥长度，须在512-16384之间
    private static final int RSA_KEY_SIZE = 1024;
    /**
     * 获取RSA公钥/私钥对
     * @return
     */
    public static KeyPair getRSAKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(RSA_KEY_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把公钥导出为字节
     * @param publicKey
     * @return
     */
    public static byte[] getPublicKeyBytes(PublicKey publicKey) {
        return publicKey.getEncoded();
    }

    /**
     * 把字节转换为公钥
     * @param publicKeyBytes
     * @return
     */
    public static PublicKey getPublicKeyFromBytes(byte[] publicKeyBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把私钥导出为字节
     * @param privateKey
     * @return
     */
    public static byte[] getPrivateKeyBytes(PrivateKey privateKey) {
        return privateKey.getEncoded();
    }

    /**
     * 把字节转换为私钥
     * @param privateKeyBytes
     * @return
     */
    public static PrivateKey getPrivateKeyFromBytes(byte[] privateKeyBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 用公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用私钥解密
     * @param encrypted
     * @param privateKey
     * @return
     */
    public static byte[] decrypt(byte[] encrypted, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用私钥进行签名
     * @param data
     * @param privateKey
     * @return
     */
    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用公钥解密签名
     */
    public static byte[] decryptSign(byte[] signedData, PublicKey publicKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(signedData);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, GeneralSecurityException {

        // 加密解密演示

        String msg = "Hello RSA.";
        System.out.println("原文：" + msg);
        // 获取公钥/私钥对
        KeyPair keyPair = getRSAKeyPair();
        // 将公钥/私钥转为可明文显示的字符串
        String publicKeyStr = Base64.getEncoder().encodeToString(getPublicKeyBytes(keyPair.getPublic()));
        String privateKeyStr = Base64.getEncoder().encodeToString(getPrivateKeyBytes(keyPair.getPrivate()));
        // 明文显示公钥/私钥
        System.out.println("PublicKey : " + publicKeyStr);
        System.out.println("PrivateKey : " + privateKeyStr);
        // 使用公钥进行加密（先将字符串类型的公钥转换为PublicKey类型），并将加密的字节码转为可明文显示的字符串
        String encrypted = Base64.getEncoder().encodeToString(encrypt(msg.getBytes(StandardCharsets.UTF_8), getPublicKeyFromBytes(Base64.getDecoder().decode(publicKeyStr))));
        // 明文显示加密后的数据
        System.out.println("Encrypted : " + encrypted);
        // 使用私钥进行解密
        System.out.println("Decrypted : " + new String(decrypt(Base64.getDecoder().decode(encrypted), getPrivateKeyFromBytes(Base64.getDecoder().decode(privateKeyStr))), "UTF-8"));

        // 数字签名演示

        String myInfo = "TianJingli";
        // 使用私钥进行签名
        String signed = Base64.getEncoder().encodeToString(sign(myInfo.getBytes(StandardCharsets.UTF_8), keyPair.getPrivate()));
        System.out.println("Signed : " + signed);
        // 用公钥解密签名内容
        System.out.println("Signer : "+new String(decryptSign(Base64.getDecoder().decode(signed),keyPair.getPublic()),"UTF-8"));
    }
}
