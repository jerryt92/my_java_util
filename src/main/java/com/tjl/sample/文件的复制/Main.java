package com.tjl.sample.文件的复制;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        File source = new File("/Users/tjl/Desktop/DISK1000.app");
        File dest = new File("/Users/tjl/Desktop/uploadFileBuffer/DISK1000.app");
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
