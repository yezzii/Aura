package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;


@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/member")
    public String getMember(Model model) {
        model.addAttribute("list", memberService.memberList());
        return "member";
    }
    //로그인 페이지
//    @GetMapping("/login")
//    public String login() {
//        return "member/login";
//    }
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

    @GetMapping("/profile")
    public String getMemberProfile(Model model, @RequestParam(value = "no")Long no){
        model.addAttribute("mList", memberService.getMemberProfile(no));
        return "profile";
    }

    @GetMapping("/login")
    public String naverLogin(HttpSession session, Model model) throws Exception{

        System.out.println("naver.go 진입");

        String clientId = "8ljUOFqbhL8ClN644LoP";//애플리케이션 클라이언트 아이디값";
        String redirectURI = URLEncoder.encode("http://localhost:8989/login/naverLogin.go", "UTF-8");
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();

        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += "&client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&state=" + state;

        session.setAttribute("state", state);
        model.addAttribute("apiURL", apiURL);

        System.out.println("apiURL >>> "+apiURL);
        System.out.println("stats >>> "+state);

        return "member/login";
    }

    @GetMapping(value = "/naverLogin.go")
    public @ResponseBody String naverLoginOk(@RequestParam(value = "code", required = false) String code, Model model,
    HttpSession session) throws Exception{

        System.out.println("/naverLogin.go  진입");

        String accessToken = memberService.getNaverAccessToken(code, session);

        HashMap<String, Object> userInfo = memberService.getNaverUserInfo(accessToken);

        System.out.println(userInfo);

        return userInfo.toString();
    }

    @GetMapping("/naverLoginOk.go")
    public String naverLoginOk(){

        System.out.println("네이버로그인 완료");
        return "index";
    }

    public String generateState(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }


}
