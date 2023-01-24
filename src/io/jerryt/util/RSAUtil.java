package io.jerryt.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/**
 * RSA工具类，包含加密和数字签名的功能
 *
 * @author JerryT
 */
public class RSAUtil {

    // RSA密钥长度，须在512-16384之间
    private static final int RSA_KEY_SIZE = 1024;
    // 数字签名算法
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";


    /**
     * 获取RSA公钥/私钥对
     *
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
     *
     * @param publicKey
     * @return
     */
    public static byte[] getPublicKeyBytes(PublicKey publicKey) {
        return publicKey.getEncoded();
    }

    /**
     * 把字节转换为公钥
     *
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
     *
     * @param privateKey
     * @return
     */
    public static byte[] getPrivateKeyBytes(PrivateKey privateKey) {
        return privateKey.getEncoded();
    }

    /**
     * 把字节转换为私钥
     *
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
     *
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
     *
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
     * 签名（除去换行和空格）
     *
     * @param privateKeyStr 私钥
     * @param data          签名内容
     * @return signature 签名值
     */
    public static String sign(String privateKeyStr, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        data = data.replaceAll("\n", "").replaceAll(" ", "");
        try {
            PrivateKey privateKey = RSAUtil.getPrivateKeyFromBytes(Base64.getDecoder().decode(privateKeyStr));
            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(data.getBytes());
            String signature = Base64.getEncoder().encodeToString(Sign.sign());
            return signature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验签（除去换行和空格）
     *
     * @param publicKeyStr 公钥
     * @param data         验签内容
     * @param signature    签名值
     * @return 验签结果
     */
    public static boolean verifySign(String publicKeyStr, String data, String signature) {
        data = data.replaceAll("\n", "").replaceAll(" ", "");
        try {
            PublicKey publicKey = RSAUtil.getPublicKeyFromBytes(Base64.getDecoder().decode(publicKeyStr));
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(data.getBytes());
            return verifySign.verify(Base64.getDecoder().decode(signature.substring(0, 172)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception {

        // 加密解密演示
        System.out.println("\n===========加密解密演示===========\n");
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
        System.out.println("\n===========数字签名演示===========\n");
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvPulcPnrUBap/nYlO/7pn9JGikc95Qxsa/I6+GS4+beZIQPa295Yelv2CE52emJ2cTioAVBE8Ulm6LSsJLilUVEzPtzQEF4APmrHa24aI1gAybPvJ3flXVqVJVpaX2qezVBKURMAAjLlcSWF04vGZIfs27hE6n91j0XQzjo97yQIDAQAB";
        System.out.println("公钥： " + publicKey);
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK8+6Vw+etQFqn+diU7/umf0kaKRz3lDGxr8jr4ZLj5t5khA9rb3lh6W/YITnZ6YnZxOKgBUETxSWbotKwkuKVRUTM+3NAQXgA+asdrbhojWADJs+8nd+VdWpUlWlpfap7NUEpREwACMuVxJYXTi8Zkh+zbuETqf3WPRdDOOj3vJAgMBAAECgYAso8Ph4XB8Ta0usLxnSTD8hgoK9UV6SCPBbhAWUGe9M1VzlkjCNrMgu6l71u9RlOKhDDAawU9apEeC6zqJLh8MlS4EVnFJy/2I7OOr0ZdEL63ZNJF1DRlaqF0PBBpegz6mB/3vqfcNfRPi79+W2/l/PRD66syz6QIgrxhTr0bmAQJBAPOT3PtwNjT8le3IdtN0VE/J8mh4bdj8YId1DyChaLDUx2QdgabRNJrLiV5xJkCW+8sEGZHQpuLLJzFgch6q5qECQQC4LulHt2j2PpO8+TSYyOhy645HwvloacDVwYzJVcjLOILC17oi59fP7Qa4+D1IJh7B2ImJKJUfhTjlQKTjwQwpAkB5CjZbAFT/mbELe32I8JrhF3KNdaLom+mABqygw3TZwrLezkbaVcW1UoWN195xZFX1ebEXI796ngd44vtyv+xhAkAvTpVKf1htTxthQVz6FThnNAuCcRjgcbE+9gy0Nd1yHRyw8Pn1NzleRZIhdlk/K9NglL6WxR6wTuaTM6xmd1IpAkEA3GFZOV0vQytMxugdwUyA7ZttpY1liC3dOaJhPLICjdDZO8SoCqe5bZ9Ek5lmOWqRh7TROplOHAkotG0o2zR/LQ==";
        System.out.println("私钥： " + privateKey);
        System.out.println("原文：" + msg);
        String signature = sign(privateKey, msg);
        System.out.println("签名值：" + signature);
        System.out.println("验签结果：" + verifySign(publicKey, msg, signature));
    }
}
