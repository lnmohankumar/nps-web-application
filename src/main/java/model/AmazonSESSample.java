package model;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AmazonSESSample {

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "lnmohankumar@gmail.com";
    static final String FROMNAME = "NPS Administrator";
	
    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
    static final String TO = "lnmohankumar@gmail.com";
    
    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIAI7XTG7CMIYY7RHJA";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "AjT7EfpvGQxQyk6pf2bN8OnrKy+rtDsfkh7oYTh8sSIr";
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    static final String CONFIGSET = "ConfigSet";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.us-east-1.amazonaws.com";
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587;
    
    static final String SUBJECT = "NPS Survey ";
    
    public static void sendMail(String url) throws Exception {

        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        
        
        String BODY = String.join(
        	    System.getProperty("line.separator"),
        	    "<h1>Please fill the NPS Survey</h1>",
        	    "<p>click ", 
        	    "<a href='" + url +"'>here</a>",
        	    " to fill the survey "
        	);
        
        
        String Body1 = "<!DOCTYPE HTML>\r\n" + 
        		"<html xmlns:th=\"http://www.thymeleaf.org\">\r\n" + 
        		"<head> \r\n" + 
        		"\r\n" + 
        		"<style>\r\n" + 
        		"pre {\r\n" + 
        		"    text-align: center;\r\n" + 
        		"}\r\n" + 
        		"</style>\r\n" + 
        		"    <title>DBS Survey</title> \r\n" + 
        		"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n" + 
        		"</head>\r\n" + 
        		"<body>\r\n" + 
        		"    <pre><h3>Thank you for visiting the Store.<h3>\r\n" + 
        	    "<p>click "+
        	    "<a href='" + url +"'>here</a>"+
        	    " to fill the survey </pre>" +
        		"</body>\r\n" + 
        		"</html>";
        
        msg.setContent(Body1,"text/html");
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
        //msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
        // Create a transport.
        Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }
}