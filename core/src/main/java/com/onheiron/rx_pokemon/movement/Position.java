package com.onheiron.rx_pokemon.movement;

import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.SurroundingTilesEvent;

import org.mini2Dx.core.geom.Point;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by carlo on 21/02/2018.
 */

public class Position {

    public final static int TILE_SIZE = 32;

    private int x,y;
    private Map<Position.Direction, Position.WalkableType> sorroundingTilesTypes = new HashMap<Position.Direction, Position.WalkableType>();

    Position(RxBus bus, final int x, final int y) {
        warp(new Point(x, y));
        bus.register(SurroundingTilesEvent.class)
                .subscribe(new Consumer<SurroundingTilesEvent>() {
                    @Override
                    public void accept(SurroundingTilesEvent surroundingTilesEvent) throws Exception {
                        sorroundingTilesTypes = surroundingTilesEvent.surroundings;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    boolean move(Direction direction) {
        int movementSpan = movementSpan(sorroundingTilesTypes.get(direction), direction);
        if(movementSpan > 0) {
            switch (direction) {
                case UP:
                    y -= TILE_SIZE * movementSpan;
                    break;
                case DOWN:
                    y += TILE_SIZE * movementSpan;
                    break;
                case LEFT:
                    x -= TILE_SIZE * movementSpan;
                    break;
                case RIGHT:
                    x += TILE_SIZE * movementSpan;
                    break;
            }
        }
        return movementSpan > 0;
    }

    void warp(Point point) {
        x = (int) point.x;
        y = (int) point.y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    private int movementSpan(WalkableType walkableType, Direction direction) {
        switch (walkableType) {
            case walkable:
                return 1;
            case surfable:
                return 0;
            case jump_down:
                return direction == Direction.DOWN ? 2 : 0;
            case jump_left:
                return direction == Direction.LEFT ? 2 : 0;
            case jump_right:
                return direction == Direction.RIGHT ? 2 : 0;
            case jump_up:
                return direction == Direction.UP ? 2 : 0;
            default:
                return 0;
        }
    }

    public enum Direction {
        UP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2);
        public final int spriteRawIndex;

        Direction(int spriteRawIndex) {
            this.spriteRawIndex = spriteRawIndex;
        }
    }

    public enum WalkableType {
        none,
        walkable,
        surfable,
        character,
        jump_down,
        jump_left,
        jump_up,
        jump_right
    }
}
