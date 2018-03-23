package com.dataprice.model.crawlers.utils;

public class FetchResults {

	private String url="";
	private String content="";
	private int servercode=0;
	private String response="";
	
	public FetchResults() {
           //Constructor
	}
	
	
	public FetchResults(String url,String id, String content, int servercode, String response) {
		this.url = url;
		this.content = content;
		this.servercode = servercode;
		this.response = response;
	}

	public String getResponse() {
		return response;
	}


	public void setResponse(String response) {
		this.response = response;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getServercode() {
		return servercode;
	}

	public void setServercode(int servercode) {
		this.servercode = servercode;
	}
	
	
	
}