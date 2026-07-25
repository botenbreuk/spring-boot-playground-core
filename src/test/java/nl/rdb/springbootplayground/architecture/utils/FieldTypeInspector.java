package nl.rdb.springbootplayground.architecture.utils;

import org.springframework.core.ResolvableType;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ConditionEvents;

public class FieldTypeInspector extends TypeInspector {

    private final JavaField field;
    private final ResolvableType collectionType;

    public FieldTypeInspector(JavaClasses javaClasses, JavaField field, ConditionEvents events) {
        super(javaClasses, events);
        this.field = field;
        this.collectionType = ResolvableType.forField(field.reflect());
    }

    @Override
    public JavaClass inspectedRaw() {
        return field.getRawType();
    }

    @Override
    public Object inspectedObject() {
        return field;
    }

    @Override
    public String getNonCollectionSatisfiedMessage() {
        return field.getFullName() + " has " + field.getRawType().getFullName() + ", which is an @Entity";
    }

    @Override
    public String getCollectionSatisfiedMessage() {
        return field.getFullName() + " has " + collectionType.getType().getTypeName() + " which contains an @Entity";
    }

    @Override
    public ResolvableType getResolvableType() {
        return collectionType;
    }
}
