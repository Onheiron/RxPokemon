package com.onheiron.rx_pokemon.game;

import com.onheiron.rx_pokemon.controls.ControlsSource;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.time.TimeSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlo on 24/02/2018.
 */
@Module
public class GameModule {

    private final RxPokemonGame rxPokemonGame;

    public GameModule(RxPokemonGame rxPokemonGame) {
        this.rxPokemonGame = rxPokemonGame;
    }

    @Provides
    @Singleton
    RenderSource providesRenderSource() {
        return rxPokemonGame;
    }

    @Provides
    @Singleton
    ControlsSource providesControlsSource() {
        return rxPokemonGame;
    }

    @Provides
    @Singleton
    TimeSource providesTimeSource() {
        return rxPokemonGame;
    }

}
