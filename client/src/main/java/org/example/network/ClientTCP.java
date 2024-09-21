package org.example.network;


import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.utils.network.ObjectSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Properties;

public final class ClientTCP {

    private static final Socket clientSocket;
    private static final int PORT;
    private static final String HOST;
    private static final int BUFFER_CAPACITY;


    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("client\\src\\main\\resources\\application.properties"));
            PORT = Integer.parseInt(properties.getProperty("PORT"));
            HOST = properties.getProperty("HOST");
            BUFFER_CAPACITY = Integer.parseInt(properties.getProperty("BUFFER_CAPACITY"));
            clientSocket = new Socket(HOST, PORT);
        } catch (ConnectException e) {
            throw new RuntimeException("Ooops! Cannot connect to server :(");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Response receiveResponse() throws IOException, ClassNotFoundException {
        byte[] receiveBuffer = new byte[BUFFER_CAPACITY * BUFFER_CAPACITY];
        int read = clientSocket.getInputStream().read(receiveBuffer);
        return ObjectSerializer.deserializeObject(receiveBuffer);
    }

    public static void sendRequest(Request request) throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        out.write(ObjectSerializer.serializeObject(request));
        out.flush();
    }

}
