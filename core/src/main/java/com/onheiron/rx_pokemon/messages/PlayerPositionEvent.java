package com.onheiron.rx_pokemon.messages;

import org.mini2Dx.core.geom.Point;

/**
 * Created by carlo on 03/03/2018.
 */

public class PlayerPositionEvent {

    public final Point position;
    public final boolean moving;

    public PlayerPositionEvent(Point position, boolean moving) {
        this.position = position;
        this.moving = moving;
    }
}
