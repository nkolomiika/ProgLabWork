package org.example.network;


import lombok.Getter;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.utils.network.ObjectSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Properties;

public final class ServerTCP {

    private static final ServerSocketChannel serverSocketChannel;
    @Getter
    private static SocketChannel socketChannel;
    private static final int PORT;
    private static final int BUFFER_CAPACITY;


    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("server\\src\\main\\resources\\application.properties"));
            PORT = Integer.parseInt(properties.getProperty("PORT"));
            BUFFER_CAPACITY = Integer.parseInt(properties.getProperty("BUFFER_CAPACITY"));
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void acceptConnection() throws IOException {
        socketChannel = serverSocketChannel.accept();
    }

    public static Request receiveRequest() throws IOException, ClassNotFoundException {
        byte[] receiveBuffer = new byte[BUFFER_CAPACITY * BUFFER_CAPACITY];
        Socket socket = socketChannel.socket();
        int read = socket.getInputStream().read(receiveBuffer);
        return ObjectSerializer.deserializeObject(receiveBuffer);
    }

    public static void sendResponse(Response response) throws IOException {
        Socket socket = socketChannel.socket();
        OutputStream out = socket.getOutputStream();
        out.write(ObjectSerializer.serializeObject(response));
        out.flush();
    }

}
