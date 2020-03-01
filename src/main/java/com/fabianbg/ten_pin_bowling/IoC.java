/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.fabianbg.ten_pin_bowling;

import com.fabianbg.ten_pin_bowling.domain.service.BowlingGameImpl;
import com.fabianbg.ten_pin_bowling.domain.service.IBowlingGame;
import com.google.inject.AbstractModule;

public class IoC extends AbstractModule {
    @Override
    protected void configure() {
        bind(IBowlingGame.class).toInstance(new BowlingGameImpl());
    }
}