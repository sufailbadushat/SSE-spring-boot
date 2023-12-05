package com.event.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        sendInitialMessage(sseEmitter);

        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitters.remove(sseEmitter));

        emitters.add(sseEmitter);

        return sseEmitter;
    }

    public void sendMessage(String title, String text) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        String eventFormatted = String.valueOf(new JSONObject()
                .put("title", title)
                .put("text", text));

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("message").data(eventFormatted));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }


    private void sendInitialMessage(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("INIT").data("Initialization message"));
        } catch (IOException e) {
            log.error("Error sending initialization message:", e);
        }
    }

}
