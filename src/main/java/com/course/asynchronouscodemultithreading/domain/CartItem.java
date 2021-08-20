package com.course.asynchronouscodemultithreading.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem
{
    private Integer itemId;
    private String itemName;
    private double rate;
    private double quantity;
    private boolean isExpired = false ;

}
