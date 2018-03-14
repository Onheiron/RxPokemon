package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.PlayerPositionEvent;
import com.onheiron.rx_pokemon.messages.TiledMapEvent;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.movement.Position;

import org.mini2Dx.core.geom.Point;
import org.mini2Dx.tiled.Tile;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by carlo on 10/03/2018.
 */
@Singleton
public class CharacterPlacer {

    private final RxBus bus;

    private final Map<Point, Character> charactersMap = new HashMap<Point, Character>();

    @Inject
    public CharacterPlacer(RxBus bus) {
        this.bus = bus;
        Observable.combineLatest(bus.register(PlayerPositionEvent.class).filter(new Predicate<PlayerPositionEvent>() {
                    @Override
                    public boolean test(PlayerPositionEvent playerPositionEvent) throws Exception {
                        return !playerPositionEvent.moving;
                    }
                }), bus.register(TiledMapEvent.class),
                new BiFunction<PlayerPositionEvent, TiledMapEvent, CharacterPlaceholder>() {
                    @Override
                    public CharacterPlaceholder apply(PlayerPositionEvent playerPositionEvent, TiledMapEvent tiledMapEvent) throws Exception {
                        int charactersLayerIndex = tiledMapEvent.tiledMap.getLayerIndex("characters");
                        int tileX = (int) (playerPositionEvent.position.x/Position.TILE_SIZE);
                        int tileY = (int) (playerPositionEvent.position.y/Position.TILE_SIZE);
                        for(int x = tileX - 25; x <= tileX + 25; x++) {
                            for(int y = tileY - 25; y <= tileY + 25; y++) {
                                Point currentPosition = new Point(x, y);
                                Tile characterTile = tiledMapEvent.tiledMap.getTile(x, y, charactersLayerIndex);
                                if(!charactersMap.containsKey(currentPosition) && characterTile != null) {
                                    charactersMap.put(currentPosition, chrateCharacter(characterTile, x, y));
                                }
                            }
                        }
                        return new CharacterPlaceholder("", playerPositionEvent.position);
                    }
                })
                .subscribe(new Consumer<CharacterPlaceholder>() {
                    @Override
                    public void accept(CharacterPlaceholder characterPlaceholder) throws Exception {

                    }
                });
    }

    private Character chrateCharacter(final Tile characterTile, int x, int y) {
        return new StillCharacter(new HashMap<MovementMode, String>() {{
            put(MovementMode.WALK, characterTile.getProperty("sprite") + ".png");
        }}, bus, x * Position.TILE_SIZE, y * Position.TILE_SIZE, characterTile.getProperty("line"));
    }

    public static class CharacterPlaceholder {
        public final String characterId;
        public final Point position;

        public CharacterPlaceholder(String characterId, Point position) {
            this.characterId = characterId;
            this.position = position;
        }
    }
}
