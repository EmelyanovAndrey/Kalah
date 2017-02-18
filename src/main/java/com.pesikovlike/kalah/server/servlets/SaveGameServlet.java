package com.pesikovlike.kalah.server.servlets;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.game.session.GameSessionService;
import com.pesikovlike.kalah.model.dao.GameStateDAO;
import com.pesikovlike.kalah.model.dao.HoleDAO;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;
import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "saveGame", urlPatterns = {"/saveGame"})
public class SaveGameServlet extends HttpServlet {

    @Inject
    private GameSessionService gameSessionService;

    @Inject
    @Named("gameStateDAO")
    private GameStateDAO gameStateDAO;

    @Inject
    @Named("holeDAO")
    private HoleDAO holeDAO;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    @EJB
    UserService userService;

    private static final Logger LOGGER = Logger.getLogger("Save Game Servlet");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        LOGGER.log(Level.SEVERE, "Start Save Game");
/*
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        if (br != null) {
            jsonStr = br.readLine();
        }
        JsonObject requestJson = Json.createReader(new StringReader(jsonStr)).readObject();
*/
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        String creatorLogin = session.getAttribute("creatorLogin").toString();
        GameSession gs = gameSessionService.getGameSession(creatorLogin);
        boolean saved = false;

        for (int i = 0; i < gameStateDAO.getGameStatesByUser(userDAO.getUserByLogin(creatorLogin)).size(); i++) {
            if (gameStateDAO.getGameStatesByUser(userDAO.getUserByLogin(creatorLogin)).get(i).getGameStateId() == gs.getGameState().getGameStateId()) {
                saved = true;
                break;
            }
        }
        if (saved) {
            gs.getGameState().setLastSaveDate(new Date());
            gameStateDAO.updateGameState(gs.getGameState());
            Hole[] holes = gs.getHolesArray();
            for (int i = 0; i < holes.length; i++) {
                holeDAO.updateHole(holes[i]);
            }
        } else {
            gs.getGameState().setLastSaveDate(new Date());
            gameStateDAO.insertGameState(gs.getGameState());
            Hole[] holes = gs.getHolesArray();
            for (int i = 0; i < holes.length; i++) {
                holeDAO.insertHole(holes[i]);
            }
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("result", "success");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(resultMap);
        response.getWriter().write(json);

    }

}
