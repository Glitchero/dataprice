package com.dataprice.model.crawlers.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentParser {

	/**
	 * This method parses the urlContent using a regular expression looking for
	 * a unique value. Multiple values matching the same regex is an illegal
	 * condition, so a warning is raised. Regex should be provided as the
	 * example: "<h2 style=\"text-align:left;\">(.*?)</h2>"
	 * 
	 * @param urlContent
	 *            Holds the content of a web page.
	 * @param regex
	 *            Regular expression used to identify values and links
	 * @return The desired value or link
	 */
	public static String parseContent(String urlContent, String regex) {

		// this pattern is specific for the given website
		Pattern pattProduct = Pattern.compile(regex, Pattern.DOTALL);

		// create a matcher from the given pattern for the URL content
		Matcher matcher = pattProduct.matcher(urlContent);

		// find the first pattern match and return null if nothing has been found
		if (!matcher.find()){
			return null;
		}

		// check if there are multiple matches and raise warning
		if (matcher.groupCount() > 1)
			System.err.println("Multiple matches found");
		
		// return the value in between (.*?)
		return matcher.group(1);
	}
	
	
public static ArrayList<String> parseMultipleContent(String urlContent, String regex) {
        
		ArrayList<String> matches  = new ArrayList<String>();
		// this pattern is specific for the given website
		Pattern pattProduct = Pattern.compile(regex);

		// create a matcher from the given pattern for the URL content
		Matcher matcher = pattProduct.matcher(urlContent);

		// find the first pattern match and return null if nothing has been found
		if (!matcher.find()){
    		return null;
		}

		matcher.reset();
		// check if there are multiple matches and raise warning
		if (matcher.groupCount() > 1)
			System.err.println("Multiple matches found");
		
		while(matcher.find()){
			matches.add(matcher.group(1));
		}
		// return the value in between (.*?)
		return matches;
	}


}
