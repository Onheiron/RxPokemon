package com.onheiron.rx_pokemon.movement;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.PlayerPositionEvent;
import com.onheiron.rx_pokemon.messages.SurroundingTilesEvent;
import com.onheiron.rx_pokemon.messages.TiledMapEvent;

import org.mini2Dx.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by carlo on 08/03/2018.
 */
@Singleton
public class SurroundingsDetector {

    public final static int TILE_SIZE = 32;

    private Position.WalkableType currentTileOriginalType = Position.WalkableType.walkable;
    private final Map<Position.Direction, Position.WalkableType> sorroundingTilesTypes = new HashMap<Position.Direction, Position.WalkableType>();
    private TiledMap currentTiledMap;
    private final Position.WalkableType type = Position.WalkableType.character;

    @Inject
    public SurroundingsDetector(final RxBus bus) {
        Observable.combineLatest(bus.register(PlayerPositionEvent.class).filter(new Predicate<PlayerPositionEvent>() {
                    @Override
                    public boolean test(PlayerPositionEvent playerPositionEvent) throws Exception {
                        return !playerPositionEvent.moving;
                    }
                }), bus.register(TiledMapEvent.class),
                new BiFunction<PlayerPositionEvent, TiledMapEvent, SurroundingTilesEvent>() {
                    @Override
                    public SurroundingTilesEvent apply(PlayerPositionEvent playerPositionEvent, TiledMapEvent tiledMapEvent) throws Exception {
                        currentTiledMap = tiledMapEvent.tiledMap;
                        detectSurroundings(playerPositionEvent.position.x, playerPositionEvent.position.y);
                        return new SurroundingTilesEvent(Position.WalkableType.walkable, sorroundingTilesTypes);
                    }
                })
                .subscribe(new Consumer<SurroundingTilesEvent>() {
                    @Override
                    public void accept(SurroundingTilesEvent surroundingTilesEvent) throws Exception {
                        bus.send(surroundingTilesEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void detectSurroundings(float x, float y) {
        sorroundingTilesTypes.put(Position.Direction.LEFT, getTileType(x - TILE_SIZE, y));
        sorroundingTilesTypes.put(Position.Direction.RIGHT, getTileType(x + TILE_SIZE, y));
        sorroundingTilesTypes.put(Position.Direction.UP, getTileType(x, y - TILE_SIZE));
        sorroundingTilesTypes.put(Position.Direction.DOWN, getTileType(x, y + TILE_SIZE));
    }

    private Position.WalkableType getTileType(float x, float y) {
        int tileX = Math.round((x / TILE_SIZE));
        int tileY = Math.round((y / TILE_SIZE));
        int walkableLayerIndex = currentTiledMap.getLayerIndex("walkable");
        if(currentTiledMap.getTile(tileX, tileY, walkableLayerIndex) != null &&
                currentTiledMap.getTile(tileX, tileY, walkableLayerIndex)
                        .containsProperty("type")) {
            return Position.WalkableType.valueOf(currentTiledMap.getTile(tileX, tileY,
                    walkableLayerIndex).getProperty("type"));
        } else {
            return Position.WalkableType.none;
        }
    }
}
