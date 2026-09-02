package nl.rdb.springbootplayground.enums;

import lombok.Getter;

@Getter
public class EnumResult {

    private final String naam;
    private final String label;

    public EnumResult(Enum<?> enumValue) {
        this.naam = enumValue.name();
        this.label = enumValue instanceof EnumLabel ? ((EnumLabel) enumValue).getLabel() : enumValue.name();
    }
}
