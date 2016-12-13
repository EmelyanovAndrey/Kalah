package com.pesikovlike.kalah.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.user.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

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
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
    {
        operation: create;
        creatorLogin: ;
        friendLogin: ;
        holeCount: ;
        stoneCount: ;
    }
*/

/**
 * Created by Igor on 06.12.2016.
 */
@WebServlet(name = "createNewGame", urlPatterns = {"/createNewGame"})
public class CreateNewGameServlet extends HttpServlet {

    @EJB
    private UserService userService;

    @Inject
    private GameBidFactory gameBidFactory;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    @Inject
    private GameBidService gameBidService;


    private static final Logger LOGGER = Logger.getLogger("CreateNewGame Servlet");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        if (br != null) {
            jsonStr = br.readLine();
        }
        JsonObject requestJson = Json.createReader(new StringReader(jsonStr)).readObject();

        HttpSession session = request.getSession();
        String creatorLogin = session.getAttribute("login").toString();
        String friendLogin = requestJson.getJsonString("friendLogin").getString();

        int holeCount = Integer.parseInt(requestJson.getJsonString("holeCount").getString()) * 2;
        int stoneCount = Integer.parseInt(requestJson.getJsonString("stoneCount").getString());

        LOGGER.log(Level.SEVERE, "Receive data: " + jsonStr);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        if (friendLogin.equals("") || userService.userExist(requestJson.getString("friendLogin"))) {
            gameBidService.deleteBid(creatorLogin);
            GameBid newBid = gameBidFactory.getGameBid();
            newBid.setCreatorLogin(creatorLogin);
            newBid.setFriendLogin(friendLogin);
            newBid.setHoleCount(holeCount);
            newBid.setStoneCount(stoneCount);
            gameBidService.addBid(newBid);

            LOGGER.log(Level.SEVERE, "Success game creation for user: " + creatorLogin);
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("result", "success");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            session.setAttribute("role", "creator");
            response.getWriter().write(json);
        } else {

            LOGGER.log(Level.SEVERE, "Error game creation for user: " + creatorLogin);
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("result", "error");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            response.getWriter().write(json);
        }
    }
}
