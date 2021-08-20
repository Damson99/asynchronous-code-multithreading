package com.course.asynchronouscodemultithreading.parallelstreams;

import com.course.asynchronouscodemultithreading.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListSpliteratorExampleTest
{
    ArrayListSpliteratorExample arrayListSpliteratorExample = new ArrayListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyValue()
    {
        int size = 1_000_000;
        ArrayList<Integer> inputList = DataSet.generateIntegerArrayList(size);
        List<Integer> resultList = arrayListSpliteratorExample.multiplyValue(inputList, 2, false);

        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void multiplyValueParallel()
    {
        int size = 1_000_000;
        ArrayList<Integer> inputList = DataSet.generateIntegerArrayList(size);
        List<Integer> resultList = arrayListSpliteratorExample.multiplyValue(inputList, 2, true);

        assertEquals(size, resultList.size());
    }
}