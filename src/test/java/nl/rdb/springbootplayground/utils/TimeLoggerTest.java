package nl.rdb.springbootplayground.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.junit.jupiter.api.Test;

class TimeLoggerTest {

    @Test
    void constructorTest() {
        assertThrows(IllegalStateException.class, () -> {
            try {
                Constructor<TimeLogger> constructor = TimeLogger.class.getDeclaredConstructor();
                assertTrue(Modifier.isPrivate(constructor.getModifiers()));
                constructor.trySetAccessible();
                constructor.newInstance();
            } catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }, "Utility class");
    }

    @Test
    void logTimeFinish_Runnable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logTimeFinish("", "", () -> person.setAge(24));

        assertEquals(24, person.getAge());
    }

    @Test
    void logTimeFinish_noClassName_Runnable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logTimeFinish("", () -> person.setAge(24));

        assertEquals(24, person.getAge());
    }

    @Test
    void logTimeFinish_Supplier() {
        Person person = TimeLogger.logTimeFinish("", "", () -> new Person("Person 1", 42));
        assertEquals(42, person.getAge());
    }

    @Test
    void logTimeFinish_noClassName_Supplier() {
        Person person = TimeLogger.logTimeFinish("", () -> new Person("Person 1", 42));
        assertEquals(42, person.getAge());
    }

    @Test
    void logTimeStartFinish_Supplier() {
        Person person = TimeLogger.logTimeStartFinish("", "", () -> new Person("Person 1", 42));
        assertEquals(42, person.getAge());
    }

    @Test
    void logTimeStartFinish_noClassName_Supplier() {
        Person person = TimeLogger.logTimeStartFinish("", () -> new Person("Person 1", 42));
        assertEquals(42, person.getAge());
    }

    @Test
    void logTimeStartFinish_Runnable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logTimeStartFinish("", "", () -> person.setAge(24));
        assertEquals(24, person.getAge());
    }

    @Test
    void logTimeStartFinishUn_UnaryOperator() {
        Person person = new Person("Person 1", 42);
        Person updated = TimeLogger.logTimeStartFinishUn("", "", person, p -> {
            p.setAge(24);
            return p;
        });
        assertEquals(24, updated.getAge());
    }

    @Test
    void logTimeStartFinishUn_noClassName_UnaryOperator() {
        Person person = new Person("Person 1", 42);
        Person updated = TimeLogger.logTimeStartFinishUn("", person, p -> {
            p.setAge(24);
            return p;
        });
        assertEquals(24, updated.getAge());
    }

    @Test
    void logTimeStartFinishFn_Function() {
        Person person = new Person("Person 1", 42);
        int age = TimeLogger.logTimeStartFinishFn("", "", person, p -> {
            p.setAge(24);
            return p.getAge();
        });
        assertEquals(24, age);
    }

    @Test
    void logTimeStartFinishFn_noClassName_Function() {
        Person person = new Person("Person 1", 42);
        int age = TimeLogger.logTimeStartFinishFn("", person, p -> {
            p.setAge(24);
            return p.getAge();
        });
        assertEquals(24, age);
    }

    @Test
    void logTimeStartFinish_noClassName_Runnable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logTimeStartFinish("", () -> person.setAge(24));
        assertEquals(24, person.getAge());
    }

    @Test
    void logTimeStartFinish_partition_Consumer() {
        List<List<Person>> partitions = List.of(List.of(new Person("Person 1", 42)), List.of(new Person("Person 2", 42)));
        TimeLogger.logTimeStartFinish("", "", partitions, values -> values.forEach(p -> p.setAge(24)));

        var ages = (partitions.stream()
                .flatMap(persons -> persons.stream().map(Person::getAge))
                .toList());

        assertThat(ages, containsInAnyOrder(24, 24));
    }

    @Test
    void logTimeStartFinish_partition_noClassName_Consumer() {
        List<List<Person>> partitions = List.of(List.of(new Person("Person 1", 42)), List.of(new Person("Person 2", 42)));
        TimeLogger.logTimeStartFinish("", partitions, values -> values.forEach(p -> p.setAge(24)));

        var ages = (partitions.stream()
                .flatMap(persons -> persons.stream().map(Person::getAge))
                .toList());

        assertThat(ages, containsInAnyOrder(24, 24));
    }

    @Test
    void logStartFinish_Runnable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logStartFinish("", "", () -> person.setAge(24));
        assertEquals(24, person.getAge());
    }

    @Test
    void logStartFinish_noClassName_UnaryOperator() {
        Person person = new Person("Person 1", 42);
        Person updated = TimeLogger.logStartFinish("", person, p -> {
            p.setAge(24);
            return p;
        });
        assertEquals(24, updated.getAge());
    }

    @Test
    void logStartFinish_noClassName_Runnable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logStartFinish("", () -> person.setAge(24));
        assertEquals(24, person.getAge());
    }

    @Test
    void logStartFinishWithEx_Callable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logStartFinishWithEx("", "", () -> {
            person.setAge(24);
            return person;
        });
        assertEquals(24, person.getAge());
    }

    @Test
    void logStartFinishWithEx_exception_Callable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logStartFinishWithEx("", "", () -> person.setAgeException(true));
        assertEquals(42, person.getAge());
    }

    @Test
    void logStartFinishWithEx_noClassName_Callable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logStartFinishWithEx("", () -> {
            person.setAge(24);
            return person;
        });
        assertEquals(24, person.getAge());
    }

    @Test
    void logStartFinishWithEx_exception_noClassName_Callable() {
        Person person = new Person("Person 1", 42);
        TimeLogger.logStartFinishWithEx("", () -> person.setAgeException(true));
        assertEquals(42, person.getAge());
    }

    @Getter
    @Setter
    static class Person {

        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Object setAgeException(boolean test) {
            if (test) {
                throw new RuntimeException("Test");
            } else {
                return null;
            }
        }
    }
}
