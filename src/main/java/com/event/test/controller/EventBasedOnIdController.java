package com.event.test.controller;

import com.event.test.service.EventBasedOnIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/id")
@Slf4j
public class EventBasedOnIdController {

    private final EventBasedOnIdService eventBasedOnIdService;
    Map<String, SseEmitter> emitters = new HashMap<>();


    //Subscribe the resource
    @CrossOrigin
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam("userId") String userId) {
        return eventBasedOnIdService.subscribe(userId);
    }

    @PostMapping("/dispatchEvent")
    public void dispatchEventToClients(@RequestParam String title, @RequestParam String text,
                                       @RequestParam String userId) {
        eventBasedOnIdService.sendEvent(title, text, userId);
    }


}