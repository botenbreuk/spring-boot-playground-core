package nl.rdb.springbootplayground.config.error;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class GenericErrorHandler {

    private final ObjectMapper objectMapper;

    public void respond(HttpServletResponse response, HttpStatus status, String errorCode) throws IOException {
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new GenericErrorResult(errorCode));
        response.getWriter().flush();
    }
}