package  com.aura.main.join;

import com.aura.main.model.MemberDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.module.FindException;

@Controller
public class joinController {

    @GetMapping("/join/join")
    public String joinmain(){

        return "join/join";

    }

    @PostMapping("/join/joinOk")
    public String joinOk(@ModelAttribute MemberDTO memberDTO){
        System.out.println("joinController.joinOk");
        System.out.println("memberDTO = " + memberDTO);

        return "index";

    }


}