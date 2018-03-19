package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import connector.DBUtil;
import model.UserCreateRequest;

@Controller
public class LoginController {
    
    @PostMapping("/login")
    @ResponseBody
    public String create(@ModelAttribute UserCreateRequest loginUser) {
    	
    	String login =  DBUtil.tryLogin(loginUser.getEmail(), loginUser.getPassword());
    	
    	if(!login.equals("false")) return login;
    	else return "false";
    }
   
    @PostMapping("/signup")
    @ResponseBody
    public String signup(@ModelAttribute UserCreateRequest loginUser) {
    	
    	String signup =  DBUtil.insertUser(loginUser.getName(), loginUser.getEmail(), loginUser.getPassword());
    	
    	if(signup.equals("true")) return "/index";
    	else return "false";

    }
    
    @GetMapping("/index")
    public String index(@ModelAttribute UserCreateRequest loginUser) {    	
    	return "/index";

    }

}
