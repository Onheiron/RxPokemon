package com.onheiron.rx_pokemon.camera;

import java.awt.Point;

import io.reactivex.Observable;

/**
 * Created by carlo on 24/02/2018.
 */

public interface Focus {

    Observable<Point> observeFocusPoint();

}
