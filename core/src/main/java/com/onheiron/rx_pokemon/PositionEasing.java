package com.onheiron.rx_pokemon;

/**
 * Created by carlo on 21/02/2018.
 */

public class PositionEasing {
    com.onheiron.rx_pokemon.MovementEasing xEasing;
    com.onheiron.rx_pokemon.MovementEasing yEasing;
    private final com.onheiron.rx_pokemon.Position position;
    private com.onheiron.rx_pokemon.Position.Direction direction = com.onheiron.rx_pokemon.Position.Direction.DOWN;

    public PositionEasing(com.onheiron.rx_pokemon.Position position) {
        this.position = position;
        xEasing = new com.onheiron.rx_pokemon.MovementEasing(position.getX());
        yEasing = new com.onheiron.rx_pokemon.MovementEasing(position.getY());
    }

    public boolean move(MovementMode requestedMovementMode, com.onheiron.rx_pokemon.Position.Direction requestedDirection) {
        if(isMoving()) return false;
        direction = requestedDirection;
        int fromX = position.getX();
        int fromY = position.getY();
        if(requestedMovementMode != MovementMode.IDLE && position.move(requestedDirection)) {
            xEasing = new com.onheiron.rx_pokemon.MovementEasing(fromX, position.getX(), (int) (32.f / requestedMovementMode.speed));
            yEasing = new com.onheiron.rx_pokemon.MovementEasing(fromY, position.getY(), (int) (32.f / requestedMovementMode.speed));
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

    public com.onheiron.rx_pokemon.Position.Direction getDirection() {
        return direction;
    }

    public int getFacingTileId() {
        return position.getTileIdInDirection(getDirection());
    }

    public boolean isMoving() {
        return !xEasing.isDone() || !yEasing.isDone();
    }
}
