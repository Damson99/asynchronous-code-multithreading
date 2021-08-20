package com.course.asynchronouscodemultithreading.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CheckoutResponse
{
    CheckoutStatus checkoutStatus;
    List<CartItem> cartItemList = new ArrayList<>();

    public CheckoutResponse(CheckoutStatus  checkoutStatus)
    {
        this.checkoutStatus = checkoutStatus;
    }

    public CheckoutResponse(CheckoutStatus checkoutStatus, List<CartItem> cartItemList)
    {
        this.checkoutStatus = checkoutStatus;
        this.cartItemList = cartItemList;
    }
}
