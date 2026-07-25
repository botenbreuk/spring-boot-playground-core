package nl.rdb.springbootplayground.shared;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.config.frontend")
public class FrontendConfigProperties {

    private String baseUrl;
}
