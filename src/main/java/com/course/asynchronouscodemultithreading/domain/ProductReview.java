package com.course.asynchronouscodemultithreading.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductReview
{
    private int noOfReviews;
    private double overallRating;
}
