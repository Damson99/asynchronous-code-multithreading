package com.course.asynchronouscodemultithreading.completablefuture;

import com.course.asynchronouscodemultithreading.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.log;

public class CompletableFutureHelloWorld
{
//    uzywajac supplyAsync wyzwalany jest glowny watek, ktory wykona metode niezaleznie od tego
//    czy uzywana jest np funkcja Thread.sleep(), watek glowny przejmuje zasoby
    public static void main(String[] args) {
        HelloWorldService helloWorldService = new HelloWorldService();

//    nastepnie reszta funkcji jest wykonywana po odpowiednim czasie
//        CompletableFuture
//                .supplyAsync(helloWorldService::helloWorld)
//                .thenAccept((result) -> log("result: " + result));
//        delay(1100);

//        jest tez mozliwosc wykonania funkcji join, ktora wymusza na watku glownym czekanie
//        do konca wykonania napisanych przeze mnie instrukcji
        CompletableFuture
                .supplyAsync(helloWorldService::helloWorld)
                .thenApply(String::toUpperCase)
                .thenAccept((result) -> log("result: " + result))
                .join();
        log("Done!");
    }
}
