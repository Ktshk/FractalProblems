package com.dinoproblems.server;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Katushka on 27.05.2019.
 */
public class ShowRecordsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String userId = request.getParameter("user_id");

        final List<RecordRow> records = DataBaseService.INSTANCE.getRecords();

        System.out.println("records = " + records);

        request.setAttribute("records", records);
        request.setAttribute("userId", userId == null ? "" : userId);

        RequestDispatcher rd = request.getRequestDispatcher("records.jsp");
        rd.forward(request, response);
    }
}
