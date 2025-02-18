package com.uniwear.backend.enums;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MemberType {
    BUSINESS("사업용계정", "B"), PERSONAL("개인계정", "P"), UNKNOWN("알수없음", "U");

    private String value;
    private String code;
  
    MemberType(String value, String code) {
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
    
    public static MemberType find(String description) {
        return Arrays.stream(values()).filter(memberType -> memberType.getCode().equals(description)).findAny().orElse(UNKNOWN);
    }

    @Converter
    public static class MemberTypeConverter implements AttributeConverter<MemberType, String> {
    
        @Override
        public String convertToDatabaseColumn(MemberType attribute) {
            if (Objects.isNull(attribute)) {
                return null;
            }
            return attribute.getCode();
        }
    
        @Override
        public MemberType convertToEntityAttribute(String dbData) {
            if (dbData.isBlank()) {
                return null;
            }
            return MemberType.find(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }
       
    }
}
