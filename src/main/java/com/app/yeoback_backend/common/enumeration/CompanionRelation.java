package com.app.yeoback_backend.common.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// 동행 관계 상태 (DB: companion_relation) : 대기 중 / 완료 / 거절
// DB enum 값이 소문자라서 value도 DB와 똑같이 소문자로 맞춘다
public enum CompanionRelation {
    PENDING("pending"), DONE("done"), REJECT("reject");

    private final String value;

    private static final Map<String, CompanionRelation> COMPANION_RELATION_MAP =
            Arrays.stream(CompanionRelation.values()).collect(Collectors.toMap(CompanionRelation::getValue, Function.identity()));

    CompanionRelation(String value) { this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CompanionRelation getCompanionRelation(String value) {
        return COMPANION_RELATION_MAP.get(value);
    }
}
