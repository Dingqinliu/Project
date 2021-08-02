package com.dingqinliu.service;

import com.dingqinliu.dao.StoryDao;
import com.dingqinliu.model.Story;

import java.sql.SQLException;

public class StoryService {
    private final StoryDao storyDao=new StoryDao();
    public Story detail(int sid) throws SQLException {
        return storyDao.selectOneUsingSid(sid);
    }

    public void getAudio(int sidInt) {
    }
}
