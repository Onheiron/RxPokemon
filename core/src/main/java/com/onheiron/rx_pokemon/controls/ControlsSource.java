package com.onheiron.rx_pokemon.controls;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by carlo on 24/02/2018.
 */

public interface ControlsSource {
    Observable<ControlEvent> observeControls(List<Integer> controlsToObserve);

    class ControlEvent {
        public final boolean keyDown;
        public final int key;

        public ControlEvent(boolean keyDown, int key) {
            this.keyDown = keyDown;
            this.key = key;
        }
    }
}
