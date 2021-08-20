package com.course.asynchronouscodemultithreading.forkjoin;

import com.course.asynchronouscodemultithreading.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.*;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;

public class ForkJoinUsingRecursion extends RecursiveTask<List<String>>
{
    private List<String> inputList;

    public ForkJoinUsingRecursion(List<String> inputList) {
        this.inputList = inputList;
    }

    public static void main(String[] args)
    {
        startTimer();
        List<String> resultList;
        List<String> names = DataSet.namesList();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinUsingRecursion forkJoinUsingRecursion = new ForkJoinUsingRecursion(names);
        resultList = forkJoinPool.invoke(forkJoinUsingRecursion);

        stopTimer();
        log("Final result: " + resultList);
    }

    private static String transformName(String name)
    {
        delay(500);
        return name.length() + " - " + name;
    }

    @Override
    protected List<String> compute()
    {
        if(inputList.size() <= 1)
        {
            List<String> resultList = new ArrayList<>();
            inputList.forEach(name -> resultList.add(transformName(name)));
            return resultList;
        }
        int midpoint = inputList.size() / 2;
        ForkJoinTask<List<String>> leftInputSize = new ForkJoinUsingRecursion(inputList.subList(0, midpoint)).fork();
        inputList = inputList.subList(midpoint, inputList.size());
        List<String> rightResult = compute();
        List<String> leftResult = leftInputSize.join();
        leftResult.addAll(rightResult);
        return leftResult;
    }
}
