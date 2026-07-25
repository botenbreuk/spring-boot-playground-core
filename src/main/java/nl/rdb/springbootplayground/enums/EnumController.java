package nl.rdb.springbootplayground.enums;

import static java.util.Arrays.stream;
import static org.springframework.util.ReflectionUtils.findField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.rdb.springbootplayground.Application;
import nl.rdb.springbootplayground.utils.Classes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enums")
public class EnumController {

    private final Map<String, List<EnumResult>> registry = new HashMap<>();

    EnumController(EnumProvider enumProvider) {
        enumProvider.findCandidateComponents(Application.class.getPackage().getName())
                .forEach(component -> {
                    Class<Enum<?>> componentClass = Classes.forName(component.getBeanClassName());
                    if (!componentClass.isAnnotationPresent(EnumIgnored.class)) {
                        registry.put(componentClass.getSimpleName(), stream(componentClass.getEnumConstants())
                                .filter(enumName -> !findField(componentClass, enumName.name()).isAnnotationPresent(EnumIgnored.class))
                                .map(SystemEnumResult::new)
                                .collect(Collectors.toList()));
                    }
                });
    }

    @GetMapping
    Map<String, List<EnumResult>> findAll() {
        return registry;
    }
}
