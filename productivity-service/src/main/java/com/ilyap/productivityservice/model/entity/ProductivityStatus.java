package com.ilyap.productivityservice.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductivityStatus {

    FAIL("Fail"),
    POOR("Poor"),
    FAIR("Fair"),
    GOOD("Good"),
    VERY_GOOD("Very Good"),
    EXCELLENT("Excellent"),
    OUTSTANDING("Outstanding");

    private final String description;
}
