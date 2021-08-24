package com.course.asynchronouscodemultithreading.apiclient;

import com.course.asynchronouscodemultithreading.domain.githubjob.GitHubJob;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.startTimer;
import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopTimer;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GitHubJobClientTest
{
    WebClient webClient = WebClient.create("https://jobs.github.com");
    GitHubJobClient gitHubJobClient = new GitHubJobClient(webClient);

    @Test
    void invokeGitHubJobWithPageAndDescription()
    {
        int pageNumber = 1;
        String description = "Java";

        List<GitHubJob> gitHubJobs = gitHubJobClient.invokeGitHubJobWithPageAndDescription(pageNumber, description);

        assertTrue(gitHubJobs.size() > 0);
        gitHubJobs.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGitHubJobWithPageAndDescriptionSequential()
    {
        List<Integer> arrayOfIntegers = List.of(1,2,3,4,5,6,7,8,9,10);
        String description = "Java";

        startTimer();
        List<GitHubJob> gitHubJobs = gitHubJobClient
                .invokeGitHubJobWithPageAndDescriptionSequential(arrayOfIntegers, description);
        stopTimer();

        assertTrue(gitHubJobs.size() > 0);
        gitHubJobs.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGitHubJobWithPageAndDescriptionAsync()
    {
        List<Integer> arrayOfIntegers = List.of(1,2,3,4,5,6,7,8,9,10);
        String description = "Java";

        startTimer();
        List<GitHubJob> gitHubJobs = gitHubJobClient
                .invokeGitHubJobWithPageAndDescriptionAsync(arrayOfIntegers, description);
        stopTimer();

        assertTrue(gitHubJobs.size() > 0);
        gitHubJobs.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGitHubJobWithPageAndDescriptionAsyncWithAllOf()
    {
        List<Integer> arrayOfIntegers = List.of(1,2,3,4,5,6,7,8,9,10);
        String description = "Java";

        startTimer();
        List<GitHubJob> gitHubJobs = gitHubJobClient
                .invokeGitHubJobWithPageAndDescriptionAsyncWithAllOf(arrayOfIntegers, description);
        stopTimer();

        assertTrue(gitHubJobs.size() > 0);
        gitHubJobs.forEach(Assertions::assertNotNull);
    }
}