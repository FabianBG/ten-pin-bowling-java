package com.fabianbg.ten_pin_bowling.unit_test.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

}