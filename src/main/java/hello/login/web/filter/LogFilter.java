package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        //1. ServletRequest는 HttpServletRequest의 부모이다 먼저 치환해서 사용해야 한다.
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String uri = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        //2. chain.doFilter를 사용하지 않으면 작동하지 않는다. 또한 WebConfig에 해당 filter를 등록해야한다.
        try{
            log.info("REQUEST [{}][{}]", uuid, uri);
            chain.doFilter(request,response);

        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]", uuid, uri);
        }


    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
