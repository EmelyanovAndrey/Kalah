package com.pesikovlike.kalah.server.websocket.test;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringWriter;

/**
 * Created by Igor on 12.11.2016.
 */
public class Figure {
    private JsonObject json;
    public Figure(JsonObject json) {
        this.setJson(json);
    }
    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        Json.createWriter(writer).write(getJson());
        return writer.toString();
    }

    public JsonObject getJson() {
        return json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }
}
