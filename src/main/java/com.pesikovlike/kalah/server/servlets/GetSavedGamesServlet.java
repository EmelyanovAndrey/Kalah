package com.pesikovlike.kalah.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;
import com.pesikovlike.kalah.user.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
        name = "getSavedGames",
        urlPatterns = {"/getSavedGames"}
)
public class GetSavedGamesServlet extends HttpServlet {
    @EJB
    private UserService userService;
    private static final Logger LOGGER = Logger.getLogger("SavedGames Servlet");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.SEVERE, "Start get saved games");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        if (br != null) {
            jsonStr = br.readLine();
        }

        LOGGER.log(Level.SEVERE, "Receive data: " + jsonStr);
        HttpSession session = request.getSession();
        String login = session.getAttribute("login").toString();
        LOGGER.log(Level.SEVERE, "login: " + login);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        List<GameState> games = userService.getSavedGamesForUser(login);
        if (games != null) {
            Gson resultMap = (new GsonBuilder()).setPrettyPrinting().create();
            JsonObject gson = new JsonObject();
            gson.addProperty("result", "success");
            JsonArray json = new JsonArray();
            Iterator iter = games.iterator();

            while (iter.hasNext()) {
                GameState gs = (GameState) iter.next();
                JsonObject obj = new JsonObject();

                int numHoles = (gs.getInitialHoleCount() + 1) * 2;
                Hole[] holes = new Hole[numHoles];
                Hole[] holesSet = new Hole[numHoles];
                holesSet = gs.getHoles().toArray(holesSet);
                for (int i = 0; i < numHoles; i++) {
                    for (int j = 0; j < numHoles; j++) {
                        if (holesSet[j].getNumber() == i) {
                            holes[i] = holesSet[j];
                            LOGGER.log(Level.SEVERE, i + ":" + holes[i].getStoneCount());
                        }
                    }
                }

                if (gs.getUser1().getLogin().equals(login)) {
                    obj.addProperty("vs", gs.getUser2().getLogin());
                    obj.addProperty("set", holes[holes.length / 2 - 1].getStoneCount() + "/" + holes[holes.length - 1].getStoneCount());
                } else {
                    obj.addProperty("vs", gs.getUser1().getLogin());
                    obj.addProperty("set",holes[holes.length - 1].getStoneCount() + "/" + holes[holes.length / 2 - 1].getStoneCount());
                }
                obj.addProperty("creatorLogin", gs.getUser1().getLogin());
                obj.addProperty("stones", Integer.valueOf(gs.getInitialStoneCount()));
                obj.addProperty("holes", Integer.valueOf(gs.getInitialHoleCount()));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date date = gs.getLastSaveDate();
                String datef = df.format(date);
                obj.addProperty("date", datef);
                obj.addProperty("id", gs.getGameStateId());

                json.add(obj);
            }

            gson.add("games", json);
            String json3 = resultMap.toJson(gson);
            response.getWriter().write(json3);
        } else {
            HashMap resultMap1 = new HashMap();
            resultMap1.put("result", "error");
            Gson gson1 = (new GsonBuilder()).setPrettyPrinting().create();
            String json2 = gson1.toJson(resultMap1);
            response.getWriter().write(json2);
        }

    }
}
