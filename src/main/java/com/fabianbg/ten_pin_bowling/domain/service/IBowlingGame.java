package com.fabianbg.ten_pin_bowling.domain.service;

import java.util.List;
import java.util.Map;

import com.fabianbg.ten_pin_bowling.domain.model.PlayerScore;

public interface IBowlingGame {

    public Map<String, PlayerScore> newGame();

    public PlayerScore makePlay(Map<String, PlayerScore> game, String player, String play);

    public PlayerScore makePlays(Map<String, PlayerScore> game, String player, List<String> plays);

    public String getPlayerResults(PlayerScore playerScores);
}