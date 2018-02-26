package com.onheiron.rx_pokemon.camera;

import com.badlogic.gdx.graphics.Color;
import com.onheiron.rx_pokemon.ValueEasing;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.render.Renderable;

import java.awt.Point;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by carlo on 24/02/2018.
 */

public class Camera extends Renderable {

    private ValueEasing x = new ValueEasing(0);
    private ValueEasing y = new ValueEasing(0);
    private ValueEasing zoom = new ValueEasing(1);
    private Disposable currentTransaction;
    private BehaviorSubject<TransactionUpdate> transactionSubject =
            BehaviorSubject.createDefault(new TransactionUpdate(TransactionUpdate.Stage.END));

    Camera(RenderSource renderSource, Focus focus) {
        super(renderSource, new ArrayList<String>() {{ add("camera"); }});
        focus.observeFocusPoint()
                .subscribe(new Consumer<Point>() {
                    @Override
                    public void accept(Point point) throws Exception {
                        x = new ValueEasing((int) Math.round(point.getX()));
                        y = new ValueEasing((int) Math.round(point.getY()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("Error observing camera position");
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected void render(RenderSource.GraphicUpdate graphicUpdate) {
        graphicUpdate.graphics.setBackgroundColor(Color.BLUE);
        graphicUpdate.graphics.scale(zoom.updateAndGet(), zoom.updateAndGet());
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
        graphicUpdate.graphics.translate(x.updateAndGet() - (graphicUpdate.graphics.getViewportWidth()/2),
                y.updateAndGet() - (graphicUpdate.graphics.getViewportHeight()/2));
    }

    public Observable<TransactionUpdate> executeTransaction(TransactionRequest transactionRequest) {
        requestTransaction(transactionRequest);
        currentTransaction.dispose();
        transactionSubject.onNext(new TransactionUpdate(TransactionUpdate.Stage.START));
        return transactionSubject;
    }

    private void requestTransaction(final TransactionRequest transactionRequest) {
        currentTransaction = transactionSubject.subscribe(new Consumer<TransactionUpdate>() {
            @Override
            public void accept(TransactionUpdate transactionUpdate) throws Exception {
                switch (transactionUpdate.stage) {
                    case START:
                        x = new ValueEasing(x.get(), x.get() + transactionRequest.translateOut.x, transactionRequest.steps);
                        y = new ValueEasing(y.get(), y.get() + transactionRequest.translateOut.y, transactionRequest.steps);
                        zoom = new ValueEasing(zoom.get(), zoom.get() + transactionRequest.zoomOut, transactionRequest.steps);
                        break;
                    case MIDDLE:
                        x = new ValueEasing(x.get(), x.get() + transactionRequest.translateIn.x, transactionRequest.steps);
                        y = new ValueEasing(y.get(), y.get() + transactionRequest.translateIn.y, transactionRequest.steps);
                        zoom = new ValueEasing(zoom.get(), zoom.get() + transactionRequest.zoomIn, transactionRequest.steps);
                        break;
                    default:
                        currentTransaction.dispose();
                }
            }
        });
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

    public static class TransactionRequest {
        public final Point translateIn;
        public final Point translateOut;
        public final int zoomIn;
        public final int zoomOut;
        public final int fadeIn;
        public final int fadeOut;
        public final int steps;

        public TransactionRequest(Point translateIn, Point translateOut, int zoomIn, int zoomOut,
                                  int fadeIn, int fadeOut, int steps) {
            this.translateIn = translateIn;
            this.translateOut = translateOut;
            this.steps = steps;
            this.zoomIn = zoomIn;
            this.zoomOut = zoomOut;
            this.fadeIn = fadeIn;
            this.fadeOut = fadeOut;
        }
    }
}
