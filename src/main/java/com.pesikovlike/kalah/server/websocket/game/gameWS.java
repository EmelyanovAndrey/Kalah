package com.pesikovlike.kalah.server.websocket.game;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.game.session.GameSessionFactory;
import com.pesikovlike.kalah.game.session.GameSessionService;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;
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
        operation: conf;
        creatorLogin: ;
        conf: yes, no;
   }
 */
@ServerEndpoint(value = "/game")
public class gameWS {

    @Inject
    private GameBidService gameBidService;
    @Inject
    private GameSessionService gameSessionService;
    @Inject
    private GameBidFactory gameBidFactory;
    @Inject
    private GameSessionFactory gameSessionFactory;

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

        }
        if (operation.equals("conf")) { //подтверждение, что нам нравится тот красавчик, что хочет с нами поиграть
            GameBid gameBid = gameBidService.getBid(mesg.get("login").getAsString());
            String conf = mesg.get("conf").getAsString();
            LOGGER.log(Level.SEVERE, "Start confirm");
            if (conf.equals("no")) {
                LOGGER.log(Level.SEVERE, "Start no");
                //тут клиент должен отправить неудачника обратно к списку заявок

                gameBid.setBlock(false);
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("operation", "conf");
                resultMap.put("conf", "no");
                resultMap.put("role", "joined");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(resultMap);
                gameBid.getSessionOfJoined().getBasicRemote().sendText(json);
                gameBid.setSessionOfJoined(null);
            } else if (conf.equals("yes")) {
                LOGGER.log(Level.SEVERE, "Start yes");
                //должна начаться игра (создать сессию)
                gameSession = gameSessionService.addGameSession(gameBid);
                gameBidService.deleteBid(gameBid.getCreatorLogin());

                Map<String, String> resultMap;
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json;

                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "conf");
                resultMap.put("conf", "yes");
                resultMap.put("role", "joined");
                if (gameSession.getGameState().getPriority() == false) {
                    resultMap.put("priority", "true");
                } else {
                    resultMap.put("priority", "false");
                }

                json = gson.toJson(resultMap);
                LOGGER.log(Level.SEVERE, "conf, to joined: " + json);
                gameSession.getSessionOfJoined().getBasicRemote().sendText(json);

                resultMap = new HashMap<String, String>();

                resultMap.put("operation", "conf");
                resultMap.put("role", "creator");
                if (gameSession.getGameState().getPriority() == true) {
                    resultMap.put("priority", "true");
                } else {
                    resultMap.put("priority", "false");
                }
                json = gson.toJson(resultMap);
                LOGGER.log(Level.SEVERE, "conf, to creator: " + json);
                gameSession.getSessionOfCreator().getBasicRemote().sendText(json);
            }
        }


        //для присоединившегося

        if (operation.equals("join")) { //попытка присоединится к сессии, отправка запроса создавшему

            GameBid gameBid = gameBidService.getBid(mesg.get("creatorLogin").getAsString());
            gameBid.setSessionOfJoined(session);

            Map<String, String> resultMap;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json;


            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "join");
            resultMap.put("joinedLogin", mesg.get("login").getAsString());
            resultMap.put("role", "creator");
            json = gson.toJson(resultMap);
            LOGGER.log(Level.SEVERE, "join, to creator: " + json);
            gameBid.getSessionOfCreator().getBasicRemote().sendText(json);

            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "join");
            resultMap.put("role", "joined");
            json = gson.toJson(resultMap);
            LOGGER.log(Level.SEVERE, "join, to joined: " + json);
            session.getBasicRemote().sendText(json);

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