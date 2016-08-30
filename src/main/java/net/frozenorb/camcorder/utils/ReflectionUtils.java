package net.frozenorb.camcorder.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public final class ReflectionUtils {

    public static void setField(Object obj, String field, Object value) {
        try {
            Field fieldObject = obj.getClass().getDeclaredField(field);

            fieldObject.setAccessible(true);
            fieldObject.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}