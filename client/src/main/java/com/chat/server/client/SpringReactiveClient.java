package com.chat.server.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

public class SpringReactiveClient {

    private final WebClient webClient;

    public SpringReactiveClient() {
        this.webClient = WebClient.create("http://localhost:8080");
    }

    public void consumeSSE() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Flux<String> eventStream = this.webClient.get()
                .uri("/listen")
                .retrieve()
                .bodyToFlux(String.class);

        eventStream.subscribe(
                content -> System.out.println("Received: " + content),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed")
        );

        latch.await();
    }

    public static void main(String[] args) throws InterruptedException {
        SpringReactiveClient client = new SpringReactiveClient();
        client.consumeSSE();
    }
}
