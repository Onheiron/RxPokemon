package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.movement.MovementMode;

import org.mini2Dx.core.graphics.Sprite;

import java.util.Map;

/**
 * Created by carlo on 21/02/2018.
 */

public class CharacterMovement {

    public final Map<MovementMode, Sprite> movementSprites;
    public final String movementLayerName;
    public final int startingX;
    public final int startingY;

    public CharacterMovement(Map<MovementMode, Sprite> movementSprites,
                             String movementLayerName, int startingX, int startingY) {
        this.movementSprites = movementSprites;
        this.movementLayerName = movementLayerName;
        this.startingX = startingX;
        this.startingY = startingY;
    }
}
