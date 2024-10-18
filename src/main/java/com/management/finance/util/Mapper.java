package com.management.finance.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Mapper {

    public static <T> Optional<T> recordToEntity(
            Record recordDto,
            Class<T> clazzEntity
    ) {
        Class<? extends Record> recordClass = recordDto.getClass();
        Field[] recordFields = recordClass.getDeclaredFields();
        Field[] clazzFields = clazzEntity.getDeclaredFields();
        try {
            T newInstanceClass = clazzEntity.getDeclaredConstructor().newInstance();
            for (Field recordField : recordFields) {
                String recordName = recordField.getName();
                for (Field field : clazzFields) {
                    String fieldName = field.getName();
                    if (recordName.equals(fieldName)) {
                        Method setterClazz = Mapper.setterMethod(field, clazzEntity);
                        Method getterRecord = recordClass.getMethod(fieldName);
                        Object param = getterRecord.invoke(recordDto);
                        setterClazz.invoke(newInstanceClass, param);
                        break;
                    }
                }
            }
            return Optional.of(newInstanceClass);
        } catch (Exception error) {
            log.error("Error in mapped record to object pojo: {}", error.getMessage());
            return Optional.empty();
        }
    }

    private static <T> Method setterMethod(Field field, Class<T> clazz) throws NoSuchMethodException {
        String prefix = "set";
        String[] splitFieldName = field.getName().split("");
        String upperCaseFirstChar = splitFieldName[0].toUpperCase();
        splitFieldName[0] = upperCaseFirstChar;
        String cameOnCaseNameField = String.join("", splitFieldName);
        String nameSetMethod = prefix + cameOnCaseNameField;
        return clazz.getMethod(nameSetMethod, field.getType());
    }
}
