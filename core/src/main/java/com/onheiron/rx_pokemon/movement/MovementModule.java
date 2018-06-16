package com.onheiron.rx_pokemon.movement;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlo on 24/02/2018.
 */
@Module
public class MovementModule {

    @Provides
    @Singleton
    @Named("player_boy1")
    Map<MovementMode, String> providesPlayerMovementAssets() {
        return new HashMap<MovementMode, String>() {{
            put(MovementMode.WALK, "trchar000.png");
            put(MovementMode.RUN, "boy_run.png");
        }};
    }

}
