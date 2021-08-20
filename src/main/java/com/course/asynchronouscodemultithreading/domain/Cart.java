package com.course.asynchronouscodemultithreading.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Cart
{
    private Integer cartId;
    private List<CartItem> cartItemList;
}
