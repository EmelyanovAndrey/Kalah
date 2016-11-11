package com.pesikovlike.kalah.server.websocket.test;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by Igor on 12.11.2016.
 */
public class FigureEncoder implements Encoder.Text<Figure> {

    public String encode(Figure figure) throws EncodeException {
        return figure.getJson().toString();
    }


    public void init(EndpointConfig ec) {
        System.out.println("init");
    }


    public void destroy() {
        System.out.println("destroy");
    }
}
