package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.movement.MovementMode;

import java.util.HashMap;

/**
 * Created by carlo on 11/03/2018.
 */

public class CharactersFactory {

    private final CharactersMap charactersMap = new CharactersMap(new HashMap<String, CharactersMap.CharacterModel>() {{
        put("oak", new CharactersMap.CharacterModel("Oak", new HashMap<MovementMode, String>() {{
            put(MovementMode.WALK, "trchar000.png");
            put(MovementMode.RUN, "boy_run.png");
        }}, "Hey Dude!"));
    }});

}
