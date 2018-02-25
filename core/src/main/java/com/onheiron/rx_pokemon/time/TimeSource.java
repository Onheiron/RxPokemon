package com.onheiron.rx_pokemon.time;

import io.reactivex.Observable;

/**
 * Created by carlo on 24/02/2018.
 */
public interface TimeSource {

    Observable<Float> observeTime();

}
