package com.pesikovlike.kalah.server.websocket.game;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor on 12.11.2016.
 */

/*
 * input message format


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
@ServerEndpoint(value = "/game")
public class gameWS {

    @Inject
    private GameBidService gameBidService;

    @EJB
    private UserService userService;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    private GameSession gameSession;

    private static final Logger LOGGER = Logger.getLogger("gameWS Servlet");

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {

        JsonParser parser = new JsonParser();
        com.google.gson.JsonObject mesg = parser.parse(message).getAsJsonObject();
        String operation = mesg.get("operation").getAsString();
        LOGGER.log(Level.SEVERE, "Message: " + message);
        //для создавшего
        if (operation.equals("create")) { //создаем заявку (на самом деле она уже создана, но мы добавляем в нее сессию вебсокета)
            GameBid gameBid = gameBidService.getBid(mesg.get("login").getAsString());
            gameBid.setSessionOfCreator(session);
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("operation", "create");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            session.getBasicRemote().sendText(json);

        } else if (operation.equals("confirm")) { //подтверждение, что нам нравится тот красавчик, что хочет с нами поиграть
            String confirm = mesg.get("confirm").getAsString();
            GameBid gameBid = gameBidService.getBid(mesg.get("creatorLogin").getAsString());
            if (confirm.equals("no")) {
                gameBid.getSessionOfJoined().getBasicRemote().sendText("{confirm: 'no'}");//тут клиент должен отправить неудачника обратно к списку заявок
                gameBid.setSessionOfJoined(null);
            } else if (confirm.equals("yes")) {
                gameBid.getSessionOfJoined().getBasicRemote().sendText("{confirm: 'yes'}");//должна начаться игра (создать сессию)
            }
        } else


            //для присоединившегося

            if (operation.equals("join")) { //попытка присоединится к сессии, отправка запроса создавшему
                GameBid gameBid = gameBidService.getBid(mesg.get("creatorLogin").getAsString());
                gameBid.setSessionOfJoined(session);
                gameBid.getSessionOfCreator().getBasicRemote().sendText("{join: '" + mesg.get("joinedLogin").getAsString() + "'}");
            }
    }

    //
    @OnOpen
    public void onOpen(Session session) {

    }

    @OnClose
    public void onClose(Session session) {
    }
}