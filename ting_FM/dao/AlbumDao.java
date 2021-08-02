package com.dingqinliu.dao;

import com.dingqinliu.model.Album;
import com.dingqinliu.model.Story;
import com.dingqinliu.util.DButil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDao {
    public Album selectOneUsingSid(int aid) throws SQLException{
        try(Connection c=DButil.getConnection()){
            String sql="SELECT aid, name, cover, header, brief, created_at, count FROM album WHERE aid=?";
            try (PreparedStatement s=c.prepareStatement(sql)){
                s.setInt(1,aid);
                try(ResultSet rs=s.executeQuery()){
                    if (!rs.next()){
                       return null;
                    }

                    Album album = new Album();
                    album.aid=aid;
                    album.name=rs.getString("name");
                    album.cover=rs.getString("cover");
                    album.header=rs.getString("header");
                    album.brief=rs.getString("brief");
                    album.createAt=rs.getDate("created_at");
                    album.count=rs.getInt("count");
//                    album.storyList=new ArrayList<>();
                    return album;
                }
            }
        }
    }

    //查专辑
    public List<Album> selectListDefault() throws SQLException{
        List<Album> albumList=new ArrayList<>();

        try(Connection c=DButil.getConnection()){
            String sql="SELECT aid, name, cover, count FROM album ORDER BY aid DESC LIMIT 20";
            try(PreparedStatement s=c.prepareStatement(sql)){
                try(ResultSet rs=s.executeQuery()){
                    while (rs.next()){
                        Album album=new Album();

                        album.aid=rs.getInt("aid");
                        album.name=rs.getString("name");
                        album.cover=rs.getString("cover");
                        album.count=rs.getInt("count");

                        albumList.add(album);
                    }
                }
            }
        }
        return albumList;
    }
    public List<Album> selectListLikeName(String likeName) throws SQLException{
        List<Album> albumList=new ArrayList<>();

        try(Connection c=DButil.getConnection()){
            String sql="SELECT aid, name, cover, count FROM album WHERE name LIKE ? ORDER BY aid DESC LIMIT 20";
            try(PreparedStatement s=c.prepareStatement(sql)){
                s.setString(1,"%"+likeName+"%");
                try(ResultSet rs=s.executeQuery()){
                    while (rs.next()){
                        Album album=new Album();

                        album.aid=rs.getInt("aid");
                        album.name=rs.getString("name");
                        album.cover=rs.getString("cover");
                        album.count=rs.getInt("count");

                        albumList.add(album);
                    }
                }
            }
        }
        return albumList;
    }
}
