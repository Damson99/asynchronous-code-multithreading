package com.course.asynchronouscodemultithreading.completablefuture;

import com.course.asynchronouscodemultithreading.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.startTimer;
import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopTimer;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldTest
{
    @Mock
    HelloWorldService helloWorldService = mock(HelloWorldService.class);

    @InjectMocks
    CompletableFutureHelloWorld completableFutureHelloWorld;

//    jesli nie damy join to asercja sie nie wykona, join wymusza na watku glownym czekanie do konca wykonania instrukcji
    @Test
    void helloWorld()
    {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorld();
        completableFuture
                .thenAccept(s -> assertEquals("HELLO WORLD", s))
                .join();
    }

    @Test
    void helloWorldWithSize()
    {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorldWithSize();
        completableFuture
                .thenAccept(s -> assertEquals("11 - HELLO WORLD", s))
                .thenAccept((s) -> log(String.valueOf(s)))
                .join();
    }

    @Test
    void helloWorldThreeAsyncCalls()
    {
        startTimer();

        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String helloWorldAsyncCalls = completableFutureHelloWorld.helloWorldThreeAsyncCalls();
        stopTimer();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldAsyncCalls);
    }

    @Test
    void helloWorldThreeAsyncCallsWithHandleFailurePath()
    {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception"));

        startTimer();
        String helloWorldAsyncCalls = completableFutureHelloWorld.helloWorldThreeAsyncCallsWithHandle();
        stopTimer();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldAsyncCalls);
    }

    @Test
    void helloWorldThreeAsyncCallsWithHandleSuccessPath()
    {
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        startTimer();
        String helloWorldAsyncCalls = completableFutureHelloWorld.helloWorldThreeAsyncCallsWithHandle();
        stopTimer();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldAsyncCalls);
    }

    @Test
    void helloWorldThreeAsyncCallsWithExceptionallyFailurePath()
    {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception"));

        startTimer();
        String helloWorldAsyncCalls = completableFutureHelloWorld.helloWorldThreeAsyncCallsWithExceptionally();
        stopTimer();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldAsyncCalls);
    }

    @Test
    void helloWorldThreeAsyncCallsWithExceptionallySuccessPath()
    {
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        startTimer();
        String helloWorldAsyncCalls = completableFutureHelloWorld.helloWorldThreeAsyncCallsWithExceptionally();
        stopTimer();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldAsyncCalls);
    }

    @Test
    void helloWorldThreeAsyncCallsWithWhenCompleteFailurePath()
    {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception"));

        startTimer();
        String helloWorldAsyncCalls = completableFutureHelloWorld.helloWorldThreeAsyncCallsWithWhenComplete();
        stopTimer();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldAsyncCalls);
    }

    @Test
    void helloWorldThreeAsyncCallsWithWhenCompleteSuccessPath()
    {
        when(helloWorldService.world()).thenCallRealMethod();
        when(helloWorldService.hello()).thenCallRealMethod();

        startTimer();
        String helloWorldAsyncCalls = completableFutureHelloWorld.helloWorldThreeAsyncCallsWithWhenComplete();
        stopTimer();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldAsyncCalls);
    }

    @Test
    void helloWorldThenCompose()
    {
        startTimer();
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorldThenCompose();
        completableFuture
                .thenAccept(s -> assertEquals("HELLO WORLD!", s))
                .thenAccept((s) -> log(String.valueOf(s)))
                .join();
        stopTimer();
    }
}