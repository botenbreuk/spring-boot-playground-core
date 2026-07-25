package nl.rdb.springbootplayground.shared.utils;

import java.util.Objects;
import java.util.function.Supplier;

public class StringUtils {

    private StringUtils() {}

    public static String stringDefault(Supplier<String> supplier) {
        return stringDefault(supplier, "");
    }

    public static String stringDefault(Supplier<String> supplier, String defaultValue) {
        try {
            return Objects.toString(supplier.get(), defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
