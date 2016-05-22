package services;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pgallazzi on 16/5/16.
 */
@Component
@Profile({"production"})
public class SecureFilter implements Filter  {

    private static final Logger logger = Logger.getLogger(SecureFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String forwardProtoHeader = servletRequest.getHeader("X-Forwarded-Proto");
        if ((null != forwardProtoHeader
                && forwardProtoHeader.equalsIgnoreCase("https")) || request.isSecure())
        {
            chain.doFilter(request, response);
        }
        else
        {
            logger.info("Redirecting to https!");
            String redirectTarget = UriComponentsBuilder
                    .fromHttpUrl(servletRequest.getRequestURL().toString()).scheme("https").build()
                    .toUriString();
            ((HttpServletResponse) response).sendRedirect(redirectTarget);
        }
    }

    @Override
    public void destroy() {

    }
}
