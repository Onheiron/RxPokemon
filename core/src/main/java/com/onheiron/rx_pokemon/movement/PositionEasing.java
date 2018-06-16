package com.onheiron.rx_pokemon.movement;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.ValueEasing;
import com.onheiron.rx_pokemon.messages.MovementControlEvent;
import com.onheiron.rx_pokemon.messages.PlayerPositionEvent;

import org.mini2Dx.core.geom.Point;

/**
 * Created by carlo on 21/02/2018.
 */

class PositionEasing {
    ValueEasing xEasing;
    ValueEasing yEasing;
    private final Position position;
    private final RxBus bus;
    private Position.Direction direction = Position.Direction.DOWN;

    PositionEasing(Position position, RxBus bus) {
        this.position = position;
        xEasing = new ValueEasing(position.getX());
        yEasing = new ValueEasing(position.getY());
        this.bus = bus;
    }

    void move(MovementControlEvent movementControlEvent) {
        bus.send(new PlayerPositionEvent(new Point(xEasing.updateAndGet(), yEasing.updateAndGet()), isMoving()));
        if(isMoving()) return;
        direction = movementControlEvent.direction;
        int fromX = position.getX();
        int fromY = position.getY();
        if(movementControlEvent.moving && position.move(direction)) {
            xEasing = new ValueEasing(fromX, position.getX(), (int) (32.f / movementControlEvent.movementMode.speed));
            yEasing = new ValueEasing(fromY, position.getY(), (int) (32.f / movementControlEvent.movementMode.speed));
        }
        return;
    }

    void warp(Point point) {
        position.warp(point);
        xEasing = new ValueEasing(position.getX());
        yEasing = new ValueEasing(position.getY());
    }

    Position.Direction getDirection() {
        return direction;
    }

    boolean isMoving() {
        return !xEasing.isDone() || !yEasing.isDone();
    }
}
