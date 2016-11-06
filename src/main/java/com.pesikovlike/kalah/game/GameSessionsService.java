package com.pesikovlike.kalah.game;

import javax.ejb.Singleton;
import javax.websocket.Session;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Igor on 07.11.2016.
 */
@Singleton
public class GameSessionsService {

    private final Set<Session> gameSessions = new HashSet<Session>();

}
