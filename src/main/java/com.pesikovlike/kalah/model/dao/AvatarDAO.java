package com.pesikovlike.kalah.model.dao;

import com.pesikovlike.kalah.model.entity.Avatar;

import javax.ejb.Local;

/**
 * Created by Igor on 31.10.2016.
 */
public interface AvatarDAO {
    public void insertAvatar(Avatar avatar);

    public void updateAvatar(Avatar avatar);

    public void deleteAvatar(Avatar avatar);

    public Avatar getAvatarById(long id);
}
