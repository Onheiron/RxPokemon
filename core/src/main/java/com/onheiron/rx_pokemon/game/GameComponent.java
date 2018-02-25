package com.onheiron.rx_pokemon.game;

import com.onheiron.rx_pokemon.map.MapModule;
import com.onheiron.rx_pokemon.movement.MovementModule;
import com.onheiron.rx_pokemon.player.PlayerModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by carlo on 24/02/2018.
 */
@Singleton
@Component(modules = {MapModule.class, GameModule.class, MovementModule.class, PlayerModule.class})
public interface GameComponent {
    void inject(RxPokemonGame rxPokemonGame);
}
