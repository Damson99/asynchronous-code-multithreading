package com.course.asynchronouscodemultithreading.parallelstreams;

import com.course.asynchronouscodemultithreading.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;

public class ParallelStreamExample
{
    public List<String> stringTransform(List<String> names)
    {
        return names
                .parallelStream()
                .map(this::transformName)
                .collect(Collectors.toList());
    }

    public List<String> stringTransformAndIsParallel(List<String> names, boolean isParallel)
    {
        Stream<String> stream = names.stream();

        if(isParallel)
            stream.parallel();

        return stream
                .map(this::transformName)
                .collect(Collectors.toList());
    }

    public List<String> string_toLowerCase(List<String> names)
    {
        return names.parallelStream().map(String::toUpperCase).collect(Collectors.toList());
    }

    public static void main(String[] args) {
       List<String> names = DataSet.namesList();
       ParallelStreamExample parallelStreamExample = new ParallelStreamExample();
       startTimer();
       List<String> returnList = parallelStreamExample.stringTransform(names);
       log("returnList: " + returnList);
       stopTimer();
    }

    private String transformName(String name)
    {
        delay(500);
        return name.length() + " - " + name;
    }
}
