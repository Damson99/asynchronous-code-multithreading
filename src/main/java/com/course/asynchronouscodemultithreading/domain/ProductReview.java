package com.course.asynchronouscodemultithreading.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductReview
{
    private int noOfReviews;
    private double overallRating;
}
