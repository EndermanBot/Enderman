package org.enderman;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class JsonHelper {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private JsonObject settingsFile;
	private String filePath;
	private JsonHelper parent;
	private String member;
	public JsonHelper(JsonObject obj) {
		settingsFile = obj;
	}
	private void setParent(JsonHelper parent, String name) {
		this.parent = parent;
		filePath = parent.filePath;
		member = name;
	}
	public JsonHelper(String path) throws JsonIOException, JsonSyntaxException, IOException {
		this.filePath = path;
		try {
			settingsFile = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
		} catch (FileNotFoundException e) {
			settingsFile = new JsonObject();
			
			saveJson();
			
		}
		
	}
	public boolean contains(String member) {
		return settingsFile.has(member);
	}
	
	public void saveJson(){
		
		if(parent != null) {
			parent.setJsonObject(member, this);
			parent.saveJson();
			return;
		} else
			try {
				FileUtils.writeTo(filePath, GSON.toJson(settingsFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public void setBoolean(String member, boolean bool) {
		settingsFile.addProperty(member, bool);
		saveJson();
	}
	public JsonHelper getJsonObject(String member)  {
		if(!contains(member)) {
			JsonHelper js = new JsonHelper(new JsonObject());
			js.setParent(this, member);
			setJsonObject(member, js);
		}
		JsonHelper result = new JsonHelper(settingsFile.get(member).getAsJsonObject());
		result.setParent(this, member);
		return result;
	}
	public JsonObject getJson() {
		return settingsFile;
	}
	public void setJsonObject(String member, JsonHelper obj) {
		settingsFile.add(member, obj.getJson());
		saveJson();
		}
	
	public boolean getBoolean(String member)  {
		if(!contains(member)) {
			setBoolean(member, false);
			
		}
		
		return settingsFile.get(member).getAsBoolean();
	}
	public float getFloat(String member)  {
		if(!contains(member)) {
			setFloat(member, 0.0f);
		}
		
		return settingsFile.get(member).getAsFloat();
	}
	public int getInteger(String member) {
		if(!contains(member)) {
			setInteger(member, 0);
		}
		return settingsFile.get(member).getAsInt();
		
	}
	public void setInteger(String member, int i) {
		settingsFile.addProperty(member, i);
		saveJson();
	}
	
	
	public String getString(String member) {
		if(!contains(member)) {
			setString(member, "");
		}
		return settingsFile.get(member).getAsString();
		
	}
	public void setString(String member, String i) {
		settingsFile.addProperty(member, i);
		saveJson();
	}
	
	
	public void setFloat(String member, float f) {
		settingsFile.addProperty(member, f);
		saveJson();
	}
}
