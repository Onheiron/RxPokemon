package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.time.TimeSource;

import org.mini2Dx.tiled.TileLayer;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by carlo on 24/02/2018.
 */

public class CharacterFactory {

    private final TileLayer movementLayer;
    private final TimeSource timeSource;
    private final RenderSource renderSource;

    @Inject
    CharacterFactory(@Named("walkable") TileLayer movementLayer,
                  TimeSource timeSource,
                  RenderSource renderSource) {
        this.movementLayer = movementLayer;
        this.timeSource = timeSource;
        this.renderSource = renderSource;
    }

    public StillCharacter getStillChatacter(Map<MovementMode, String> movementAssetsPaths,
                                            int identifier, int x, int y) {
        return new StillCharacter(movementLayer, movementAssetsPaths, renderSource,
                timeSource, identifier, x, y);
    }

}
