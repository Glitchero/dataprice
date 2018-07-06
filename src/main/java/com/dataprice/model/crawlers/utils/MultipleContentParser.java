package com.dataprice.model.crawlers.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultipleContentParser {

	
	
	public static ArrayList<String> parseMultipleContent(String urlContent, String regex) {
        
		ArrayList<String> matches  = new ArrayList<String>();
		Pattern pattProduct = Pattern.compile(regex);

		Matcher matcher = pattProduct.matcher(urlContent);

		if (!matcher.find()){
    		return null;
		}

		matcher.reset();
		if (matcher.groupCount() > 1)
			System.err.println("Multiple match found");
		
		while(matcher.find()){
			matches.add(matcher.group(1));
		}
		return matches;
	}
	
	
}