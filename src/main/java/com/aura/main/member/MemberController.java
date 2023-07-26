package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    //로그인 페이지
    @GetMapping("/login")
    public String login() {
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


    }
