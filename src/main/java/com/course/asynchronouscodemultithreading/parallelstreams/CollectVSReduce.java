package com.course.asynchronouscodemultithreading.parallelstreams;

import com.course.asynchronouscodemultithreading.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.*;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;


//reduce przy konkatenacji String jest niewydajna, String jest immutable (niezmienny)
//i funkcja tworzy kilka Stringow podczas ich łączenia
//natomiast joining uzywa StringBuildera do laczenia Stringow
public class CollectVSReduce
{
    private static final List<Integer> integers = List.of(1,2,3,4,5,6,7,8);

    public static String reduce()
    {
        List<String> names = DataSet.namesList();
        return names
                .parallelStream()
                .reduce("", (s1, s2) -> s1 + s2);
    }

    public static String collect()
    {
        List<String> names = DataSet.namesList();
        return names
                .parallelStream()
                .collect(Collectors.joining());
    }

//    dzieli na rozmiar listy integers i do kazdego elementu dodaje identity, czyli 1, nastepnie wszystko sumuje
//    jesli uzyjesz sequential streamu dodasz do calej sumy listy tylko raz wartosc identity
    public static Integer reduceInt()
    {
        return integers
                .parallelStream()
                .reduce(1, Integer::sum);
    }

    public static void main(String[] args)
    {
        startTimer();
        log(reduce());
        stopTimer();

        resetTimer();

        startTimer();
        log(collect());
        stopTimer();

        resetTimer();

        startTimer();
        log(reduceInt().toString());
        stopTimer();
    }
}
