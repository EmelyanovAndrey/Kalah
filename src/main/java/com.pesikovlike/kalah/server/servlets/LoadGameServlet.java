package com.pesikovlike.kalah.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.game.session.GameSessionFactory;
import com.pesikovlike.kalah.game.session.GameSessionService;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;
import com.pesikovlike.kalah.user.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
        name = "loadGame",
        urlPatterns = {"/loadGame"}
)
public class LoadGameServlet extends HttpServlet {
    @EJB
    private UserService userService;
    @Inject
    @Named("userDAO")
    private UserDAO userDAO;
    @Inject
    private GameSessionService gameSessionService;
    private static final Logger LOGGER = Logger.getLogger("LoadGame Servlet");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.SEVERE, "Start load game");

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        if (br != null) {
            jsonStr = br.readLine();
        }
        LOGGER.log(Level.SEVERE, jsonStr);
        javax.json.JsonObject requestJson = Json.createReader(new StringReader(jsonStr)).readObject();

        LOGGER.log(Level.SEVERE, "Receive data: " + jsonStr);
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        String creatorLogin = requestJson.getString("creatorLogin");
        String login = requestJson.getString("login");
        String id = requestJson.getString("id");

        session.setAttribute("login", login);
        session.setAttribute("creatorLogin", creatorLogin);
        session.setAttribute("load", "true");
        session.setAttribute("vs", "human");
        session.setAttribute("id", id);
        if(login.equals(creatorLogin)) {
            session.setAttribute("role", "creator");
        } else {
            session.setAttribute("role", "joined");
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("result", "success");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(resultMap);
        response.getWriter().write(json);
    }
}
