package br.fcv.wiquery_test.support.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = null;
        
        if (request instanceof HttpServletRequest) {
            
            httpRequest = (HttpServletRequest) request;
            if (logger.isDebugEnabled())
                logger.debug(buildBeforeMessage(httpRequest));
        } else {
            
            logger.warn("request of type: {} is not a instance of {}",
                    request.getClass().getName(), HttpServletRequest.class.getName());
        }        
        
        try {
            chain.doFilter(request, response);
        } finally {
            if (httpRequest != null) {
                if (logger.isDebugEnabled())
                    logger.debug(buildAfterMessage(httpRequest));
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {        
    }
    
    private String buildBeforeMessage(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder("[Before] ");
        appendDefaultMessage(request, buffer);
        return buffer.toString();
    }
    
    private String buildAfterMessage(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder("[After] ");
        appendDefaultMessage(request, buffer);
        return buffer.toString();
    }
    
    private void appendDefaultMessage(HttpServletRequest request, StringBuilder buffer) {        
        buffer.append(request.getRequestURI());
        
        if (request.getQueryString() != null) {
            buffer.append('?').append(request.getQueryString());
        }
    }

}
