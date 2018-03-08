package com.onheiron.rx_pokemon.movement;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.TiledMapEvent;

import org.mini2Dx.core.geom.Point;
import org.mini2Dx.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by carlo on 21/02/2018.
 */

public class Position {

    public final static int TILE_SIZE = 32;

    private int x,y;
    private WalkableType currentTileOriginalType;
    private TiledMap currentTiledMap;
    private final WalkableType type;
    private final Map<Direction, WalkableType> sorroundingTilesTypes = new HashMap<Direction, WalkableType>();

    public Position(RxBus bus, final int x, final int y) {
        this.x = x;
        this.y = y;
        bus.register(TiledMapEvent.class)
                .subscribe(new Consumer<TiledMapEvent>() {
                    @Override
                    public void accept(TiledMapEvent tiledMapEvent) throws Exception {
                        currentTiledMap = tiledMapEvent.tiledMap;
                        warp(new Point(x, y));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        this.type = WalkableType.walkable;
    }

    protected boolean move(Direction direction) {
        detectSurroundings();
        int movementSpan = movementSpan(direction);
        if(movementSpan > 0) {
            int preX = x;
            int preY = y;
            switch (direction) {
                case UP:
                    y -= TILE_SIZE * movementSpan;
                    break;
                case DOWN:
                    y += TILE_SIZE * movementSpan;
                    break;
                case LEFT:
                    x -= TILE_SIZE * movementSpan;
                    break;
                case RIGHT:
                    x += TILE_SIZE * movementSpan;
                    break;
            }
            updateTilesTypes(preX, preY, x, y);
        }
        return movementSpan > 0;
    }

    public void warp(Point point) {
        x = (int) point.x;
        y = (int) point.y;
        this.currentTileOriginalType = getTileType(x, y);
        detectSurroundings();
        updateTilesTypes(x, y, x, y);
    }

    public WalkableType getTileTypeInDirection(Direction direction) {
        return sorroundingTilesTypes.get(direction);
    }

    private void updateTilesTypes(int preX, int preY, int x, int y) {
        setTileType(preX, preY, currentTileOriginalType);
        currentTileOriginalType = getTileType(x, y);
        setTileType(x, y, type);
    }

    private void detectSurroundings() {
        sorroundingTilesTypes.put(Direction.LEFT, getTileType(x - TILE_SIZE, y));
        sorroundingTilesTypes.put(Direction.RIGHT, getTileType(x + TILE_SIZE, y));
        sorroundingTilesTypes.put(Direction.UP, getTileType(x, y - TILE_SIZE));
        sorroundingTilesTypes.put(Direction.DOWN, getTileType(x, y + TILE_SIZE));
    }

    private int movementSpan(Direction direction) {
        switch (sorroundingTilesTypes.get(direction)) {
            case walkable:
                return 1;
            case surfable:
                return 0;
            case jump_down:
                return direction == Direction.DOWN ? 2 : 0;
            case jump_left:
                return direction == Direction.LEFT ? 2 : 0;
            case jump_right:
                return direction == Direction.RIGHT ? 2 : 0;
            case jump_up:
                return direction == Direction.UP ? 2 : 0;
            default:
                return 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private WalkableType getTileType(int x, int y) {
        int tileX = Math.round(((float) x / TILE_SIZE));
        int tileY = Math.round(((float) y / TILE_SIZE));
        int walkableLayerIndex = currentTiledMap.getLayerIndex("walkable");
        if(currentTiledMap.getTile(tileX, tileY, walkableLayerIndex) != null &&
                currentTiledMap.getTile(tileX, tileY, walkableLayerIndex)
                        .containsProperty("type")) {
            return WalkableType.valueOf(currentTiledMap.getTile(tileX, tileY,
                    walkableLayerIndex).getProperty("type"));
        } else {
            return WalkableType.none;
        }
    }

    private void setTileType(int x, int y, WalkableType type) {
        int tileX = Math.round(((float) x / TILE_SIZE));
        int tileY = Math.round(((float) y / TILE_SIZE));
        int walkableLayerIndex = currentTiledMap.getLayerIndex("walkable");
        if(currentTiledMap.getTile(tileX, tileY, walkableLayerIndex) != null) {
            currentTiledMap.getTile(tileX, tileY, walkableLayerIndex).setProperty("type", type.name());
        }
    }

    public enum Direction {
        UP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2);
        public final int spriteRawIndex;

        Direction(int spriteRawIndex) {
            this.spriteRawIndex = spriteRawIndex;
        }
    }

    public enum WalkableType {
        none,
        walkable,
        surfable,
        character,
        jump_down,
        jump_left,
        jump_up,
        jump_right
    }
}
