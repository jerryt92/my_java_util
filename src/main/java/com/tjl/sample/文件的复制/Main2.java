package com.tjl.sample.文件的复制;

import java.io.*;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "/Users/tjl/Desktop/File/";
        String newPath = "/Users/tjl/Desktop/Copy/";
        String fileName;
        String newFileName;
        System.out.println("欢迎使用文件复制程序");
        System.out.println("请输入源文件路径：\n例：\"/Users/UserName/Desktop/File/\"（macOS/Linux）\n\"C:\\Users\\UserName\\Desktop\"（Windows）");
        filePath = scanner.next();
        System.out.println("请输入文件名（含后缀）\n例：\"File.dat\"");
        fileName = scanner.next();
        newFileName = fileName;
        try {
            File file = new File(filePath + fileName);
//            byte[] bytes = file.readAllBytes();
            System.out.println("请输入文件存放路径：\n例：\"/Users/UserName/Desktop/Copy/\"（macOS/Linux）\n\"C:\\Users\\UserName\\Desktop\"（Windows）");
            newPath = scanner.next();
            if (newPath.equals(filePath)) newFileName = "(2)" + newFileName;
            File copyDir = new File(newPath);
            copyDir.mkdir();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(newPath + newFileName)));
//            dos.write(bytes);
            dos.close();
            System.out.println("操作完成！");
        } catch (FileNotFoundException e) {
            System.out.println("没有找到这个文件,或目录不存在！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.out.println("文件过大！");
        }
    }
}
