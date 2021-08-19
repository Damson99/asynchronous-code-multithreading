package com.course.asynchronouscodemultithreading.util;

import org.apache.commons.lang3.time.StopWatch;


import static java.lang.Thread.sleep;

public class CommonUtil
{
    public static StopWatch stopWatch = new StopWatch();

    public static void delay(long delayInMilliSeconds)
    {
        try
        {
            sleep(delayInMilliSeconds);
        } catch (InterruptedException e)
        {
            LoggerUtil.log("Exception is: " + e.getMessage());
        }
    }
}
