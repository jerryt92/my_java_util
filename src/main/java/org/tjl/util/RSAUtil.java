package org.tjl.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.util.HashMap;

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

    public static void main(String[] args) throws Exception {

        // 加密解密演示

        String msg = "Hello RSA.";
        System.out.println("原文：" + msg);
        // 获取公钥/私钥对
        KeyPair keyPair = getRSAKeyPair();
        // 将公钥/私钥转为可明文显示的字符串
//        String publicKeyStr = Base64.getEncoder().encodeToString(getPublicKeyBytes(keyPair.getPublic()));
//        String privateKeyStr = Base64.getEncoder().encodeToString(getPrivateKeyBytes(keyPair.getPrivate()));
        String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcV42kb1PYv5Elj3mY2kKLhY7e7Oeb1pal7E5NH+tNyEUEATol00h1iTvymDk7VehIVKJy1hCY5z6rfubMoJW3hKdgdX9UzwpdfKrKWjf9//ak5yLXN9n7iFL7ng6n0V0r2/k+ispcQqsG8bPKJAHnSBDsGMJZKJfJo08tx0hkmwIDAQAB";
        String privateKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJxXjaRvU9i/kSWPeZjaQouFjt7s55vWlqXsTk0f603IRQQBOiXTSHWJO/KYOTtV6EhUonLWEJjnPqt+5syglbeEp2B1f1TPCl18qspaN/3/9qTnItc32fuIUvueDqfRXSvb+T6KylxCqwbxs8okAedIEOwYwlkol8mjTy3HSGSbAgMBAAECgYAsimkJEspxcso4SDLdUDkrJKa6bgXiCPWsWbFJGbHg3BCFfpABXLtE+Q8CI0oS1Huzt6D8VG4wEZlyJFo+q/Va+0x8dU+JcsUdFORqrlQfr4gX5k721BzbImjvycG7HMvz2ye6Jt+6cUvxbHnzetVW+FFUlioGQOcC2XuDsVuGkQJBAORHRN8s+p4M9EHy2y4gPDfzIaR73wXZFD4LY0DzLiQCXwQDQHV2Y+/XAt7lXrbcWZrmqzamTGyUOBlgXlYRcDMCQQCvU+wDMBrzmPqwvKEazkdqGH+fFKSK9ypK7FFRJOWJAXMd3aauNVzMVKhFmc+MO3PnfOtDq129mc3xclosRbH5AkEAtbjSZ8M7otP3IgS9XKPGrFd9IZ6GdPZROe8AzTSJN5s3nk8kYh2kAsqr+1qmonUZU8lq5K9PyWPYoMLpdiSdpwJBAJRmxG4uWaG26vqNrw+xamEzO1K7dkrpyrKANJQqVt8Qiw/MfTkXkeSiA4xmFHbuG7zkz34HnuDNPrQPxqOSmoECQELY/swtXk5Z4x0Ak9BTXFpCejOzgKPXqbPGLbbS6VER+5p0aaAyiyckVCeN5AeVCI359l128Barq25LpXn8Iws=";
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

        // 获取证书
        KeyPair certificate = getRSAKeyPair();
        HashMap<KeyPair,Object> infoMap = new HashMap<>();
        // 数字签名使用私钥加密原数据的摘要
        String msg_md = MDUtil.getMessageDigest(msg.getBytes(), "SHA-256");
        // 使用私钥进行签名
        String signed = Base64.getEncoder().encodeToString(sign(msg_md.getBytes(StandardCharsets.UTF_8), certificate.getPrivate()));
        System.out.println("Signed : " + signed);
        // 用公钥解密签名内容
        System.out.println("Signer : "+new String(decryptSign(Base64.getDecoder().decode(signed),certificate.getPublic()),"UTF-8"));
        // 验证
        System.out.println(MDUtil.getMessageDigest(msg.getBytes(), "MD5").equals(new String(decryptSign(Base64.getDecoder().decode(signed),certificate.getPublic()))));
    }
}
