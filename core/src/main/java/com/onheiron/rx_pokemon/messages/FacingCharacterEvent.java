package com.onheiron.rx_pokemon.messages;

/**
 * Created by carlo on 11/03/2018.
 */

public class FacingCharacterEvent {

    public final String charId;
    public final String line;

    public FacingCharacterEvent(String charId, String line) {
        this.charId = charId;
        this.line = line;
    }
}
