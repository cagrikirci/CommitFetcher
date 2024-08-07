package com.example.commit_task.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class GitLabClient {

    private final WebClient webClient;

    public GitLabClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://gitlab.com/api/v4").build();
    }

    public String getCommits(String projectId) {
        String sinceDate = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE);

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/projects/{projectId}/repository/commits")
                        .queryParam("since", sinceDate)
                        .build(projectId))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
