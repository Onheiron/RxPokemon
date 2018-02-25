package com.onheiron.rx_pokemon.player;

import com.onheiron.rx_pokemon.character.Character;
import com.onheiron.rx_pokemon.controls.PlayerControls;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.time.TimeSource;

import org.mini2Dx.tiled.TileLayer;

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

    private final PlayerControls controls;

    @Inject
    public Player(@Named("walkable") TileLayer movementLayer,
                  @Named("player_boy1") Map<MovementMode, String> movementAssetsPaths,
                  TimeSource timeSource,
                  PlayerControls playerControls,
                  RenderSource renderSource) {
        super(movementLayer, movementAssetsPaths, renderSource, timeSource, 0, STARTING_X, STARTING_Y);
        this.controls = playerControls;
    }

    @Override
    public void update(float delta) {
        move(controls.getRequestedMovementMode(), controls.getRequestedDirection());
        if(controls.gerRequestedInteraction()) {
            switch (movementHandler.getFacingTileId()) {
                case 3:
                    System.out.println("Hi dude!");
                    break;
            }
        }
    }
}
