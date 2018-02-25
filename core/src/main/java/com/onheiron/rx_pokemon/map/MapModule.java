package com.onheiron.rx_pokemon.map;

import com.badlogic.gdx.Gdx;

import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.exception.TiledException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by carlo on 24/02/2018.
 */
@Module
public class MapModule {

    @Provides
    @Singleton
    @Named("world")
    TiledMap providesTiledMap() {
        try {
            return new TiledMap(Gdx.files.internal("map.tmx"));
        } catch (TiledException e) {
            e.printStackTrace();
            return new TiledMap();
        }
    }

    @Provides
    @Singleton
    @Named("walkable")
    TileLayer providesWalkableLayer(@Named("world") TiledMap map) {
        return map.getTileLayer("walkable");
    }
}
