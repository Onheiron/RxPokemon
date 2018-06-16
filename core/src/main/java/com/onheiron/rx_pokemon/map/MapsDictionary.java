package com.onheiron.rx_pokemon.map;

import org.mini2Dx.core.geom.Point;

import java.util.Map;

/**
 * Created by carlo on 25/02/2018.
 */

public class MapsDictionary {

    public final Map<String , MapModel> maps;

    public MapsDictionary(Map<String, MapModel> maps) {
        this.maps = maps;
    }

    public static class MapModel {

        public final String mapPath;
        public final String mapName;
        public final MapType mapType;
        public final Point entryPoint;

        public MapModel(String mapPath, String mapName, MapType mapType, Point entryPoint) {
            this.mapPath = mapPath;
            this.mapName = mapName;
            this.mapType = mapType;
            this.entryPoint = entryPoint;
        }

        public enum MapType {
            OUTSIDE,
            BUILDING,
            CAVE
        }
    }
}
