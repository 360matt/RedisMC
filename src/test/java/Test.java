import fr.i360matt.redismc.RedisClient;
import fr.i360matt.redismc.RedisConnection;
import fr.i360matt.redismc.Storage;
import fr.i360matt.redismc.storage.RedisHashMap;
import fr.i360matt.redismc.storage.RedisList;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {

        RedisClient.create(
                new RedisConnection("localhost", 6379),
                "rouge",
                "couleur"
        );

        RedisHashMap<Integer, String> map = Storage.getMap("liste");
        map.put(1, "rouge");
        map.put(2, "bleu");

        System.out.println(map.get(1));
        System.out.println(map.get(2));

        map.clear();

    }

}
