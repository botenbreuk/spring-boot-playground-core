package nl.rdb.springbootplayground.config.error;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericErrorHandler {

    private final JsonMapper jsonMapper;

    public void respond(HttpServletResponse response, HttpStatus status, String errorCode) throws IOException {
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        jsonMapper.writeValue(response.getWriter(), new GenericErrorResult(errorCode));
        response.getWriter().flush();
    }
}