package com.fabianbg.ten_pin_bowling.domain.service;

import java.util.Map;

import com.fabianbg.ten_pin_bowling.domain.model.PlayerScore;

public interface IBowlingGame {

    public Map<String, PlayerScore> newGame();

    public PlayerScore makePlay(String player, String score);

    public String getPLayerResults(PlayerScore player);
}