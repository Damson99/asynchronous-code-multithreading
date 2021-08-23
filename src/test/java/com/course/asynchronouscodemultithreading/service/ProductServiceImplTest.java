package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.Product;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.startTimer;
import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopTimer;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest
{
    private final ProductReviewService productReviewService = new ProductReviewService();
    private final ProductInfoService productInfoService = new ProductInfoService();
    private final InventoryService inventoryService = new InventoryService();
    private final ProductServiceImpl productService = new ProductServiceImpl(productInfoService, productReviewService, inventoryService);
    private final String productId = UUID.randomUUID().toString();


    @Test
    void retrieveProductDetails()
    {
        startTimer();
        Product product = productService.retrieveProductDetails(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption ->
        {
            assertNotNull(productOption.getInventory());
        });
        assertNotNull(product.getProductReview());
        stopTimer();
    }

    @Test
    void retrieveProductDetailsWithoutClientBlocking()
    {
        startTimer();
        productService.retrieveProductDetailsWithoutClientBlocking(productId)
                .thenAccept((result) ->
                {
                    assertNotNull(result);
                    assertTrue(result.getProductInfo().getProductOptions().size() > 0);
                    assertNotNull(result.getProductReview());
                });

        stopTimer();
    }
}