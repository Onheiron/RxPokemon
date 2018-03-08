package com.onheiron.rx_pokemon.messages;

import org.mini2Dx.core.geom.Point;

/**
 * Created by carlo on 03/03/2018.
 */

public class WarpEvent {

    public final Point from;
    public final Point to;

    public WarpEvent(Point from, Point to) {
        this.from = from;
        this.to = to;
    }
}
