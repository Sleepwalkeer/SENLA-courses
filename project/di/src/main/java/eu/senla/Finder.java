package eu.senla;

import eu.senla.annotations.Autowired;
import eu.senla.annotations.Value;

import java.lang.reflect.*;
public class Finder {
    public static Constructor<?> findAnnotatedConstructor(Class<?> klass) {
        for (Constructor<?> declaredConstructor : klass.getDeclaredConstructors()) {
            if (declaredConstructor.isAnnotationPresent(Autowired.class)) {
                declaredConstructor.setAccessible(true);
                return declaredConstructor;
            }
        }
        return null;
    }

    public static Method findAnnotatedMethod(Class<?> klass) {
        for (Method declaredMethod : klass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Autowired.class)) {
                declaredMethod.setAccessible(true);
                return declaredMethod;
            }
        }
        return null;
    }

    public static Field findAnnotatedField(Class<?> klass) {
        for (Field declaredField : klass.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                declaredField.setAccessible(true);
                return declaredField;
            }
        }
        return null;
    }

    public static Field findValueAnnotatedField(Class<?> klass) {
        for (Field declaredField : klass.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Value.class)) {
                declaredField.setAccessible(true);
                return declaredField;
            }
        }
        return null;
    }
}
