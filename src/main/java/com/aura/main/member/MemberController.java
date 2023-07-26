package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    //로그인 페이지
    @GetMapping("/login")


    public String login(Model model) {

        return "member/login";
    }
    //로그인 체크
    @PostMapping("/check")
    public String loginCheck(HttpServletRequest request, @RequestParam("id")String memberId, @RequestParam("password")String memberPwd) {

        MemberDTO member = memberService.loginCheck(memberId,memberPwd);
        if(member !=null){
            // 로그인 성공
            HttpSession session = request.getSession();
            session.setAttribute("loginmember",member);
            return "index";
        }else{
            //로그인 실패
            return "member/insert";

        }
    }
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session){
        //세션 무효화
        session.invalidate();
        return "redirect:/login";
    }
    // 카카오 API 로그인

    @RestController
    @AllArgsConstructor
    @RequestMapping("/kakao")
    public class kakaoController{
        @GetMapping("/login")
        public String kakao(@RequestParam String code,Model model)throws IOException{
            System.out.println("code = " + code);
            String access_token = memberService.getToken(code);


            return "member/kakaologin";
        }
    }

}
