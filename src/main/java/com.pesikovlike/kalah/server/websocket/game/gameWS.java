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
import com.pesikovlike.kalah.model.dao.GameStateDAO;
import com.pesikovlike.kalah.model.dao.HoleDAO;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.Avatar;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;
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
import java.util.*;
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
    @Named("gameStateDAO")
    private GameStateDAO gameStateDAO;
    @Inject
    @Named("holeDAO")
    private HoleDAO holeDAO;
    @Inject
    @Named("avatarDAO")
    private AvatarDAO avatarDAO;

    private GameSession gameSession;
    private String creatorLogin;
    private String login;
    private GameBid gameBid;
    private AI ai;
    private boolean fromLoad = false;
    private String loadRole = "";

    private static final Logger LOGGER = Logger.getLogger("gameWS Servlet");

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        LOGGER.log(Level.SEVERE, "Message: " + message);
        JsonParser parser = new JsonParser();
        com.google.gson.JsonObject mesg = parser.parse(message).getAsJsonObject();
        String operation = mesg.get("operation").getAsString();

        if (operation.equals("save")) {

            boolean saved = false;

            for (int i = 0; i < gameStateDAO.getGameStatesByUser(userDAO.getUserByLogin(gameSession.getGameState().getUser1().getLogin())).size(); i++) {
                if (gameStateDAO.getGameStatesByUser(userDAO.getUserByLogin(gameSession.getGameState().getUser1().getLogin())).get(i).getGameStateId() == gameSession.getGameState().getGameStateId()) {
                    saved = true;
                    break;
                }
            }
            if (saved) {
                gameSession.getGameState().setLastSaveDate(new Date());
                gameStateDAO.updateGameState(gameSession.getGameState());
                Hole[] holes = gameSession.getHolesArray();
                for (int i = 0; i < holes.length; i++) {
                    holeDAO.updateHole(holes[i]);
                }
            } else {
                gameSession.getGameState().setLastSaveDate(new Date());
                gameStateDAO.insertGameState(gameSession.getGameState());
                Hole[] holes = gameSession.getHolesArray();
                for (int i = 0; i < holes.length; i++) {
                    holeDAO.insertHole(holes[i]);
                }
            }

            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("result", "success");
            resultMap.put("operation", "save");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            session.getBasicRemote().sendText(json);
        }

        if (operation.equals("load")) {
            fromLoad = true;
            LOGGER.log(Level.SEVERE, "Start load");
            login = mesg.get("login").getAsString();
            creatorLogin = mesg.get("creatorLogin").getAsString();
            long id = mesg.get("id").getAsLong();
            LOGGER.log(Level.SEVERE, "Id: " + id);
            GameSession ses = gameSessionService.getGameSessionForLoad(id);
            if (ses != null && ses.getSessionOfCreator() == null) {
                gameSessionService.deleteGameSession(creatorLogin);
            }

            if (ses == null) {
                loadRole = "creator";
                LOGGER.log(Level.SEVERE, "ses == null");
                List<GameState> games = userService.getSavedGamesForUser(login);
                Iterator<GameState> iter = games.iterator();
                GameState state = null;
                while (iter.hasNext()) {
                    state = iter.next();
                    if (state.getGameStateId() == id) {
                        break;
                    }
                }

                gameSession = gameSessionService.addGameSession(login, state, session, null);
                LOGGER.log(Level.SEVERE, "After add");

                Map<String, String> resultMap;
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json;
                String role = mesg.get("role").getAsString();
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "load");
                resultMap.put("suboperation", "create");
                resultMap.put("role", role);
                int numHoles = gameSession.getGameState().getInitialHoleCount();
                resultMap.put("holeCount", String.valueOf(numHoles));
                resultMap.put("stoneCount", String.valueOf(gameSession.getGameState().getInitialStoneCount()));
                Hole[] holes = gameSession.getHolesArray();

                for (int i = 0; i < holes.length; i++) {
                    resultMap.put(String.valueOf(holes[i].getNumber()), String.valueOf(holes[i].getStoneCount()));
                }
                if (creatorLogin.equals(login)) {
                    if (gameSession.getGameState().getPriority()) {
                        resultMap.put("priority", "true");
                    } else {
                        resultMap.put("priority", "false");
                    }
                } else {
                    if (gameSession.getGameState().getPriority()) {
                        resultMap.put("priority", "false");
                    } else {
                        resultMap.put("priority", "true");
                    }
                }

                User user = userDAO.getUserByLogin(mesg.get("login").getAsString());
                Avatar avatar = avatarDAO.getAvatarById(user.getAvatar().getAvatarId());
                resultMap.put("yourAvatar", avatar.getFilePath());
                resultMap.put("login", login);

                if (creatorLogin.equals(login)) {
                    user = userDAO.getUserByLogin(gameSession.getGameState().getUser2().getLogin());
                } else {
                    user = userDAO.getUserByLogin(gameSession.getGameState().getUser1().getLogin());
                }
                avatar = avatarDAO.getAvatarById(user.getAvatar().getAvatarId());
                resultMap.put("enemyLogin", user.getLogin());
                resultMap.put("enemyAvatar", avatar.getFilePath());

                json = gson.toJson(resultMap);
                LOGGER.log(Level.SEVERE, "response: " + json);
                session.getBasicRemote().sendText(json);

                creatorLogin = login;
            } else {
                loadRole = "joined";
                LOGGER.log(Level.SEVERE, "ses != null");
                List<GameState> games = userService.getSavedGamesForUser(login);
                Iterator<GameState> iter = games.iterator();
                GameState state = null;
                while (iter.hasNext()) {
                    state = iter.next();
                    if (state.getGameStateId() == id) {
                        break;
                    }
                }
                if (state.getUser1().getLogin().equals(login)) {
                    gameSession = gameSessionService.getGameSession(state.getUser2().getLogin());
                } else {
                    gameSession = gameSessionService.getGameSession(state.getUser1().getLogin());
                }
                gameSession.setSessionOfJoined(session);

                LOGGER.log(Level.SEVERE, "After add");

                Map<String, String> resultMap;
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json;
                String role = mesg.get("role").getAsString();
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "load");
                resultMap.put("suboperation", "join");
                resultMap.put("role", role);

                int numHoles = gameSession.getGameState().getInitialHoleCount();
                resultMap.put("holeCount", String.valueOf(numHoles));
                resultMap.put("stoneCount", String.valueOf(gameSession.getGameState().getInitialStoneCount()));
                Hole[] holes = gameSession.getHolesArray();

                for (int i = 0; i < holes.length; i++) {
                    resultMap.put(String.valueOf(holes[i].getNumber()), String.valueOf(holes[i].getStoneCount()));
                }
                String priority = "";

                if (creatorLogin.equals(login)) {
                    if (gameSession.getGameState().getPriority()) {
                        priority = "true";
                    } else {
                        priority = "false";
                    }
                } else {
                    if (gameSession.getGameState().getPriority()) {
                        priority = "false";
                    } else {
                        priority = "true";
                    }
                }

                resultMap.put("priority", priority);
                User user = userDAO.getUserByLogin(login);
                Avatar avatar = avatarDAO.getAvatarById(user.getAvatar().getAvatarId());
                resultMap.put("yourAvatar", avatar.getFilePath());
                resultMap.put("login", login);

                if (creatorLogin.equals(login)) {
                    user = userDAO.getUserByLogin(gameSession.getGameState().getUser2().getLogin());

                } else {
                    user = userDAO.getUserByLogin(gameSession.getGameState().getUser1().getLogin());
                }
                creatorLogin = user.getLogin();
                avatar = avatarDAO.getAvatarById(user.getAvatar().getAvatarId());
                resultMap.put("enemyLogin", user.getLogin());
                resultMap.put("enemyAvatar", avatar.getFilePath());

                json = gson.toJson(resultMap);
                LOGGER.log(Level.SEVERE, "response: " + json);
                session.getBasicRemote().sendText(json);

                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "continue");
                if (priority.equals("true")) {
                    resultMap.put("priority", "false");
                } else {
                    resultMap.put("priority", "true");
                }
                json = gson.toJson(resultMap);
                LOGGER.log(Level.SEVERE, "response: " + json);
                gameSession.getSessionOfCreator().getBasicRemote().sendText(json);

            }
            return;
        }

        if (operation.equals("createAI")) {
            String level = mesg.get("level").getAsString();
            String holeCount = mesg.get("holeCount").getAsString();
            String stoneCount = mesg.get("stoneCount").getAsString();
            if (mesg.get("prior").getAsString().equals("true"))
                ai = new AI(Integer.parseInt(holeCount), Integer.parseInt(stoneCount), Integer.parseInt(level), true);
            else
                ai = new AI(Integer.parseInt(holeCount), Integer.parseInt(stoneCount), Integer.parseInt(level), false);
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("operation", "createAI");
            Avatar avatar = avatarDAO.getAvatarById(100);
            Avatar avatarAI = avatarDAO.getAvatarById(0);
            resultMap.put("avatar", avatar.getFilePath());
            resultMap.put("avatarAI", avatarAI.getFilePath());
            resultMap.put("holeCount", holeCount);
            resultMap.put("stoneCount", stoneCount);
            resultMap.put("level", level);
            resultMap.put("prior", mesg.get("prior").getAsString());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            session.getBasicRemote().sendText(json);

        }

        if (operation.equals("gs")) {
            gameSession = gameSessionService.getGameSession(creatorLogin);
            gameBid = null;
            LOGGER.log(Level.SEVERE, "gsssssssss: " + String.valueOf(gameSession == null));
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
                    String joinedLogin = mesg.get("joinedLogin").getAsString();
                    gameSession = gameSessionService.addGameSession(gameBid);
                    gameSession.getGameState().setUser2(userDAO.getUserByLogin(joinedLogin));
                    gameBidService.deleteBid(gameBid.getCreatorLogin());
                    gameBid = null;
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

            Map<String, String> resultMap;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json;

            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "step");
            resultMap.put("num", mesg.get("num").getAsString());

            gameSessionService.makeStep(creatorLogin, mesg.get("num").getAsInt());
            GameSession ses = gameSessionService.getGameSession(creatorLogin);
            LOGGER.log(Level.SEVERE, ses.getInfo());
            json = gson.toJson(resultMap);
            if (!fromLoad) {
                LOGGER.log(Level.SEVERE, "step: " + mesg.get("role"));
                if (mesg.get("role").getAsString().equals("creator")) {
                    LOGGER.log(Level.SEVERE, "creator");
                    gameSession.getSessionOfJoined().getBasicRemote().sendText(json);
                } else {
                    LOGGER.log(Level.SEVERE, "joined");
                    gameSession.getSessionOfCreator().getBasicRemote().sendText(json);
                }
            } else {
                LOGGER.log(Level.SEVERE, "from load");
                if (loadRole.equals("creator")) {
                    LOGGER.log(Level.SEVERE, "creator");
                    gameSession.getSessionOfJoined().getBasicRemote().sendText(json);
                } else {
                    LOGGER.log(Level.SEVERE, "joined");
                    gameSession.getSessionOfCreator().getBasicRemote().sendText(json);
                }
            }
        }

        if (operation.equals("stepAI")) {
            LOGGER.log(Level.SEVERE, "Start step AI");

            Map<String, String> resultMap;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json;
            resultMap = new HashMap<String, String>();
            int num = mesg.get("num").getAsInt();
            int respNum = -1;
            if (num == -1) {
                resultMap.put("first", "true");
                respNum = ai.AIStep();
            } else {
                ai.playerStep(num);
                respNum = ai.AIStep();
            }

            resultMap.put("operation", "stepAI");
            resultMap.put("num", String.valueOf(respNum));

            json = gson.toJson(resultMap);

            session.getBasicRemote().sendText(json);
        }

        if (operation.equals("end")) {
            Hole[] holes = gameSession.getHolesArray();
            for (int i = 0; i < gameSession.getGameState().getHoles().size(); i++) {
                holeDAO.deleteHole(holes[i]);
            }
            gameStateDAO.deleteGameState(gameSession.getGameState());
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

        if (gameBid != null && gameBid.getSessionOfCreator().equals(session)) {
            if (gameBid.getSessionOfJoined() != null) {
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "creator closed");

                json = gson.toJson(resultMap);
                gameBid.getSessionOfJoined().getBasicRemote().sendText(json);
                gameBid = null;
            }
            gameBidService.deleteBid(creatorLogin);
        } else if (gameBid != null && gameBid.getSessionOfJoined().equals(session)) {
            gameBid.setSessionOfJoined(null);
            resultMap = new HashMap<String, String>();
            resultMap.put("operation", "joined closed");

            json = gson.toJson(resultMap);
            gameBid.getSessionOfCreator().getBasicRemote().sendText(json);
            gameBid.setBlock(false);
            gameBid = null;
        } else {
            LOGGER.log(Level.SEVERE, "leave game session");
            if (gameSession != null && gameSession.getSessionOfCreator() != null && gameSession.getSessionOfCreator().equals(session)) {
                LOGGER.log(Level.SEVERE, "creator");
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "creator closed in game");

                json = gson.toJson(resultMap);
                if (gameSession != null && gameSession.getSessionOfJoined() != null) {
                    gameSession.getSessionOfJoined().getBasicRemote().sendText(json);
                }
            } else if (gameSession != null && gameSession.getSessionOfJoined() != null && gameSession.getSessionOfJoined().equals(session)) {
                LOGGER.log(Level.SEVERE, "joined");
                resultMap = new HashMap<String, String>();
                resultMap.put("operation", "joined closed in game");

                json = gson.toJson(resultMap);
                LOGGER.log(Level.SEVERE, "//TODO: " + String.valueOf(gameSession == null));
                if (gameSession != null && gameSession.getSessionOfCreator() != null) {
                    LOGGER.log(Level.SEVERE, "//TODO");
                    gameSession.getSessionOfCreator().getBasicRemote().sendText(json);
                    LOGGER.log(Level.SEVERE, "//TODO");
                }
            }
            //TODO тут надо сохранить игру
            LOGGER.log(Level.SEVERE, "//TODO");
            gameSessionService.deleteGameSession(creatorLogin);
            LOGGER.log(Level.SEVERE, "gs count: " + gameSessionService.getAllGameSessions().size());
        }
    }
}