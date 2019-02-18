package com.example.api.pubsub;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class ServerConfig {
    private String dbUrl;
    private String dbPwd;
    private String dbUser;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbPwd() {
        return dbPwd;
    }

    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "dbUrl='" + dbUrl + '\'' +
                ", dbPwd='" + dbPwd + '\'' +
                ", dbUser='" + dbUser + '\'' +
                '}';
    }
}
