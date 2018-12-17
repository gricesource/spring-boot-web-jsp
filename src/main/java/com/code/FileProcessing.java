package com.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class FileProcessing {

	static final String ID_STRING = "\"id\":";
	static final String ID_END = ",";
	static final int FOLLOWER_LIMIT = 5;

	public String createOutput(String userId) {

		JSONObject jsonObject = new JSONObject();
		addFollowers(userId, jsonObject);
		
		String jsonString = jsonObject.toString();
		Gson ggson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement =  new JsonParser().parse(jsonString);
		
		String jsonOutput = ggson.toJson(jsonElement);
		System.out.println(jsonOutput);

		try {
			File resourceFile = new File("./codingChallengeOutput.txt");
			FileWriter fileWriter = new FileWriter(resourceFile);
			fileWriter.write(jsonOutput);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return jsonOutput;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject addFollowers(String userId, JSONObject jsonObject) {
		
		JSONArray idArray = new JSONArray();
		JSONArray followerArray = new JSONArray();
		
		for (String id: getFollowerList(userId)) {
    	    followerArray.add(new JsonPrimitive(id));
    	    addFollowerLevelTwoAndThree(followerArray, id);
		}
		idArray.add(followerArray);
		jsonObject.put(userId, idArray);
		
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	private void addFollowerLevelTwoAndThree(JSONArray followerArray, String id) {

		JSONArray tempArray = new JSONArray(); 
		for (String followerId: getFollowerList(id)) {
			 tempArray.add(new JsonPrimitive(followerId));
			 
			 JSONArray lastTempArray = new JSONArray(); 
			 for (String followerLastId: getFollowerList(followerId)) {
				 lastTempArray.add(new JsonPrimitive(followerLastId));
			 }
			 tempArray.add(lastTempArray);
		 }
		 followerArray.add(tempArray);
	}
	
	public ArrayList<String> getFollowerList(String id) {

		ArrayList<String> followerList = new ArrayList<String>();
		String response = "";
        try {
         	final RestTemplate restTemplate = new RestTemplate();
         	 response = 
  			        restTemplate.getForObject("https://api.github.com/user/" +id+ "/followers", String.class);
         	 
        }
        catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		ArrayList<String> list = getValues(response, ID_STRING, ID_END);
		
		if (list.size() > 0) {
			for (int i=0;i<FOLLOWER_LIMIT; i++) {
				followerList.add(list.get(i));
			}	
		}
		
		return followerList;
	}
	
	private ArrayList<String> getValues(String str, String findStr, String endChar) {
		
		ArrayList<String> list = new ArrayList<String>();
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){
		    lastIndex = str.indexOf(findStr,lastIndex);
		    if(lastIndex != -1){
		        count ++;
		        lastIndex += findStr.length();
	            int endIndex = getEndIndex(lastIndex, endChar, str);
	            list.add(str.substring(lastIndex, endIndex-1));
		    }
		}
		return list;
	}	
	
	private int getEndIndex(int lastIndex, String findStr, String str) {
		
		int count = 0;
		
	    while(lastIndex != -1){

	        lastIndex = str.indexOf(findStr,lastIndex);

	        if(lastIndex != -1){
	            count ++;
	            lastIndex += findStr.length();
	            return lastIndex;
	        }
	    }
		return count;
	}
}