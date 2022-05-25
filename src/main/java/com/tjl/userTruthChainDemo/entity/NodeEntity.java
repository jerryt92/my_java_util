package com.tjl.userTruthChainDemo.entity;

import com.tjl.util.MDUtil;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class NodeEntity {
    private String p_id;

    private String p_key;
    private String c_id;

    private String c_key;
    private String id;
    private String aesData;

    public NodeEntity(String p_id, String p_key, String c_id, String c_key, String id, String password) throws GeneralSecurityException {
        password = getMessageDigest(password.getBytes(StandardCharsets.UTF_8), "MD5");
        this.p_id = p_id;
        this.p_key = aesLock(password, p_key);
        this.c_id = c_id;
        this.c_key = aesLock(password, c_key);
        this.id = id;
        this.aesData = aesLock(password, MDUtil.getMessageDigest(this.toString(), "SHA-256"));
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAesData() {
        return aesData;
    }

    public void setAesData(String aesData) {
        this.aesData = aesData;
    }

    public void showInfo() {
        System.out.println("--------------------NODE INFO--------------------");
        System.out.println("p_id : " + p_id);
        System.out.println("c_id : " + c_id);
        System.out.println("id : " + id);
        System.out.println("dataStr : " + aesData);
        System.out.println("-------------------------------------------------");
    }

    public boolean verifySelf(String password) throws GeneralSecurityException {
        password = getMessageDigest(password.getBytes(StandardCharsets.UTF_8), "MD5");
        System.out.println(MDUtil.getMessageDigest(this.toString(), "SHA-256"));
        System.out.println(aesUnlock(password, this.aesData));
        return MDUtil.getMessageDigest(this.toString(), "SHA-256").equals(aesUnlock(password, this.aesData));
    }

    @Override
    public String toString() {
        return "NodeEntity{" +
                "p_id='" + p_id + '\'' +
                ", p_key='" + p_key + '\'' +
                ", c_id='" + c_id + '\'' +
                ", c_key='" + c_key + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    /**
     * AES加密
     * @param key AES密钥必须为16位(AES-128)或32位(AES-256)
     * @param data
     * @return locked
     * @throws GeneralSecurityException
     */
    public static String aesLock(String key, String data) throws GeneralSecurityException {
        String locked;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        locked = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        return locked;
    }

    /**
     * AES解密
     * @param key AES密钥必须为16位(AES-128)或32位(AES-256)
     * @param data
     * @return unlocked
     * @throws GeneralSecurityException
     */
    public static String aesUnlock(String key, String data) throws GeneralSecurityException {
        String unlocked;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        unlocked = new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        return unlocked;
    }

    /**
     * 信息摘要
     *
     * @param data
     * @param algorithm 可选：MD5 、 SHA-1 、 SHA-256 、 SHA-384 、 SHA-512
     * @return
     */
    private static String getMessageDigest(byte[] data, String algorithm) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(data);

            // 将字节数据转换为十六进制
            for (byte b : bytes) {
                stringBuilder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
