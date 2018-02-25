package com.onheiron.rx_pokemon.movement;

/**
 * Created by carlo on 21/02/2018.
 */

public class MovementEasing {
    private final int from;
    private final int to;
    private final int delta;
    private final float step;
    private final boolean increasing;
    private float current;

    public MovementEasing(int just) {
        this(just, just, 1);
    }

    public MovementEasing(int from, int to, int steps) {
        this.from = from;
        this.delta = to - from;
        this.to = to;
        this.step = ((float)this.delta) / steps;
        this.current = from;
        this.increasing = Integer.signum(delta) > 0;
    }

    public boolean isDone() {
        return (increasing && current >= to) || (!increasing && current <= to);
    }

    public int get() {
        return Math.round(current);
    }

    public int moveAndGet() {
        if (isDone()) return to;
        current += step;
        return Math.round(current);
    }
}
