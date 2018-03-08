package com.onheiron.rx_pokemon.controls;

import com.badlogic.gdx.Input;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.messages.KeyEvent;
import com.onheiron.rx_pokemon.messages.MovementControlEvent;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.movement.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * Created by carlo on 21/02/2018.
 */
@Singleton
public class PlayerControls {

    private final BehaviorRelay<MovementMode> movementModeRelay = BehaviorRelay.createDefault(MovementMode.WALK);
    private final BehaviorRelay<Control> directionRelay = BehaviorRelay.createDefault(Control.MOVE_DOWN);
    private final BehaviorRelay<Boolean> movingRelay = BehaviorRelay.createDefault(false);
    private Disposable movingDelayDisposable;

    private final Stack<Control> directionalCommandsStack = new Stack<Control>();
    private boolean onBike, requestInteraction;
    private final Map<Integer, Control> keyMap = new HashMap<Integer, Control>() {{
        put(Input.Keys.W, Control.MOVE_UP);
        put(Input.Keys.S, Control.MOVE_DOWN);
        put(Input.Keys.A, Control.MOVE_LEFT);
        put(Input.Keys.D, Control.MOVE_RIGHT);
        put(Input.Keys.SPACE, Control.RUN);
        put(Input.Keys.E, Control.INTERACT);
    }};

    @Inject
    public PlayerControls(final RxBus bus) {
        bus.register(KeyEvent.class)
                .subscribe(new Consumer<KeyEvent>() {
                    @Override
                    public void accept(KeyEvent keyEvent) throws Exception {
                        if (keyEvent.pressed) {
                            keyPressed(keyEvent.keyCode);
                        } else {
                            keyReleased(keyEvent.keyCode);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        Observable.combineLatest(movementModeRelay, directionRelay, movingRelay, new Function3<MovementMode, Control, Boolean, MovementControlEvent>() {
            @Override
            public MovementControlEvent apply(MovementMode movementMode, Control movementControl, Boolean moving) throws Exception {
                return new MovementControlEvent(movementMode, controlToDirection(movementControl), moving, false);
            }
        }).subscribe(new Consumer<MovementControlEvent>() {
            @Override
            public void accept(MovementControlEvent movementControlEvent) throws Exception {
                bus.send(movementControlEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    private void keyPressed(int keyCode) {
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
                movementModeRelay.accept(MovementMode.RUN);
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

    private void keyReleased(int keyCode) {
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
                movementModeRelay.accept(MovementMode.WALK);
                break;
            case INTERACT:
                requestInteraction = false;
                break;
        }
    }

    private void updateDirection() {
        if(movingDelayDisposable != null && !movingDelayDisposable.isDisposed()) {
            movingDelayDisposable.dispose();
        }
        if(directionalCommandsStack.isEmpty()) {
            movingRelay.accept(false);
        } else {
            movingDelayDisposable = Completable.complete()
                    .delay(100, TimeUnit.MILLISECONDS)
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            movingRelay.accept(true);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
            directionRelay.accept(directionalCommandsStack.peek());
        }
    }

    private Position.Direction controlToDirection(Control control) {
        switch (control) {
            case MOVE_RIGHT: return Position.Direction.RIGHT;
            case MOVE_DOWN: return Position.Direction.DOWN;
            case MOVE_LEFT: return Position.Direction.LEFT;
            case MOVE_UP: return Position.Direction.UP;
            default: return null;
        }
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
