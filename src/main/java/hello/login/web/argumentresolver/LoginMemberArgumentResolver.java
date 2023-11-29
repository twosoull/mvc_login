package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        // 요청한 컨트롤러 메소드에 @Login 어노테이션이 붙어 있는가 물어본는 것
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        // parameter.getParameterType() 에서는 사용한 파라미터의 타입인 즉 현재로써는 Member가 된다.
        //public String homeLoginV3ArgumentResolver(
        // @Login Member member, Model model, HttpServletRequest request){
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());


        return hasLoginAnnotation && hasMemberType; //두개가 만족 할 시에 아래의 resolveArgument가 작동한다.
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false); //세션을 없으면 만들지 않음

        if(session == null){
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
