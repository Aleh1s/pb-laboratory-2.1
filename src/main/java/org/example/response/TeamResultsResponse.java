package org.example.response;

import org.example.TeamResult;

import java.util.List;

public class TeamResultsResponse {
    private final List<TeamResult> teamResults;
    private final boolean allDataIsValid;

    public TeamResultsResponse(List<TeamResult> teamResults, boolean allDataIsValid) {
        this.teamResults = teamResults;
        this.allDataIsValid = allDataIsValid;
    }

    public List<TeamResult> getTeamResults() {
        return teamResults;
    }

    public boolean isAllDataIsValid() {
        return allDataIsValid;
    }

}
