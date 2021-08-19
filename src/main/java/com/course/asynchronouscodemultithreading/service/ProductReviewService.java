package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.ProductReview;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;

public class ProductReviewService
{
    public ProductReview retrieveReviews(String productId)
    {
        delay(1000);
        return new ProductReview(200, 4.5);
    }
}
