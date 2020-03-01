package com.fabianbg.ten_pin_bowling.domain.model;

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

    private Frame[] frames;
}