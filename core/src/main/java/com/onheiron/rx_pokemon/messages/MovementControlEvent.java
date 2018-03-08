package com.onheiron.rx_pokemon.messages;

import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.movement.Position;

/**
 * Created by carlo on 04/03/2018.
 */

public class MovementControlEvent {

    public final MovementMode movementMode;
    public final Position.Direction direction;
    public final boolean moving;
    public final boolean interaction;

    public MovementControlEvent(MovementMode movementMode, Position.Direction direction,
                                boolean moving, boolean interaction) {
        this.movementMode = movementMode;
        this.direction = direction;
        this.moving = moving;
        this.interaction = interaction;
    }
}
