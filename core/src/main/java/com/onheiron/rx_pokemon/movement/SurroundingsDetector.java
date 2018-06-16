package com.onheiron.rx_pokemon.movement;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.FacingCharacterEvent;
import com.onheiron.rx_pokemon.messages.PlayerPositionEvent;
import com.onheiron.rx_pokemon.messages.SurroundingTilesEvent;
import com.onheiron.rx_pokemon.messages.TiledMapEvent;

import org.mini2Dx.tiled.Tile;
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

    private final Map<Position.Direction, Position.WalkableType> sorroundingTilesTypes = new HashMap<Position.Direction, Position.WalkableType>();
    private final RxBus bus;

    @Inject
    public SurroundingsDetector(final RxBus bus) {
        this.bus = bus;
        Observable.combineLatest(bus.register(PlayerPositionEvent.class).filter(new Predicate<PlayerPositionEvent>() {
                    @Override
                    public boolean test(PlayerPositionEvent playerPositionEvent) throws Exception {
                        return !playerPositionEvent.moving;
                    }
                }), bus.register(TiledMapEvent.class),
                new BiFunction<PlayerPositionEvent, TiledMapEvent, SurroundingTilesEvent>() {
                    @Override
                    public SurroundingTilesEvent apply(PlayerPositionEvent playerPositionEvent, TiledMapEvent tiledMapEvent) throws Exception {
                        detectWalkableSurroundings(playerPositionEvent.position.x, playerPositionEvent.position.y);
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

    private void detectWalkableSurroundings(float x, float y) {
        sorroundingTilesTypes.put(Position.Direction.LEFT, getTileType(x - TILE_SIZE, y));
        sorroundingTilesTypes.put(Position.Direction.RIGHT, getTileType(x + TILE_SIZE, y));
        sorroundingTilesTypes.put(Position.Direction.UP, getTileType(x, y - TILE_SIZE));
        sorroundingTilesTypes.put(Position.Direction.DOWN, getTileType(x, y + TILE_SIZE));
        detectCharacterSurroundings(x, y);
    }

    private void detectCharacterSurroundings(float x, float y) {
        sorroundingTilesTypes.put(Position.Direction.LEFT, getChatacter(Position.Direction.LEFT, x - TILE_SIZE, y));
        sorroundingTilesTypes.put(Position.Direction.RIGHT, getChatacter(Position.Direction.RIGHT, x + TILE_SIZE, y));
        sorroundingTilesTypes.put(Position.Direction.UP, getChatacter(Position.Direction.UP, x, y - TILE_SIZE));
        sorroundingTilesTypes.put(Position.Direction.DOWN, getChatacter(Position.Direction.DOWN, x, y + TILE_SIZE));
    }

    private Position.WalkableType getChatacter(Position.Direction direction, float x, float y) {
        Position.WalkableType baseWalkableType = sorroundingTilesTypes.get(direction);
        TiledMap tiledMap = bus.value(TiledMapEvent.class).tiledMap;
        int tileX = Math.round((x / TILE_SIZE));
        int tileY = Math.round((y / TILE_SIZE));
        int charactersLayerIndex = tiledMap.getLayerIndex("characters");
        Tile charTile = tiledMap.getTile(tileX, tileY, charactersLayerIndex);
        if(charTile != null) {
            bus.send(new FacingCharacterEvent(charTile.getProperty("id"), charTile.getProperty("line")));
            return Position.WalkableType.character;
        } else {
            return baseWalkableType;
        }
    }

    private Position.WalkableType getTileType(float x, float y) {
        TiledMap tiledMap = bus.value(TiledMapEvent.class).tiledMap;
        int tileX = Math.round((x / TILE_SIZE));
        int tileY = Math.round((y / TILE_SIZE));
        int walkableLayerIndex = tiledMap.getLayerIndex("walkable");
        if(tiledMap.getTile(tileX, tileY, walkableLayerIndex) != null &&
                tiledMap.getTile(tileX, tileY, walkableLayerIndex)
                        .containsProperty("type")) {
            return Position.WalkableType.valueOf(tiledMap.getTile(tileX, tileY,
                    walkableLayerIndex).getProperty("type"));
        } else {
            return Position.WalkableType.none;
        }
    }
}
