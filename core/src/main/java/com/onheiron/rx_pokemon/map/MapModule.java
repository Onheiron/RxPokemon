package com.onheiron.rx_pokemon.map;

import org.mini2Dx.core.geom.Point;

import java.util.HashMap;

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
    MapsDictionary providesMapDictionary() {
        return new MapsDictionary(new HashMap<String, MapsDictionary.MapModel>() {{
            put("map", new MapsDictionary.MapModel("map.tmx", "map", MapsDictionary.MapModel.MapType.OUTSIDE, new Point(13792, 13696)));
            put("house_1", new MapsDictionary.MapModel("house_1.tmx", "house_1", MapsDictionary.MapModel.MapType.BUILDING, new Point(128, 256)));

        }});
    }
}
