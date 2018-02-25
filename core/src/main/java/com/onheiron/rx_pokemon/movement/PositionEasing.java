package com.onheiron.rx_pokemon.movement;

/**
 * Created by carlo on 21/02/2018.
 */

public class PositionEasing {
    MovementEasing xEasing;
    MovementEasing yEasing;
    private final Position position;
    private Position.Direction direction = Position.Direction.DOWN;

    public PositionEasing(Position position) {
        this.position = position;
        xEasing = new MovementEasing(position.getX());
        yEasing = new MovementEasing(position.getY());
    }

    public boolean move(MovementMode requestedMovementMode, Position.Direction requestedDirection) {
        if(isMoving()) return false;
        direction = requestedDirection;
        int fromX = position.getX();
        int fromY = position.getY();
        if(requestedMovementMode != MovementMode.IDLE && position.move(requestedDirection)) {
            xEasing = new MovementEasing(fromX, position.getX(), (int) (32.f / requestedMovementMode.speed));
            yEasing = new MovementEasing(fromY, position.getY(), (int) (32.f / requestedMovementMode.speed));
            return true;
        }
        return false;
    }

    public int getX() {
        return xEasing.get();
    }

    public int getY() {
        return yEasing.get();
    }

    public int moveAndGetX() {
        return xEasing.moveAndGet();
    }

    public int moveAndGetY() {
        return yEasing.moveAndGet();
    }

    public Position.Direction getDirection() {
        return direction;
    }

    public int getFacingTileId() {
        return position.getTileIdInDirection(getDirection());
    }

    public boolean isMoving() {
        return !xEasing.isDone() || !yEasing.isDone();
    }
}
