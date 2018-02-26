package com.onheiron.rx_pokemon.movement;

import com.onheiron.rx_pokemon.ValueEasing;

import java.awt.Point;

/**
 * Created by carlo on 21/02/2018.
 */

public class PositionEasing {
    ValueEasing xEasing;
    ValueEasing yEasing;
    private final Position position;
    private Position.Direction direction = Position.Direction.DOWN;

    public PositionEasing(Position position) {
        this.position = position;
        xEasing = new ValueEasing(position.getX());
        yEasing = new ValueEasing(position.getY());
    }

    public boolean move(MovementMode requestedMovementMode, Position.Direction requestedDirection) {
        if(isMoving()) return false;
        direction = requestedDirection;
        int fromX = position.getX();
        int fromY = position.getY();
        if(requestedMovementMode != MovementMode.IDLE && position.move(requestedDirection)) {
            xEasing = new ValueEasing(fromX, position.getX(), (int) (32.f / requestedMovementMode.speed));
            yEasing = new ValueEasing(fromY, position.getY(), (int) (32.f / requestedMovementMode.speed));
            return true;
        }
        return false;
    }

    public void warp(Point point) {
        position.warp(point);
        xEasing = new ValueEasing(position.getX());
        yEasing = new ValueEasing(position.getY());
    }

    public int getX() {
        return xEasing.get();
    }

    public int getY() {
        return yEasing.get();
    }

    public int moveAndGetX() {
        return xEasing.updateAndGet();
    }

    public int moveAndGetY() {
        return yEasing.updateAndGet();
    }

    public Position.Direction getDirection() {
        return direction;
    }

    public Position.WalkableType getFacingTileType() {
        return position.getTileTypeInDirection(getDirection());
    }

    public boolean isMoving() {
        return !xEasing.isDone() || !yEasing.isDone();
    }
}
