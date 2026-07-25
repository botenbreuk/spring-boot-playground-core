package nl.rdb.springbootplayground.config;

import static org.springframework.data.domain.Sort.Direction.DESC;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

/**
 * This component defines the de-serialization to a Pageable object.
 *
 * @author bas
 */
@Configuration
public class PageableConfiguration {

    private final PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    public PageableConfiguration(PageableHandlerMethodArgumentResolver pageableArgumentResolver) {this.pageableArgumentResolver = pageableArgumentResolver;}

    /**
     * Configures the PageableArgumentResolver to de-serialize to a Pageable object with one-based page number.
     * This is the configuration of de-serialization from url params.
     */
    @PostConstruct
    public void configurePageableArgumentResolver() {
        pageableArgumentResolver.setFallbackPageable(PageRequest.of(0, 10, DESC, "id"));
        pageableArgumentResolver.setMaxPageSize(200);
        pageableArgumentResolver.setOneIndexedParameters(true);
    }
}
