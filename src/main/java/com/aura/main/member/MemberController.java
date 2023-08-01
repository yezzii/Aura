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
import java.util.Map;
import java.util.Objects;


@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    //로그인 페이지
    @GetMapping("/login")

    public String login(Model model) {
        model.addAttribute("kakaoUrl", memberService.KakaoLogin());
        return "member/login";
    }

    //로그인 체크
    @PostMapping("/check")
    public String loginCheck(HttpServletRequest request, @RequestParam("id") String memberId, @RequestParam("password") String memberPwd) {

        MemberDTO member = memberService.loginCheck(memberId, memberPwd);
        if (member != null) {
            // 로그인 성공
            HttpSession session = request.getSession();
            session.setAttribute("loginmember", member);
            return "index";
        } else {
            //로그인 실패
            return "member/insert";

        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        //세션 무효화
        session.invalidate();
        return "redirect:/login";
    }
    // 카카오 API 로그인

    @RestController
    @AllArgsConstructor
    @RequestMapping("/kakao")
    public class kakaoController {
        @GetMapping("/login")
        public String KakaoLogin(@RequestParam String code, HttpServletResponse response,
                                 HttpServletRequest request, Model model) throws IOException {
            System.out.println("code = " + code);
            String access_token = memberService.getToken(code);
            Map<String, Object> userInfo = memberService.getUserInfo(access_token);
            System.out.println("###nickname#### : " + userInfo.get("nickname"));
            System.out.println("###email#### : " + userInfo.get("email"));
            System.out.println("###id#### : " + userInfo.get("id"));
            String kakao_id = (String) userInfo.get("id");
            MemberDTO dto = memberService.kakaologinCheck(kakao_id);
            System.out.println(dto);

            int check = 0;

            if (dto != null) {
                check = 1;
            }

            if (check == 1) {
            }

            return "member/kakaologin";
        }
    }
}
