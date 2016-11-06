package com.pesikovlike.kalah.server;

/**
 * Created by Igor on 06.11.2016.
 */

import com.pesikovlike.kalah.model.dao.interfaces.AvatarDAO;
import com.pesikovlike.kalah.model.dao.interfaces.DAOFactory;
import com.pesikovlike.kalah.model.dao.InitFactory;
import com.pesikovlike.kalah.model.entity.Avatar;

import javax.inject.Inject;
import javax.inject.Named;


/**Если нужно будет использовать друной источник данных и начальную инициализацию БД,
то надо наследовать от этого класса, переопределять init(), добавить перед классом @Alternative,
 и создать новый beans.xml, добавив в него:
 <alternatives>
    <class>%путь к созданному классу%</class>
 </alternatives>*/
public class PostgresHibernateStartup {

    @Inject
    @Named("daoFactory")
    private DAOFactory daoFactory;

    public void init(){
        InitFactory.init(InitFactory.DataSources.POSTGRES_HIBERNATE);

        AvatarDAO avatarDAO = daoFactory.getAvatarDAO();
        Avatar avatar = new Avatar(0, "Sam", "/path");
        avatarDAO.insertAvatar(avatar);

    }
}
