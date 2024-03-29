package com.course.asynchronouscodemultithreading.parallelstreams;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.*;

//nie uzywa sie streamow gdy wykonujesz operacje logiczne i do owrappowywania (new Object()), inaczej boxing i unboxing

public class ParallelStreamPerformance
{
    public int sumUsingIntStream(int count, boolean isParallel)
    {
        startTimer();
        IntStream intStream = IntStream.rangeClosed(0, count);
        if(isParallel)
            intStream.parallel();

        int sum = intStream
                .sum();
        stopTimer();
        resetTimer();
        return sum;
    }

    public int sumUsingList(List<Integer> inputList, boolean isParallel)
    {
        startTimer();
        Stream<Integer> inputStream = inputList.stream();
        if(isParallel)
            inputStream.parallel();

        int sum = inputStream
                .mapToInt(Integer::intValue) //unboxing
                .sum();
        stopTimer();
        resetTimer();
        return sum;
    }

    public int sumUsingIterate(int n, boolean isParallel)
    {
        startTimer();
        Stream<Integer> integerStream = Stream.iterate(0, i -> i + 1);
        if(isParallel)
            integerStream.parallel();

        int sum = integerStream
                .limit(n + 1)
                .reduce(0, Integer::sum);
        stopTimer();
        resetTimer();
        return sum;
    }













}
