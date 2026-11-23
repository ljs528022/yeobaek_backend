package com.app.yeoback_backend.common.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// 여행 상태 (DB: trip_status) : 작성 중 / 임시 저장 / 완료
// DB enum 값이 소문자라서 value도 DB와 똑같이 소문자로 맞춘다
public enum TripStatus {
    WRITE("write"), SAVE("save"), FINISH("finish");

    private final String value;

    private static final Map<String, TripStatus> TRIP_STATUS_MAP =
            Arrays.stream(TripStatus.values()).collect(Collectors.toMap(TripStatus::getValue, Function.identity()));

    TripStatus(String value) { this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TripStatus getTripStatus(String value) {
        return TRIP_STATUS_MAP.get(value);
    }
}
