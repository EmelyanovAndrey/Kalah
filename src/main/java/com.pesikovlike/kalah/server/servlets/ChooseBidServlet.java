//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pesikovlike.kalah.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.user.UserService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
        name = "chooseBid",
        urlPatterns = {"/chooseBid"}
)
public class ChooseBidServlet extends HttpServlet {
    @EJB
    private UserService userService;
    @Inject
    private GameBidFactory gameBidFactory;
    @Inject
    @Named("userDAO")
    private UserDAO userDAO;
    @Inject
    private GameBidService gameBidService;
    private static final Logger LOGGER = Logger.getLogger("ChooseBid Servlet");

    public ChooseBidServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.SEVERE, "Start choose bid");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        if(br != null) {
            jsonStr = br.readLine();
        }

        LOGGER.log(Level.SEVERE, "Receive data: " + jsonStr);
        JsonParser parser = new JsonParser();
        JsonObject requestJson = parser.parse(jsonStr).getAsJsonObject();
        String creatorLogin = requestJson.get("creatorLogin").getAsString();
        HttpSession session = request.getSession();
        String joinedLogin = session.getAttribute("login").toString();
        GameBid gameBid = gameBidService.getBid(creatorLogin);
        LOGGER.log(Level.SEVERE, "GameBid: " + gameBid);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        HashMap resultMap;
        Gson gson;
        String json;
        if(gameBid != null && !gameBid.isBlock()) {
            LOGGER.log(Level.SEVERE, "Success game joined for user: " + joinedLogin + ", creator: " + creatorLogin);
            gameBid.setBlock(true);
            session.setAttribute("creatorLogin", creatorLogin);
            resultMap = new HashMap();
            resultMap.put("result", "success");
            resultMap.put("role", "joined");
            resultMap.put("creatorLogin", creatorLogin);
            gson = (new GsonBuilder()).setPrettyPrinting().create();
            json = gson.toJson(resultMap);
            session.setAttribute("role", "joined");
            session.setAttribute("load", "false");
            session.setAttribute("vs", "human");
            response.getWriter().write(json);
        } else {
            LOGGER.log(Level.SEVERE, "Error bid for creator: " + creatorLogin + ", joined: " + joinedLogin);
            resultMap = new HashMap();
            resultMap.put("result", "error");
            if(gameBid != null && gameBid.isBlock()) {
                resultMap.put("type", "block");
                LOGGER.log(Level.SEVERE, "GameBid is blocked");
            } else if(gameBid == null) {
                resultMap.put("type", "not exist");
                LOGGER.log(Level.SEVERE, "GameBid is null");
            }

            gson = (new GsonBuilder()).setPrettyPrinting().create();
            json = gson.toJson(resultMap);
            response.getWriter().write(json);
        }

    }
}
