package com.onheiron.rx_pokemon.camera;

import com.onheiron.rx_pokemon.RxBus;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by carlo on 24/02/2018.
 */
@Singleton
public class PlayerCamera extends Camera {

    @Inject
    PlayerCamera(RxBus bus) {
        super(bus);
    }
}
