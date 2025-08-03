package com.vtaveira.domain.event;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Slf4j
public class EventBus {

  private static EventBus instance;
  private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Consumer<?>>> listeners = new ConcurrentHashMap<>();
  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  public static synchronized EventBus getInstance() {
    if (EventBus.instance == null) {
      EventBus.instance = new EventBus();
    }
    return EventBus.instance;
  }

  public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
    log.info("subscribing to event type: [{}]", eventType.getSimpleName());
    listeners.computeIfAbsent(eventType, _ -> new CopyOnWriteArrayList<>())
        .add(listener);
  }

  public <T> void unsubscribe(Class<T> eventType, Consumer<T> listener) {
    var eventListeners = this.listeners.get(eventType);
    if (eventListeners != null) {
      eventListeners.remove(listener);
      if (eventListeners.isEmpty()) {
        this.listeners.remove(eventType);
      }
      log.info("unsubscribing from event type: [{}]", eventType.getSimpleName());
    }
  }

  public <T> void publish(T event) {
    log.info("publishing event [{}]", event.getClass().getSimpleName());
    var eventListeners = this.listeners.get(event.getClass());
    if (eventListeners != null) {
      eventListeners.forEach(listener -> executor.submit(() -> {
        try {
          log.info("invoking event listener: [{}]", listener.getClass().getSimpleName());
          @SuppressWarnings("unchecked")
          var typedListener = (Consumer<T>) listener;
          typedListener.accept(event);
        } catch (Exception e) {
          log.error("error in event listener: [{}]", e.getMessage());
        }
      }));
    }
  }
}
