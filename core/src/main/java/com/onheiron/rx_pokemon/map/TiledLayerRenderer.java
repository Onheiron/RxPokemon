package com.onheiron.rx_pokemon.map;

import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.render.Renderable;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by carlo on 24/02/2018.
 */
@Singleton
public class TiledLayerRenderer extends Renderable {

    private final MapCoordinator mapCoordinator;

    @Inject
    protected TiledLayerRenderer(RenderSource renderSource, MapCoordinator mapCoordinator) {
        super(renderSource, new ArrayList<String>() {{
            add("ground");
            add("objects");
            add("overlay_1");
            add("overlay_2");
            add("walkable");
        }});
        this.mapCoordinator = mapCoordinator;
    }

    @Override
    protected void render(RenderSource.GraphicUpdate graphicUpdate) {
        mapCoordinator.getCurrentMap().draw(graphicUpdate.graphics, 0, 0, graphicUpdate.layer.getIndex());
    }
}
