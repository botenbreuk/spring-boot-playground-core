package nl.rdb.springbootplayground.enums;

import nl.rdb.springbootplayground.utils.Classes;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.stereotype.Component;

@Component
class EnumProvider extends ClassPathScanningCandidateComponentProvider {

    EnumProvider() {
        super(false);
        addIncludeFilter((metadataReader, _) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return Classes.forName(className).isEnum();
        });
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }
}
