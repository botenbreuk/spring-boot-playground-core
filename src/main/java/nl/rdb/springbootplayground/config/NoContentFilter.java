package nl.rdb.springbootplayground.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class NoContentFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        if (httpServletResponse.getContentType() == null ||
                httpServletResponse.getContentType().equals("")) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }
}