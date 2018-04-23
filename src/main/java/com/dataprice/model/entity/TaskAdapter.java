package com.dataprice.model.entity;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TaskAdapter implements JsonSerializer<Task>{

	@Override
	public JsonElement serialize(Task task, Type type, JsonSerializationContext jsc) {
		JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("taskName", task.getTaskName());
        jsonObject.addProperty("retailId", task.getRetail().getRetailId());
     // jsonObject.add("retail", new Gson().toJsonTree(task.getRetail()));
      //  JsonElement jsonRetail = jsc.serialize(task.getRetail());
      //  jsonObject.add("retail", jsonRetail);
        
        jsonObject.addProperty("seed", task.getSeed());
        return jsonObject;
	}

}
