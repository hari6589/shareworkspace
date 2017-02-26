package app.bsro.model.util;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class WordUtils {

	private static Set<String> exclusions;
	
	static {
		exclusions = new HashSet<String>();
		exclusions.add("II");
	}
	
	public static String capitalizeFully(String value, String delims) {
		StringBuilder newValue = new StringBuilder();
		StringTokenizer tokenizer = new StringTokenizer(value, delims, true);
		boolean insertSpace = (value.indexOf(" ") > -1);
		while(tokenizer.hasMoreTokens()) {
			String token = StringUtils.trimToEmpty(tokenizer.nextToken());
			
			char[] capitizeDelims =  {'.'};
			if(!exclusions.contains(token)) 
				newValue.append(org.apache.commons.lang.WordUtils.capitalizeFully(token,capitizeDelims));
			else {
				newValue.append(token);
			}
		
			if(insertSpace)
				newValue.append(" ");
		}
		
		String end = StringUtils.trimToEmpty(newValue.toString());
		
		//Replace multiple spaces with a single one
		end = end.replaceAll(" +",  " ");
		
		//Replace a comma with a leading space with just a comma
		end = StringUtils.replace(end, " ,", ",");
		

		//For each delim token, replace all occurrences of ' token ' with just the token.
		StringTokenizer delimTokenizer = new StringTokenizer(delims, ",", false);
		while(delimTokenizer.hasMoreTokens()) {
			String token = StringUtils.trimToEmpty(delimTokenizer.nextToken());
			if(token.length() > 0) {
				String repl = " "+token+" ";
				end = StringUtils.replace(end, repl, token);
			}
			
		}
		return end;
	}
}
