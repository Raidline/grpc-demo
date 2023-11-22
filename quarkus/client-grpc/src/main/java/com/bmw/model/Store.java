package com.bmw.model;

import java.util.List;

public record Store(
        long id,
        String name,
        String address,
        String phone,
        List<BubbleTea> bubbleTeas
) {
}
