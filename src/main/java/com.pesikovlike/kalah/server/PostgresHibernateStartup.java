package com.pesikovlike.kalah.server;

/**
 * Created by Igor on 06.11.2016.
 */

import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.session.GameSessionFactory;
import com.pesikovlike.kalah.model.dao.DAOFactory;


/**Если нужно будет использовать друной источник данных и начальную инициализацию БД,
то надо наследовать от этого класса, переопределять init(), добавить перед классом @Alternative,
 и создать новый beans.xml, добавив в него:
 <alternatives>
    <class>%путь к созданному классу%</class>
 </alternatives>*/
public class PostgresHibernateStartup {

    public void init(){
        DAOFactory.init(DAOFactory.DataSources.POSTGRES_HIBERNATE);
        GameSessionFactory.init(GameSessionFactory.GameSessionImpl.IMPL);
        GameBidFactory.init(GameBidFactory.GameBidImpl.IMPL);
    }
}
