package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;
import static com.course.asynchronouscodemultithreading.util.LoggerUtil.*;


public class ProductServiceImpl implements ProductService
{
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ProductReviewService productReviewService;
    private final ProductInfoService productInfoService;
    private final InventoryService inventoryService;


    public ProductServiceImpl(ProductInfoService productInfoService, ProductReviewService productReviewService, InventoryService inventoryService)
    {
        this.productInfoService = productInfoService;
        this.productReviewService = productReviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetails(String productId)
    {
        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply((productInfo ->
                {
                    productInfo.setProductOptions(updateInventory(productInfo));
                    return productInfo;
                }));

        CompletableFuture<ProductReview> cfProductReview = CompletableFuture
                .supplyAsync(() -> productReviewService.retrieveReviews(productId))
                .exceptionally((ex) ->
                {
                    log("Exception of ProductReviewService: " + ex.getMessage());
                    return ProductReview.builder()
                            .noOfReviews(0)
                            .overallRating(0.0)
                            .build();
                });

        return cfProductInfo
                .thenCombine(cfProductReview, (productInfo, productReview) -> new Product(productId, productInfo, productReview))
                .whenComplete((result, ex) ->
                        log("Product is: ".concat(String.valueOf(result)).concat( " | Exception of ProductService: ").concat(String.valueOf(ex))))
                .join(); //blocking the thread
    }

    public CompletableFuture<Product> retrieveProductDetailsWithoutClientBlocking(String productId)
    {
        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId));

        CompletableFuture<ProductReview> cfProductReview = CompletableFuture
                .supplyAsync(() -> productReviewService.retrieveReviews(productId));

        return cfProductInfo
                .thenCombine(cfProductReview, (productInfo, productReview) -> new Product(productId, productInfo, productReview));
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> cfProductOption = productInfo.getProductOptions()
                .stream()
                .map(productOption ->
                {
                    return CompletableFuture.supplyAsync(() -> inventoryService.retrieveInventory(productOption))
                            .exceptionally((ex) ->
                            {
                                log("Exception of ProductService in updateInventory: ".concat(String.valueOf(ex)));
                                return Inventory
                                        .builder()
                                        .count(1)
                                        .build();
                            })
                            .thenApply(inventory ->
                            {
                                productOption.setInventory(inventory);
                                return productOption;
                            });
                }).collect(Collectors.toList());

        return cfProductOption.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }



    @Deprecated
    public Product retrieveProductDetailsOld(String productId) throws InterruptedException, ExecutionException, TimeoutException {
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
            delay(100);
            System.out.println("Processing...");
        }

        ProductInfo productInfo = productInfoFuture.get(2, TimeUnit.SECONDS);
        ProductReview productReview = productReviewFuture.get();

        log("Total time taken: ");
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
        InventoryService inventoryService = new InventoryService();
        ProductService productService = new ProductServiceImpl(productInfoService, productReviewService, inventoryService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is: " + product);
//        unnecessary for web app
        executorService.shutdown();
    }
}






