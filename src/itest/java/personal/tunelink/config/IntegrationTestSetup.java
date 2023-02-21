package personal.tunelink.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@TestConfiguration
public class IntegrationTestSetup {

    public IntegrationTestSetup() {
        mockServletRequestContext();
    }

    private void mockServletRequestContext() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setContextPath("itest");
        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attrs);
    }
}
