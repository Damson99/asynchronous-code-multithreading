package com.course.asynchronouscodemultithreading.service;

import com.course.asynchronouscodemultithreading.domain.Inventory;
import com.course.asynchronouscodemultithreading.domain.ProductOption;

import static com.course.asynchronouscodemultithreading.util.CommonUtil.delay;

public class InventoryService
{
    public Inventory retrieveInventory(ProductOption productOption)
    {
        delay(500);
        return Inventory.builder()
                .count(2)
                .build();
    }
}
