package com.course.asynchronouscodemultithreading.executor;


import com.course.asynchronouscodemultithreading.domain.Product;
import com.course.asynchronouscodemultithreading.domain.ProductInfo;
import com.course.asynchronouscodemultithreading.domain.ProductReview;
import com.course.asynchronouscodemultithreading.service.ProductInfoService;
import com.course.asynchronouscodemultithreading.service.ProductReviewService;

import java.util.concurrent.*;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopWatch;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.log;


public class ProductServiceUsingExecutor 
{

    static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private ProductInfoService productInfoService;
    private ProductReviewService productReviewService;

    public ProductServiceUsingExecutor(ProductInfoService productInfoService, ProductReviewService productReviewService)
    {
        this.productInfoService = productInfoService;
        this.productReviewService = productReviewService;
    }

    public Product retrieveProductDetails(String productId) throws ExecutionException, InterruptedException, TimeoutException
    {
        stopWatch.start();

        Future<ProductInfo> productInfoFuture = executorService.submit(() -> productInfoService.retrieveProductInfo(productId));
        Future<ProductReview> reviewFuture = executorService.submit(() -> productReviewService.retrieveReviews(productId));

        //ProductInfo productInfo = productInfoFuture.get();
        ProductInfo productInfo = productInfoFuture.get(2, TimeUnit.SECONDS);
        ProductReview review = reviewFuture.get();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException
    {
        ProductInfoService productInfoService = new ProductInfoService();
        ProductReviewService productReviewService = new ProductReviewService();
        ProductServiceUsingExecutor productService = new ProductServiceUsingExecutor(productInfoService, productReviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);
        executorService.shutdown();

    }
}
