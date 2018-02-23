package com.onheiron.rx_pokemon;

import org.mini2Dx.tiled.TileLayer;

import java.util.Map;

/**
 * Created by carlo on 21/02/2018.
 */

public class Player extends Character {

    private final PlayerControls controls = new PlayerControls();

    public Player(TileLayer movementLayer, Map<MovementMode, String> movementAssetsPaths, int x, int y) {
        super(movementLayer, movementAssetsPaths, 0, x, y);
    }

    @Override
    public void update(float delta) {
        move(controls.getRequestedMovementMode(), controls.getRequestedDirection());
        if(controls.gerRequestedInteraction()) {
            switch (movementHandler.getFacingTileId()) {
                case 3:
                    System.out.println("Hi dude!");
                    break;
            }
        }
    }

    public void keyPressed(int keyCode) {
        controls.keyPressed(keyCode);
    }

    public void keyReleased(int keyCode) {
        controls.keyReleased(keyCode);
    }
}
