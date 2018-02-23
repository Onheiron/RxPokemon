package com.onheiron.rx_pokemon;

import org.mini2Dx.tiled.TileLayer;

import java.util.Map;
import java.util.Random;

/**
 * Created by carlo on 22/02/2018.
 */

public class RoamingCharacter extends Character {

    Random random = new Random();

    public RoamingCharacter(TileLayer movementLayer,
                            Map<MovementMode, String> movementAssetsPaths, int identifier, int x, int y) {
        super(movementLayer, movementAssetsPaths, identifier, x, y);
    }

    @Override
    public void update(float delta) {
        switch (random.nextInt(4)) {
            case 0:
                move(MovementMode.WALK, Position.Direction.DOWN);
                break;
            case 1:
                move(MovementMode.WALK, Position.Direction.UP);
                break;
            case 2:
                move(MovementMode.WALK, Position.Direction.LEFT);
                break;
            case 3:
                move(MovementMode.WALK, Position.Direction.RIGHT);
                break;
        }
    }
}
