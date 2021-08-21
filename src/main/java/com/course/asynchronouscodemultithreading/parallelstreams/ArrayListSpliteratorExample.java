package com.course.asynchronouscodemultithreading.parallelstreams;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.*;

public class ArrayListSpliteratorExample
{
    public List<Integer> multiplyValue(ArrayList<Integer> inputList, int multiplyValue, boolean isParallel)
    {
        startTimer();

        Stream<Integer> integerStream = inputList.stream();
        if(isParallel)
            integerStream.parallel();

        List<Integer> resultList = integerStream
                .map(integer -> integer * multiplyValue)
                .collect(Collectors.toList());
        stopTimer();
        resetTimer();
        return resultList;
    }
}
