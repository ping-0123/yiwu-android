package com.yinzhiwu.yiwu.view;

import java.sql.Time;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by ping on 2017/4/19.
 */

public class Interval {
    private int id;

    private String name;

    @NotNull
    private Time start;

    @NotNull
    private Time end;

    @Min(0)
    private float hours;

    public Interval(String name,Time start, Time end) {
        this.name = name;
        this.start = start;
        this.end = end;
        hours =(end.getTime()-start.getTime())/(1000*3600);
    }
}
