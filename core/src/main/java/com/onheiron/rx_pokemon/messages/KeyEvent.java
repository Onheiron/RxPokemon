package com.onheiron.rx_pokemon.messages;

/**
 * Created by carlo on 03/03/2018.
 */

public class KeyEvent {
    public final int keyCode;
    public final boolean pressed;

    public KeyEvent(int keyCode, boolean pressed) {
        this.keyCode = keyCode;
        this.pressed = pressed;
    }
}
