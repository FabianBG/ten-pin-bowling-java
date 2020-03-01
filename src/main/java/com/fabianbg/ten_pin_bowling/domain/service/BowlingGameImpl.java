package com.fabianbg.ten_pin_bowling.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fabianbg.ten_pin_bowling.domain.model.Frame;
import com.fabianbg.ten_pin_bowling.domain.model.PlayerScore;

public class BowlingGameImpl implements IBowlingGame {

    private final int STRIKE_VALUE = 10;

    private final int NUMBER_OF_FRAMES = 10;

    private final String STRIKE_LABEL = "X";

    private final String SPARSE_LABEL = "/";

    private final String FAULT_LABEL = "F";

    public Map<String, PlayerScore> newGame() {
        return new HashMap<>();
    }

    @Override
    public PlayerScore makePlay(Map<String, PlayerScore> game, String player, String score) {
        PlayerScore playerScores = this.getPlayerScore(game, player);
        playerScores.getPlays().add(score);
        this.generatePlayerScore(playerScores);
        return playerScores;
    }

    @Override
    public PlayerScore makePlays(Map<String, PlayerScore> game, String player, List<String> plays) {
        PlayerScore playerScores = this.getPlayerScore(game, player);
        playerScores.setPlays(plays);
        this.generatePlayerScore(playerScores);
        return playerScores;
    }

    @Override
    public String getPlayerResults(PlayerScore playerScores) {
        StringBuilder frame = new StringBuilder();
        StringBuilder pinfall = new StringBuilder();
        StringBuilder score = new StringBuilder();
        frame.append("Frame");
        pinfall.append("Pinfalls");
        score.append("Score");
        Frame[] frames = playerScores.getFrames();
        for (int i = 0; i < frames.length; i++) {
            frame.append(String.format("\t\t%d", i + 1));
            final int plays = frames[i].getPinfalls().size();
            String pinScore = frames[i].getPinfalls().stream().reduce("", (String a, String b) -> {
                if (plays == 1) {
                    return String.format("%s\t\t%s", a, b);
                }
                return String.format("%s\t%s", a, b);
            });
            pinfall.append(pinScore);
            score.append(String.format("\t\t%d", frames[i].getScore()));
        }
        frame.append(String.format("\n%s", playerScores.getName()));
        frame.append(String.format("\n%s", pinfall.toString()));
        frame.append(String.format("\n%s\n", score.toString()));
        return frame.toString();
    }

    private PlayerScore getPlayerScore(Map<String, PlayerScore> game, String player) {
        if (game.containsKey(player)) {
            return game.get(player);
        }

        PlayerScore newPlayer = new PlayerScore(player, NUMBER_OF_FRAMES);
        game.put(player, newPlayer);
        return newPlayer;
    }

    private PlayerScore generatePlayerScore(PlayerScore player) {
        final Frame[] frames = player.getFrames();
        final List<String> scores = player.getPlays();

        int actualScore = 0;
        int actualPlay = 0;
        int furterScoresIndex = 0;

        for (int i = 0; i < this.NUMBER_OF_FRAMES; i++) {
            Frame actual = new Frame();
            frames[i] = actual;
            String score = scores.get(actualPlay++);
            int scoreParsed = this.parseScore(score);

            if (scoreParsed == this.STRIKE_VALUE) {
                actual.getPinfalls().add(this.STRIKE_LABEL);
                actualScore += scoreParsed;
                furterScoresIndex = actualPlay + 2;
                // Escape if no have enough plays to calculate the score
                if (scores.size() < furterScoresIndex)
                    break;
                actualScore = calculateScore(actualScore, scores.subList(actualPlay, furterScoresIndex));
            } else {
                actual.getPinfalls().add(score);
                furterScoresIndex = actualPlay + 1;

                // Escape if no have enough plays to calculate the score
                if (scores.size() < furterScoresIndex)
                    break;

                final int nextScore = parseScore(scores.get(actualPlay++));
                if (nextScore + scoreParsed == this.STRIKE_VALUE) {
                    actual.getPinfalls().add(this.SPARSE_LABEL);
                    actualScore = calculateScore(actualScore, scores.subList(actualPlay, furterScoresIndex + 1));
                } else {
                    actual.getPinfalls().add(score);
                }
                actualScore += nextScore + scoreParsed;
            }

            if (i == this.NUMBER_OF_FRAMES - 1) {
                final List<String> extraScores = scores.subList(actualPlay, furterScoresIndex).stream()
                        .map((String s) -> {
                            return parseScore(s) == this.STRIKE_VALUE ? this.STRIKE_LABEL : s;
                        }).collect(Collectors.toList());
                actual.getPinfalls().addAll(extraScores);
            }

            actual.setScore(actualScore);
        }
        player.setFrames(frames);
        return player;
    }

    private int calculateScore(int actual, List<String> scores) {
        return scores.stream().map(s -> parseScore(s)).reduce(actual, Integer::sum);
    }

    private int parseScore(String score) {
        return score.equals(this.FAULT_LABEL) ? 0 : Integer.parseInt(score);
    }

}