package kr.co.eceris.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

public class RouteFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        String port = request.getParameter("port");
        try {
            URL url = UriComponentsBuilder.fromUri(ctx.getRouteHost().toURI())
                    .port(new Integer(port))
                    .build().toUri().toURL();
            ctx.setRouteHost(url);
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }

        return null;
    }
}
