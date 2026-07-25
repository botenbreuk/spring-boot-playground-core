package nl.rdb.springbootplayground.enums;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemEnumResult extends EnumResult {

    public List<EnumResult> ketens;

    public SystemEnumResult(Enum<?> enumConstant) {

    }
}
