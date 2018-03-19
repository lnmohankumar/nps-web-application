package hello;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import connector.DBUtil;
import model.AmazonSESSample;
import model.RatedUser;
import model.SurveyData;
import model.UserCreateRequest;

@Controller
public class NPSController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    @GetMapping("/calculateNPS") 
    @ResponseBody
    public String calculateNPS(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        
    	
    	
    	Map<String, List<RatedUser>> NPSUser = DBUtil.calculateNPS();
    	
    	int detractors = NPSUser.get("Detractors").size();
    	int passive = NPSUser.get("Passive").size();
    	int promotors = NPSUser.get("Promoters").size();
		
		int total =  detractors + passive + promotors;
		float nps = ( promotors* 100 /total)  - ( detractors  * 100 /total);
		
		
    	 model.addAttribute("nps", nps);
    	 model.addAttribute("Detractors", detractors);
    	 model.addAttribute("Passive", passive);
    	 model.addAttribute("Promoters", promotors);
    	 
    	 
        return nps + "#" + detractors  + "#" + passive + "#" + promotors;
    }
    

    
    @PostMapping("/surveyData")
    public String surveyData(@ModelAttribute SurveyData data) {

    	DBUtil.insertNPS(data.getName1(), data.getRating(), data.getEmail1(), data.getComment(), Long.parseLong(data.getTimestamp1()));
    	
    return "greeting";
    }
    
    
    @GetMapping("/dashboard")
    public String dashboard(@ModelAttribute SurveyData data) {
    	return "dashboard";
    }
    
    @GetMapping("/user")
    public String dashboard1(@ModelAttribute SurveyData data) {
    	return "user";
    }
    
    @GetMapping("/customers")
    @ResponseBody
    public String customers(@ModelAttribute SurveyData data) {
    	return DBUtil.users();
    }
    
    @GetMapping("/customer")
    public String customer(@ModelAttribute SurveyData data) {
    	return "customer";
    }
    
    @PostMapping("/dashboard")
    public String displaydashboard(@ModelAttribute SurveyData data) {
    	return "dashboard";
    }

    
    @GetMapping("/survey")
    public String survey(@RequestParam(name="surveyToken", required=true) String name, Model model) {
        // Decode data on other side, by processing encoded data
    	
        byte[] valueDecoded = Base64.getDecoder().decode(name);
        String[] tokenList = new String(valueDecoded).split("#");
        
    	model.addAttribute("email", tokenList[0]);
    	model.addAttribute("name", tokenList[1]);
    	model.addAttribute("timeStamp", tokenList[2]);
    	model.addAttribute("isfirstTime", DBUtil.checkSurvey(Long.parseLong(tokenList[2])));
        return "survey";
    }
    
    @PostMapping("/generateURL")
    public String generateURL(@ModelAttribute UserCreateRequest user) {

    String email = user.getEmail();
    String name = user.getName();
    long timeStamp = System.currentTimeMillis();
    
    
    String key = email + "#" + name + "#" + timeStamp ;

    String bytesEncoded = Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    
    String surveyLink = "http://localhost:8080/survey?surveyToken=" + bytesEncoded;
    
    DBUtil.insertURL(name, surveyLink, email, timeStamp, 0);

    try {
		AmazonSESSample.sendMail(surveyLink);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    return "dashboard";
    }

}
