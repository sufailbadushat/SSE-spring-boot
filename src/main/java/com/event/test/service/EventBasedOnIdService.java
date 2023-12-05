package com.event.test.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EventBasedOnIdService {
    Map<String, SseEmitter> emitters = new HashMap<>();
    public SseEmitter subscribe(String userId) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        sendInitEvent(sseEmitter);

        emitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> handleCompletion(userId, sseEmitter));
        sseEmitter.onTimeout(() -> handleTimeout(userId, sseEmitter));
        sseEmitter.onError((e) -> handleError(userId, sseEmitter, e));

        return sseEmitter;
    }




    public void sendEvent( String title, String text, String userId) {
        String eventFormatted = String.valueOf(new JSONObject()
                .put("title", title)
                .put("text", text));

        SseEmitter sseEmitter = emitters.get(userId);

        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("Latest news").data(eventFormatted));
            } catch (IOException e) {
                sseEmitter.complete();
                emitters.remove(userId);
            }
        }
    }


    private void sendInitEvent(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            log.error("Error sending INIT event to SSE emitter", e);
        }
    }



    private void handleCompletion(String userId, SseEmitter sseEmitter) {
        emitters.remove(userId);
    }

    private void handleTimeout(String userId, SseEmitter sseEmitter) {
        emitters.remove(userId);
    }

    private void handleError(String userId, SseEmitter sseEmitter, Throwable e) {
        emitters.remove(userId);
    }

}
