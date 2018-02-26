package com.onheiron.rx_pokemon.game;

import com.onheiron.rx_pokemon.camera.PlayerCamera;
import com.onheiron.rx_pokemon.character.CharacterFactory;
import com.onheiron.rx_pokemon.controls.ControlsSource;
import com.onheiron.rx_pokemon.map.MapCoordinator;
import com.onheiron.rx_pokemon.map.TiledLayerRenderer;
import com.onheiron.rx_pokemon.movement.MovementMode;
import com.onheiron.rx_pokemon.player.Player;
import com.onheiron.rx_pokemon.render.RenderSource;
import com.onheiron.rx_pokemon.time.TimeSource;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxPokemonGame extends BasicGame implements RenderSource, ControlsSource, TimeSource {

    public static final String GAME_IDENTIFIER = "com.onheiron.rx_pokemon";

    private final Subject<Float> timeSubject = PublishSubject.create();
    private final Subject<GraphicUpdate> renderSubject = PublishSubject.create();
    private final Subject<ControlsSource.ControlEvent> controlsSubject = PublishSubject.create();
    @Inject Player player;
    @Inject PlayerCamera playerCamera;
    @Inject TiledLayerRenderer tiledLayerRenderer;
    @Inject CharacterFactory characterFactory;
    @Inject MapCoordinator mapCoordinator;

    @Override
    public void initialise() {
        GameComponent gameComponent = DaggerGameComponent.builder()
                .gameModule(new GameModule(this))
                .build();
        gameComponent.inject(this);
        characterFactory.getStillChatacter(new HashMap<MovementMode, String>() {{
            put(MovementMode.WALK, "phone001.png");
            put(MovementMode.RUN, "boy_run.png");
        }}, 13696, 13696);
    }

    @Override
    public void update(float delta) {
        timeSubject.onNext(delta);
    }

    @Override
    public void interpolate(float alpha) { }

    @Override
    public boolean keyUp(int keycode) {
        controlsSubject.onNext(new ControlEvent(false, keycode));
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        controlsSubject.onNext(new ControlEvent(true, keycode));
        return true;
    }

    @Override
    public void render(Graphics g) {
        renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("doors"), g));
        renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("camera"), g));
        renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("ground"), g));
        renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("objects"), g));
        renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("characters"), g));
        renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("overlay_1"), g));
        renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("overlay_2"), g));
        //renderSubject.onNext(new GraphicUpdate(mapCoordinator.getCurrentMap().getTileLayer("walkable"), g));
    }

    @Override
    public Observable<GraphicUpdate> observeLayer(final List<String> layersNames) {
        return renderSubject.filter(new Predicate<GraphicUpdate>() {
            @Override
            public boolean test(GraphicUpdate graphicUpdate) throws Exception {
                return layersNames.contains(graphicUpdate.layer.getName());
            }
        });
    }

    @Override
    public Observable<ControlEvent> observeControls(final List<Integer> controlsToObserve) {
        return controlsSubject.filter(new Predicate<ControlEvent>() {
            @Override
            public boolean test(ControlEvent controlEvent) throws Exception {
                return controlsToObserve.contains(controlEvent.key);
            }
        });
    }

    @Override
    public Observable<Float> observeTime() {
        return timeSubject;
    }
}
