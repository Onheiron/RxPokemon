package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.movement.MovementMode;

import java.util.Map;

/**
 * Created by carlo on 11/03/2018.
 */

public class CharactersMap {

    public final Map<String, CharacterModel> characters;

    public CharactersMap(Map<String, CharacterModel> characters) {
        this.characters = characters;
    }

    public static class CharacterModel {

        public final String name;
        public final Map<MovementMode, String> movementModeMap;
        public final String speech;

        public CharacterModel(String name, Map<MovementMode, String> movementModeMap, String speech) {
            this.name = name;
            this.movementModeMap = movementModeMap;
            this.speech = speech;
        }
    }
}
