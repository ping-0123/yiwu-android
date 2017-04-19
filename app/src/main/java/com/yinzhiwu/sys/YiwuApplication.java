package com.yinzhiwu.sys;

import android.app.Application;

import com.yinzhiwu.yiwu.view.Employee;

/**
 * Created by ping on 2017/4/17.
 */

public class YiwuApplication extends Application {

    private Employee employee;

    private String baseApiUrl = "http://192.168.0.115:8080/yiwu/";



    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getBaseApiUrl() {
        return baseApiUrl;
    }

    public void setBaseApiUrl(String baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
    }
}
