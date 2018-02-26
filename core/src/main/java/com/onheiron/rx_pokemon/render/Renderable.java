package com.onheiron.rx_pokemon.render;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by carlo on 24/02/2018.
 */

public abstract class Renderable {

    protected final RenderSource renderSource;

    protected Renderable(RenderSource renderSource, final List<String> renderOnLayers) {
        this.renderSource = renderSource;
        renderSource.observeLayer(renderOnLayers)
                .subscribe(new Consumer<RenderSource.GraphicUpdate>() {
                    @Override
                    public void accept(RenderSource.GraphicUpdate graphicUpdate) throws Exception {
                        render(graphicUpdate);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("Error observing rendering...");
                        throwable.printStackTrace();
                    }
                });
    }

    protected abstract void render(RenderSource.GraphicUpdate graphicUpdate);
}
