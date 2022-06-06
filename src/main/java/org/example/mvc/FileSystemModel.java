package org.example.mvc;

import org.example.Logger;
import org.example.MatchOutcome;
import org.example.ResultTable;
import org.example.TeamResult;
import org.example.exception.CanNotCreateResultFileException;
import org.example.exception.CanNotOpenFileException;
import org.example.exception.InvalidTeamCountException;
import org.example.exception.InvalidTeamDataException;
import org.example.response.TeamResultsResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FileSystemModel implements Model {

    private final ResultTable resultTable;
    private final List<Observer> observers;
    private final Logger logger = Logger.getInstance();

    public FileSystemModel() {
        observers = new ArrayList<>();
        resultTable = new ResultTable();
    }

    @Override
    public boolean readResults(File dir) {
        boolean readingIsSuccess = true;

        File[] files = dir.listFiles();
        if (files == null) {
            String message = String.format("Can not access to directory [%s]. Perhaps it does not exist or it does not contain some files", dir.getPath());
            logger.log(message);
            notifyObservers(message);
            return false;
        }

        if (files.length == 0) {
            String message = String.format("Directory is empty [%s]", dir.getPath());
            logger.log(message);
            notifyObservers(message);
            return false;
        }

        for (File file : files) {
            if (!file.getName().equals("result.csv")) {
                try {
                    String[] results = readLines(file);
                    TeamResultsResponse teamResultsResponse = parseTeamResults(results, file);
                    if (!teamResultsResponse.isAllDataIsValid()) {
                        readingIsSuccess = false;
                    }
                    resultTable.addAll(teamResultsResponse.getTeamResults());
                } catch (CanNotOpenFileException e) {
                    logger.log(String.format(
                            "%s. File [%s]", e.getMessage(), file.getName()
                    ));
                    readingIsSuccess = false;
                } catch (InvalidTeamCountException e) {
                    logger.log(String.format(
                            "%s. File [%s]. Row %s", e.getMessage(), file.getName(), 0
                    ));
                    readingIsSuccess = false;
                }
            }
        }

        if (readingIsSuccess) {
            notifyObservers("Reading is successful");
        } else {
            notifyObservers(String.format("Reading is unsuccessful. See logs [ %s ]", Logger.getLogFilePath()));
        }

        return readingIsSuccess;
    }

    private TeamResultsResponse parseTeamResults(String[] results, File file) {
        boolean allDataIsValid = true;
        List<TeamResult> teamResults = new LinkedList<>();
        for (int i = 0; i < results.length; i++) {

            String result = results[i];
            if (result == null || result.isBlank()) {
                logger.log(String.format(
                                "Team results can not be empty string or null. File %s. Row %d", file.getName(), i + 1
                        )
                );
                allDataIsValid = false;
            } else {
                result = result.trim();

                try {
                    String name = parseName(result);
                    Queue<MatchOutcome> matchOutcomes = parseMatchOutcomes(result);

                    teamResults.add(new TeamResult(name, matchOutcomes));
                } catch (InvalidTeamDataException e) {
                    logger.log(
                            String.format("%s. File %s. Row %d", e.getMessage(), file.getName(), i + 1)
                    );
                    allDataIsValid = false;
                }
            }
        }

        return new TeamResultsResponse(teamResults, allDataIsValid);
    }


    private String parseName(String result) throws InvalidTeamDataException {
        int commaIndex = result.indexOf(',');

        if (commaIndex == -1) {
            throw new InvalidTeamDataException("Team results must be separated by commas " + result);
        }

        String name = result.substring(0, commaIndex);
        if (name.isBlank()) {
            throw new InvalidTeamDataException("Team name can not be empty string " + result);
        }

        return name;
    }

    private Queue<MatchOutcome> parseMatchOutcomes(String result) throws InvalidTeamDataException {
        result = result.substring(result.indexOf(',') + 1);

        if (result.isBlank()) {
            throw new InvalidTeamDataException("Match outcomes can not be empty string");
        }

        String[] unparsedMatchOutcomes = result.split(",");
        int length = unparsedMatchOutcomes.length;
        if (length != 10) {
            throw new InvalidTeamDataException("Team result must contain 10 match outcomes but actually " + length);
        }

        Queue<MatchOutcome> matchOutcomes = new LinkedList<>();
        for (String matchOutcome : unparsedMatchOutcomes) {
            matchOutcomes.add(parseMatchOutcome(matchOutcome));
        }

        return matchOutcomes;
    }

    private MatchOutcome parseMatchOutcome(String matchOutcome) throws InvalidTeamDataException {
        boolean containsSeparator = matchOutcome.contains(":");

        if (!containsSeparator) {
            throw new InvalidTeamDataException("Match outcome must be separated by ':' symbol but actually " + matchOutcome);
        }

        String[] goals = matchOutcome.split(":");

        if (goals.length != 2) {
            throw new InvalidTeamDataException("Invalid match outcome format " + matchOutcome);
        }

        try {
            int scored = Integer.parseInt(goals[0]);
            int missed = Integer.parseInt(goals[1]);

            return new MatchOutcome(scored, missed);
        } catch (NumberFormatException e) {
            throw new InvalidTeamDataException("Invalid goals number " + matchOutcome, e);
        }
    }


    @Override
    public void writeResults(File dir) {
        File result;

        try {
            result = createResultFileIfNotExist(dir);
        } catch (CanNotCreateResultFileException e) {
            logger.log(e.getMessage());
            notifyObservers("Can not create result file");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(result, false))) {
            writer.write(resultTable.toCsv());
        } catch (IOException e) {
            logger.log(String.format("Can not write to directory %s", dir.getPath()));
            notifyObservers(String.format("Can not write to directory %s", dir.getPath()));
            return;
        }

        notifyObservers(String.format("Writing is successful\nCongratulation, you can find result file in directory %s", dir.getPath()));
    }

    private File createResultFileIfNotExist(File dir) throws CanNotCreateResultFileException {
        File resultFile = new File(dir, "result.csv");

        if (!resultFile.exists()) {
            try {
                resultFile.createNewFile();
            } catch (IOException e) {
                throw new CanNotCreateResultFileException(String.format(
                        "Can not create result.csv file in directory %s", dir.getPath()
                ), e);
            }
        }

        return resultFile;
    }

    private String[] readLines(File file) throws CanNotOpenFileException, InvalidTeamCountException {
        String[] lines;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String teamCountStr = reader.readLine();

            int teamCount = parseTeamCount(teamCountStr);

            lines = new String[teamCount];
            for (int i = 0; i < teamCount; i++) {
                lines[i] = reader.readLine();
            }

        } catch (IOException e) {
            throw new CanNotOpenFileException("Can not open file", e);
        }

        return lines;
    }

    private int parseTeamCount(String teamCount) throws InvalidTeamCountException {
        if (teamCount != null) {
            teamCount = teamCount.trim();
        } else {
            throw new InvalidTeamCountException("Team count can not be null");
        }

        int count;
        if (teamCount.isBlank()) {
            throw new InvalidTeamCountException("Team count can not be empty string");
        }

        try {
            count = Integer.parseInt(teamCount);
        } catch (NumberFormatException e) {
            throw new InvalidTeamCountException(String.format("Invalid team count number [%s]", teamCount), e);
        }

        if (count <= 0) {
            throw new InvalidTeamCountException(String.format("Team count can not be less than or equals to 0 [%s]", teamCount));
        }

        return count;
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o :
                observers) {
            o.update(message);
        }
    }
}
