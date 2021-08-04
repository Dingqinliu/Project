package com.dingqinliu.service;

import com.dingqinliu.dao.AlbumDao;
import com.dingqinliu.dao.StoryDao;
import com.dingqinliu.model.Album;

import java.sql.SQLException;
import java.util.List;

public class AlbumService {
    //首页 最新的专辑列表

    private final AlbumDao albumDao=new AlbumDao();
    private final StoryDao storyDao=new StoryDao();

    //最近的列表
    public List<Album> latestList(String keyword) throws SQLException{
        if (keyword==null){
            return albumDao.selectListDefault();
        }else {
            return albumDao.selectListLikeName(keyword);
        }
    }

    //列表详情
    public Album detail(int aid) throws SQLException{
        Album album = albumDao.selectOneUsingSid(aid);
        if (album==null){
            return null;
        }

        album.storyList=storyDao.selectListUsingAid(aid);

        return album;
    }

    public int save(Integer uid, String name, String brief, String cover, String header) throws SQLException {
        return albumDao.insert(uid,name,brief,cover,header);
    }
}
