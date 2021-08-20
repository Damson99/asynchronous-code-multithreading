package com.course.asynchronouscodemultithreading.util;

import org.apache.commons.lang3.time.StopWatch;


import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;
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
            log("Exception is: " + e.getMessage());
        }
    }

    public static void startTimer()
    {
        stopWatch.start();
    }

    public static void stopTimer()
    {
        stopWatch.stop();
        log("Total time taken: " + stopWatch.getTime());
    }

    public static void resetTimer()
    {
        stopWatch.reset();
    }
}
