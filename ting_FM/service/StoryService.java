package com.dingqinliu.service;

import com.dingqinliu.dao.StoryDao;
import com.dingqinliu.model.Story;

import java.io.InputStream;
import java.sql.SQLException;

public class StoryService {
    private final StoryDao storyDao=new StoryDao();
    public Story detail(int sid) throws SQLException {
        return storyDao.selectOneUsingSid(sid);
    }

    public InputStream getAudio(int sid) throws SQLException{
        return storyDao.selectOneAudioColumnUsingSid(sid);
    }
}

