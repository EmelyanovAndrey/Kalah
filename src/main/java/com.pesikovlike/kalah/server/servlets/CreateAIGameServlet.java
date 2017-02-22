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

@WebServlet(name = "createAIGame", urlPatterns = {"/createAIGame"})
public class CreateAIGameServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger("CreateAIGame Servlet");

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

        session.setAttribute("role", "creator");
        if (session.getAttribute("login") == null) {
            session.setAttribute("login", "Калах-мэн");
        } else {
            session.setAttribute("login", "Калах-мэн");
        }
        int prior = Integer.parseInt(requestJson.getJsonString("prior").getString());
        session.setAttribute("holeCount", Integer.parseInt(requestJson.getJsonString("holeCount").getString()));
        session.setAttribute("stoneCount", Integer.parseInt(requestJson.getJsonString("stoneCount").getString()));
        session.setAttribute("level", Integer.parseInt(requestJson.getJsonString("level").getString()));
        session.setAttribute("vs", "ai");
        if (prior == 1) {
            session.setAttribute("prior", "true");
        } else {
            session.setAttribute("prior", "false");
        }
        session.setAttribute("role", "creator");
        LOGGER.log(Level.SEVERE, "Create AI Game ");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("result", "success");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(resultMap);
        response.getWriter().write(json);
    }
}
