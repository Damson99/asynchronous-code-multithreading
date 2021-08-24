package com.course.asynchronouscodemultithreading.completablefuture;

import com.course.asynchronouscodemultithreading.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.course.asynchronouscodemultithreading.util.LoggerUtil.log;



// UZYWAJ CompletableFuture gdy masz do wykonania blocking calls, w innym przypadku uzywaj parallelStream()
public class CompletableFutureHelloWorld
{
    private final HelloWorldService helloWorldService;

    public CompletableFutureHelloWorld(HelloWorldService helloWorldService)
    {
        this.helloWorldService = helloWorldService;
    }

//    uzywajac supplyAsync wyzwalany jest glowny watek, ktory wykona metode niezaleznie od tego
//    czy uzywana jest np funkcja Thread.sleep(), watek glowny przejmuje zasoby
    public CompletableFuture<String> helloWorld()
    {
        return CompletableFuture
              .supplyAsync(helloWorldService::helloWorld)
              .thenApply(String::toUpperCase);
    }

    public static void main(String[] args)
    {
        HelloWorldService helloWorldService = new HelloWorldService();
//    nastepnie reszta funkcji jest wykonywana po odpowiednim czasie, delay(1100) jest zeby wszystkie instrukcje sie wykonaly
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

//    CompletableFuture i ParallelStream uzywa tego samego ThreadPool, dlatego mozemy zdefiniowac wlasny
//    zeby nie doprowadzic do wycieku pamieci
    public String helloWorldThreeAsyncCalls()
    {
        Executor threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello, threadPool);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world, threadPool);
        CompletableFuture<String> sentence =  CompletableFuture.supplyAsync(() -> " Hi CompletableFuture!", threadPool);

        return hello
                .thenCombine(world, (h, w) ->
                {
                    log("thenCombine(h, w)");
                    return h.concat(w);
                })
                .thenCombine(sentence, (prev, curr) ->
                {
                    log("thenCombine(prev, curr)");
                    return prev.concat(curr);
                })
                .thenApply((result) ->
                {
                    log("thenApply(result)");
                    return result.toUpperCase();
                })
                .join();
    }

//    dzieki uzywaniu np. thenCombineAsync() jestesmy w stanie dla kazdego takiego wyrazenia lamba wygenerowac oddzielne watki
//    i jako drugi argument dajemy nasz costomowy ThreadPool
//    nie jest to wskazane, ale gdy pojawia sie problemy wydajnosciowe podczas wspodzielenia puli watkow dla CompletableFuture
//    i parallelStream mozna sprobowac uzyc takiej implementacji
    public String helloWorldThreeAsyncCallsWithAsyncPipeline()
    {
        Executor threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello, threadPool);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world, threadPool);
        CompletableFuture<String> sentence =  CompletableFuture.supplyAsync(() -> " Hi CompletableFuture!", threadPool);

        return hello
                .thenCombineAsync(world, (h, w) ->
                {
                    log("thenCombine(h, w)");
                    return h.concat(w);
                })
                .thenCombineAsync(sentence, (prev, curr) ->
                {
                    log("thenCombine(prev, curr)");
                    return prev.concat(curr);
                })
                .thenApplyAsync((result) ->
                {
                    log("thenApply(result)");
                    return result.toUpperCase();
                })
                .join();
    }

//        exceptionally nie bierze udzialu w pipe jesli wyjatek sie nie pojawi, posiada funkcje recovery
//        handle bierze udzial w pipe - trzeba napisac logike, ktora sprawdza czy Throwable e jest rowne null, posiada funkcje recovery
//        whenComplete nie posiada funkcji recovery, przechwytuje wyjatek i idzie do nastenpnej funkcji whenComplete jesli istnieje

    public String helloWorldThreeAsyncCallsWithHandle()
    {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> sentence =  CompletableFuture.supplyAsync(() -> " Hi CompletableFuture!");

        return hello
                .handle((result, e) ->
                {
                    return ifExceptionOccurred(e, "Exception: ", "hello ", result);
                })
                .thenCombine(world, String::concat)
                .handle((result, e) ->
                {
                    return ifExceptionOccurred(e, "Second Exception: ", "world!", result);
                })
                .thenCombine(sentence, String::concat)
                .thenApply(String::toUpperCase)
                .join();
    }

    public String helloWorldThreeAsyncCallsWithExceptionally()
    {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> sentence =  CompletableFuture.supplyAsync(() -> " Hi CompletableFuture!");

        return hello
                .exceptionally((e) ->
                {
                    log("Exception: ".concat(e.getMessage()));
                    return "hello ";
                })
                .thenCombine(world, String::concat)
                .exceptionally((e) ->
                {
                    log("Second Exception: ".concat(e.getMessage()));
                    return "world!";
                })
                .thenCombine(sentence, String::concat)
                .thenApply(String::toUpperCase)
                .join();
    }

    public String helloWorldThreeAsyncCallsWithWhenComplete()
    {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> sentence =  CompletableFuture.supplyAsync(() -> " Hi CompletableFuture!");

        return hello
                .whenComplete((result, e) ->
                {
                    ifExceptionOccurred(e, "Exception: ", "hello ", result);
                })
                .thenCombine(world, String::concat)
                .whenComplete((result, e) ->
                {
                    ifExceptionOccurred(e, "Exception: ", "hello ", result);
                })
                .thenCombine(sentence, String::concat)
                .thenApply(String::toUpperCase)
                .join();
    }

    private String ifExceptionOccurred(Throwable e, String exceptionMessage, String defaultResult, String result)
    {
        log("result: ".concat(result));
        if(e != null)
        {
            log(exceptionMessage.concat(e.getMessage()));
            return defaultResult;
        }
        return result;
    }

    //    thenCompose jest zalezny od wykonania poprzedniej funkcji
    public CompletableFuture<String> helloWorldThenCompose()
    {
        return CompletableFuture.supplyAsync(helloWorldService::hello)
                .thenCompose(helloWorldService::helloWorldWithCompletableFuture)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloWorldWithSize()
    {
        return CompletableFuture
                .supplyAsync(helloWorldService::helloWorld)
                .thenApply(s -> (String.valueOf(s.length()).concat(" - ").concat(s)).toUpperCase());
    }
}