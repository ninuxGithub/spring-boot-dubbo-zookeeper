package com.example.api.thread;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author shenzm
 * @date 2019-5-14
 * @description 作用
 */
public class RWLockTest2 {


    public static void main(String[] args) {
        MyCount count = new MyCount("12341820341230", 100);
        User user = new User("tom", count);
        int num = 5;
        for (int i = 1; i <= num; i++) {
            user.getCash();
            user.setCash(i * 1000);
        }
    }
}

class User {
    private String name;
    private MyCount count;
    private ReadWriteLock lock;

    public User(String name, MyCount count) {
        this.name = name;
        this.count = count;
        this.lock = new ReentrantReadWriteLock();
    }

    public void getCash() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.readLock().lock();
                try {
                    int cash = count.getCash();
                    System.out.println(Thread.currentThread().getName() + "用户："+name+" getCash " + cash);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.readLock().unlock();
                }
            }
        }).start();
    }

    public void setCash(final int cash) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.writeLock().lock();
                try {
                    count.setCash(cash);
                    System.out.println(Thread.currentThread().getName() + "用户："+name+ " setCash " + cash);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        }).start();
    }


}

class MyCount {
    private String id;
    private int cash;

    public MyCount(String id, int cash) {
        this.id = id;
        this.cash = cash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}

