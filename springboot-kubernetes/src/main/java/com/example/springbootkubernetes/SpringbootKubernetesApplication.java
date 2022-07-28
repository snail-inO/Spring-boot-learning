package com.example.springbootkubernetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class SpringbootKubernetesApplication {

    private WebClient webClient = WebClient.create();

    public static void main(String[] args) {
        SpringApplication.run(SpringbootKubernetesApplication.class, args);
    }

    @GetMapping
    public Mono<String> index() {
        return webClient.get().uri("http://k8s-workshop-name-service")
                .retrieve()
                .toEntity(String.class)
                .map(entity -> {
                    String host = entity.getHeaders().get("k8s-host").get(0);
                    return "Hello " + entity.getBody() + " from " + host;
                });

    }
}
