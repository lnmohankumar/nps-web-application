package hello;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Util {

	public static void writeToFile(String fileName, String content) 
			  throws IOException {
			    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			    writer.write(content);
			     
			    writer.close();
			}
	

	
}
