package com.onheiron.rx_pokemon.map;

import com.badlogic.gdx.Gdx;

import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.exception.TiledException;

import java.awt.Point;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.onheiron.rx_pokemon.movement.Position.TILE_SIZE;

/**
 * Created by carlo on 25/02/2018.
 */
@Singleton
public class MapCoordinator {

    private TiledMap currentMap;
    private String currentMapName;
    private final MapsDictionary mapsDictionary;

    @Inject
    public MapCoordinator(final MapsDictionary mapsDictionary) {
        this.mapsDictionary = mapsDictionary;
        currentMapName = "map";
        setCurrentMap();
    }

    public Point checkWarp(Point point) {
        if(showMap(getTileLink(point))) {
            return mapsDictionary.maps.get(currentMapName).entryPoint;
        } else {
            return point;
        }
    }

    private boolean showMap(String mapName) {
        if(mapsDictionary.maps.containsKey(mapName) &&
                !mapName.equals(currentMapName)) {
            currentMapName = mapName;
            setCurrentMap();
            return true;
        }
        return false;
    }

    private String getTileLink(Point point) {
        int tileX = Math.round(((float) point.getX() / TILE_SIZE));
        int tileY = Math.round(((float) point.getY() / TILE_SIZE));
        int doorsLayerIndex = currentMap.getLayerIndex("doors");
        if(currentMap.getTile(tileX, tileY, doorsLayerIndex) != null &&
                currentMap.getTile(tileX, tileY, doorsLayerIndex).containsProperty("toMap")) {
            return currentMap.getTile(tileX, tileY, doorsLayerIndex).getProperty("toMap");
        } else {
            return currentMapName;
        }
    }

    private void setCurrentMap() {
        try {
            currentMap = new TiledMap(Gdx.files.internal(mapsDictionary.maps.get(currentMapName).mapPath));
        } catch (TiledException e) {
            e.printStackTrace();
            currentMap = new TiledMap();
        }
    }

    public TiledMap getCurrentMap() {
        return currentMap;
    }
}
