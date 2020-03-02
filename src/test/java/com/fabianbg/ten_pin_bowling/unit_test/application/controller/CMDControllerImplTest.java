package com.fabianbg.ten_pin_bowling.unit_test.application.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.fabianbg.ten_pin_bowling.application.cotroller.CMDControllerImpl;
import com.fabianbg.ten_pin_bowling.application.util.IFileUtils;
import com.fabianbg.ten_pin_bowling.domain.model.Frame;
import com.fabianbg.ten_pin_bowling.domain.model.PlayerScore;
import com.fabianbg.ten_pin_bowling.domain.service.IBowlingGame;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CMDControllerImplTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Mock
    private IFileUtils fileManager;

    @Mock
    private IBowlingGame bowlingGameService;

    @Mock
    private PrintStream printer;

    private CMDControllerImpl cmdController;

    @Before
    public void setup() {
        this.cmdController = new CMDControllerImpl(bowlingGameService, fileManager, printer);
    }

    @Test
    public void initWithNoComand_ShouldPrintError() {
        this.cmdController.init(new String[] {});
        verify(this.printer, times(1)).println("not enough arguments.");
    }

    @Test
    public void initWithCommandNotExist_ShouldPrintError() {
        this.cmdController.init(new String[] { "" });
        verify(this.printer, times(1)).println("no command found.");
    }

    @Test
    public void initWithCommandNoArgs_ShouldPrintError() {
        this.cmdController.init(new String[] { "--process-plays" });
        verify(this.printer, times(1)).println("missing command params.");
    }

    @Test
    public void initWithProccesPlaysCMDAndFileError_ShouldPrintFileError() throws IOException {

        final String filePath = "/data/file.txt";

        when(this.fileManager.readFile(filePath)).thenThrow(new IOException());

        this.cmdController.init(new String[] { "--process-plays", filePath });

        verify(this.fileManager, times(1)).readFile(filePath);
        verify(this.printer, times(1)).println("error reading the file");
    }

    @Test
    public void initWithProccesPlaysCMDAndFileLineInvalid_ShouldPrintLineErrorNotInRange() throws IOException {

        final String filePath = "/data/file.txt";
        final Stream<String> fileStream = Arrays.asList("Steve\t22", "Steve\t2").stream();

        when(this.fileManager.readFile(filePath)).thenReturn(fileStream);

        this.cmdController.init(new String[] { "--process-plays", filePath });
        verify(this.printer, times(1)).println("error parsing a line the score is out of range 0-10");
    }

    @Test
    public void initWithProccesPlaysCMDAndFileLineInvalid_ShouldPrintLineErrorArgs() throws IOException {

        final String filePath = "/data/file.txt";
        final Stream<String> fileStream = Arrays.asList("Steve\t1", "Steve\t1\t2").stream();

        when(this.fileManager.readFile(filePath)).thenReturn(fileStream);

        this.cmdController.init(new String[] { "--process-plays", filePath });
        verify(this.printer, times(1)).println("error parsing a line too much arguments");
    }

    @Test
    public void initWithProccesPlaysCMD_ShouldPrintResults() throws IOException {

        final String filePath = "/data/file.txt";
        final Stream<String> fileStream = Arrays.asList("Steve\t1", "Steve\t2", "Peter\t2").stream();
        final Map<String, PlayerScore> game = new HashMap<>();
        String player1 = "Steve";
        String player2 = "Peter";
        List<String> plays1 = Arrays.asList("1", "2");
        List<String> plays2 = Arrays.asList("2");
        PlayerScore player1Score = new PlayerScore();
        player1Score.setName(player1);
        player1Score.setPlays(plays1);
        player1Score.setFrames(new Frame[] {});
        PlayerScore player2Score = new PlayerScore();
        player2Score.setName(player2);
        player2Score.setPlays(plays2);
        player2Score.setFrames(new Frame[] {});
        String table1 = "table1";
        String table2 = "table2";

        when(this.fileManager.readFile(filePath)).thenReturn(fileStream);
        when(this.bowlingGameService.newGame()).thenReturn(game);
        when(this.bowlingGameService.makePlays(game, player1, plays1)).thenReturn(player1Score);
        when(this.bowlingGameService.makePlays(game, player2, plays2)).thenReturn(player2Score);
        when(this.bowlingGameService.getPlayerResults(player1Score)).thenReturn(table1);
        when(this.bowlingGameService.getPlayerResults(player2Score)).thenReturn(table2);

        this.cmdController.init(new String[] { "--process-plays", filePath });

        verify(this.fileManager, times(1)).readFile(filePath);
        verify(this.bowlingGameService, times(1)).newGame();
        verify(this.bowlingGameService, times(1)).makePlays(game, player1, plays1);
        verify(this.bowlingGameService, times(1)).makePlays(game, player2, plays2);
        verify(this.bowlingGameService, times(1)).getPlayerResults(player1Score);
        verify(this.bowlingGameService, times(1)).getPlayerResults(player2Score);
        verify(this.printer, times(1)).println(String.format("%s%s", table1, table2));
    }

}