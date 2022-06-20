package com.tjl.sample.文件的复制;

import java.io.*;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        File source = new File("/Users/tjl/q.png");
        File dest = new File("/Users/tjl/q_aes.png");
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) != -1){
                output.write(buf, 0, bytesRead);
            }
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
