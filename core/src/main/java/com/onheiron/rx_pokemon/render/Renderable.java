package com.onheiron.rx_pokemon.render;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.RenderLayerEvent;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by carlo on 24/02/2018.
 */

public abstract class Renderable {

    protected final RxBus bus;

    protected Renderable(RxBus bus, final List<String> renderOnLayers) {
        this.bus = bus;
        bus.register(RenderLayerEvent.class)
                .filter(new Predicate<RenderLayerEvent>() {
                    @Override
                    public boolean test(RenderLayerEvent renderLayerEvent) throws Exception {
                        return renderOnLayers.contains(renderLayerEvent.layer.getName());
                    }
                })
                .subscribe(new Consumer<RenderLayerEvent>() {
                    @Override
                    public void accept(RenderLayerEvent renderLayerEvent) throws Exception {
                        render(renderLayerEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    protected abstract void render(RenderLayerEvent renderLayerEvent);
}
