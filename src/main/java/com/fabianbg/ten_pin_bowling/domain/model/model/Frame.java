package com.fabianbg.ten_pin_bowling.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
@AllArgsConstructor
public class Frame {

    public Frame() {
        this.pinfalls = new ArrayList<>();
    }

    private Integer score;

    private List<String> pinfalls;
}