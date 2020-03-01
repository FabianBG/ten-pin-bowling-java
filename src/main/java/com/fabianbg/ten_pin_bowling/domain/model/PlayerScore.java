package com.fabianbg.ten_pin_bowling.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScore {

    private String name;

    private List<String> plays;

    private Frame[] frames;

    public PlayerScore(String name, int frames) {
        this.name = name;
        this.plays = new ArrayList<>();
        this.frames = new Frame[frames];
    }

}