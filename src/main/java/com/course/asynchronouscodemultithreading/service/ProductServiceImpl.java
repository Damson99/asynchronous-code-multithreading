package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.Product;
import com.course.asynchronouscodemultithreading.domain.ProductInfo;
import com.course.asynchronouscodemultithreading.domain.ProductReview;

import java.util.concurrent.*;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopWatch;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;


public class ProductServiceImpl implements ProductService
{
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private ProductInfoService productInfoService;
    private ProductReviewService productReviewService;


    public ProductServiceImpl(ProductInfoService productInfoService, ProductReviewService productReviewService)
    {
        this.productInfoService = productInfoService;
        this.productReviewService = productReviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException, ExecutionException, TimeoutException
    {
        stopWatch.start();

//        NON BLOCKING CALLS
//        ProductInfo productInfo = productInfoService.retrieveProductInfo(productId);   //blocking call
//        ProductReview productReview = productReviewService.retrieveReviews(productId); //blocking call

//        MULTITHREADING
//        ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);
//        ProductReviewRunnable productReviewRunnable = new ProductReviewRunnable(productId);
//
//        Thread productInfoThread = new Thread(productInfoRunnable);
//        Thread productReviewThread = new Thread(productReviewRunnable);
//
//        productInfoThread.start();
//        productReviewThread.start();
//
//        productInfoThread.join();
//        productReviewThread.join();
//
//        ProductInfo productInfo = productInfoRunnable.getProductInfo();
//        ProductReview productReview = productReviewRunnable.getProductReview();

//        MULTITHREADING WITH API
        Future<ProductInfo> productInfoFuture = executorService.submit(() -> productInfoService.retrieveProductInfo(productId));
        Future<ProductReview> productReviewFuture = executorService.submit(() -> productReviewService.retrieveReviews(productId));
        while(!productReviewFuture.isDone())
        {
            Thread.sleep(100);
            System.out.println("Processing...");
        }

        ProductInfo productInfo = productInfoFuture.get(2, TimeUnit.SECONDS);
        ProductReview productReview = productReviewFuture.get();

        stopWatch.stop();
        log("Total time taken: " + stopWatch.getTime());
        return new Product(productId, productInfo, productReview);
    }

    private class ProductReviewRunnable implements Runnable
    {
        private ProductReview productReview;
        private String productId;

        public ProductReviewRunnable(String productId)
        {
            this.productId = productId;
        }

        public ProductReview getProductReview()
        {
            return productReview;
        }

        @Override
        public void run()
        {
            productReview = productReviewService.retrieveReviews(productId);
        }
    }

    private class ProductInfoRunnable implements Runnable
    {
        private ProductInfo productInfo;
        private String productId;

        public ProductInfoRunnable(String productId)
        {
            this.productId = productId;
        }

        public ProductInfo getProductInfo()
        {
            return productInfo;
        }

        @Override
        public void run()
        {
            productInfo = productInfoService.retrieveProductInfo(productId);
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException
    {
        ProductInfoService productInfoService = new ProductInfoService();
        ProductReviewService productReviewService = new ProductReviewService();
        ProductService productService = new ProductServiceImpl(productInfoService, productReviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is: " + product);
//        unnecessary for web app
        executorService.shutdown();
    }
}






