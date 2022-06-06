package org.example;

public class MatchOutcome {

    private final int scored;
    private final int missed;

    public MatchOutcome(int scored, int missed) {
        this.scored = scored;
        this.missed = missed;
    }

    public int determineScore() {
        if (scored > missed) {
            return 3;
        } else if (scored < missed) {
            return 0;
        } else {
            return 1;
        }
    }
}
