package com.pesikovlike.kalah.server.servlets;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
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
 * Created by Igor on 06.11.2016.
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @EJB
    UserService userService;

    private static final Logger LOGGER = Logger.getLogger( "Login Servlet" );
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        LOGGER.log(Level.SEVERE, "Start login");

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        if (br != null) {
            jsonStr = br.readLine();
        }
        JsonObject requestJson = Json.createReader(new StringReader(jsonStr)).readObject();

        String login = requestJson.getString("login");
        String password = requestJson.getString("password");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        if (userService.authorize(login, password) == 0) {

            session.setAttribute("login", login);

            LOGGER.log(Level.SEVERE, "Success login for user: " + login);

            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("result", "success");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            response.getWriter().write(json);
        } else {
            LOGGER.log(Level.SEVERE, "Error registration for user: " + login);
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("result", "error");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            response.getWriter().write(json);

        }
    }

}
