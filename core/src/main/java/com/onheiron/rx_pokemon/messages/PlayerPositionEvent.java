package com.onheiron.rx_pokemon.messages;

import org.mini2Dx.core.geom.Point;

/**
 * Created by carlo on 03/03/2018.
 */

public class PlayerPositionEvent {

    public final Point position;

    public PlayerPositionEvent(Point position) {
        this.position = position;
    }
}
