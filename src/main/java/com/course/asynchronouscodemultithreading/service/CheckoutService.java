package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.Cart;
import com.course.asynchronouscodemultithreading.domain.CartItem;
import com.course.asynchronouscodemultithreading.domain.CheckoutResponse;
import com.course.asynchronouscodemultithreading.domain.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.startTimer;
import static com.course.asynchronouscodemultithreading.util.CommonUtil.stopTimer;

public class CheckoutService
{
    private PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService)
    {
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkout(Cart cart)
    {
        startTimer();
        List<CartItem> priceValidatedList = cart.getCartItemList()
                .parallelStream()
                .peek(cartItem ->
                {
                    boolean isPriceInvalid = priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceInvalid);
                }).filter(CartItem::isExpired).collect(Collectors.toList());
        stopTimer();

        if(priceValidatedList.size() > 0)
            return new CheckoutResponse(CheckoutStatus.FAILURE, priceValidatedList);

        return new CheckoutResponse(CheckoutStatus.SUCCESS);
    }
}
