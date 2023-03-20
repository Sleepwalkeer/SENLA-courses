package eu.senla.utils.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends GenericFilterBean implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestInfo = String.format("%s %s", httpRequest.getMethod(), httpRequest.getRequestURL().toString());
        logger.info("Incoming Request : " + requestInfo);

        chain.doFilter(request, response);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String responseInfo = String.format("Status %d %s", httpResponse.getStatus(), HttpStatus.valueOf(httpResponse.getStatus()).getReasonPhrase());
        logger.info("Outgoing Response : " + responseInfo);
    }
}
