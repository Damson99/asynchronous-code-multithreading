package com.course.asynchronouscodemultithreading.apiclient;


import com.course.asynchronouscodemultithreading.domain.githubjob.GitHubJob;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;
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
        List<CompletableFuture<List<GitHubJob>>> cfGitHubJob =
                pageNumbers
                        .stream()
                        .map(pageNumber -> CompletableFuture.supplyAsync(() -> invokeGitHubJobWithPageAndDescription(pageNumber, description)))
                        .collect(Collectors.toList());

        return cfGitHubJob
                .stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

//    allOf() funkcja zwraca typ Void, ale czeka az wszystkie CompletableFuture sie wykonaja w porownaniu do funkcji join,
//    ktora wyrzuci natychmiast unchecked CompletionException
//    wedlug testow najbardziej wydajne pod wzgledem czasu wykonania
    public List<GitHubJob> invokeGitHubJobWithPageAndDescriptionAsyncWithAllOf(List<Integer> pageNumbers, String description)
    {
        List<CompletableFuture<List<GitHubJob>>> cfGitHubJob =
                pageNumbers
                        .stream()
                        .map(pageNumber -> CompletableFuture.supplyAsync(() -> invokeGitHubJobWithPageAndDescription(pageNumber, description)))
                        .collect(Collectors.toList());

        CompletableFuture<Void> cfAllOf = CompletableFuture.allOf(cfGitHubJob.toArray(new CompletableFuture[cfGitHubJob.size()]));

//        w tym momencie wykorzystujemy join(), ale dzieki funkcji allOf() i tak wszystkie CompletableFuture zostaly wykonane
//        ta czesc kodu zostanie wykonana gdy allOf() sie wykona
        return cfAllOf
                .thenApply(var -> cfGitHubJob.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()))
                .join();
    }

//    anyOf() zwraca najszybciej dostarczone dane uzywajac parallelStream(), ktore sa takie same i pochodza z roznych zrodel
//    np sa 3 calle przychodzace zawierajace te same dane
//    - z bazy danych, soap i rest api - i jesli pierwszy przyjdzie z bazy to zostanie obsluzony a reszta porzucona
    public String retrieveDataFromFastestSourcesAsyncWithAnyOf()
    {
        CompletableFuture<String> dbCall = CompletableFuture.supplyAsync(() ->
        {
            log("db call");
            delay(0);
            return "db call";
        });

        CompletableFuture<String> soapCall = CompletableFuture.supplyAsync(() ->
        {
            log("soap call");
            delay(1000);
            return "soap call";
        });

        CompletableFuture<String> restapiCall = CompletableFuture.supplyAsync(() ->
        {
            log("restapi call");
            delay(1500);
            return "restapi call";
        });

        List<CompletableFuture<String>> cfList = List.of(dbCall, soapCall, restapiCall);
        CompletableFuture<Object> sourcesAnyOf = CompletableFuture.anyOf(cfList.toArray(new CompletableFuture[cfList.size()]));

        return (String) sourcesAnyOf
                .thenApply(var ->
                {
                    if(var instanceof String)
                        return var;
                    else
                        return null;
                }).join();
    }
}
