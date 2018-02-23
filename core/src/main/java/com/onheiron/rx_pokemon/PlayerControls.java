package com.onheiron.rx_pokemon;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by carlo on 21/02/2018.
 */

public class PlayerControls {

    private final Stack<Control> directionalCommandsStack = new Stack<Control>();
    private Position.Direction lastDirection = Position.Direction.DOWN;
    private MovementMode movementModeCommand = MovementMode.WALK;
    private boolean onBike, requestInteraction;
    private final Map<Integer, Control> keyMap = new HashMap<Integer, Control>() {{
        put(Input.Keys.W, Control.MOVE_UP);
        put(Input.Keys.S, Control.MOVE_DOWN);
        put(Input.Keys.A, Control.MOVE_LEFT);
        put(Input.Keys.D, Control.MOVE_RIGHT);
        put(Input.Keys.SPACE, Control.RUN);
        put(Input.Keys.E, Control.INTERACT);
    }};

    public void keyPressed(int keyCode) {
        Control requestedControl = keyMap.get(keyCode);
        requestedControl = requestedControl != null ? requestedControl : Control.UNHANDLED;
        switch (requestedControl) {
            case MOVE_UP:
            case MOVE_DOWN:
            case MOVE_LEFT:
            case MOVE_RIGHT:
                requestNewMovementControl(requestedControl);
                updateDirection();
                break;
            case RUN:
                movementModeCommand = MovementMode.RUN;
                break;
            case TOGGLE_BIKE:
                onBike = !onBike;
                break;
            case INTERACT:
                requestInteraction = true;
                break;
        }
    }

    private void requestNewMovementControl(Control requestedControl) {
        if(directionalCommandsStack.isEmpty() ||
                directionalCommandsStack.peek() != requestedControl) {
            directionalCommandsStack.push(requestedControl);
        }
    }

    public void keyReleased(int keyCode) {
        Control requestedControl = keyMap.get(keyCode);
        requestedControl = requestedControl != null ? requestedControl : Control.UNHANDLED;
        switch (requestedControl) {
            case MOVE_UP:
            case MOVE_DOWN:
            case MOVE_LEFT:
            case MOVE_RIGHT:
                if(!directionalCommandsStack.isEmpty()) {
                    directionalCommandsStack.removeElement(requestedControl);
                    updateDirection();
                }
                break;
            case RUN:
                movementModeCommand = MovementMode.WALK;
                break;
            case INTERACT:
                requestInteraction = false;
                break;
        }
    }

    private void updateDirection() {
        if(directionalCommandsStack.isEmpty()) return;
        switch (directionalCommandsStack.peek()) {
            case MOVE_UP:
                lastDirection = Position.Direction.UP;
                break;
            case MOVE_DOWN:
                lastDirection = Position.Direction.DOWN;
                break;
            case MOVE_LEFT:
                lastDirection = Position.Direction.LEFT;
                break;
            case MOVE_RIGHT:
                lastDirection = Position.Direction.RIGHT;
                break;
        }
    }

    public MovementMode getRequestedMovementMode() {
        return shouldMove() ? movementModeCommand : MovementMode.IDLE;
    }

    public boolean gerRequestedInteraction() {
        return requestInteraction;
    }

    public Position.Direction getRequestedDirection() {
        return lastDirection;
    }

    private boolean shouldMove() {
        return !directionalCommandsStack.isEmpty();
    }

    public enum Control {
        UNHANDLED,
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        RUN,
        TOGGLE_BIKE,
        INTERACT
    }
}
