package com.pesikovlike.kalah.server.servlets;


import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor on 06.11.2016.
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @EJB
    UserService userService;

    private static final Logger LOGGER = Logger.getLogger( "MyLogger" );
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (userService.authorize(login, password) == 0) {
            LOGGER.log( Level.SEVERE, "Success authorization for user: " + login + " with password: " + password);
            request.getRequestDispatcher("/profile.html").forward(request, response);
        } else {
            LOGGER.log( Level.SEVERE, "Error authorization for user: " + login + " with password: " + password);
            request.getRequestDispatcher("/index.html").forward(request, response);
        }
    }

}
