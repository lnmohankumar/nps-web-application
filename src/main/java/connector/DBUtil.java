package connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import model.RatedUser;

public class DBUtil {
	
	public static Map<String, List<RatedUser>> calculateNPS() {
		
		MongoDatabase database = DBConnector.getInstance().getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("RATING");

		Map<String, List<RatedUser>>  NPSUser = new HashMap<String, List<RatedUser>>(); 
		
		NPSUser.put("Detractors", new ArrayList<RatedUser>() );
		NPSUser.put("Passive", new ArrayList<RatedUser>() );
		NPSUser.put("Promoters", new ArrayList<RatedUser>() );
		
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		    	Document row = cursor.next();
		    	
		    	String name = row.get("name").toString();
		        int rating = Integer.parseInt(row.get("rating").toString()) ;
		        String email = row.get("email").toString();

		        RatedUser rater = new RatedUser(email, name, rating);
		        
		        if(rating <= 6 ) NPSUser.get("Detractors").add(rater);
		        else if(rating >= 9 ) NPSUser.get("Promoters").add(rater);
		        else NPSUser.get("Passive").add(rater); 
		    }
		} finally {
		    cursor.close();
		}
		
		return NPSUser;
	}
	
	public static int insertNPS(String name, String rating, String email, String comment, Long timestamp) {
		
		MongoDatabase database = DBConnector.getInstance().getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("RATING");
		
		Document doc = new Document("name", name)
                .append("rating", rating)
                .append("email", email)
                .append("comment", comment)
		        .append("timestamp", timestamp);
		collection.insertOne(doc);
		
		return 0;
	}
	
	public static int insertURL(String name, String url, String email, Long timestamp, int userid ) {
		
		MongoDatabase database = DBConnector.getInstance().getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("SURVEY");
		
		Document doc = new Document("name", name)
                .append("url", url)
                .append("email", email)
                .append("timestamp", timestamp)
                .append("userid", userid);
		collection.insertOne(doc);
		
		return 0;
	}
	
	public static String insertUser(String name, String email, String passsword) {
		
		MongoDatabase database = DBConnector.getInstance().getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("USER");
		
		Document foundDoc = collection.find(eq("email", email)).first();	

		if(foundDoc != null && !foundDoc.isEmpty()&& foundDoc.getString("email") != null && foundDoc.getString("email").equals(email)) return "User Exist!!!";
		
		Document doc = new Document("name", name)
                .append("email", email)
                .append("password", passsword)
                .append("userid", System.currentTimeMillis());
		collection.insertOne(doc);
		return "true";
	}
	
	public static String checkSurvey( Long timestammp) {
		
		MongoDatabase database = DBConnector.getInstance().getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("RATING");
		
		Document foundDoc = collection.find(eq("timestamp", timestammp) ).first();	
    	System.out.println(timestammp + " " + foundDoc);
		if(foundDoc != null && foundDoc.size() > 0) return "true";
		  
		return "false";
	}
	
	public static String tryLogin(String email, String password) {

		MongoDatabase database = DBConnector.getInstance().getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("USER");
		System.out.println(email + "=" + password);
		Document foundDoc = collection.find(eq("email", email)).first();	

		
		if(foundDoc!= null && !foundDoc.isEmpty() && foundDoc.getString("password") != null && foundDoc.getString("password").equals(password)) return foundDoc.getString("name") + "#" + foundDoc.getString("email");
		else return "false";
		
	}
	
	public static String users() {
		MongoDatabase database = DBConnector.getInstance().getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("RATING");
		
		StringBuffer bf = new StringBuffer();
		
		Boolean flag = false;
		for(Document i:collection.find()){  
			
			if(flag) {
				bf.append("=").append(i.getString("name")).append("#").append(i.getString("email")).append("#").append(i.getString("rating"));
			}else {
				flag= true;
				bf.append(i.getString("name")).append("#").append(i.getString("email")).append("#").append(i.getString("rating"));
			}
		     
		   }  
		System.out.println(bf.toString());
		return bf.toString();
	}


}
