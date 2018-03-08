package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.MovementControlEvent;
import com.onheiron.rx_pokemon.messages.PlayerPositionEvent;
import com.onheiron.rx_pokemon.messages.RenderLayerEvent;
import com.onheiron.rx_pokemon.messages.UpdateEvent;
import com.onheiron.rx_pokemon.messages.WarpEvent;
import com.onheiron.rx_pokemon.movement.MovementHandler;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.render.Renderable;

import org.mini2Dx.core.geom.Point;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by carlo on 21/02/2018.
 */

public abstract class Character extends Renderable {

    protected final MovementHandler movementHandler;

    public Character(Map<MovementMode, String> movementAssetsPaths, RxBus bus, int x, int y) {
        super(bus, new ArrayList<String>() {{ add("characters"); }});
        bus.register(WarpEvent.class)
                .subscribe(new Consumer<WarpEvent>() {
                    @Override
                    public void accept(WarpEvent warpEvent) throws Exception {
                        if(!warpEvent.from.equals(warpEvent.to)) {
                            warp(warpEvent.to);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        movementHandler = new MovementHandler(bus, movementAssetsPaths, x, y);
        bus.send(new PlayerPositionEvent(new Point(Math.round(movementHandler.getX()), Math.round(movementHandler.getY()))));
        bus.register(UpdateEvent.class)
                .subscribe(new Consumer<UpdateEvent>() {
                    @Override
                    public void accept(UpdateEvent updateEvent) throws Exception {
                        update(updateEvent.delta);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("Error observing character updates");
                        throwable.printStackTrace();
                    }
                });
    }

    public abstract void update(float delta);

    public void move(MovementControlEvent movementControlEvent) {
        movementHandler.move(movementControlEvent);
        bus.send(new PlayerPositionEvent(new Point(Math.round(movementHandler.getX()),
                Math.round(movementHandler.getY()))));
    }

    private void warp(Point point) {
        movementHandler.warp(point);
        bus.send(point);
    }

    @Override
    public void render(RenderLayerEvent renderLayerEvent) {
        float x = movementHandler.moveAndGetX();
        float y = movementHandler.moveAndGetY();
        renderLayerEvent.graphics.drawTextureRegion(movementHandler.getCurrentTextureRegion(), x, y - 16);
    }
}
