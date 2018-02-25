package com.onheiron.rx_pokemon.map;

import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.render.Renderable;

import org.mini2Dx.tiled.TiledMap;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by carlo on 24/02/2018.
 */
@Singleton
public class TiledLayerRenderer extends Renderable {

    private final TiledMap tiledMap;

    @Inject
    protected TiledLayerRenderer(RenderSource renderSource, @Named("world")TiledMap tiledMap) {
        super(renderSource, new ArrayList<String>() {{
            add("ground");
            add("objects");
            add("overlay_1");
            add("overlay_2");
        }});
        this.tiledMap = tiledMap;
    }

    @Override
    protected void render(RenderSource.GraphicUpdate graphicUpdate) {
        tiledMap.draw(graphicUpdate.graphics, 0, 0, tiledMap.getLayerIndex(graphicUpdate.layer));
    }
}
