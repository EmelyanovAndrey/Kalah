package com.pesikovlike.kalah.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
        name = "getBids",
        urlPatterns = {"/getBids"}
)
public class GetBidsServlet extends HttpServlet {
    @Inject
    private GameBidService gameBidService;
    private static final Logger LOGGER = Logger.getLogger("GetBids Servlet");

    public GetBidsServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.SEVERE, "Start get bids");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        if(br != null) {
            jsonStr = br.readLine();
        }

        LOGGER.log(Level.SEVERE, "Receive data: " + jsonStr);
        HttpSession session = request.getSession();
        String login = session.getAttribute("login").toString();
        LOGGER.log(Level.SEVERE, "login: " + login);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        Set bids = this.gameBidService.getAllGameBidsForUser(login);
        if(bids != null) {
            Gson resultMap = (new GsonBuilder()).setPrettyPrinting().create();
            JsonObject gson = new JsonObject();
            gson.addProperty("result", "success");
            JsonArray json = new JsonArray();
            Iterator json1 = bids.iterator();

            while(json1.hasNext()) {
                GameBid bid = (GameBid)json1.next();
                JsonObject obj = new JsonObject();
                obj.addProperty("vs", bid.getCreatorLogin());
                obj.addProperty("stones", Integer.valueOf(bid.getStoneCount()));
                obj.addProperty("holes", Integer.valueOf(bid.getHoleCount()));
                json.add(obj);
            }

            gson.add("bids", json);
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
