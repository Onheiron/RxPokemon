package com.onheiron.rx_pokemon;

/**
 * Created by carlo on 21/02/2018.
 */

public enum MovementMode {
    IDLE(0.f),
    WALK(0.5f),
    RUN(1.0f),
    BIKE(2.0f),
    SURF(1.f);
    public final float speed;

    MovementMode(float speed) {
        this.speed = speed;
    }
}
