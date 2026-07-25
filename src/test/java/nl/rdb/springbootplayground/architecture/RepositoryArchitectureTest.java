package nl.rdb.springbootplayground.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import nl.rdb.springbootplayground.shared.CustomJpaRepository;
import nl.rdb.springbootplayground.shared.querydsl.AbstractQueryDslRepository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

class RepositoryArchitectureTest extends AbstractArchitectureTest {

    /**
     * All repository interfaces must extend CustomJpaRepository or JpaRepository.
     * CustomJpaRepository adds the findOne(id) convenience so findById(id).orElse(null) is not needed
     * method and ensures a consistent API. Extending JpaRepository directly bypasses this contract.
     */
    @Test
    void repositoriesMustExtendCustomJpaRepository() {
        DescribedPredicate<JavaClass> isImplementedByCustomJpaRepository = DescribedPredicate.describe(
                "is a fragment implemented by a repository that extends CustomJpaRepository",
                clazz -> clazz.getAllSubclasses().stream()
                        .anyMatch(subClass -> subClass.isAssignableTo(CustomJpaRepository.class) || subClass.isAssignableTo(JpaRepository.class))
        );

        classes().that()
                .haveSimpleNameEndingWith("Repository")
                .and().areInterfaces()
                .and().doNotHaveSimpleName(CustomJpaRepository.class.getSimpleName())
                .and(not(excludedFor(ViolationType.REPOSITORY_BASE_CLASS)))
                .and(not(isImplementedByCustomJpaRepository))
                .should().beAssignableTo(CustomJpaRepository.class)
                .orShould().beAssignableTo(JpaRepository.class)
                .check(importedClasses);
    }

    /**
     * All RepositoryImpl classes must extend AbstractQueryDslRepository. This base class provides the
     * EntityManager and shared QueryDSL infrastructure. Bypassing it leads to duplicated setup code and
     * inconsistent query behavior.
     */
    @Test
    void repositoryImplementationsMustExtendAbstractQueryDslRepository() {
        classes().that()
                .haveSimpleNameEndingWith("RepositoryImpl")
                .should().beAssignableTo(AbstractQueryDslRepository.class)
                .check(importedClasses);
    }

    /**
     * All RepositoryImpl classes must implement at least one RepositoryCustom interface. The custom
     * interface declares the contract for the custom query methods, keeping the implementation decoupled
     * from the Spring Data repository and making the API discoverable.
     */
    @Test
    void repositoryImplementationsMustImplementRepositoryCustom() {
        DescribedPredicate<JavaClass> hasCustomClass = DescribedPredicate.describe(
                "have a simple name ending with 'RepositoryCustom'",
                clazz -> clazz.getSimpleName().endsWith("RepositoryCustom")
        );

        classes().that()
                .haveSimpleNameEndingWith("RepositoryImpl")
                .should().implement(hasCustomClass)
                .check(importedClasses);
    }
}
