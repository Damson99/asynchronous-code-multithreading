package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.startTimer;
import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopTimer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest
{

    @Mock
    private ProductReviewService productReviewService;

    @Mock
    private ProductInfoService productInfoService;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private final String productId = UUID.randomUUID().toString();


    @Test
    void retrieveProductDetailsWithExceptionHandling()
    {
        when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
        when(productReviewService.retrieveReviews(any())).thenThrow(new RuntimeException("Exception occurred"));
        when(inventoryService.retrieveInventory(any())).thenCallRealMethod();

        startTimer();
        Product product = productService.retrieveProductDetails(productId);
        stopTimer();

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption -> assertNotNull(productOption.getInventory()));
        assertEquals(product.getProductReview().getNoOfReviews(), 0);
    }

    @Test
    void retrieveProductDetailsWithError()
    {
        when(productInfoService.retrieveProductInfo(any())).thenThrow(new RuntimeException("Exception occurred"));
        when(productReviewService.retrieveReviews(any())).thenCallRealMethod();
//        InventoryService zadziala tylko w przypadku gdy InfoService zadziala, a w tym jest zwracany wyjatek
//        when(inventoryService.retrieveInventory(any())).thenCallRealMethod();

        assertThrows(RuntimeException.class, () -> productService.retrieveProductDetails(productId));
    }

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