package com.pesikovlike.kalah.server.websocket.game;


import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Igor on 12.11.2016.
 */

/*
 * input message format
   {
        operation: create;
        creatorLogin: ;
        friendLogin: ;
        holeCount: ;
        stoneCount: ;
   }

   {
        operation: join;
        creatorLogin: ;
        joinedLogin: ;
   }

   {
        operation: confirm;
        creatorLogin: ;
        confirm: yes, no;
   }
 */
@ServerEndpoint(value = "/game/creator")
public class ForCreatorSocket {

    @Inject
    private GameBidService gameBidService;

    @EJB
    private UserService userService;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;


    private GameSession gameSession;

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        String operation = json.getString("operation");

        if (operation.equals("create")) { //создаем заявку
            if (userService.userExist(json.getString("friendLogin"))) {
                gameBidService.addBid(json.getJsonString("creatorLogin").getString(),
                        json.getJsonString("friendLogin").getString(),
                        json.getJsonNumber("holeCount").intValue(),
                        json.getJsonNumber("stoneCount").intValue(),
                        session);
                session.getBasicRemote().sendText("{result: 'success'}");
            } else {
                session.getBasicRemote().sendText("{result: 'error';" +
                                                    "type: 'friend not exist'}");
            }

        } else if (operation.equals("confirm")) { //подтверждение, что нам нравится тот красавчик, что хочет с нами поиграть
            String confirm = json.getJsonString("confirm").getString();
            GameBid gameBid = gameBidService.getBid(json.getJsonString("creatorLogin").getString());
            if (confirm.equals("no")) {
                gameBid.getSessionOfJoined().getBasicRemote().sendText("{confirm: 'no'}");//тут клиент должен отправить неудачника обратно к списку заявок
                gameBid.setSessionOfJoined(null);
            } else if (confirm.equals("yes")) {
                gameBid.getSessionOfJoined().getBasicRemote().sendText("{confirm: 'yes'}");//должна начаться игра (создать сессию)
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {

    }

    @OnClose
    public void onClose(Session session) {
    }
}