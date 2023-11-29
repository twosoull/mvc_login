package hello.login;

import hello.login.web.Interceptor.LogInterceptor;
import hello.login.web.Interceptor.LoginCheckInterceptor;
import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {//WebMvcConfigurer은 인터셉터 등록 시 필요


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    // 오버라이드 단축키 command + N 혹은 control + o
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")//허용 패턴
                .excludePathPatterns("/css/**","/*.ico","/error"); //미허용 패턴
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/members/add","/login","/logout",
                        "/css/**","/*.ico","/error");


    }

    //@Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); //만든 필터를 넣는다.
        filterRegistrationBean.setOrder(1); //작동 순서를 정한다.
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 url일 때에 작동할지를 적는다.

        return filterRegistrationBean;

    }

    //@Bean
    public FilterRegistrationBean loginFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter()); //만든 필터를 넣는다.
        filterRegistrationBean.setOrder(2); //작동 순서를 정한다.
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 url일 때에 작동할지를 적는다.

        return filterRegistrationBean;

    }

}
