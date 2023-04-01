package services.taskmanagers;

import com.google.gson.*;
import models.Task;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TaskDeserializer  implements JsonDeserializer<Task> {
    private String taskTypeElementName;
    private Gson gson;
    private Map<String, Class<? extends Task>> taskTypeRegistry;

    public TaskDeserializer(String taskTypeElementName) {
        this.taskTypeElementName = taskTypeElementName;
        this.gson= Managers.getGsonForDeserializer();
        this.taskTypeRegistry = new HashMap<>();
    }
    public void registerBarnType(String taskTypeName, Class<? extends Task> taskType) {
        taskTypeRegistry.put(taskTypeName, taskType);
    }

    @Override
    public Task deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            JsonObject taskObject = jsonElement.getAsJsonObject();
            JsonElement taskTypeElement = taskObject.get(taskTypeElementName);

            Class<? extends Task> taskType = taskTypeRegistry.get(taskTypeElement.getAsString());
            return gson.fromJson(taskObject, taskType);
        } catch (NullPointerException e) {
            return null;
        }

    }
}
