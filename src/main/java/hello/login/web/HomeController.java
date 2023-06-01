package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;
    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name="loginId" , required = false)Long loginId, Model model){

        if(loginId == null){
            return "home";
        }

        Member member = (Member)memberRepository.findById(loginId);

        if(member == null){
            return "home";
        }

        model.addAttribute("member",member);

        return "homelogin";
    }

    @GetMapping("/")
    public String homeLoginV02(Model model, HttpServletRequest request){

        Member member = (Member)sessionManager.getSession(request);

        if(member == null){
            return "home";
        }

        model.addAttribute("member",member);

        return "homelogin";
    }

}