package com.onheiron.rx_pokemon.movement;

import com.onheiron.rx_pokemon.map.MapCoordinator;

import org.mini2Dx.tiled.TileLayer;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlo on 21/02/2018.
 */

public class Position {

    public final static int TILE_SIZE = 32;

    private int x,y;
    private WalkableType currentTileOriginalType;
    private final WalkableType type;
    private final MapCoordinator mapCoordinator;
    private final Map<Direction, WalkableType> sorroundingTilesTypes = new HashMap<Direction, WalkableType>();

    public Position(MapCoordinator mapCoordinator, WalkableType type, int x, int y) {
        this.type = WalkableType.walkable;
        this.mapCoordinator = mapCoordinator;
        warp(new Point(x, y));
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
        x = point.x;
        y = point.y;
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
        int prod = tileX * tileY;
        int walkableLayerIndex = mapCoordinator.getCurrentMap().getLayerIndex("walkable");
        if(prod >= 0 && mapCoordinator.getCurrentMap().getTile(tileX, tileY,
                walkableLayerIndex) != null && mapCoordinator.getCurrentMap().getTile(tileX, tileY,
                walkableLayerIndex).containsProperty("type")) {
            return WalkableType.valueOf(mapCoordinator.getCurrentMap().getTile(tileX, tileY,
                    walkableLayerIndex).getProperty("type"));
        } else {
            return WalkableType.none;
        }
    }

    private void setTileType(int x, int y, WalkableType type) {
        int tileX = Math.round(((float) x / TILE_SIZE));
        int tileY = Math.round(((float) y / TILE_SIZE));
        int walkableLayerIndex = mapCoordinator.getCurrentMap().getLayerIndex("walkable");
        if(mapCoordinator.getCurrentMap().getTile(tileX, tileY, walkableLayerIndex) != null) {
            mapCoordinator.getCurrentMap().getTile(tileX, tileY,
                    walkableLayerIndex).setProperty("type", type.name());
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
