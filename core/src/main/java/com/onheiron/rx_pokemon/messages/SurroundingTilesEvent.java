package com.onheiron.rx_pokemon.messages;

import com.onheiron.rx_pokemon.movement.Position;

import java.util.Map;

/**
 * Created by carlo on 08/03/2018.
 */

public class SurroundingTilesEvent {

    public final Position.WalkableType currentTileType;
    public final Map<Position.Direction, Position.WalkableType> surroundings;

    public SurroundingTilesEvent(Position.WalkableType currentTileType, Map<Position.Direction,
            Position.WalkableType> surroundings) {
        this.currentTileType = currentTileType;
        this.surroundings = surroundings;
    }
}
