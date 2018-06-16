package com.onheiron.rx_pokemon;

import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by carlo on 03/03/2018.
 */
@Singleton
public class RxBus {

    private final Map<Class, BehaviorRelay<Object>> channelsMap = new HashMap<Class, BehaviorRelay<Object>>();

    @Inject RxBus() {}

    public <T> void send(T item) {
        findOrCreateRelay(item.getClass()).accept(item);
    }

    public <T> Observable<T> register(final Class<T> type) {
        return findOrCreateRelay(type).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object message) throws Exception {
                return type.isAssignableFrom(message.getClass());
            }
        }).map(new Function<Object, T>() {
            @Override
            public T apply(Object message) throws Exception {
                //noinspection unchecked
                return (T) message;
            }
        });
    }

    @Nullable
    public <T> T value(Class<T> type) {
        if(channelsMap.containsKey(type)) {
            return (T) channelsMap.get(type).getValue();
        } else {
            return null;
        }
    }

    private BehaviorRelay<Object> findOrCreateRelay(Class<?> type) {
        BehaviorRelay<Object> relay = channelsMap.get(type);
        if(relay == null) {
            relay = BehaviorRelay.create();
            channelsMap.put(type, relay);
        }
        return relay;
    }

}
