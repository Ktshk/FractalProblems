package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Katushka on 06.02.2020.
 */
public class QuestProblems {
    private String name;
    private String description;

    private Date start;
    private Date end;

    private List<Problem> problems;

    public QuestProblems(String name, String description, Date start, Date end, List<Problem> problems) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.problems = problems;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Problem getProblem(Calendar date) {
        final Calendar questStart = Calendar.getInstance();
        questStart.setTime(start);

        final Calendar questEnd = Calendar.getInstance();
        questEnd.setTime(end);
        questEnd.add(Calendar.DATE, 1);
        int i = 0;


        while (questStart.before(questEnd)) {
            if (questStart.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                    && questStart.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                    && questStart.get(Calendar.DATE) == date.get(Calendar.DATE)) {
                return problems.get(i);
            }
            questStart.add(Calendar.DATE, 1);
            i++;
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
