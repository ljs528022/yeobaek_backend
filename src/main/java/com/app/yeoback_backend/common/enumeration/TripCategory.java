package com.app.yeoback_backend.common.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// 여행 카테고리 (DB: trip_category) : 국내 / 해외
// DB enum 값이 소문자라서 value도 DB와 똑같이 소문자로 맞춘다
public enum TripCategory {
    DOMESTIC("domestic"), OVERSEAS("overseas");

    private final String value;

    private static final Map<String, TripCategory> TRIP_CATEGORY_MAP =
            Arrays.stream(TripCategory.values()).collect(Collectors.toMap(TripCategory::getValue, Function.identity()));

    TripCategory(String value) { this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TripCategory getTripCategory(String value) {
        return TRIP_CATEGORY_MAP.get(value);
    }
}
