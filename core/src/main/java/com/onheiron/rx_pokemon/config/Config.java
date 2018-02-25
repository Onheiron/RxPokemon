package com.onheiron.rx_pokemon.config;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by carlo on 24/02/2018.
 */
@Singleton
public class Config {

    public final int tileSize = 32;

    @Inject
    Config(){}

}
