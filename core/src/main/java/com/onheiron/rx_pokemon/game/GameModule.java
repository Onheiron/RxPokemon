package com.onheiron.rx_pokemon.game;

import dagger.Module;

/**
 * Created by carlo on 24/02/2018.
 */
@Module
public class GameModule {

    private final RxPokemonGame rxPokemonGame;

    public GameModule(RxPokemonGame rxPokemonGame) {
        this.rxPokemonGame = rxPokemonGame;
    }

}
