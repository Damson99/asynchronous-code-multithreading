package com.course.asynchronouscodemultithreading.util;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopWatch;

public class LoggerUtil
{
    public static void log(String message)
    {
        System.out.println("[" + Thread.currentThread().getName() + "] - [" + message + "]");
    }
}
