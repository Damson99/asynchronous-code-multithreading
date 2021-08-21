package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.CartItem;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.log;

public class PriceValidatorService
{
    public boolean isCartItemInvalid(CartItem cart)
    {
        int itemId = cart.getItemId();
        log("isCartItemInvalid: " + cart.getItemId());
        delay(500);
        return itemId == 7 || itemId == 9 || itemId == 11;
    }
}
