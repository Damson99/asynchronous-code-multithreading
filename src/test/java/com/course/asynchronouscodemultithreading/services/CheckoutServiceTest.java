package com.course.asynchronouscodemultithreading.services;


import com.course.asynchronouscodemultithreading.domain.Cart;
import com.course.asynchronouscodemultithreading.domain.CheckoutResponse;
import com.course.asynchronouscodemultithreading.domain.CheckoutStatus;
import com.course.asynchronouscodemultithreading.service.CheckoutService;
import com.course.asynchronouscodemultithreading.service.PriceValidatorService;
import com.course.asynchronouscodemultithreading.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutServiceTest
{
    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @Test
    void
    checkNumberOfCores()
    {
        System.out.println("Number of cores: " + Runtime.getRuntime().availableProcessors());
    }

    @Test
    void
    parallelism()
    {
        System.out.println("parallelism: " + ForkJoinPool.getCommonPoolParallelism());
    }

    @Test
    void checkout()
    {
        Cart cart = DataSet.createCart(9);

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void modifyParallelism()
    {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");
        Cart cart= DataSet.createCart(100);

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }
}















