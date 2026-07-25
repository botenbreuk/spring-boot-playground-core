package nl.rdb.springbootplayground.architecture.utils;

import org.springframework.core.ResolvableType;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ConditionEvents;

public class ReturnTypeInspector extends TypeInspector {

    private final JavaMethod method;
    private final ResolvableType collectionType;

    public ReturnTypeInspector(JavaClasses javaClasses, JavaMethod method, ConditionEvents events) {
        super(javaClasses, events);
        this.method = method;
        this.collectionType = ResolvableType.forMethodReturnType(method.reflect());
    }

    @Override
    public JavaClass inspectedRaw() {
        return method.getRawReturnType();
    }

    @Override
    public Object inspectedObject() {
        return method;
    }

    @Override
    public String getNonCollectionSatisfiedMessage() {
        return method.getFullName() + " returns " + method.getRawReturnType().getFullName() + ", which is an @Entity";
    }

    @Override
    public String getCollectionSatisfiedMessage() {
        return method.getFullName() + " returns " + collectionType.getType().getTypeName() + " which contains an @Entity";
    }

    @Override
    public ResolvableType getResolvableType() {
        return collectionType;
    }
}
