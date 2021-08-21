package com.course.asynchronouscodemultithreading.parallelstreams;

import com.course.asynchronouscodemultithreading.util.CommonUtil;
import com.course.asynchronouscodemultithreading.util.LoggerUtil;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.*;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;


//ArrayList jest najwydajniejszy dla ParallelStreams, jest indexowany, będzie posortowany
//LinkedList nie jest wydajny, będzie posortowany
//Set jest wydajny, nieposortowany

public class ParallelStreamResultOrder
{
    public static void main(String[] args)
    {
        startTimer();
        List<Integer> inputList = List.of(1,2,3,4,5,6,7,8);
        log("InputList: " + inputList);
        List<Integer> resultsList = listOrder(inputList);
        log("ResultList " + resultsList);
        stopTimer();

        resetTimer();

        startTimer();
        Set<Integer> inputSet = Set.of(1,2,3,4,5,6,7,8);
        log("InputSet: " + inputSet);
        Set<Integer> resultsSet = setOrder(inputSet);
        log("ResultSet " + resultsSet);
        stopTimer();
    }

    public static List<Integer> listOrder(List<Integer> inputList)
    {
        return inputList
                .parallelStream()
                .map(integer -> integer * 999999999)
                .collect(Collectors.toList());
    }

    public static Set<Integer> setOrder(Set<Integer> inputSet)
    {
        return inputSet
                .parallelStream()
                .map(integer -> integer * 999999999)
                .collect(Collectors.toSet());
    }
}
