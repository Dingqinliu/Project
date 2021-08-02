package com.dingqinliu.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Story {
    public Integer sid;
    public String name;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    public Date createdAt;
    public Integer count;

}
