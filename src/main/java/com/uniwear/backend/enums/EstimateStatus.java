package com.uniwear.backend.enums;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EstimateStatus {
    BEFORE_SUBMISSION("제출전", "BS"), 
    ESTIMATE_IN_PROGRESS("견적문의중", "EIP"), ESTIMATE_COMPLETE("견적완료", "EC"),
    DRAFT_IN_PROGRESS("시안수정중", "DIP"), DRAFT_COMPLETE("시안확정", "DC"),
    UNKNOWN("알수없음", "U");

    private String value;
    private String code;
  
    EstimateStatus(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getName() {
        return name();
    }

    public String getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }
    
    public static EstimateStatus find(String description) {
        return Arrays.stream(values()).filter(estimateStatus -> estimateStatus.getCode().equals(description)).findAny().orElse(UNKNOWN);
    }

    @Converter
    public static class EstimateStatusConverter implements AttributeConverter<EstimateStatus, String> {
    
        @Override
        public String convertToDatabaseColumn(EstimateStatus attribute) {
            if (Objects.isNull(attribute)) {
                return EstimateStatus.UNKNOWN.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public EstimateStatus convertToEntityAttribute(String dbData) {
            return EstimateStatus.find(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }
       
    }
}
