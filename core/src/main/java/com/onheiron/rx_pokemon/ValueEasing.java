package com.onheiron.rx_pokemon;

/**
 * Created by carlo on 21/02/2018.
 */

public class ValueEasing {
    private int from;
    private int to;
    private int delta;
    private float step;
    private boolean increasing;
    private float current;

    public ValueEasing(int just) {
        this(just, just, 1);
    }

    public ValueEasing(int from, int to, int steps) {
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

    public int updateAndGet() {
        if (isDone()) return to;
        current += step;
        return Math.round(current);
    }

    public void revert() {
        int currentTo = to;
        to = from;
        from = currentTo;
        delta = -delta;
        increasing = !increasing;
        current = from;
    }
}
