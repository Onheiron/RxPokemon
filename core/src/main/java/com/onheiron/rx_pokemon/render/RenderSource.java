package com.onheiron.rx_pokemon.render;

import org.mini2Dx.core.graphics.Graphics;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by carlo on 24/02/2018.
 */

public interface RenderSource {

    Observable<GraphicUpdate> observeLayer(List<String> layersNames);

    class GraphicUpdate {
        public final String layer;
        public final Graphics graphics;

        public GraphicUpdate(String layer, Graphics graphics) {
            this.layer = layer;
            this.graphics = graphics;
        }
    }
}
