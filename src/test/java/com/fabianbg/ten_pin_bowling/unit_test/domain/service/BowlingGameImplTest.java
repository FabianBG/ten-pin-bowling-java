package com.fabianbg.ten_pin_bowling.unit_test.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fabianbg.ten_pin_bowling.domain.model.Frame;
import com.fabianbg.ten_pin_bowling.domain.model.PlayerScore;
import com.fabianbg.ten_pin_bowling.domain.service.BowlingGameImpl;
import com.fabianbg.ten_pin_bowling.domain.service.IBowlingGame;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BowlingGameImplTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private IBowlingGame bowlingGameService;

    @Before
    public void setup() {
        this.bowlingGameService = new BowlingGameImpl();
    }

    @Test
    public void newGame_ShouldReturnANewGameMap() {
        Map<String, PlayerScore> res = this.bowlingGameService.newGame();
        assertNotNull(res);
    }

    @Test
    public void makePlayWithNoStrikeAndFaultInNewGame_ShouldReturnAPlayerScoreWithEmptyScore() {
        final String plays = "8";
        Map<String, PlayerScore> newGame = this.bowlingGameService.newGame();
        final String name = "Steve";

        PlayerScore result = this.bowlingGameService.makePlay(newGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames()[0].getScore(), 0);
    }

    @Test
    public void makePlayWithStrikeInNewGame_ShouldReturnAPlayerScoreWithEmptyScore() {
        final String plays = "10";
        Map<String, PlayerScore> newGame = this.bowlingGameService.newGame();
        final String name = "Steve";

        PlayerScore result = this.bowlingGameService.makePlay(newGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames()[0].getScore(), 0);
    }

    @Test
    public void makePlaysWithValidDataInNewGame_ShouldReturnAPlayerScoreWithPlays() {
        final List<String> plays = Arrays.asList("8", "2", "7", "3", "3", "4", "10", "2", "8", "10", "10", "8", "0",
                "10", "8", "2", "9");
        Map<String, PlayerScore> newGame = this.bowlingGameService.newGame();
        final String name = "Steve";

        PlayerScore result = this.bowlingGameService.makePlays(newGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames().length, 10);
        assertEquals(result.getFrames()[result.getFrames().length - 1].getScore(), 170);
    }

    @Test
    public void makePlaysWithValidDataInExistingGame_ShouldReturnAnExistingPlayerScoreWithPlays() {
        final List<String> plays = Arrays.asList("10", "7", "3", "9", "0", "10", "0", "8", "8", "2", "F", "6", "10",
                "10", "10", "8", "1");
        Map<String, PlayerScore> customGame = this.bowlingGameService.newGame();
        final String name = "Jeff";
        PlayerScore jeffScore = new PlayerScore(name, 10);
        customGame.put(name, jeffScore);

        PlayerScore result = this.bowlingGameService.makePlays(customGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames().length, 10);
        assertEquals(result.getFrames()[result.getFrames().length - 1].getScore(), 167);
    }

    @Test
    public void makePlaysWithValidDataInExistingGame_ShouldReturnANewPlayerScoreWithPlays() {
        final List<String> plays = Arrays.asList("8", "1", "7", "3", "3", "4", "10", "2", "8", "10", "10", "8", "0",
                "10", "8", "2", "9");
        Map<String, PlayerScore> customGame = this.bowlingGameService.newGame();
        customGame.put("Jeff", new PlayerScore());
        final String name = "Steve";

        PlayerScore result = this.bowlingGameService.makePlays(customGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames().length, 10);
        assertEquals(result.getFrames()[result.getFrames().length - 1].getScore(), 162);
    }

    @Test
    public void makePlaysWithFullStrikesInNewGame_ShouldReturnAPlayerScoreWithPlays() {
        final List<String> plays = Arrays.asList("10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10",
                "10");
        Map<String, PlayerScore> newGame = this.bowlingGameService.newGame();
        final String name = "Steve";

        PlayerScore result = this.bowlingGameService.makePlays(newGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames().length, 10);
        assertEquals(result.getFrames()[result.getFrames().length - 1].getScore(), 300);
    }

    @Test
    public void makePlaysWithFullFaultsInNewGame_ShouldReturnAPlayerScoreWithPlays() {
        final List<String> plays = Arrays.asList("F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F",
                "F", "F", "F", "F", "F", "F");
        Map<String, PlayerScore> newGame = this.bowlingGameService.newGame();
        final String name = "Steve";

        PlayerScore result = this.bowlingGameService.makePlays(newGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames().length, 10);
        assertEquals(result.getFrames()[result.getFrames().length - 1].getScore(), 0);
    }

    @Test
    public void makePlaysWithFullZeroInNewGame_ShouldReturnAPlayerScoreWithPlays() {
        final List<String> plays = Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0");
        Map<String, PlayerScore> newGame = this.bowlingGameService.newGame();
        final String name = "Steve";

        PlayerScore result = this.bowlingGameService.makePlays(newGame, name, plays);

        assertNotNull(result);
        assertEquals(result.getName(), name);
        assertEquals(result.getFrames().length, 10);
        assertEquals(result.getFrames()[result.getFrames().length - 1].getScore(), 0);
    }

    @Test
    public void makePlaysWithNoData_ShouldReturnAPlayerScoreWithPlays() {
        final List<String> plays = Arrays.asList();
        Map<String, PlayerScore> newGame = this.bowlingGameService.newGame();
        final String name = "Steve";

        exceptionRule.expect(IndexOutOfBoundsException.class);
        this.bowlingGameService.makePlays(newGame, name, plays);
    }

    @Test
    public void getPlayerResultsNormalInput_shouldReturnTheStringTable() {
        final PlayerScore player = new PlayerScore();
        player.setName("Steve");
        player.setFrames(new Frame[] { new Frame(17, Arrays.asList("8", "/")), new Frame(30, Arrays.asList("7", "/")),
                new Frame(37, Arrays.asList("3", "4")), new Frame(57, Arrays.asList("X")),
                new Frame(77, Arrays.asList("2", "/")), new Frame(105, Arrays.asList("X")),
                new Frame(123, Arrays.asList("X")), new Frame(131, Arrays.asList("8", "0")),
                new Frame(151, Arrays.asList("X")), new Frame(170, Arrays.asList("8", "/", "9")), });

        final String table = String.join("", "Frame\t\t1\t\t2\t\t3\t\t4\t\t5\t\t6\t\t7\t\t8\t\t9\t\t10\n", "Steve\n",
                "Pinfalls\t8\t/\t7\t/\t3\t4\t\tX\t2\t/\t\tX\t\tX\t8\t0\t\tX\t8\t/\t9\n",
                "Score\t\t17\t\t30\t\t37\t\t57\t\t77\t\t105\t\t123\t\t131\t\t151\t\t170\n");
        final String result = this.bowlingGameService.getPlayerResults(player);

        System.err.println(table);
        System.err.println(result);

        assertEquals(result, table);
    }

    @Test
    public void getPlayerResultsFullFaults_shouldReturnStringTable() {
        final PlayerScore player = new PlayerScore();
        player.setName("Steve");
        player.setFrames(new Frame[] { new Frame(0, Arrays.asList("F", "F")), new Frame(0, Arrays.asList("F", "F")),
                new Frame(0, Arrays.asList("F", "F")), new Frame(0, Arrays.asList("F", "F")),
                new Frame(0, Arrays.asList("F", "F")), new Frame(0, Arrays.asList("F", "F")),
                new Frame(0, Arrays.asList("F", "F")), new Frame(0, Arrays.asList("F", "F")),
                new Frame(0, Arrays.asList("F", "F")), new Frame(0, Arrays.asList("F", "F")), });

        final String table = String.join("", "Frame\t\t1\t\t2\t\t3\t\t4\t\t5\t\t6\t\t7\t\t8\t\t9\t\t10\n", "Steve\n",
                "Pinfalls\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\tF\n",
                "Score\t\t0\t\t0\t\t0\t\t0\t\t0\t\t0\t\t0\t\t0\t\t0\t\t0\n");
        final String result = this.bowlingGameService.getPlayerResults(player);

        assertEquals(result, table);
    }

    @Test
    public void getPlayerResultsFullStrike_shouldReturnStringTable() {
        final PlayerScore player = new PlayerScore();
        player.setName("Steve");
        player.setFrames(new Frame[] { new Frame(30, Arrays.asList("X")), new Frame(60, Arrays.asList("X")),
                new Frame(90, Arrays.asList("X")), new Frame(120, Arrays.asList("X")),
                new Frame(150, Arrays.asList("X")), new Frame(180, Arrays.asList("X")),
                new Frame(210, Arrays.asList("X")), new Frame(240, Arrays.asList("X")),
                new Frame(270, Arrays.asList("X")), new Frame(300, Arrays.asList("X", "X", "X")), });

        final String table = String.join("", "Frame\t\t1\t\t2\t\t3\t\t4\t\t5\t\t6\t\t7\t\t8\t\t9\t\t10\n", "Steve\n",
                "Pinfalls\t\tX\t\tX\t\tX\t\tX\t\tX\t\tX\t\tX\t\tX\t\tX\tX\tX\tX\n",
                "Score\t\t30\t\t60\t\t90\t\t120\t\t150\t\t180\t\t210\t\t240\t\t270\t\t300\n");
        final String result = this.bowlingGameService.getPlayerResults(player);

        assertEquals(result, table);
    }

    @Test
    public void getPlayerResultsBadInput_shouldReturnException() {
        final PlayerScore player = null;
        exceptionRule.expect(NullPointerException.class);
        this.bowlingGameService.getPlayerResults(player);
    }

}