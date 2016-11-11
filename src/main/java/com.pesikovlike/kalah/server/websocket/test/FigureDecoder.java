package com.pesikovlike.kalah.server.websocket.test;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * Created by Igor on 12.11.2016.
 */
public class FigureDecoder implements Decoder.Text<Figure> {

    public Figure decode(String string) throws DecodeException {
        JsonObject jsonObject = Json.createReader(new StringReader(string)).readObject();
        return  new Figure(jsonObject);
    }

    public boolean willDecode(String string) {
        try {
            Json.createReader(new StringReader(string)).readObject();
            return true;
        } catch (JsonException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public void init(EndpointConfig ec) {
        System.out.println("init");
    }

    public void destroy() {
        System.out.println("destroy");
    }
}
