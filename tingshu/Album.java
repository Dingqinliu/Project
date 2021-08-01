package com.dingqinliu.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Album {
    public  Integer aid;
    public String name;
    public String cover;
    public Integer count;

    public String header;
    public String brief;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    public Date createAt;

    public List<Story> storyList;

}
