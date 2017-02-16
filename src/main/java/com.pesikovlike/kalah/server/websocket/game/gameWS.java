package com.pesikovlike.kalah.server.websocket.game;


import com.google.gson.*;
import com.pesikovlike.kalah.ai.AI;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.game.session.GameSessionFactory;
import com.pesikovlike.kalah.game.session.GameSessionService;
import com.pesikovlike.kalah.model.dao.AvatarDAO;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.Avatar;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.User;
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

    @Inject
    @Named("avatarDAO")
    private AvatarDAO avatarDAO;

    private GameSession gameSession;
    private String creatorLogin;
    private GameBid gameBid;
    private AI ai;

    private static final Logger LOGGER = Logger.getLogger("gameWS Servlet");

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {

        JsonParser parser = new JsonParser();
        com.google.gson.JsonObject mesg = parser.parse(message).getAsJsonObject();
        String operation = mesg.get("operation").getAsString();
        LOGGER.log(Level.SEVERE, "Message: " + message);

        if (operation.equals("createAI")) {
            String level = mesg.get("level").getAsString();
            String holeCount = mesg.get("holeCount").getAsString();
            String stoneCount = mesg.get("stoneCount").getAsString();
            boolean isFirst = true;
            ai = new AI(Integer.parseInt(holeCount), Integer.parseInt(stoneCount), Integer.parseInt(level), isFirst);

            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("operation", "createAI");
            Avatar avatar = avatarDAO.getAvatarById(100);
            Avatar avatarAI = avatarDAO.getAvatarById(0);
            resultMap.put("avatar", avatar.getFilePath());
            resultMap.put("avatarAI", avatarAI.getFilePath());
            resultMap.put("holeCount", holeCount);
            resultMap.put("stoneCount", stoneCount);
            resultMap.put("level", level);
            resultMap.put("prior", String.valueOf(isFirst));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            session.getBasicRemote().sendText(json);

        }

        //для создавшего
        if (operation.equals("create")) { //создаем заявку (на самом деле она уже создана, но мы добавляем в нее сессию вебсокета)
            creatorLogin = mesg.get("login").getAsString();
            gameBid = gameBidService.getBid(creatorLogin);
            gameBid.setSessionOfCreator(session);
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("operation", "create");
            User user = userDAO.getUserByLogin(creatorLogin);
            Avatar avatar = avatarDAO.getAvatarById(user.getAvatar().getAvatarId());
            resultMap.put("avatar", avatar.getFilePath());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            session.getBasicRemote().sendText(json);

        }
        if (operation.equals("conf")) { //подтверждение, что нам нравится тот красавчик, что хочет с нами поиграть

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
                if (gameBid.getSessionOfJoined() != null) {
                    gameSession = gameSessionService.addGameSession(gameBid);
                    gameBidService.deleteBid(gameBid.getCreatorLogin());
                    LOGGER.log(Level.SEVERE, "Start yes");
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
        }


        if (operation.equals("join")) { //попытка присоединится к сессии, отправка запроса создавшему
            creatorLogin = mesg.get("creatorLogin").getAsString();
            gameBid = gameBidService.getBid(creatorLogin);
            gameBid.setSessionOfJoined(session);

            Map<String, String> resultMap;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json;


            User user = userDAO.getUserByLogin(mesg.get("login").getAsString());
            Avatar avatar = avatarDAO.getAvatarById(user.getAvatar().getAvatarId());

            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "join");
            resultMap.put("joinedLogin", mesg.get("login").getAsString());
            resultMap.put("role", "creator");
            resultMap.put("enemyAvatar", avatar.getFilePath());
            json = gson.toJson(resultMap);
            LOGGER.log(Level.SEVERE, "join, to creator: " + json);
            gameBid.getSessionOfCreator().getBasicRemote().sendText(json);

            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "join");
            resultMap.put("role", "joined");
            resultMap.put("yourAvatar", avatar.getFilePath());

            user = userDAO.getUserByLogin(creatorLogin);
            avatar = avatarDAO.getAvatarById(user.getAvatar().getAvatarId());
            resultMap.put("enemyLogin", creatorLogin);
            resultMap.put("enemyAvatar", avatar.getFilePath());
            json = gson.toJson(resultMap);
            LOGGER.log(Level.SEVERE, "join, to joined: " + json);
            session.getBasicRemote().sendText(json);

        }

        if (operation.equals("getBoard")) {

            Map<String, String> resultMap;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json;


            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "getBoard");
            resultMap.put("holeCount", String.valueOf(gameBid.getHoleCount()));
            resultMap.put("stoneCount", String.valueOf(gameBid.getStoneCount()));

            json = gson.toJson(resultMap);
            LOGGER.log(Level.SEVERE, "getBoard: " + json);
            session.getBasicRemote().sendText(json);
        }


        if (operation.equals("step")) {


            GameSession gameSession = gameSessionService.getGameSession(creatorLogin);
            Map<String, String> resultMap;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json;

            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "step");
            resultMap.put("num", mesg.get("num").getAsString());

            json = gson.toJson(resultMap);
            LOGGER.log(Level.SEVERE, "step: " + json);
            if (mesg.get("role").getAsString().equals("creator")) {

                gameSession.getSessionOfJoined().getBasicRemote().sendText(json);
            } else {

                gameSession.getSessionOfCreator().getBasicRemote().sendText(json);
            }

        }

        if (operation.equals("stepAI")) {
            LOGGER.log(Level.SEVERE, "Start step AI");

            Map<String, String> resultMap;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json;
            int num = mesg.get("num").getAsInt();
            ai.playerStep(num);
            int respNum = ai.AIStep();
            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "stepAI");
            resultMap.put("num", String.valueOf(respNum));

            json = gson.toJson(resultMap);

            session.getBasicRemote().sendText(json);
        }
    }

    //
    @OnOpen
    public void onOpen(Session session) {

    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException, EncodeException {
        LOGGER.log(Level.SEVERE, "close: " + reason.getCloseCode());
        Map<String, String> resultMap;
        String json;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (gameBid.getSessionOfCreator().equals(session)) {
            if (gameBid.getSessionOfJoined() != null) {
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "creator closed");

                json = gson.toJson(resultMap);
                gameBid.getSessionOfJoined().getBasicRemote().sendText(json);
            }
            gameBidService.deleteBid(creatorLogin);
        } else if (gameBid.getSessionOfJoined().equals(session)) {
            gameBid.setSessionOfJoined(null);
            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "joined closed");

            json = gson.toJson(resultMap);
            gameBid.getSessionOfCreator().getBasicRemote().sendText(json);
            gameBid.setBlock(false);
        } else {
            if (gameSession.getSessionOfCreator().equals(session)) {
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "creator closed in game");

                json = gson.toJson(resultMap);
                gameBid.getSessionOfJoined().getBasicRemote().sendText(json);
            } else if (gameSession.getSessionOfJoined().equals(session)) {
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "joined closed in game");

                json = gson.toJson(resultMap);
                gameBid.getSessionOfJoined().getBasicRemote().sendText(json);
            }
            //TODO тут надо сохранить игру
            gameSessionService.deleteGameSession(creatorLogin);
        }
    }
}