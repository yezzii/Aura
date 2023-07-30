package  com.aura.main.join;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class joinController {

    @GetMapping("/join/join")
    public String joinmain(){

        return "join/join";

    }

    @PostMapping("/join/joinOk")
    public String joinOk(){
        System.out.println("joinController.joinOk");

        return null;

    }


}