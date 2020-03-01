package com.fabianbg.ten_pin_bowling.application.cotroller;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fabianbg.ten_pin_bowling.application.util.IFileUtils;
import com.fabianbg.ten_pin_bowling.domain.model.PlayerScore;
import com.fabianbg.ten_pin_bowling.domain.service.IBowlingGame;
import com.google.inject.Inject;

public class CMDControllerImpl implements IAppController {

    private final String SCORE_FILE_SEP = "\\s+";

    @Inject
    private IBowlingGame bowlingGame;

    @Inject
    private IFileUtils fileManager;

    private final PrintStream printer = System.out;

    public void init(String[] args) {
        this.executeCommand(args);
    }

    private String executeCommand(String[] args) {
        try {
            if (args.length == 0)
                throw new Exception("not enough arguments.");
            switch (args[0]) {
                case "--process-plays":
                    if (args.length < 2)
                        throw new Exception("not enough command params.");
                    Map<String, List<String>> mappedPlays = this.proccessOrderAndGroupPlaysFile(args[1]);
                    String results = this.getScoreTablesFromGroupedPlays(mappedPlays);
                    this.printer.print(results);
                    return results;
                default:
                    throw new Exception("no command found.");
            }

        } catch (Exception e) {
            this.printer.print(e.getMessage());
            return e.getMessage();
        }
    }

    private Map<String, List<String>> proccessOrderAndGroupPlaysFile(String filePath) throws Exception {
        try {
            return this.fileManager.readFile(filePath).map((String line) -> {
                try {
                    return this.proccessScoreFileLine(line);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }).collect(Collectors.groupingBy((String[] line) -> line[0],
                    Collectors.mapping((String[] line) -> line[1], Collectors.toList())));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("error reading the file");
        }
    }

    private String getScoreTablesFromGroupedPlays(Map<String, List<String>> plays) throws Exception {
        try {
            Map<String, PlayerScore> game = this.bowlingGame.newGame();
            return plays.entrySet().stream().map((Map.Entry<String, List<String>> e) -> {
                PlayerScore player = this.bowlingGame.makePlays(game, e.getKey(), e.getValue());
                return this.bowlingGame.getPlayerResults(player);
            }).reduce(String::concat).get();
        } catch (Exception e) {
            throw new Exception("error processing the plays");
        }

    }

    private String[] proccessScoreFileLine(String line) throws Exception {
        String[] values = line.split(this.SCORE_FILE_SEP);
        if (values.length != 2)
            throw new Exception("error parsing a line too much arguments");
        final int score = values[1].equals("F") ? 0 : Integer.parseInt(values[1]);
        if (score < 0 || score > 10)
            throw new Exception("error parsing a line the score is out of range 0-10");

        return values;
    }
}