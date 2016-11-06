package com.pesikovlike.kalah.server;

/**
 * Created by Igor on 06.11.2016.
 */

import com.pesikovlike.kalah.model.dao.DAOFactory;
import com.pesikovlike.kalah.model.dao.interfaces.AvatarDAO;
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
    @Named("avatarDAO")
    private AvatarDAO avatarDAO;

    public void init(){
        DAOFactory.init(DAOFactory.DataSources.POSTGRES_HIBERNATE);

        Avatar avatar = new Avatar();
        avatar.setAvatarName("SubZero");
        avatar.setFilePath("/SubZero.jpg");
        avatarDAO.insertAvatar(avatar);
    }
}
