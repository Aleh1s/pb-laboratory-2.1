package org.example;

import java.util.Queue;

public class TeamResult {

    private String name;
    private Queue<MatchOutcome> matchOutcomes;

    public TeamResult(String name,
                      Queue<MatchOutcome> matchOutcomes) {
        this.name = name;
        this.matchOutcomes = matchOutcomes;
    }

    public String toCsv() {
        return String.format("%s,%d", name, countPoints());
    }

    public int countPoints() {
        MatchOutcome mo;
        int totalScore = 0;

        while ((mo = matchOutcomes.poll()) != null) {
            totalScore += mo.determineScore();
        }

        return totalScore;
    }
}
