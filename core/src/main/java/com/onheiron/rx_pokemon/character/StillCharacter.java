package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.movement.MovementMode;

import java.util.Map;

/**
 * Created by carlo on 11/03/2018.
 */

public class StillCharacter extends Character {

    private final String line;

    public StillCharacter(Map<MovementMode, String> movementAssetsPaths, RxBus bus, int x, int y, String line) {
        super(movementAssetsPaths, bus, x, y);
        this.line = line;
    }

    @Override
    public void update(float delta) {
        // DO NOTHING
    }
}
