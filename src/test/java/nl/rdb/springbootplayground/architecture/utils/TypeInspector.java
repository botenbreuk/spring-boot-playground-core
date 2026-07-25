package nl.rdb.springbootplayground.architecture.utils;

import jakarta.persistence.Entity;

import org.springframework.core.ResolvableType;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public abstract class TypeInspector {

    private final JavaClasses importedClasses;
    private final ConditionEvents events;

    protected TypeInspector(JavaClasses importedClasses, ConditionEvents events) {
        this.importedClasses = importedClasses;
        this.events = events;
    }

    public void inspect() {
        inspectElementItself();
        inspectCollectionElements();
    }

    private void inspectElementItself() {
        if (inspectedRaw().isAnnotatedWith(Entity.class)) {
            events.add(SimpleConditionEvent.satisfied(inspectedObject(), getNonCollectionSatisfiedMessage()));
        }
    }

    private void inspectCollectionElements() {
        for (ResolvableType collectionElementType : getResolvableType().asCollection().getGenerics()) {
            try {
                Class<?> resolvedClass = collectionElementType.resolve();
                if (resolvedClass != null) {
                    JavaClass collectionElementClass = importedClasses.get(resolvedClass);
                    if (collectionElementClass.isAnnotatedWith(Entity.class)) {
                        events.add(SimpleConditionEvent.satisfied(inspectedObject(), getCollectionSatisfiedMessage()));
                    }
                }
            } catch (IllegalArgumentException err) {
                // class does not exist in the importedClasses – continue
                continue;
            }
        }
    }

    public abstract JavaClass inspectedRaw();

    public abstract Object inspectedObject();

    public abstract String getNonCollectionSatisfiedMessage();

    public abstract String getCollectionSatisfiedMessage();

    public abstract ResolvableType getResolvableType();
}
