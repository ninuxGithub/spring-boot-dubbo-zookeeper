package com.example.api.zookeeper.selectmaster;

import java.io.Serializable;

/**
 * @author shenzm
 * @date 2019-2-14
 * @description 作用
 */
public class RunningData implements Serializable {
    private Long cid;

    private String name;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{'cid':"+cid+", 'name':"+name+"}";
    }
}
