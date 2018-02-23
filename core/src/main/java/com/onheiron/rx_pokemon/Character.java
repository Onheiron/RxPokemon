package com.onheiron.rx_pokemon;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TileLayer;

import java.util.Map;

/**
 * Created by carlo on 21/02/2018.
 */

public abstract class Character {

    protected final MovementHandler movementHandler;
    private final int identifier;

    public Character(TileLayer movementLayer, Map<MovementMode, String> movementAssetsPaths, int identifier, int x, int y) {
        this.identifier = identifier;
        movementHandler = new MovementHandler(movementLayer, movementAssetsPaths, identifier, x, y);
    }

    public abstract void update(float delta);

    public void move(com.onheiron.rx_pokemon.MovementMode requestedMovementMode, com.onheiron.rx_pokemon.Position.Direction requestedDirection) {
        movementHandler.move(requestedMovementMode, requestedDirection);
    }

    public void render(Graphics g) {
        float x = getX();
        float y = getY();
        g.drawTextureRegion(movementHandler.getCurrentTextureRegion(),
                x + (g.getViewportWidth() / 2) - 64,
                y + (g.getViewportHeight() / 2) - 32);
    }


    public float getX() {
        return movementHandler.getX();
    }

    public float getY() {
        return movementHandler.getY();
    }
}
