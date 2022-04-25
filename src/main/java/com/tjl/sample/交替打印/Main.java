package com.tjl.sample.交替打印;

public class Main {
    public static void main(String[] args) {
        MyPrint print = new MyPrint();
        Thread thread1 = new Thread(print,"线程A");
        Thread thread2 = new Thread(print,"线程B");
        thread1.start();
        thread2.start();
    }
}

class MyPrint implements Runnable{
    String string;
    int i = 1;
    @Override
    public void run() {
        while (true){
            synchronized (this){
                this.notify();//在这里唤醒的目的是为保证拿到锁的线程只有一个 不会立即释放锁 退出代码块才会释放锁
                if (i <= 100 ){
                    System.out.println(Thread.currentThread().getName()+" "+i++);
                }else {
                    break;
                }
                try {
                    this.wait();//打印过数据的线程等待 必须等到没打印过数字的拿到锁了才能唤醒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}