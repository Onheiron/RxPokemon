package com.onheiron.rx_pokemon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.exception.TiledException;

import java.util.HashMap;

public class RxPokemonGame extends BasicGame {

    public static final String GAME_IDENTIFIER = "com.onheiron.rx_pokemon";
    private static final int STARTING_X = 13312;
    private static final int STARTING_Y = 13312;

    private TiledMap tiledMap;
    private Player player;
    private StillCharacter prof;

    @Override
    public void initialise() {
        try {
            tiledMap = new TiledMap(Gdx.files.internal("map.tmx"));
            player = new Player(tiledMap.getTileLayer("walkable"),
                    new HashMap<MovementMode, String>() {{
                        put(MovementMode.WALK, "trchar000.png");
                        put(MovementMode.RUN, "boy_run.png");
                    }}, STARTING_X, STARTING_Y);
            prof = new StillCharacter(tiledMap.getTileLayer("walkable"),
                    new HashMap<MovementMode, String>() {{
                        put(MovementMode.WALK, "phone001.png");
                        put(MovementMode.RUN, "boy_run.png");
                    }}, 3, STARTING_X + 32, STARTING_Y + 32);
        } catch (TiledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(float delta) {
        player.update(delta);
        prof.update(delta);
    }

    @Override
    public void interpolate(float alpha) {

    }

    @Override
    public boolean keyUp(int keycode) {
        player.keyReleased(keycode);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        player.keyPressed(keycode);
        return true;
    }


    @Override
    public void render(Graphics g) {
        g.setBackgroundColor(Color.RED);
        g.translate(player.getX(), player.getY());
        tiledMap.draw(g, 0, 0, 1);
        tiledMap.draw(g, 0, 0, 2);
        player.render(g);
        prof.render(g);
        tiledMap.draw(g, 0, 0, 3);
        tiledMap.draw(g, 0, 0, 4);
    }
}
