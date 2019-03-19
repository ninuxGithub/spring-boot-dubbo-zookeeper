package com.example.consumer.bean;

import java.io.Serializable;

/**
 * @author shenzm
 * @date 2019-3-18
 * @description 作用
 */
public class Message implements Serializable {
    private String msg;

    public Message(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
