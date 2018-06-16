package com.onheiron.rx_pokemon.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.MovementControlEvent;
import com.onheiron.rx_pokemon.messages.RenderLayerEvent;

import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.TextureRegion;

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
    private final PositionEasing positionEasing;
    private int stepAnimationStep = 0;

    public MovementHandler(RxBus bus, Map<MovementMode, String> movementAssetsPaths, int x, int y) {
        this.positionEasing = new PositionEasing(new Position(bus, x, y), bus);
        for (MovementMode movementMode : movementAssetsPaths.keySet()) {
            movementSprites.put(movementMode, new Sprite(new Texture(Gdx.files.internal(movementAssetsPaths.get(movementMode))),
                    CHARACHTER_PIXEL_WIDTH, CHARACHTER_PIXEL_HEIGHT));
        }
    }

    public void move(MovementControlEvent movementControlEvent) {
        movementMode = movementControlEvent.movementMode;
        if (stepStep % Math.round((float) Position.TILE_SIZE / STEP_FRAME_COUNT) == 0) {
            stepAnimationStep = (stepAnimationStep + 1) % STEP_FRAME_COUNT;
        }
        stepStep++;
        stepStep = stepStep % Math.round((float) Position.TILE_SIZE / movementMode.speed);
        positionEasing.move(movementControlEvent);
    }

    public void warp(Point point) {
        stepStep = 0;
        stepAnimationStep = 0;
        positionEasing.warp(point);
    }

    private TextureRegion getCurrentTextureRegion() {
        if (positionEasing.isMoving()) {
            Texture textureToDraw = movementSprites.get(movementMode).getTexture();
            return new TextureRegion(textureToDraw, stepAnimationStep * Position.TILE_SIZE, positionEasing.getDirection().spriteRawIndex * 48, Position.TILE_SIZE, 46);
        } else {
            Texture textureToDraw = movementSprites.get(MovementMode.WALK).getTexture();
            return new TextureRegion(textureToDraw, 0, positionEasing.getDirection().spriteRawIndex * 48, Position.TILE_SIZE, 46);
        }
    }

    public void render(RenderLayerEvent renderLayerEvent) {
        renderLayerEvent.graphics.drawTextureRegion(getCurrentTextureRegion(), positionEasing.xEasing.get(),
                positionEasing.yEasing.get() - 16);
    }
}
