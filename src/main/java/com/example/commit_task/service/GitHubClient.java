package com.example.commit_task.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public String getCommits(String owner, String repo) {
        String sinceDate = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE);

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/{owner}/{repo}/commits")
                        .queryParam("since", sinceDate)
                        .build(owner, repo))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
