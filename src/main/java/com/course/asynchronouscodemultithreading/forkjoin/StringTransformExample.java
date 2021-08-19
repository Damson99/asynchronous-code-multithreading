package com.course.asynchronouscodemultithreading.forkjoin;


import com.course.asynchronouscodemultithreading.util.DataSet;

import java.util.ArrayList;
import java.util.List;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;
import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopWatch;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.log;

public class StringTransformExample
{
    public static void main(String[] args)
    {
        stopWatch.start();
        List<String> resultList = new ArrayList<>();

        List<String> names = DataSet.namesList();
        names.forEach(name ->
        {
            String newValue = transformName(name);
            resultList.add(newValue);
        });
        stopWatch.stop();
        log("Final result: " + resultList);
        log("Total time taken: " + stopWatch.getTime());
    }

    private static String transformName(String name)
    {
        delay(500);
        return name.length() + " - " + name;
    }
}
