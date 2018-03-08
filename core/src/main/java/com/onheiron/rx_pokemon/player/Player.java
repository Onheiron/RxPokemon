package com.onheiron.rx_pokemon.player;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.character.Character;
import com.onheiron.rx_pokemon.messages.MovementControlEvent;
import com.onheiron.rx_pokemon.movement.MovementMode;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by carlo on 21/02/2018.
 */
@Singleton
public class Player extends Character {

    private static final int STARTING_X = 13792;
    private static final int STARTING_Y = 13696;

    @Inject
    public Player(@Named("player_boy1") Map<MovementMode, String> movementAssetsPaths, RxBus bus) {
        super(movementAssetsPaths,  bus, STARTING_X, STARTING_Y);
    }

    @Override
    public void update(float delta) {
        MovementControlEvent movementControlEvent = bus.value(MovementControlEvent.class);
        move(movementControlEvent);
        if(movementControlEvent.interaction) {
            switch (movementHandler.getFacingTileType()) {
                case character:
                    System.out.println("Hi dude!");
                    break;
            }
        }
    }
}
