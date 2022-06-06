package org.example;

import java.util.LinkedList;
import java.util.List;

public class ResultTable {

    private final List<TeamResult> teamResults = new LinkedList<>();

    public void addAll(List<TeamResult> teamResults) {
        this.teamResults.addAll(teamResults);
    }

    public String toCsv() {
        StringBuilder sb = new StringBuilder();

        for (TeamResult tr: teamResults) {
            sb.append(tr.toCsv()).append('\n');
        }

        return sb.substring(0, sb.toString().length() - 1);
    }
}
