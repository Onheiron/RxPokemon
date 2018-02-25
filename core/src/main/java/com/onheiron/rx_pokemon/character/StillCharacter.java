package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.time.TimeSource;

import org.mini2Dx.tiled.TileLayer;

import java.util.Map;
import java.util.Random;

/**
 * Created by carlo on 22/02/2018.
 */
public class StillCharacter extends Character{

    StillCharacter(TileLayer movementLayer, Map<MovementMode, String> movementAssetsPaths,
                          RenderSource renderSource, TimeSource timeSource, int identifier, int x, int y) {
        super(movementLayer, movementAssetsPaths, renderSource, timeSource, identifier, x, y);
    }

    @Override
    public void update(float delta) {}
}
