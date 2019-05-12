package ro.nicuch.lwsal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ro.nicuch.lwsal.options.AnimationOptions;
import ro.nicuch.lwsal.types.*;

public class AnimationsManager {
    public static Animation readJsonString(String json) {
        if (json == null || json.isEmpty())
            return new AnimationText();
        JsonElement jsonElement = new Gson().fromJson(json, JsonElement.class);
        if (jsonElement.isJsonPrimitive()) {
            return new AnimationText(jsonElement.getAsString());
        } else if (jsonElement.isJsonArray()) {
            return readJsonArray(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonObject()) {
            return readJsonObject(jsonElement.getAsJsonObject());
        }
        return new AnimationText();
    }

    public static AnimationCompound readJsonArray(JsonArray jsonArray) {
        AnimationCompound compound = new AnimationCompound();
        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonPrimitive()) {
                compound.addAnimation(new AnimationText(jsonElement.getAsString()));
            } else if (jsonElement.isJsonArray()) {
                compound.addAnimation(new AnimationCompound().addAnimation(readJsonArray(jsonElement.getAsJsonArray())));
            } else if (jsonElement.isJsonObject()) {
                compound.addAnimation(readJsonObject(jsonElement.getAsJsonObject()));
            }
        }
        return compound;
    }

    public static Animation readJsonObject(JsonObject jsonObject) {
        if (!jsonObject.has("type"))
            return null;
        String type = jsonObject.get("type").getAsString();
        AnimationOptions options = jsonObject.has("options") ?
                readOptionsFromJsonObject(jsonObject.getAsJsonObject("options")) : new AnimationOptions();
        Animation text = checkJsonElementType(jsonObject.get("text"));
        switch (type.toLowerCase()) {
            case "rainbow":
                return new AnimationRainbow(text, options);
            case "reverse":
                return new AnimationReverse(text, options);
            case "scroller":
                return new AnimationScroller(text, options);
        }
        return null;
    }

    private static Animation checkJsonElementType(JsonElement element) {
        Animation anim = null;
        if (element.isJsonPrimitive())
            anim = new AnimationText(element.getAsString());
        else if (element.isJsonArray())
            anim = readJsonArray(element.getAsJsonArray());
        else if (element.isJsonObject())
            anim = readJsonObject(element.getAsJsonObject());
        return anim;
    }

    private static AnimationOptions readOptionsFromJsonObject(JsonObject jsonObject) {
        AnimationOptions options = new AnimationOptions();
        JsonElement element;
        if (jsonObject.has("update_time")) {
            element = jsonObject.get("update_time");
            if (element.isJsonPrimitive())
                options.setOptionIntEnum(
                        AnimationOptions.OptionIntEnum.UPDATE_TIME, element.getAsInt());
        }
        if (jsonObject.has("display_size")) {
            element = jsonObject.get("display_size");
            if (element.isJsonPrimitive())
                options.setOptionIntEnum(
                        AnimationOptions.OptionIntEnum.DISPLAY_SIZE, element.getAsInt());
        }
        if (jsonObject.has("space_between")) {
            element = jsonObject.get("space_between");
            if (element.isJsonPrimitive())
                options.setOptionIntEnum(
                        AnimationOptions.OptionIntEnum.SPACE_BETWEEN, element.getAsInt());
        }

        if (jsonObject.has("strip_colors")) {
            element = jsonObject.get("strip_colors");
            if (element.isJsonPrimitive())
                options.setOptionBooleanEnum(
                        AnimationOptions.OptionBooleanEnum.STRIP_COLORS, element.getAsBoolean());
        }
        return options;
    }
}
