package com.fabianbg.ten_pin_bowling.application.cotroller;

import java.io.IOException;
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

    private PrintStream printer = System.out;

    public CMDControllerImpl() {
    }

    public CMDControllerImpl(IBowlingGame bowlingGame, IFileUtils fileManager, PrintStream printer) {
        this.fileManager = fileManager;
        this.bowlingGame = bowlingGame;
        this.printer = printer;
    }

    public void init(String[] args) {
        this.cmdHandler(args);
    }

    private String cmdHandler(String[] args) {
        try {
            if (args.length == 0)
                throw new Exception("not enough arguments.");
            switch (args[0]) {
                case "--process-plays":
                    if (args.length < 2)
                        throw new Exception("missing command params.");
                    Map<String, List<String>> mappedPlays = this.proccessOrderAndGroupPlaysFile(args[1]);
                    String results = this.getScoreTablesFromGroupedPlays(mappedPlays);
                    this.printer.println(results);
                    return results;
                default:
                    throw new Exception("no command found.");
            }

        } catch (Exception e) {
            this.printer.println(e.getMessage());
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
        } catch (IOException e) {
            throw new Exception("error reading the file");
        } catch (Exception e) {
            throw e;
        }
    }

    private String getScoreTablesFromGroupedPlays(Map<String, List<String>> plays) throws Exception {
        try {
            String header = this.bowlingGame.getFrameHeader();
            Map<String, PlayerScore> game = this.bowlingGame.newGame();
            String results = plays.entrySet().stream().map((Map.Entry<String, List<String>> e) -> {
                PlayerScore player = this.bowlingGame.makePlays(game, e.getKey(), e.getValue());
                return this.bowlingGame.getPlayerResults(player);
            }).reduce(String::concat).get();
            return String.format("%s\n%s", header, results);
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