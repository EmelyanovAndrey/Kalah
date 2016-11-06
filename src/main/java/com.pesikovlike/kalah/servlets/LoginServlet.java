package com.pesikovlike.kalah.servlets;

import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Igor on 06.11.2016.
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @EJB
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (userService.register(login, password, null, 0) == 0) {
            request.getRequestDispatcher("/profile.html").forward(request, response);
        } else {
            request.getRequestDispatcher("/index.html").forward(request, response);
        }
    }

}
