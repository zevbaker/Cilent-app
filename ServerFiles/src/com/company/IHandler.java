package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// contains a functios to handle our connection services
public interface IHandler {
    public abstract void handle(InputStream fromClient,
                                OutputStream toClient) throws IOException, ClassNotFoundException;

}
