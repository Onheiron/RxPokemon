package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.map.MapCoordinator;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.time.TimeSource;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by carlo on 24/02/2018.
 */

public class CharacterFactory {

    private final MapCoordinator mapCoordinator;
    private final TimeSource timeSource;
    private final RenderSource renderSource;

    @Inject
    CharacterFactory(MapCoordinator mapCoordinator,
                  TimeSource timeSource,
                  RenderSource renderSource) {
        this.mapCoordinator = mapCoordinator;
        this.timeSource = timeSource;
        this.renderSource = renderSource;
    }

    public StillCharacter getStillChatacter(Map<MovementMode, String> movementAssetsPaths, int x, int y) {
        return new StillCharacter(mapCoordinator, movementAssetsPaths, renderSource,
                timeSource, x, y);
    }

}
