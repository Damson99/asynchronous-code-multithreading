package com.course.asynchronouscodemultithreading.parallelstreams;

import com.course.asynchronouscodemultithreading.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Collectors;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamExampleTest
{

    ParallelStreamExample parallelStreamExample = new ParallelStreamExample();

    @Test
    void stringTransform()
    {
        List<String> names = DataSet.namesList();

        startTimer();
        List<String> resultList = parallelStreamExample.stringTransform(names);
        stopTimer();

        assertEquals(4, resultList.size());
        resultList.forEach(name -> assertTrue(name.contains("-")));


    }

    @ParameterizedTest
    @ValueSource(booleans =  {false, true})
    void stringTransformAndIsParallel(boolean isParallel)
    {
        List<String> names = DataSet.namesList();

        startTimer();
        List<String> resultList = parallelStreamExample.stringTransformAndIsParallel(names, isParallel);
        stopTimer();

        resetTimer();
        assertEquals(4, names.size());
        resultList.forEach(name -> assertTrue(name.contains("-")));
    }

    @Test
    void string_toLowerCase()
    {
        List<String> names = DataSet.namesList();

        startTimer();
        List<String> resultList = parallelStreamExample.string_toLowerCase(names);
        stopTimer();

        assertEquals(4, names.size());
        List<String> checkList =  names.stream().map(String::toUpperCase).collect(Collectors.toList());
        assertEquals(checkList, resultList);

    }
}