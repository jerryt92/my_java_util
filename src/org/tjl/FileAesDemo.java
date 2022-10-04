package org.tjl;

import org.apache.commons.io.FileUtils;
import org.tjl.util.AESUtil;
import org.tjl.util.MDUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileAesDemo {
    public static void main(String[] args) throws Exception {
        File encryptedFile = new File("/Users/tjl/Downloads/UML.png.aes.bin");
        byte[] bytes = AESUtil.aesDecrypt(FileUtils.readFileToByteArray(encryptedFile), MDUtil.getMessageDigest("123".getBytes(StandardCharsets.UTF_8), "MD5").getBytes(StandardCharsets.UTF_8));
        File file = new File("/Users/tjl/Downloads/UML.png");
        FileUtils.writeByteArrayToFile(file,bytes);
    }

}
