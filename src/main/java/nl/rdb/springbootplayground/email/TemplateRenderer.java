package nl.rdb.springbootplayground.email;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Utility class to facilitate the rendering of templates.
 * It uses StringTemplate as the templating engine. See also: <a href="http://www.stringtemplate.org/">http://www.stringtemplate.org/</a>
 * <p>
 * Note that all templates should be available on the CLASSPATH under the 'templates' directory.
 */
@Slf4j
@Component
public final class TemplateRenderer {

    private final TemplateEngine templateEngine;

    public TemplateRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Renders the template with the given template name (should be available on the CLASSPATH under the 'templates' directory) with the given parameters.
     *
     * @param templateName the name of the template to render.
     * @param parameters   the parameters that should be plugged into the template when rendered. The keys are the parameter names and the values are the actual parameters.
     * @return the rendered template with the given parameters plugged in.
     */
    public String render(String templateName, Map<String, Object> parameters) {
        log.debug("Rendering template: {}, with parameters: {}", templateName, parameters);
        Context context = getContext(parameters);
        return templateEngine.process(templateName, context);
    }

    private Context getContext(Map<String, Object> parameters) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }
}