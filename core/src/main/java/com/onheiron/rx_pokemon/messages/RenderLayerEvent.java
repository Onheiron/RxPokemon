package com.onheiron.rx_pokemon.messages;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TileLayer;

/**
 * Created by carlo on 03/03/2018.
 */

public class RenderLayerEvent {

    public final Graphics graphics;
    public final TileLayer layer;

    public RenderLayerEvent(Graphics graphics, TileLayer layer) {
        this.graphics = graphics;
        this.layer = layer;
    }
}
