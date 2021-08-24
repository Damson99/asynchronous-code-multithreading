package com.course.asynchronouscodemultithreading.apiclient;


import com.course.asynchronouscodemultithreading.domain.githubjob.GitHubJob;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.course.asynchronouscodemultithreading.util.LoggerUtil.log;

public class GitHubJobClient
{
    private final WebClient webClient;

    public GitHubJobClient(WebClient webClient)
    {
        this.webClient = webClient;
    }

    public List<GitHubJob> invokeGitHubJobWithPageAndDescription(int pageNumber, String description)
    {
        String uri = UriComponentsBuilder
                .fromUriString("/kategoria/komputery")
                .queryParam("description", description)
                .queryParam("page", pageNumber)
                .buildAndExpand()
                .toUriString();

        log("uri is: ".concat(uri));

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GitHubJob.class)
                .collectList()
                .block();
    }

    public List<GitHubJob> invokeGitHubJobWithPageAndDescriptionSequential(List<Integer> pageNumbers, String description)
    {
        return pageNumbers
                .stream()
                .map(pageNumber -> invokeGitHubJobWithPageAndDescription(pageNumber, description))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<GitHubJob> invokeGitHubJobWithPageAndDescriptionAsync(List<Integer> pageNumbers, String description)
    {
        List<CompletableFuture<List<GitHubJob>>> CFGitHubJob =
                pageNumbers
                        .stream()
                        .map(pageNumber -> CompletableFuture.supplyAsync(() -> invokeGitHubJobWithPageAndDescription(pageNumber, description)))
                        .collect(Collectors.toList());

        return CFGitHubJob
                .stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
