package com.course.asynchronouscodemultithreading.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Builder
public class Product
{
    private String productId;
    private ProductInfo productInfo;
    private ProductReview productReview;
}
