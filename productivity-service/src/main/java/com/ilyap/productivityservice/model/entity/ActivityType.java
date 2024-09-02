package com.ilyap.productivityservice.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {

    EXERCISE("Gym"),
    WORK("Work"),
    LEARN("Learn"),
    WALK("Walk"),
    COOK("Cook"),
    HOBBY("Hobby"),
    FAMILY_TIME("Family Time");

    private final String description;
}
