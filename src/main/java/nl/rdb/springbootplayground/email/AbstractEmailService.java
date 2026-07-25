package nl.rdb.springbootplayground.email;

import lombok.RequiredArgsConstructor;
import nl.rdb.springbootplayground.shared.FrontendConfigProperties;

@RequiredArgsConstructor
public abstract class AbstractEmailService {

    public final Mailer mailer;
    public final FrontendConfigProperties frontendConfigProperties;
    public final MailConfigProperties mailConfigProperties;

    protected abstract String getPath(FrontendConfigProperties frontendConfigProperties);

    public String getBaseUrl() {
        return frontendConfigProperties.getBaseUrl();
    }

    public String getPath() {
        return getPath(frontendConfigProperties);
    }

    public String buildUrl() {
        return buildUrl(getBaseUrl(), getPath());
    }

    public String buildUrl(String baseUrl, String path) {
        return "%s%s".formatted(baseUrl, path);
    }

    public <T> String buildUrl(T value) {
        return "%s%s/%s".formatted(getBaseUrl(), getPath(), value);
    }

    public String buildUrl(Long id, String suffix) {
        return "%s%s/%d%s".formatted(getBaseUrl(), getPath(), id, suffix);
    }
}
