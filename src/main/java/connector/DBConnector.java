package connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import model.RatedUser;

public class DBConnector {

	
	private static MongoClient single_instance = null;
	 
    // variable of type String
    public String s;
 
    // private constructor restricted to this class itself
    private DBConnector()
    {
        s = "Hello I am a string part of Singleton class";
    }
 
    // static method to create instance of Singleton class
    public static MongoClient getInstance()
    {
        if (single_instance == null)
            single_instance =  new MongoClient( "localhost" , 27017 );
 
        return single_instance;
    }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		MongoDatabase database = mongoClient.getDatabase("NPS");
		MongoCollection<Document> collection = database.getCollection("RATING");
		
		int pramoters = 0, detractors = 0, passive = 0;

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
		
		int total = NPSUser.get("Detractors").size() + NPSUser.get("Promoters").size() + NPSUser.get("Passive").size();
		float nps = ( NPSUser.get("Promoters").size()* 100 /total)  - ( NPSUser.get("Detractors").size()  * 100 /total);
		
		System.out.println(NPSUser);
		System.out.println(nps);
		
	}
	

}
