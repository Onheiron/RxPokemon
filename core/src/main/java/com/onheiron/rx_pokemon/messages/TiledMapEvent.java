package com.onheiron.rx_pokemon.messages;

import org.mini2Dx.tiled.TiledMap;

/**
 * Created by carlo on 03/03/2018.
 */

public class TiledMapEvent {

    public final TiledMap tiledMap;

    public TiledMapEvent(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }
}
