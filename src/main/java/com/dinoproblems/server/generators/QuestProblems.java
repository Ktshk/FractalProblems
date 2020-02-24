package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;

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
}
