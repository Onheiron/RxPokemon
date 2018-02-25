package com.onheiron.rx_pokemon.movement;

import org.mini2Dx.tiled.TileLayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlo on 21/02/2018.
 */

public class Position {

    public final static int TILE_SIZE = 32;

    private final int UNACCESSIBLE_BLOCK_ID = 0;
    private final int WALKABLE_BLOCK_ID = 13505;
    private final int SURFABLE_BLOCK_ID = 13506;
    private final int UP_DOWN_BLOCK_ID = 13509;
    private final int RIGHT_LEFT_BLOCK_ID = 13510;
    private final int LEFT_RIGHT_BLOCK_ID = 13511;
    private final int DOWN_UP_BLOCK_ID = 13512;

    private int x,y;
    private int currentTileOriginalId;
    private final int identifier;
    private final TileLayer accesibleTilesLayer;
    private final Map<Direction, Integer> sorroundingTilesIds = new HashMap<Direction, Integer>();

    public Position(TileLayer accesibleTilesLayer, int identifier, int x, int y) {
        this.identifier = identifier;
        this.accesibleTilesLayer = accesibleTilesLayer;
        this.x = x;
        this.y = y;
        int tileX = Math.round(((float) x / TILE_SIZE));
        int tileY = Math.round(((float) y / TILE_SIZE));
        this.currentTileOriginalId = accesibleTilesLayer.getTileId(tileX, tileY);
        System.out.println("ID "+ currentTileOriginalId);
        detectSurroundings();
        updateTilesIds(x, y, x, y);
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
            updateTilesIds(preX, preY, x, y);
        }
        return movementSpan > 0;
    }

    public int getTileIdInDirection(Direction direction) {
        return sorroundingTilesIds.get(direction);
    }

    private void updateTilesIds(int preX, int preY, int x, int y) {
        int tilePreX = Math.round(((float) preX / TILE_SIZE));
        int tilePreY = Math.round(((float) preY / TILE_SIZE));
        int tileX = Math.round(((float) x / TILE_SIZE));
        int tileY = Math.round(((float) y / TILE_SIZE));
        accesibleTilesLayer.setTileId(tilePreX, tilePreY, currentTileOriginalId);
        currentTileOriginalId = accesibleTilesLayer.getTileId(tileX, tileY);
        accesibleTilesLayer.setTileId(tileX, tileY, identifier);
    }

    private void detectSurroundings() {
        int tileX = Math.round(((float) x / TILE_SIZE));
        int tileY = Math.round(((float) y / TILE_SIZE));
        sorroundingTilesIds.put(Direction.LEFT, accesibleTilesLayer.getTileId(tileX - 1, tileY));
        sorroundingTilesIds.put(Direction.RIGHT, accesibleTilesLayer.getTileId(tileX + 1, tileY));
        sorroundingTilesIds.put(Direction.UP, accesibleTilesLayer.getTileId(tileX, tileY - 1));
        sorroundingTilesIds.put(Direction.DOWN, accesibleTilesLayer.getTileId(tileX, tileY + 1));
    }

    private int movementSpan(Direction direction) {
        switch (sorroundingTilesIds.get(direction)) {
            case WALKABLE_BLOCK_ID:
                return 1;
            case SURFABLE_BLOCK_ID:
                return 0;
            case UP_DOWN_BLOCK_ID:
                return direction == Direction.DOWN ? 2 : 0;
            case RIGHT_LEFT_BLOCK_ID:
                return direction == Direction.LEFT ? 2 : 0;
            case LEFT_RIGHT_BLOCK_ID:
                return direction == Direction.RIGHT ? 2 : 0;
            case DOWN_UP_BLOCK_ID:
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
}
