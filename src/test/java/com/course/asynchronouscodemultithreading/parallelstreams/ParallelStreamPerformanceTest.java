package com.course.asynchronouscodemultithreading.parallelstreams;

import com.course.asynchronouscodemultithreading.util.DataSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamPerformanceTest
{
    ParallelStreamPerformance parallelStreamPerformance = new ParallelStreamPerformance();
    private static final int size = 1_000_000;

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void sumUsingIntStream(boolean isParallel)
    {
        int sum = parallelStreamPerformance.sumUsingIntStream(size, isParallel);
        log("sumUsingIntStream: " + sum + ", isParallel: " + isParallel + "\n");
        assertEquals(1784293664, sum);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void sumUsingList(boolean isParallel)
    {
        ArrayList<Integer> inputList = DataSet.generateIntegerArrayList(size);
        int sum = parallelStreamPerformance.sumUsingList(inputList, isParallel);
        log("sumUsingList: " + sum + ", isParallel: " + isParallel + "\n");
        assertEquals(1784293664, sum);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void sumUsingIterate(boolean isParallel)
    {
        int sum = parallelStreamPerformance.sumUsingIterate(size, isParallel);
        log("sumUsingIterate: " + sum + ", isParallel: " + isParallel + "\n");
        assertEquals(1784293664, sum);
    }
}