package com.onheiron.rx_pokemon.messages;

import org.mini2Dx.core.graphics.Graphics;

/**
 * Created by carlo on 03/03/2018.
 */

public class RenderEvent {

    public final Graphics graphics;

    public RenderEvent(Graphics graphics) {
        this.graphics = graphics;
    }
}
