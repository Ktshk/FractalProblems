package com.dinoproblems.server;

import com.dinoproblems.server.generators.QuestProblems;
import com.dinoproblems.server.generators.QuestProblemsLoader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
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
        final String button = request.getParameter("button");
        int selectedRecords = 0;
        if (button != null && Integer.valueOf(button) == 1) {
            selectedRecords = 1;
        }
        if (button != null && Integer.valueOf(button) == 2) {
            selectedRecords = 2;
        }

        final List<RecordRow> records = DataBaseService.INSTANCE.getRecords(false, null, false);
        final List<RecordRow> expertRecords = DataBaseService.INSTANCE.getRecords(true, null, false);

//        QuestProblems questProblems = QuestProblemsLoader.INSTANCE.getLastQuestProblems(Calendar.getInstance());
//        String scenarioName = questProblems.getProblem(0).getProblemScenario().getScenarioId();
        final List<RecordRow> questRecords = DataBaseService.INSTANCE.getRecords(false, /*scenarioName*/null, true);

        request.setAttribute("records", records);
        request.setAttribute("expertRecords", expertRecords);
        request.setAttribute("userId", userId == null ? "" : userId);
        request.setAttribute("selectedRecords", selectedRecords);
        request.setAttribute("questRecords", questRecords);
        request.setAttribute("questName", "Сегодня");


        RequestDispatcher rd = request.getRequestDispatcher("records.jsp");
        rd.forward(request, response);
    }
}
