package org.example.network.utils;

import org.example.network.dto.Request;
import org.example.network.dto.Transfer;

import java.io.*;

/**
 * Класс для сериализации и десериализации данных
 */
public class ObjectSerializer {

    public static <T extends Transfer> byte[] serializeObject(T o) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(o);
            out.flush();
            out.close();
            return bos.toByteArray();
        }
    }

    public static <T extends Transfer> T deserializeObject(byte[] byteArray)
            throws IOException, ClassNotFoundException {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            @SuppressWarnings("unchecked")
            T request = (T) ois.readObject();

            ois.close();
            bis.close();
            return request;
        }
    }


}