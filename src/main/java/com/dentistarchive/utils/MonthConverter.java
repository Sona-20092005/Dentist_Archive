package com.dentistarchive.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Month;

@Converter(autoApply = false)
public class MonthConverter implements AttributeConverter<Month, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Month month) {
        return month == null ? null : month.getValue();
    }

    @Override
    public Month convertToEntityAttribute(Integer value) {
        return value == null ? null : Month.of(value);
    }
}