package com.onheiron.rx_pokemon.messages;

/**
 * Created by carlo on 03/03/2018.
 */

public class UpdateEvent {

    public final float delta;

    public UpdateEvent(float delta) {
        this.delta = delta;
    }
}
