package com.uniwear.backend.enums;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeliveryCaseType {
    FRAN("프랜차이즈", "FRANCHISE"), GOODS("굿즈", "GOODS"), SCHOOL("과잠", "SCHOOL"),
    FESTIVAL("행사", "FESTIVAL"), UNIFORM("유니폼", "UNIFORM"), UNKNOWN("알수없음", "UNKNOWN");

    private String value;
    private String code;
  
    DeliveryCaseType(String value, String code) {
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
    
    public static DeliveryCaseType find(String description) {
        return Arrays.stream(values()).filter(deliveryCaseType -> deliveryCaseType.getCode().equals(description)).findAny().orElse(UNKNOWN);
    }

    @Converter
    public static class DeliveryCaseTypeConverter implements AttributeConverter<DeliveryCaseType, String> {
    
        @Override
        public String convertToDatabaseColumn(DeliveryCaseType attribute) {
            if (Objects.isNull(attribute)) {
                return DeliveryCaseType.UNKNOWN.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public DeliveryCaseType convertToEntityAttribute(String dbData) {
            return DeliveryCaseType.find(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }
       
    }
}
