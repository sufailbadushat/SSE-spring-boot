package com.event.test.controller;

import com.event.test.service.EventService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

//Subscribe the resource
    @CrossOrigin
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe() {
        return eventService.subscribe();
    }

//Send the msg to all clients
    @PostMapping("/dispatchEvent")
    public void dispatchEventToClients(@RequestParam String title, @RequestParam String text) {
        eventService.sendMessage(title, text);
    }
}
//        for (SseEmitter emitter : emitters) {
//            try {
//                emitter.send(SseEmitter.event().name("Latest news").data(eventFormatted));
//            } catch (IOException e) {
//                emitter.complete();
//                emitters.remove(emitter);
//            }
//        }

//}


//    @Scheduled(fixedRate = 1000)
//    public void dispatchEventToClients() {
//        for (SseEmitter emitter : emitters) {
//            try {
//                emitter.send(System.currentTimeMillis());
//            } catch (IOException e) {
//                emitter.complete();
//                emitters.remove(emitter);
//            }
//        }
//
//    }

