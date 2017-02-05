package com.pesikovlike.kalah.server.servlets;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.model.dao.AvatarDAO;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.Avatar;
import com.pesikovlike.kalah.model.entity.User;
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
@WebServlet(name = "changeUserData", urlPatterns = {"/changeUserData"})
public class ChangeUserDataServlet extends HttpServlet {

    @EJB
    private UserService userService;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    @Inject
    @Named("avatarDAO")
    private AvatarDAO avatarDAO;


    private static final Logger LOGGER = Logger.getLogger("Registration Servlet");

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
        LOGGER.log(Level.SEVERE, "Start change user data");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        String oldLogin = session.getAttribute("login").toString();
        if (userService.change(oldLogin, requestJson.getJsonString("login").getString(), requestJson.getJsonString("password").getString(), requestJson.getJsonString("email").getString()) == 0) {

            session.setAttribute("login", requestJson.getJsonString("login").getString());

            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("result", "success");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            response.getWriter().write(json);
        } else {
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("result", "error");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            response.getWriter().write(json);
        }

    }
}
