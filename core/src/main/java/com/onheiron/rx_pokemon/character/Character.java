package com.onheiron.rx_pokemon.character;

import com.onheiron.rx_pokemon.camera.Focus;
import com.onheiron.rx_pokemon.map.MapCoordinator;
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
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;

import static com.onheiron.rx_pokemon.movement.Position.TILE_SIZE;

/**
 * Created by carlo on 21/02/2018.
 */

public abstract class Character extends Renderable implements Focus {

    protected final MovementHandler movementHandler;
    protected final MapCoordinator mapCoordinator;

    private final BehaviorSubject<Point> positionSubject = BehaviorSubject.create();

    public Character(MapCoordinator mapCoordinator, Map<MovementMode, String> movementAssetsPaths,
                     RenderSource renderSource, TimeSource timeSource, int x, int y) {
        super(renderSource, new ArrayList<String>() {{ add("characters"); }});
        this.mapCoordinator = mapCoordinator;
        movementHandler = new MovementHandler(mapCoordinator, movementAssetsPaths,
                Position.WalkableType.character, x, y);
        positionSubject.onNext(new Point(Math.round(movementHandler.getX()),
                Math.round(movementHandler.getY())));
        timeSource.observeTime()
                .subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(Float delta) throws Exception {
                        update(delta);
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

    public void move(MovementMode requestedMovementMode, Position.Direction requestedDirection) {
        movementHandler.move(requestedMovementMode, requestedDirection);
        positionSubject.onNext(new Point(Math.round(movementHandler.getX()),
                Math.round(movementHandler.getY())));
        Point currentPoint = new Point(Math.round(movementHandler.getX()), Math.round(movementHandler.getY()));
        Point newPoint = mapCoordinator.checkWarp(currentPoint);
        if(!newPoint.equals(currentPoint)) {
            warp(newPoint);
        }
    }

    private void warp(Point point) {
        movementHandler.warp(point);
        positionSubject.onNext(point);
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
