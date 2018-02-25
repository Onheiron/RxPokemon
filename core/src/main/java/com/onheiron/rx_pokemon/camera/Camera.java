package com.onheiron.rx_pokemon.camera;

import com.badlogic.gdx.graphics.Color;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.render.Renderable;

import java.awt.Point;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * Created by carlo on 24/02/2018.
 */

public class Camera extends Renderable {

    private int x, y;
    private float zoom = 1.f;

    Camera(RenderSource renderSource, Focus focus) {
        super(renderSource, new ArrayList<String>() {{ add("camera"); }});
        focus.observeFocusPoint()
                .subscribe(new Consumer<Point>() {
                    @Override
                    public void accept(Point point) throws Exception {
                        x = (int) Math.round(point.getX());
                        y = (int) Math.round(point.getY());
                    }
                });
    }

    @Override
    protected void render(RenderSource.GraphicUpdate graphicUpdate) {
        graphicUpdate.graphics.setBackgroundColor(Color.BLUE);
        graphicUpdate.graphics.scale(zoom, zoom);
        graphicUpdate.graphics.translate(x - (graphicUpdate.graphics.getViewportWidth()/2),
                y - (graphicUpdate.graphics.getViewportHeight()/2));
    }
}
