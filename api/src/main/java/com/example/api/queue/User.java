package com.example.api.queue;

import java.io.Serializable;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用
 */
public class User implements Serializable {

    private String name;

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
