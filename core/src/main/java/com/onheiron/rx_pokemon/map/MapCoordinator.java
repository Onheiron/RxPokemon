package com.onheiron.rx_pokemon.map;

import com.badlogic.gdx.Gdx;
import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.PlayerPositionEvent;
import com.onheiron.rx_pokemon.messages.RenderEvent;
import com.onheiron.rx_pokemon.messages.RenderLayerEvent;
import com.onheiron.rx_pokemon.messages.TiledMapEvent;
import com.onheiron.rx_pokemon.messages.WarpEvent;

import org.mini2Dx.core.geom.Point;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.exception.TiledException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

import static com.onheiron.rx_pokemon.movement.Position.TILE_SIZE;

/**
 * Created by carlo on 25/02/2018.
 */
@Singleton
public class MapCoordinator {

    private TiledMap currentMap;
    private String currentMapName;
    private final MapsDictionary mapsDictionary;
    private final RxBus bus;

    @Inject
    public MapCoordinator(final RxBus bus, final MapsDictionary mapsDictionary) {
        this.bus = bus;
        this.mapsDictionary = mapsDictionary;
        currentMapName = "map";
        setCurrentMap();
        bus.register(PlayerPositionEvent.class)
                .subscribe(new Consumer<PlayerPositionEvent>() {
                    @Override
                    public void accept(PlayerPositionEvent playerPositionEvent) throws Exception {
                        if(showMap(getTileLink(playerPositionEvent.position))) {
                            bus.send(new WarpEvent(playerPositionEvent.position, mapsDictionary.maps.get(currentMapName).entryPoint));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        bus.register(RenderEvent.class)
                .subscribe(new Consumer<RenderEvent>() {
                    @Override
                    public void accept(RenderEvent renderEvent) throws Exception {
                        bus.send(new RenderLayerEvent(renderEvent.graphics, currentMap.getTileLayer("doors")));
                        bus.send(new RenderLayerEvent(renderEvent.graphics, currentMap.getTileLayer("camera")));
                        bus.send(new RenderLayerEvent(renderEvent.graphics, currentMap.getTileLayer("ground")));
                        bus.send(new RenderLayerEvent(renderEvent.graphics, currentMap.getTileLayer("objects")));
                        bus.send(new RenderLayerEvent(renderEvent.graphics, currentMap.getTileLayer("characters")));
                        bus.send(new RenderLayerEvent(renderEvent.graphics, currentMap.getTileLayer("overlay_1")));
                        bus.send(new RenderLayerEvent(renderEvent.graphics, currentMap.getTileLayer("overlay_2")));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
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
        int tileX = Math.round((point.getX() / TILE_SIZE));
        int tileY = Math.round((point.getY() / TILE_SIZE));
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
            bus.send(new TiledMapEvent(currentMap));
        } catch (TiledException e) {
            e.printStackTrace();
            currentMap = new TiledMap();
        }
    }
}
