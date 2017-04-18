package com.yinzhiwu.yiwu.view;

/**
 * Created by ping on 2017/4/17.
 */

public class Account {

    private String account;

    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account(String account, String password) {

        this.account = account;
        this.password = password;
    }
}
