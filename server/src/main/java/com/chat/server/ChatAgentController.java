package com.chat.server;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class ChatAgentController {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    @GetMapping("/initiate/{username}")
    public ResponseEntity<String> initate(@PathVariable String username) {
        String message = username + " connected.";
        sink.tryEmitNext(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping(value = "/listen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> updateListeners() {
        return sink.asFlux();
    }
}
