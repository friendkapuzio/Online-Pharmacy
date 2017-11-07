package by.bsu.machulski.controller;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.command.CommandFactory;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);
        AbstractCommand command = new CommandFactory().defineCommand(request);
        Router router = command.execute(content);
        content.insertValues(request);
        if (RoutingType.FORWARD.equals(router.getRoutingType())) {
            request.getRequestDispatcher(router.getPath()).forward(request, response);
        } else if (RoutingType.REDIRECT.equals(router.getRoutingType())) {
            response.sendRedirect(request.getContextPath() + router.getPath());
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        ConnectionPool.getInstance().closePool();
    }
}
