package com.pesikovlike.kalah.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.model.dao.UserDAO;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor on 06.12.2016.
 */
@WebServlet(name = "getSessionData", urlPatterns = {"/getSessionData"})
public class GetSessionDataServlet extends HttpServlet {


    private static final Logger LOGGER = Logger.getLogger("GetSessionData Servlet");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String login = session.getAttribute("login").toString();
        String role = session.getAttribute("role").toString();

        LOGGER.log(Level.SEVERE, "User login: " + login);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("result", "success");
        resultMap.put("login", login);
        resultMap.put("role", role);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(resultMap);
        response.getWriter().write(json);
    }
}
