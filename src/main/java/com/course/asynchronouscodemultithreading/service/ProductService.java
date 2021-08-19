package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.Product;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ProductService
{
    Product retrieveProductDetails(String productId) throws InterruptedException, ExecutionException, TimeoutException;
}
