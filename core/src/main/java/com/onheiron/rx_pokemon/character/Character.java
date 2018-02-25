package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.camera.Focus;
import com.onheiron.rx_pokemon.movement.MovementHandler;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.movement.Position;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.render.Renderable;
import com.onheiron.rx_pokemon.time.TimeSource;

import org.mini2Dx.tiled.TileLayer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by carlo on 21/02/2018.
 */

public abstract class Character extends Renderable implements Focus {

    protected final MovementHandler movementHandler;

    private final ReplaySubject<Point> positionSubject = ReplaySubject.create();

    public Character(TileLayer movementLayer, Map<MovementMode, String> movementAssetsPaths,
                     RenderSource renderSource, TimeSource timeSource, int identifier, int x, int y) {
        super(renderSource, new ArrayList<String>() {{ add("characters"); }});
        movementHandler = new MovementHandler(movementLayer, movementAssetsPaths, identifier, x, y);
        positionSubject.onNext(new Point(Math.round(movementHandler.getX()),
                Math.round(movementHandler.getY())));
        timeSource.observeTime()
                .subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(Float delta) throws Exception {
                        update(delta);
                    }
                });
    }

    public abstract void update(float delta);

    public void move(MovementMode requestedMovementMode, Position.Direction requestedDirection) {
        movementHandler.move(requestedMovementMode, requestedDirection);
        positionSubject.onNext(new Point(Math.round(movementHandler.getX()),
                Math.round(movementHandler.getY())));
    }

    @Override
    public Observable<Point> observeFocusPoint() {
        return positionSubject;
    }

    @Override
    public void render(RenderSource.GraphicUpdate graphicUpdate) {
        float x = movementHandler.moveAndGetX();
        float y = movementHandler.moveAndGetY();
        graphicUpdate.graphics.drawTextureRegion(movementHandler.getCurrentTextureRegion(), x, y - 16);
    }
}
