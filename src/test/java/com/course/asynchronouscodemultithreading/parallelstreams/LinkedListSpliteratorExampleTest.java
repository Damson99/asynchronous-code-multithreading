package com.course.asynchronouscodemultithreading.parallelstreams;

import com.course.asynchronouscodemultithreading.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListSpliteratorExampleTest
{
    LinkedListSpliteratorExample linkedListSpliteratorExample = new LinkedListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyValue()
    {
        int size = 1_000_000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);
        List<Integer> resultList = linkedListSpliteratorExample.multiplyValue(inputList, 2, false);

        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void multiplyValueParallel()
    {
        int size = 1_000_000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);
        List<Integer> resultList = linkedListSpliteratorExample.multiplyValue(inputList, 2, true);

        assertEquals(size, resultList.size());
    }
}