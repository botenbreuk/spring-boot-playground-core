package nl.rdb.springbootplayground.utils;

import static lombok.AccessLevel.PRIVATE;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class Classes {

    public static <T> Class<T> forName(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            clazz.getMethod(methodName, parameterTypes);
            return true;
        } catch (NoSuchMethodException _) {
            return false;
        }
    }

    public static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean hasPackage(String packageName) {
        return Package.getPackage(packageName) != null;
    }
}