package com.bmw.model;

public record BubbleTea(
        long id,
        String name,
        String description,
        double price,
        long storeId
) {
}
