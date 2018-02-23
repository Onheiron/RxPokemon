package com.onheiron.rx_pokemon;

import org.mini2Dx.tiled.TileLayer;

import java.util.Map;
import java.util.Random;

/**
 * Created by carlo on 22/02/2018.
 */

public class StillCharacter extends Character {

    Random random = new Random();

    public StillCharacter(TileLayer movementLayer,
                          Map<MovementMode, String> movementAssetsPaths, int identifier, int x, int y) {
        super(movementLayer, movementAssetsPaths, identifier, x, y);
    }

    @Override
    public void update(float delta) {}
}
