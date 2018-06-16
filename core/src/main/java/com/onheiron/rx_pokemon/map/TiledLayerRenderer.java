package com.onheiron.rx_pokemon.map;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.RenderLayerEvent;
import com.onheiron.rx_pokemon.messages.TiledMapEvent;
import com.onheiron.rx_pokemon.render.Renderable;

import org.mini2Dx.tiled.TiledMap;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

/**
 * Created by carlo on 24/02/2018.
 */
@Singleton
public class TiledLayerRenderer extends Renderable {

    private TiledMap tiledMap;

    @Inject
    protected TiledLayerRenderer(RxBus bus) {
        super(bus, new ArrayList<String>() {{
            add("ground");
            add("objects");
            add("overlay_1");
            add("overlay_2");
            add("walkable");
        }});
        bus.register(TiledMapEvent.class)
                .subscribe(new Consumer<TiledMapEvent>() {
                    @Override
                    public void accept(TiledMapEvent tiledMapEvent) throws Exception {
                        tiledMap = tiledMapEvent.tiledMap;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected void render(RenderLayerEvent renderLayerEvent) {
        tiledMap.draw(renderLayerEvent.graphics, 0, 0, renderLayerEvent.layer.getIndex());
    }
}
