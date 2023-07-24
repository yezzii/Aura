package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/member")
    public String getMember(Model model) {
        model.addAttribute("list", memberService.memberList());

        return "member";
    }

    @GetMapping("/profile")
    public String getMemberProfile(Model model, @RequestParam(value = "no")Long no){
        model.addAttribute("mList",memberService.getMemberProfile(no));
        return "profile";
    }

}
