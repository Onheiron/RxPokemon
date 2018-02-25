package com.onheiron.rx_pokemon.player;

import com.onheiron.rx_pokemon.camera.Focus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlo on 24/02/2018.
 */
@Module
public class PlayerModule {

    @Provides
    @Singleton
    @Named("player")
    Focus providesPlayerFocus(Player player) {
        return player;
    }

}
