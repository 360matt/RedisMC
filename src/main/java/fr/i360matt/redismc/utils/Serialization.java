package fr.i360matt.redismc.utils;

import java.io.*;
import java.util.Base64;

public class Serialization {

    public static String serialize (Serializable serializable) {
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeUnshared(serializable);
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Serializable> T deserialize (String serialized) {
        final byte[] bytes = Base64.getDecoder().decode(serialized);
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) in.readUnshared();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
