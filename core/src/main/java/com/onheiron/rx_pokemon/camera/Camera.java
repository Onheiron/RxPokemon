package com.onheiron.rx_pokemon.camera;

import com.badlogic.gdx.graphics.Color;
import com.onheiron.rx_pokemon.RxBus;
import com.onheiron.rx_pokemon.ValueEasing;
import com.onheiron.rx_pokemon.messages.PlayerPositionEvent;
import com.onheiron.rx_pokemon.messages.RenderLayerEvent;
import com.onheiron.rx_pokemon.render.Renderable;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by carlo on 24/02/2018.
 */

public class Camera extends Renderable {

    private ValueEasing x = new ValueEasing(0);
    private ValueEasing y = new ValueEasing(0);
    private ValueEasing zoom = new ValueEasing(1);
    private BehaviorSubject<TransactionUpdate> transactionSubject =
            BehaviorSubject.createDefault(new TransactionUpdate(TransactionUpdate.Stage.END));

    Camera(RxBus bus) {
        super(bus, new ArrayList<String>() {{ add("camera"); }});
        bus.register(PlayerPositionEvent.class)
                .subscribe(new Consumer<PlayerPositionEvent>() {
                    @Override
                    public void accept(PlayerPositionEvent playerPositionEvent) throws Exception {
                        x = new ValueEasing(Math.round(playerPositionEvent.position.getX()));
                        y = new ValueEasing(Math.round(playerPositionEvent.position.getY()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected void render(RenderLayerEvent renderLayerEvent) {
        renderLayerEvent.graphics.setBackgroundColor(Color.BLUE);
        renderLayerEvent.graphics.scale(zoom.updateAndGet(), zoom.updateAndGet());
        TransactionUpdate transactionUpdate = transactionSubject.getValue();
        if (zoom.isDone() && x.isDone() && y.isDone()) {
            switch (transactionUpdate.stage) {
                case START:
                    transactionSubject.onNext(new TransactionUpdate(TransactionUpdate.Stage.MIDDLE));
                    break;
                case MIDDLE:
                    transactionSubject.onNext(new TransactionUpdate(TransactionUpdate.Stage.END));
                    break;
            }

        }
        renderLayerEvent.graphics.translate(x.updateAndGet() - (renderLayerEvent.graphics.getViewportWidth()/2),
                y.updateAndGet() - (renderLayerEvent.graphics.getViewportHeight()/2));
    }

    public static class TransactionUpdate {

        public final Stage stage;

        public TransactionUpdate(Stage stage) {
            this.stage = stage;
        }

        public enum Stage {
            START,
            MIDDLE,
            END
        }
    }
}
