package hello.login.web.login;

import hello.login.domain.login.LoginForm;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm){
        return "login/loginForm";
    }

    //@PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response){

        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //쿠키에 시간 정보를 주지 않으면 세션쿠키가 된다.
        Cookie cookie = new Cookie("loginId",String.valueOf(member.getId()));
        response.addCookie(cookie);

        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV02(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request){

        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(member,response);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV03(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request){

        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession(); //default: true
        //true는 세션이 있으면 기존 세션을 반환, 세션이 없으면 새로운 세션을 생성해서 반환한다.
        //false는 세션이 있으면 기존 세션을 반환한다.
        //세션이 없으면 새로운 세션을 생성하지 않는다. null을 반환한다.

        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:/";
    }
   //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response,"loginId");

        return "redirect:/";
    }
    //@PostMapping("/logout")
    public String logoutV02(HttpServletResponse response,HttpServletRequest request){
        sessionManager.expire(request);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV03(HttpServletResponse response,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        //true로 하면 없어도 하나를 만들어서 반환하기 때문에 false를 사용
        if(session != null){
            session.invalidate();
        }

        return "redirect:/";
    }
    private void expireCookie(@NotNull HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName,null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
