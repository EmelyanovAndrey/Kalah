package com.pesikovlike.kalah.server.websocket.game;



import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.game.session.GameSession;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Igor on 30.11.2016.
 */

/*
   {
        operation: join;
        creatorLogin: ;
        joinedLogin: ;
   }
 */
@ServerEndpoint(value = "/game/joined")
public class ForJoinedSocket {

    @Inject
    private GameBidService gameBidService;


    private GameSession gameSession;

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        String operation = json.getString("operation");
        if (operation.equals("join")) { //попытка присоединится к сессии, отправка запроса создавшему
            GameBid gameBid = gameBidService.getBid(json.getJsonString("creatorLogin").getString());
            gameBid.setSessionOfJoined(session);
            gameBid.getSessionOfCreator().getBasicRemote().sendText("{join: '" + json.getString("joinedLogin") + "'}");
        }
    }

    @OnOpen
    public void onOpen(Session session) {

    }

    @OnClose
    public void onClose(Session session) {
    }
}
