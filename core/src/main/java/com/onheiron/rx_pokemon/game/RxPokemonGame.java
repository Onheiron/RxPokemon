package com.onheiron.rx_pokemon.game;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.camera.PlayerCamera;
import com.onheiron.rx_pokemon.controls.PlayerControls;
import com.onheiron.rx_pokemon.map.MapCoordinator;
import com.onheiron.rx_pokemon.map.TiledLayerRenderer;
import com.onheiron.rx_pokemon.messages.KeyEvent;
import com.onheiron.rx_pokemon.messages.RenderEvent;
import com.onheiron.rx_pokemon.messages.UpdateEvent;
import com.onheiron.rx_pokemon.player.Player;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import javax.inject.Inject;

public class RxPokemonGame extends BasicGame {

    public static final String GAME_IDENTIFIER = "com.onheiron.rx_pokemon";

    @Inject RxBus bus;

    @Inject Player player;
    @Inject PlayerCamera playerCamera;
    @Inject TiledLayerRenderer tiledLayerRenderer;
    @Inject MapCoordinator mapCoordinator;
    @Inject PlayerControls playerControls;

    @Override
    public void initialise() {
        GameComponent gameComponent = DaggerGameComponent.builder()
                .gameModule(new GameModule(this))
                .build();
        gameComponent.inject(this);
    }

    @Override
    public void update(float delta) {
        bus.send(new UpdateEvent(delta));
    }

    @Override
    public void interpolate(float alpha) { }

    @Override
    public boolean keyUp(int keycode) {
        bus.send(new KeyEvent(keycode, false));
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        bus.send(new KeyEvent(keycode, true));
        return true;
    }

    @Override
    public void render(Graphics g) {
        bus.send(new RenderEvent(g));
    }
}
