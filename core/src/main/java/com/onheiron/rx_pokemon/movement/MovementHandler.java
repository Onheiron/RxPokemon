package com.onheiron.rx_pokemon.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.onheiron.rx_pokemon.map.MapCoordinator;

import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.tiled.TileLayer;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlo on 21/02/2018.
 */

public class MovementHandler {

    private final static int CHARACHTER_PIXEL_HEIGHT = 46;
    private final static int CHARACHTER_PIXEL_WIDTH = Position.TILE_SIZE;
    private final static int STEP_FRAME_COUNT = 4;
    private final Map<MovementMode, Sprite> movementSprites = new HashMap<MovementMode, Sprite>();
    private int stepStep = 0;
    private MovementMode movementMode = MovementMode.WALK;
    protected final PositionEasing positionEasing;
    private int stepAnimationStep = 0;

    public MovementHandler(MapCoordinator mapCoordinator, Map<MovementMode, String> movementAssetsPaths, Position.WalkableType type, int x, int y) {
        this.positionEasing = new PositionEasing(new Position(mapCoordinator, type, x, y));
        for (MovementMode movementMode : movementAssetsPaths.keySet()) {
            movementSprites.put(movementMode, new Sprite(new Texture(Gdx.files.internal(movementAssetsPaths.get(movementMode))),
                    CHARACHTER_PIXEL_WIDTH, CHARACHTER_PIXEL_HEIGHT));
        }
    }

    public void move(MovementMode requestedMovementMode, Position.Direction requestedDirection) {
        if(!positionEasing.isMoving()) {
            movementMode = requestedMovementMode;
        }
        if (stepStep % Math.round((float) Position.TILE_SIZE / STEP_FRAME_COUNT) == 0) {
            stepAnimationStep = (stepAnimationStep + 1) % STEP_FRAME_COUNT;
        }
        stepStep++;
        stepStep = stepStep % Math.round((float) Position.TILE_SIZE / movementMode.speed);
        positionEasing.move(requestedMovementMode, requestedDirection);
    }

    public void warp(Point point) {
        stepStep = 0;
        stepAnimationStep = 0;
        positionEasing.warp(point);
    }

    public TextureRegion getCurrentTextureRegion() {
        if (movementMode != MovementMode.IDLE) {
            Texture textureToDraw = movementSprites.get(movementMode).getTexture();
            return new TextureRegion(textureToDraw, stepAnimationStep * Position.TILE_SIZE, positionEasing.getDirection().spriteRawIndex * 48, Position.TILE_SIZE, 46);
        } else {
            Texture textureToDraw = movementSprites.get(MovementMode.WALK).getTexture();
            return new TextureRegion(textureToDraw, 0, positionEasing.getDirection().spriteRawIndex * 48, Position.TILE_SIZE, 46);
        }
    }

    public Position.WalkableType getFacingTileType() {
        return positionEasing.getFacingTileType();
    }

    public float getX() {
        return positionEasing.getX();
    }

    public float getY() {
        return positionEasing.getY();
    }

    public float moveAndGetX() {
        return positionEasing.moveAndGetX();
    }

    public float moveAndGetY() {
        return positionEasing.moveAndGetY();
    }
}
