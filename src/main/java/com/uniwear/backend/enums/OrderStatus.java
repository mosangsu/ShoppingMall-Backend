package com.uniwear.backend.enums;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus {
    BEFORE_DEPOSIT("입금전", "BD"), PAYMENT_COMPLETE("결제완료", "PC"),
    PRODUCTION_IN_PROGRESS("상품준비중", "PIP"), DELIVERY_IN_PROGRESS("배송중", "DIP"),
    DELIVERY_COMPLETE("배송완료", "DC"), EXCHANGE("교환", "E"),
    RETURN("반품", "R"), CANCEL("취소", "C"), 
    UNKNOWN("알수없음", "U");

    private String value;
    private String code;
  
    OrderStatus(String value, String code) {
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
    
    public static OrderStatus find(String description) {
        return Arrays.stream(values()).filter(orderStatus -> orderStatus.getCode().equals(description)).findAny().orElse(UNKNOWN);
    }

    @Converter
    public static class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    
        @Override
        public String convertToDatabaseColumn(OrderStatus attribute) {
            if (Objects.isNull(attribute)) {
                return OrderStatus.UNKNOWN.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public OrderStatus convertToEntityAttribute(String dbData) {
            return OrderStatus.find(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }
       
    }
}
