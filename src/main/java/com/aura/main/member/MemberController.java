package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    //로그인 페이지
    @GetMapping("/login")

    public String login(Model model) {
        model.addAttribute("kakaoUrl",memberService.KakaoLogin());
        return "member/login";
    }
    //로그인 체크
    @PostMapping("/check")
    public String loginCheck(HttpServletRequest request,HttpServletResponse response,
                             @RequestParam("id")String memberId,
                             @RequestParam("password")String memberPwd)throws IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        MemberDTO member = memberService.loginCheck(memberId,memberPwd);
        MemberDTO checkId = memberService.checkId(memberId);
        MemberDTO checkPwd = memberService.checkPwd(memberPwd);

        if(member !=null){
            // 로그인 성공
            HttpSession session = request.getSession();
            session.setAttribute("loginmember",member);
            return "index";
        }else{
            //로그인 실패
            if(checkId == null) {
                //아이디가 틀린경우
                out.println("<script>");
                out.println("alert('아이디가 틀립니다.')");
                out.println("history.back()");
                out.println("</script>");

            }else if(checkPwd == null){
                //아이디가 틀린경우
                out.println("<script>");
                out.println("alert('비밀번호가 틀립니다.')");
                out.println("history.back()");
                out.println("</script>");
            }
        }
        return "member/login";
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
        public String KakaoLogin(@RequestParam String code,HttpServletResponse response,
                                 HttpServletRequest request,Model model)throws IOException{

            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            String link = null;

            System.out.println("code = " + code);
            String access_token = memberService.getToken(code);
            Map<String, Object> userInfo = memberService.getUserInfo(access_token);
            System.out.println("###nickname#### : "+userInfo.get("nickname"));
            System.out.println("###email#### : "+userInfo.get("email"));
            System.out.println("###id#### : "+userInfo.get("id"));
            String kakao_id = (String) userInfo.get("id");
            MemberDTO dto = memberService.kakaologinCheck(kakao_id);
            System.out.println(dto);

            if(dto != null){
                // 회원일 경우
                HttpSession session = request.getSession();
                session.setAttribute("UserId",dto.getMemberId());
                session.setAttribute("KakaoId",dto.getMemberId());
                session.setAttribute("kakaoImage",userInfo.get("profile_image"));
                out.println("<script>");
                out.println("alert('로그인 성공')");
                out.println("</script>");
                link = "index";
            }else {
                //회원이 아닐경우
                model.addAttribute("KakaoId",userInfo.get("id"))
                        .addAttribute("Thumbnail_Image",userInfo.get("thumbnail_image"))
                        .addAttribute("NickName",userInfo.get("nickname"))
                        .addAttribute("Email",userInfo.get("email"))
                        .addAttribute("Gender",userInfo.get("gender"))
                        .addAttribute("Birthday",userInfo.get("birthday"))
                        .addAttribute("Age_Ranger",userInfo.get("age_ranger"));
                        out.println("<script>");
                        out.println("alert('아이디가 존재하지 않습니다.')");
                        out.println("</script>");
                        link = "member/kakaoInsert";
            }
            out.close();
            return link;
        }
    }





}
