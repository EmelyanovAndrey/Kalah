package com.pesikovlike.kalah.server.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.model.dao.AvatarDAO;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.Avatar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
        name = "getAvatars",
        urlPatterns = {"/getAvatars"}
)
public class GetAvatarsServlet extends HttpServlet {

    @Inject
    @Named("avatarDAO")
    private AvatarDAO avatarDAO;

    private static final Logger LOGGER = Logger.getLogger("GetAvatars Servlet");

    public GetAvatarsServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.SEVERE, "Start get avatars");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        List<Avatar> avatars = avatarDAO.getAvatars();

        if(avatars != null) {
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            JsonObject res = new JsonObject();
            res.addProperty("result", "success");
            JsonArray arr = new JsonArray();

            for (Avatar avatar : avatars) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", avatar.getAvatarId());
                obj.addProperty("name", avatar.getAvatarName());
                obj.addProperty("path", avatar.getFilePath());
                arr.add(obj);
            }

            res.add("avatars", arr);
            String json = gson.toJson(res);
            LOGGER.log(Level.SEVERE, "response: " + json);
            response.getWriter().write(json);
        } else {
            HashMap resultMap = new HashMap();
            resultMap.put("result", "error");
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            String json = gson.toJson(resultMap);
            response.getWriter().write(json);
        }

    }
}
