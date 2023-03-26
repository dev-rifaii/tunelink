package perosnal.tunelink.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import perosnal.tunelink.exceptions.InvalidJwtException;
import perosnal.tunelink.exceptions.TuneLinkException;
import perosnal.tunelink.jwt.JwtManager;

import java.io.IOException;
import java.util.*;

import static perosnal.tunelink.globals.Constants.NO_AUTH_ENDPOINTS;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final JwtManager jwtManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            var servletRequest = (HttpServletRequest) request;

            if (NO_AUTH_ENDPOINTS.contains(servletRequest.getServletPath())) {
                chain.doFilter(servletRequest, response);
                return;
            }

            String jwt = servletRequest.getHeader("Authorization").replace("Bearer ", "");

            String userId = jwtManager.extractSub(jwt);

            var requestWrapper = new HeaderMapRequestWrapper(servletRequest);
            requestWrapper.addHeader("userId", userId);

            chain.doFilter(requestWrapper, response);
        } catch (NullPointerException | InvalidJwtException | TuneLinkException e) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid");
            log.warn(e.getClass().getSimpleName() + "Message: " + e.getMessage());
        }
    }

    private static class HeaderMapRequestWrapper extends HttpServletRequestWrapper {

        public HeaderMapRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        private final Map<String, String> headerMap = new HashMap<>();

        public void addHeader(String name, String value) {
            headerMap.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = super.getHeader(name);
            if (headerMap.containsKey(name)) {
                headerValue = headerMap.get(name);
            }
            return headerValue;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            names.addAll(headerMap.keySet());
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (headerMap.containsKey(name)) {
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }

    }
}
