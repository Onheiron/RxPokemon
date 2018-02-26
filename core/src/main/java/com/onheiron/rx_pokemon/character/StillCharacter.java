package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.map.MapCoordinator;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.time.TimeSource;

import org.mini2Dx.tiled.TileLayer;

import java.util.Map;

/**
 * Created by carlo on 22/02/2018.
 */
public class StillCharacter extends Character{

    StillCharacter(MapCoordinator mapCoordinator, Map<MovementMode, String> movementAssetsPaths,
                   RenderSource renderSource, TimeSource timeSource, int x, int y) {
        super(mapCoordinator, movementAssetsPaths, renderSource, timeSource, x, y);
    }

    @Override
    public void update(float delta) {}
}
