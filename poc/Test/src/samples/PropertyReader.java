package samples;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

	public static void main(String[] args) {
		PropertyReader prop = new PropertyReader();
		System.out.println(prop.getPropertyValues());
	}
	
	public String getPropertyValues() {
		String propString = null;
		try {
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			if(inputStream != null) {
				prop.load(inputStream);
				propString = prop.getProperty("host") + " " 
						+ prop.getProperty("username")  + " "
						+ prop.getProperty("password");
			} else {
				propString = "InputStream is null";
			}
			return propString;
		} catch (Exception e) {
			System.out.println("Exception : " + e);
			return ""+e;
		}
	}
}
