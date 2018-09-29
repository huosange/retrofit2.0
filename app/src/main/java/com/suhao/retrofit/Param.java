package com.suhao.retrofit;

/**
 * post提交的对象
 */
public class Param {
    private String username;
    private String password;

    public Param(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
