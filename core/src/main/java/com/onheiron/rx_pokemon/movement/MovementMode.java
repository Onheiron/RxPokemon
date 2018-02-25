package com.onheiron.rx_pokemon.movement;

/**
 * Created by carlo on 21/02/2018.
 */

public enum MovementMode {
    IDLE(0.f),
    WALK(1.f),
    RUN(2.0f),
    BIKE(4.0f),
    SURF(1.f);
    public final float speed;

    MovementMode(float speed) {
        this.speed = speed;
    }
}
