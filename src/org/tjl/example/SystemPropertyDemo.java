package org.tjl.example;

public class SystemPropertyDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("Java的运行环境版本：" + System.getProperty("java.version"));
        System.out.println("Java的运行环境供应商：" + System.getProperty("java.vendor"));
        System.out.println("Java供应商的URL：" + System.getProperty("java.vendor.url"));
        System.out.println("Java的安装路径：" + System.getProperty("java.home"));
        System.out.println("Java的虚拟机规范版本：" + System.getProperty("java.vm.specification.version"));
        System.out.println("Java的虚拟机规范供应商：" + System.getProperty("java.vm.specification.vendor"));
        System.out.println("Java的虚拟机规范名称：" + System.getProperty("java.vm.specification.name"));
        System.out.println("Java的虚拟机实现版本：" + System.getProperty("java.vm.version"));
        System.out.println("Java的虚拟机实现供应商：" + System.getProperty("java.vm.vendor"));
        System.out.println("Java的虚拟机实现名称：" + System.getProperty("java.vm.name"));
        System.out.println("Java运行时环境规范版本：" + System.getProperty("java.specification.version"));
        System.out.println("Java运行时环境规范供应商：" + System.getProperty("java.specification.vender"));
        System.out.println("Java运行时环境规范名称：" + System.getProperty("java.specification.name"));
        System.out.println("Java的类格式版本号：" + System.getProperty("java.class.version"));
        System.out.println("Java的类路径：" + System.getProperty("java.class.path"));
        System.out.println("加载库时搜索的路径列表：" + System.getProperty("java.library.path"));
        System.out.println("默认的临时文件路径：" + System.getProperty("java.io.tmpdir"));
        System.out.println("一个或多个扩展目录的路径：" + System.getProperty("java.ext.dirs"));
        System.out.println("操作系统的名称：" + System.getProperty("os.name"));
        System.out.println("操作系统的构架：" + System.getProperty("os.arch"));
        System.out.println("操作系统的版本：" + System.getProperty("os.version"));
        System.out.println("文件分隔符：" + System.getProperty("file.separator"));   // 在 unix 系统中是＂／＂
        System.out.println("路径分隔符：" + System.getProperty("path.separator"));   // 在 unix 系统中是＂:＂
        System.out.println("行分隔符：" + System.getProperty("line.separator"));   // 在 unix 系统中是＂/n＂
        System.out.println("用户的账户名称：" + System.getProperty("user.name"));
        System.out.println("用户的主目录：" + System.getProperty("user.home"));
        System.out.println("用户的当前工作目录：" + System.getProperty("user.dir"));
    }

}
