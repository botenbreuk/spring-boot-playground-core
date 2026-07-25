package nl.rdb.springbootplayground.architecture.utils;

import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public class ParameterVerifier {

    private final JavaMethod method;
    private final String expectedParameterName;
    private final Type expectedType;
    private int position = 0;
    private String allParameterNames;

    public ParameterVerifier(JavaMethod method, String expectedParameterName, Type expectedType) {
        this.method = method;
        this.expectedParameterName = expectedParameterName;
        this.expectedType = expectedType;
    }

    private boolean hasInitiativeId() {
        String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method.reflect());
        this.allParameterNames = StringUtils.join(parameterNames, ", ");
        for (String parameterName : parameterNames) {
            if (parameterName.equals(expectedParameterName)) {
                return true;
            }
            position++;
        }
        return false;
    }

    public void verify(ConditionEvents events) {
        if (!hasInitiativeId()) {
            events.add(SimpleConditionEvent.violated(
                    method,
                    method.getFullName() + " does not have a parameter with name " + getExpectedParameterName() + ", but does have: " + getParameterNames()));
            return;
        }
        if (!isOfTypeLong()) {
            events.add(SimpleConditionEvent.violated(
                    method, method.getFullName() + " parameter with name " + getExpectedParameterName() + " is not of type " + getExpectedType().getTypeName()
                            + ", but of type " + getType()));
        }
    }

    private String getExpectedParameterName() {
        return expectedParameterName;
    }

    private Type getExpectedType() {
        return expectedType;
    }

    private boolean isOfTypeLong() {
        return getType().equals(expectedType);
    }

    private String getParameterNames() {
        return this.allParameterNames;
    }

    private Type getType() {
        return method.reflect().getGenericParameterTypes()[position];
    }
}
