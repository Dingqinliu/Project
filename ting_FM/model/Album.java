package com.dingqinliu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class Album {
    public  Integer aid;
    public String name;
    public String cover;
    public Integer count;

    @JsonInclude(JsonInclude.Include.NON_NULL) //为null的项不输出
    public String header;
    @JsonInclude(JsonInclude.Include.NON_NULL) //为null的项不输出
    public String brief;

    @JsonInclude(JsonInclude.Include.NON_NULL) //为null的项不输出
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    public Date createAt;

    @JsonInclude(JsonInclude.Include.NON_NULL) //为null的项不输出
    public List<Story> storyList;

}
